package juegoTateti;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class JuegoTateti {

    //Datos conexion base de datos
    public static final String URL_DB = "jdbc:mysql://127.0.0.1:3306/tateti";
    public static final String USUARIO_DB = "root";
    public static final String PASSWORD_DB = "******";

    private static final int CODIGO_IDIOMA_ESP = 1;

    public static final char FICHA_JUGADOR = 'X';
    public static final char FICHA_COMPUTADORA = 'O';

    private static Scanner lector = new Scanner(System.in);

    private static int codigoLenguajeSeleccionado = CODIGO_IDIOMA_ESP;  //Español por defecto

    public static void main(String[] args) {
        try {
            Connection miConexion = DriverManager.getConnection(URL_DB, USUARIO_DB, PASSWORD_DB);

            System.out.println("Bienvenido a sistema de base de datos de jugadas de TATETI.");

            boolean terminar = false;

            while(!terminar) {
                mostrarMenuPrincipal();
                int opcionElegida = leerOpcion();

                switch(opcionElegida) {
                    case 1: mostrarIdiomasDisponibles(miConexion);
                        break;
                    case 2: jugar(miConexion);
                        break;
                    case 3: mostrarPartidasJugador(miConexion);
                        break;
                    case 4: agregarMensajeNuevo(miConexion);
                        break;
                    case 5: terminar = true;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("No conecta");
            e.printStackTrace();
        }
    }

    private static int leerOpcion() {
        int opcionElegida = lector.nextInt();
        while(opcionElegida < 1 || opcionElegida > 5) {
            System.out.println("La opcion ingresada es incorrecta.");
            mostrarMenuPrincipal();
            opcionElegida = lector.nextInt();
        }
        return opcionElegida;
    }

    private static void mostrarMenuPrincipal() {
        System.out.println();
        System.out.println("1 - Cambiar idioma.");
        System.out.println("2 - Jugar partida.");
        System.out.println("3 - Ver resultados generales.");
        System.out.println("4 - Ver resultados por jugador.");
        System.out.println("5 - Salir");
        System.out.println();
    }

    private static void mostrarPartidasJugador(Connection miConexion) throws SQLException {
        System.out.println();
        System.out.println("Ingrese nombre del jugador: ");

        //Por alguna razon el siguiente nextLine el programa lo ignora y tuve que ponerlo 2 veces
        lector.nextLine();
        String nombreDeJugador = lector.nextLine();

        System.out.println();

        ResultSet miResultSet = miConexion.createStatement().executeQuery("SELECT * FROM info_partidas WHERE nombre_jugador = '" + nombreDeJugador + "'");

        while(miResultSet.next()) {
            System.out.println(miResultSet.getString("id_partida") + " " + miResultSet.getString("nombre_jugador") + " " + miResultSet.getString("inicio_partida") + " " + miResultSet.getString("fin_partida") + " " + miResultSet.getString("ganador") + " " + miResultSet.getString("idioma_elegido"));
        }
    }

    private static void mostrarMensajesDelSistema(Connection miConexion) throws SQLException {
        System.out.println();
        System.out.println("Ingrese codigo de idioma en el que desea ver el mensaje:");
        int codigoIdiomaSeleccionado = lector.nextInt();

        System.out.println();
        System.out.println("Ingrese codigo de mensaje:");
        int codigoDeMensaje = lector.nextInt();
        System.out.println();

        ResultSet miResultSet = miConexion.createStatement().executeQuery("SELECT * FROM mensajes where cod_idioma = " + codigoIdiomaSeleccionado + " AND cod_mensaje = " + codigoDeMensaje);

        //4- Recorrer el ResultSet
        while(miResultSet.next()) {
            System.out.println(miResultSet.getString("mensaje"));
        }

    }

    private static void mostrarIdiomasDisponibles(Connection miConexion) throws SQLException {
        System.out.println();
        System.out.println("Idiomas disponibles: ");
        System.out.println();

        ResultSet miResultSet = miConexion.createStatement().executeQuery("SELECT * FROM tabla_idiomas");
        while(miResultSet.next()) {
            System.out.println(miResultSet.getString("id_idioma") + " - " + miResultSet.getString("nombre_idioma"));
        }
    }

    private static void agregarMensajeNuevo (Connection miConexion) throws SQLException {
        System.out.println();
        System.out.print("Ingrese codigo del mensaje: ");
        int codigoDelMensaje = lector.nextInt();
        System.out.print("Ingrese codigo de idioma del mensaje: ");
        int codigoIdioma = lector.nextInt();
        System.out.print("Ingrese mensaje: ");
        lector.nextLine();
        String mensaje = lector.nextLine();

        miConexion.createStatement().executeUpdate(String.format("INSERT INTO mensajes values (%d, %d,'%s')", codigoDelMensaje, codigoIdioma, mensaje));

        System.out.println();
        System.out.print("Mensaje agregado exitosamente!");
    }

    private static void jugar(Connection miConexion) {
        System.out.println("Ingrese su nombre: ");
        lector.nextLine(); //Por alguna razon el siguiente nextLine el programa lo ignora y tuve que ponerlo 2 veces
        String nombreJugador = lector.nextLine();
        System.out.println();

        TableroTateti tableroTateti = new TableroTateti();
        System.out.println("Ficha de "+ nombreJugador + ": X");
        System.out.println("Ficha de la máquina: O");
        System.out.println();

        Jugador jugadorPersona = new JugadorPersona(FICHA_JUGADOR, nombreJugador);
        Jugador jugadorComputadora = new JugadorMaquina(FICHA_COMPUTADORA);

        LocalDateTime fechaYHoraInicio = LocalDateTime.now();

        for(int i = 0; i < 9 && !tableroTateti.hayGanador() ; i++) {
            System.out.println();
            tableroTateti.mostrarTablero();
            System.out.println();
            if(i%2 == 0) {
                jugadorPersona.jugar(tableroTateti);
            } else {
                jugadorComputadora.jugar(tableroTateti);
            }
        }

        tableroTateti.mostrarTablero();

        int resultadoPartida = 0; //Se queda en 0 si el jugador pierde

        if(tableroTateti.hayGanador()) {
            System.out.println("TATETI!!!");

            if(elJugadorEsGanador(tableroTateti)) {
                resultadoPartida = 1; //Cambia a 1 si se detecta que el jugador gana
                System.out.println("El ganador es " + nombreJugador);
            } else {
                System.out.println("El ganador es la computadora u.u");
            }
        } else {
            System.out.println("Empate");
            resultadoPartida = 2; //Cambia a 2 si se detecta empate
        }

        LocalDateTime fechaYHoraFin = LocalDateTime.now();

        String insertQuery = "INSERT INTO tateti.info_partidas (nombre_jugador, inicio_partida, fin_partida, ganador, idioma_elegido)\n" +
                "VALUES ('"+ nombreJugador + "', '" + fechaYHoraInicio + "', '" + fechaYHoraFin + "', " + resultadoPartida + ", " + codigoLenguajeSeleccionado + ")";

        try {
            miConexion.createStatement().executeUpdate(insertQuery);
        } catch (SQLException e) {
            System.err.println("Hubo un error al guardar los datos de la partida.");
        }
    }

    private static boolean elJugadorEsGanador(TableroTateti tableroTateti) {
        return tableroTateti.getUltimaFichaJugada() == FICHA_JUGADOR;
    }




}
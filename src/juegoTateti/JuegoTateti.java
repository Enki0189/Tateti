package juegoTateti;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Scanner;

public class JuegoTateti {

    // Datos conexion base de datos
    public static final String URL_DB = "jdbc:mysql://localhost:3306/tateti";
    public static final String USUARIO_DB = "root";
    public static final String PASSWORD_DB = "010420";

    private static final int CODIGO_IDIOMA_ESP = 1;    

    public static final char FICHA_JUGADOR = 'X';
    public static final char FICHA_COMPUTADORA = 'O';

    private static Scanner lector = new Scanner(System.in);

    private static int codigoLenguajeSeleccionado = CODIGO_IDIOMA_ESP; // Español por defecto

    public static void main(String[] args) {
        try {
            Connection miConexion = DriverManager.getConnection(URL_DB, USUARIO_DB, PASSWORD_DB);

            System.out.println("Bienvenido a sistema de base de datos de jugadas de TATETI.");

            boolean terminar = false;

            while (!terminar) {
                mostrarMenuPrincipal(miConexion);
                int opcionElegida = leerOpcion(miConexion);

                switch (opcionElegida) {
                    case 1:
                        mostrarIdiomasDisponibles(miConexion);
                        break;
                    case 2:
                        jugar(miConexion);
                        break;
                    case 3:
                    	//modificar esta funci�n
                        mostrarPartidasJugador(miConexion);
                        break;
                    case 4:
                    	mostrarPartidasJugador(miConexion); 
                        break;
                    case 5:
                    	mostrarMensajesDelSistema(miConexion);
                    	break;
                    case 6:
                        terminar = true;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("No conecta");
            e.printStackTrace();
        }
    }
    
    private static ResultSet generarMensajes(int codigoLenguajeSeleccionado, Connection miConexion) throws SQLException {
    	Statement statement = miConexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    	ResultSet resultSetMensajes = statement.executeQuery("SELECT * FROM mensajes WHERE cod_idioma = " + codigoLenguajeSeleccionado);
    	return resultSetMensajes;
    }

    private static int leerOpcion(Connection miConexion) throws SQLException {
    	ResultSet resultSetMensajes = generarMensajes(codigoLenguajeSeleccionado, miConexion);
    	resultSetMensajes.absolute(25);
        int opcionElegida = lector.nextInt();
        while (opcionElegida < 1 || opcionElegida > 5) {
        	System.out.println(resultSetMensajes.getString("mensaje"));
			mostrarMenuPrincipal(miConexion);
            opcionElegida = lector.nextInt();
        }
        return opcionElegida;
    }

    private static void mostrarMenuPrincipal(Connection miConexion) throws SQLException {
    	ResultSet resultSetMensajes = generarMensajes(codigoLenguajeSeleccionado, miConexion);
        System.out.println();
        resultSetMensajes.absolute(13);
        System.out.println("1 - " + resultSetMensajes.getString("mensaje"));
        resultSetMensajes.absolute(14);
        System.out.println("2 - " + resultSetMensajes.getString("mensaje"));
        resultSetMensajes.absolute(15);
        System.out.println("3 - " + resultSetMensajes.getString("mensaje"));
        resultSetMensajes.absolute(16);
        System.out.println("4 - " + resultSetMensajes.getString("mensaje"));
        //agregar este mensaje a la BD
        //resultSetMensajes.absolute(33);
        System.out.println("5 - " + "Ver un mensaje seg�n su c�digo");
        resultSetMensajes.absolute(17);
        System.out.println("6 - " + resultSetMensajes.getString("mensaje"));
        

        System.out.println();
    }

    private static void mostrarPartidasJugador(Connection miConexion) throws SQLException {
    	ResultSet resultSetMensajes = generarMensajes(codigoLenguajeSeleccionado, miConexion);
    	resultSetMensajes.absolute(26);
        System.out.println();
        System.out.println(resultSetMensajes.getString("mensaje"));

        // Por alguna razon el siguiente nextLine el programa lo ignora y tuve que
        // ponerlo 2 veces
        lector.nextLine();
        String nombreDeJugador = lector.nextLine();

        System.out.println();

        ResultSet miResultSet = miConexion.createStatement()
                .executeQuery("SELECT * FROM info_partidas WHERE nombre_jugador = '" + nombreDeJugador + "'");

        while (miResultSet.next()) {
            System.out.println(miResultSet.getString("id_partida") + " " + miResultSet.getString("nombre_jugador") + " "
                    + miResultSet.getString("inicio_partida") + " " + miResultSet.getString("fin_partida") + " "
                    + miResultSet.getString("ganador") + " " + miResultSet.getString("idioma_elegido"));
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

        ResultSet miResultSet = miConexion.createStatement().executeQuery("SELECT * FROM mensajes where cod_idioma = "
                + codigoIdiomaSeleccionado + " AND cod_mensaje = " + codigoDeMensaje);

        // 4- Recorrer el ResultSet
        while (miResultSet.next()) {
            System.out.println(miResultSet.getString("mensaje"));
        }

    }

    private static void mostrarIdiomasDisponibles(Connection miConexion) throws SQLException {
    	ResultSet resultSetMensajes = generarMensajes(codigoLenguajeSeleccionado, miConexion);
    	resultSetMensajes.absolute(23);
    	System.out.println();
        System.out.println(resultSetMensajes.getString("mensaje"));
        System.out.println();
        
        for (int i= 19; i<23; i++) {
        	resultSetMensajes.absolute(i);
        	System.out.println((i-18)+ " - " + resultSetMensajes.getString("mensaje"));
        }
        
        /*ResultSet miResultSet = miConexion.createStatement().executeQuery("SELECT * FROM tabla_idiomas");
        while (miResultSet.next()) {
            System.out.println(miResultSet.getString("id_idioma") + " - " + miResultSet.getString("nombre_idioma"));
        }*/
        
        elegirIdioma(miConexion);
    }
    
    private static void elegirIdioma(Connection miConexion) throws SQLException {
    	ResultSet resultSetMensajes = generarMensajes(codigoLenguajeSeleccionado, miConexion);
    	resultSetMensajes.absolute(24);
    	System.out.println(resultSetMensajes.getString("mensaje"));
        int numeroIdioma = lector.nextInt();
        codigoLenguajeSeleccionado = numeroIdioma;
        ResultSet resultSetMensajes2 = generarMensajes(codigoLenguajeSeleccionado, miConexion);
    	resultSetMensajes2.absolute(18);
    	System.out.println(resultSetMensajes2.getString("mensaje"));
    }

    //borrar toda esta funci�n, creo
    private static void agregarMensajeNuevo(Connection miConexion) throws SQLException {
        System.out.println();
        System.out.print("Ingrese codigo del mensaje: ");
        int codigoDelMensaje = lector.nextInt();
        System.out.print("Ingrese codigo de idioma del mensaje: ");
        int codigoIdioma = lector.nextInt();
        System.out.print("Ingrese mensaje: ");
        lector.nextLine();
        String mensaje = lector.nextLine();

        miConexion.createStatement().executeUpdate(
                String.format("INSERT INTO mensajes values (%d, %d,'%s')", codigoDelMensaje, codigoIdioma, mensaje));

        System.out.println();
        System.out.print("Mensaje agregado exitosamente!");
    }

    private static void jugar(Connection miConexion) throws SQLException {
    	ResultSet resultSetMensajes = generarMensajes(codigoLenguajeSeleccionado, miConexion);
    	resultSetMensajes.absolute(2);
    	System.out.println(resultSetMensajes.getString("mensaje"));
        lector.nextLine(); // Por alguna razon el siguiente nextLine el programa lo ignora y tuve que
                           // ponerlo 2 veces
        String nombreJugador = lector.nextLine();
        System.out.println();

        TableroTateti tableroTateti = new TableroTateti();
        resultSetMensajes.absolute(27);
        System.out.println(resultSetMensajes.getString("mensaje") + " " + nombreJugador + ": X");
        resultSetMensajes.absolute(28);
        System.out.println(resultSetMensajes.getString("mensaje") +": O");
        System.out.println();

        Jugador jugadorPersona = new JugadorPersona(FICHA_JUGADOR, nombreJugador);
        Jugador jugadorComputadora = new JugadorMaquina(FICHA_COMPUTADORA);

        LocalDateTime fechaYHoraInicio = LocalDateTime.now();

        for (int i = 0; i < 9 && !tableroTateti.hayGanador(); i++) {
            System.out.println();
            tableroTateti.mostrarTablero();
            System.out.println();
            if (i % 2 == 0) {
                jugadorPersona.jugar(tableroTateti, miConexion, codigoLenguajeSeleccionado);
            } else {
                jugadorComputadora.jugar(tableroTateti, miConexion, codigoLenguajeSeleccionado);
            }
        }

        tableroTateti.mostrarTablero();

        int resultadoPartida = 0; // Se queda en 0 si el jugador pierde

        if (tableroTateti.hayGanador()) {
            System.out.println("TATETI!!!");

            if (elJugadorEsGanador(tableroTateti)) {
                resultadoPartida = 1; // Cambia a 1 si se detecta que el jugador gana
                resultSetMensajes.absolute(29);
                System.out.println(resultSetMensajes.getString("mensaje") + " " + nombreJugador);
            } else {
            	resultSetMensajes.absolute(30);
                System.out.println(resultSetMensajes.getString("mensaje") + " u.u");
            }
        } else {
        	resultSetMensajes.absolute(12);
            System.out.println(resultSetMensajes.getString("mensaje"));
            resultadoPartida = 2; // Cambia a 2 si se detecta empate
        }

        LocalDateTime fechaYHoraFin = LocalDateTime.now();

        String insertQuery = "INSERT INTO tateti.info_partidas (nombre_jugador, inicio_partida, fin_partida, ganador, idioma_elegido)\n"
                +
                "VALUES ('" + nombreJugador + "', '" + fechaYHoraInicio + "', '" + fechaYHoraFin + "', "
                + resultadoPartida + ", " + codigoLenguajeSeleccionado + ")";

        try {
            miConexion.createStatement().executeUpdate(insertQuery);
        } catch (SQLException e) {
        	resultSetMensajes.absolute(31);
            System.out.println(resultSetMensajes.getString("mensaje"));
        }
    }

    private static boolean elJugadorEsGanador(TableroTateti tableroTateti) {
        return tableroTateti.getUltimaFichaJugada() == FICHA_JUGADOR;
    }

}

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
    public static final String URL_DB = "jdbc:mysql://localhost:3306/tateti?serverTimezone=UTC";
    public static final String USUARIO_DB = "root";
    public static final String PASSWORD_DB = "*****";

    private static final int CODIGO_IDIOMA_ESP = 1;    

    public static final char FICHA_JUGADOR = 'X';
    public static final char FICHA_COMPUTADORA = 'O';

    private static Scanner lector = new Scanner(System.in);

    private static int codigoLenguajeSeleccionado = CODIGO_IDIOMA_ESP; // Espa√±ol por defecto

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
                        mostrarPartidas(miConexion);
                        break;
                    case 4:
                    	mostrarPartidasJugador(miConexion); 
                        break;
                    case 5:
                    	mostrarMensajesDelSistema(miConexion);
                    	break;
                    case 6:
                        terminar = true;
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("No conecta");
            e.printStackTrace();
        }
    }
    
    private static void tablaFilaInicial(Connection miConexion, String formatoAlineado) throws SQLException {
    	String nombreJugadorMenu = generarMensajeString(codigoLenguajeSeleccionado, miConexion, 43);
		String inicioPartidaMenu = generarMensajeString(codigoLenguajeSeleccionado, miConexion, 44);
		String finPartidaMenu = generarMensajeString(codigoLenguajeSeleccionado, miConexion, 45);
		String ganadorMenu = generarMensajeString(codigoLenguajeSeleccionado, miConexion, 46);
		String idiomaElegidoMenu = generarMensajeString(codigoLenguajeSeleccionado, miConexion, 47);
		
		System.out.format("+------+------------------+--------------------+--------------------+-----------+-----------------+%n");
		System.out.format(formatoAlineado, "ID", nombreJugadorMenu, inicioPartidaMenu, finPartidaMenu, ganadorMenu, idiomaElegidoMenu);
		System.out.format("+------+------------------+--------------------+--------------------+-----------+-----------------+%n");
    }
    
    private static void tablaContenido(Connection miConexion, String formatoAlineado, ResultSet miResultSet) throws SQLException {
    	String id = miResultSet.getString("id_partida");
		String nombre_jugador = miResultSet.getString("nombre_jugador");
		String inicio_partida = miResultSet.getString("inicio_partida");
		String fin_partida = miResultSet.getString("fin_partida");
		String ganador = miResultSet.getString("ganador");
		if (ganador.equals("0")) {
			ganador = "M·quina";
		} else if (ganador.equals("1")) {
			ganador = nombre_jugador;
		} else {
			ganador = "Empate";
		}
		String idioma = miResultSet.getString("idioma_elegido");
		if (idioma.equals("1")) {
			idioma = generarMensajeString(codigoLenguajeSeleccionado, miConexion, 19);
		} else if (idioma.equals("2")) {
			idioma = generarMensajeString(codigoLenguajeSeleccionado, miConexion, 20);
		} else if (idioma.equals("3")) {
			idioma = generarMensajeString(codigoLenguajeSeleccionado, miConexion, 21);
		} else if (idioma.equals("4")) {
			idioma = generarMensajeString(codigoLenguajeSeleccionado, miConexion, 22);
		}
		System.out.format(formatoAlineado, id, nombre_jugador, inicio_partida, fin_partida, ganador,idioma);
    }
    
    private static void mostrarPartidas(Connection miConexion) throws SQLException {
    	try {
    		System.out.println(" ");
    		System.out.println("1 - " + generarMensajeString(codigoLenguajeSeleccionado, miConexion, 34));
    		System.out.println("2 - " + generarMensajeString(codigoLenguajeSeleccionado, miConexion, 35));
    		int opcion = lector.nextInt();
    		String formatoAlineado = "| %-4s | %-16s | %-19s| %-19s| %-10s| %-16s| %n";
    		if (opcion == 1) {
	    		ResultSet miResultSet = miConexion.createStatement().executeQuery("SELECT * FROM info_partidas");
	    		tablaFilaInicial(miConexion, formatoAlineado);  
	    		while (miResultSet.next()) {
	    			tablaContenido(miConexion, formatoAlineado, miResultSet);
	    		}
	    		System.out.format("+------+------------------+--------------------+--------------------+-----------+-----------------+%n");
    		} else if (opcion == 2) {
    			System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 39));
    			int cantidad = lector.nextInt();
    			tablaFilaInicial(miConexion, formatoAlineado);    			
    			ResultSet miResultSet = miConexion.createStatement().executeQuery("SELECT * FROM info_partidas ORDER BY id_partida DESC LIMIT " + cantidad);
    			while (miResultSet.next()) {
    				tablaContenido(miConexion, formatoAlineado, miResultSet);
    			}
    			System.out.format("+------+------------------+--------------------+--------------------+-----------+-----------------+%n");
    		} else {
    			System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 40));
    		}
    		
    	} catch (Exception e) {
    		System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 41));
    	}
    }

	private static ResultSet generarMensajes(int codigoLenguajeSeleccionado, Connection miConexion) throws SQLException {
    	Statement statement = miConexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    	ResultSet resultSetMensajes = statement.executeQuery("SELECT * FROM mensajes WHERE cod_idioma = " + codigoLenguajeSeleccionado);
    	return resultSetMensajes;
    }
    
	protected static String generarMensajeString(int codigoLenguajeSeleccionado, Connection miConexion, int codigoMensaje) throws SQLException {
    	Statement statement = miConexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    	ResultSet resultSetMensajes = statement.executeQuery("SELECT * FROM mensajes WHERE cod_idioma = " + codigoLenguajeSeleccionado);
    	resultSetMensajes.absolute(codigoMensaje);
    	String mensaje = resultSetMensajes.getString("mensaje");
    	return mensaje;
    }

    private static int leerOpcion(Connection miConexion) throws SQLException {
        int opcionElegida = lector.nextInt();
        while (opcionElegida < 1 || opcionElegida > 6) {
        	System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 25));
			mostrarMenuPrincipal(miConexion);
            opcionElegida = lector.nextInt();
        }
        return opcionElegida;
    }

    private static void mostrarMenuPrincipal(Connection miConexion) throws SQLException {
        System.out.println();
        System.out.println("1 - " + generarMensajeString(codigoLenguajeSeleccionado, miConexion, 13));
        System.out.println("2 - " + generarMensajeString(codigoLenguajeSeleccionado, miConexion, 14));
        System.out.println("3 - " + generarMensajeString(codigoLenguajeSeleccionado, miConexion, 15));
        System.out.println("4 - " + generarMensajeString(codigoLenguajeSeleccionado, miConexion, 16));
        System.out.println("5 - " + generarMensajeString(codigoLenguajeSeleccionado, miConexion, 33));
        System.out.println("6 - " + generarMensajeString(codigoLenguajeSeleccionado, miConexion, 17));
        
        System.out.println();
    }

    private static void mostrarPartidasJugador(Connection miConexion) throws SQLException {
    	try {
    		System.out.println();
    		System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 26));

    		// Por alguna razon el siguiente nextLine el programa lo ignora y tuve que
    		// ponerlo 2 veces
    		lector.nextLine();
    		String nombreDeJugador = lector.nextLine();
    		System.out.println();
    		String formatoAlineado = "| %-4s | %-16s | %-19s| %-19s| %-10s| %-16s| %n";
    		ResultSet miResultSet = miConexion.createStatement()
                .executeQuery("SELECT * FROM info_partidas WHERE nombre_jugador = '" + nombreDeJugador + "'");
    		
    		
    		if (!miResultSet.isBeforeFirst()) {
    			System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 38));
    		} else {
    			tablaFilaInicial(miConexion, formatoAlineado); 
    			while (miResultSet.next()) {
    				tablaContenido(miConexion, formatoAlineado, miResultSet);
	    		}
    			System.out.format("+------+------------------+--------------------+--------------------+-----------+-----------------+%n");
    		}
    	} catch (Exception e) {
    		System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 38));
    	}
    }

    private static void mostrarMensajesDelSistema(Connection miConexion) throws SQLException {
        System.out.println();
        System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 36));
        int codigoIdiomaSeleccionado = lector.nextInt();

        System.out.println();
        System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 37));
        int codigoDeMensaje = lector.nextInt();
        System.out.println();

        ResultSet miResultSet = miConexion.createStatement().executeQuery("SELECT * FROM mensajes where cod_idioma = "
                + codigoIdiomaSeleccionado + " AND cod_mensaje = " + codigoDeMensaje);

        // 4- Recorrer el ResultSet
        if (!miResultSet.isBeforeFirst()) {
        	System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 42));
        } else {
	        while (miResultSet.next()) {
	            System.out.println(miResultSet.getString("mensaje"));
	        }
        }

    }

    private static void mostrarIdiomasDisponibles(Connection miConexion) throws SQLException {
    	System.out.println();
    	System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 23));
        System.out.println();
        
        ResultSet resultSetMensajes = generarMensajes(codigoLenguajeSeleccionado, miConexion);
        for (int i= 19; i<23; i++) {
        	resultSetMensajes.absolute(i);
        	System.out.println((i-18)+ " - " + resultSetMensajes.getString("mensaje"));
        }
        //lo modifiquÈ para que diga los idiomas en todos los idiomas
        /*ResultSet miResultSet = miConexion.createStatement().executeQuery("SELECT * FROM tabla_idiomas");
        while (miResultSet.next()) {
            System.out.println(miResultSet.getString("id_idioma") + " - " + miResultSet.getString("nombre_idioma"));
        }*/
        
        elegirIdioma(miConexion);
    }
    
    private static void elegirIdioma(Connection miConexion) throws SQLException {
    	System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 24));
        int numeroIdioma = lector.nextInt();
        codigoLenguajeSeleccionado = numeroIdioma;
        System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 18));
    }

    private static void jugar(Connection miConexion) throws SQLException {
    	System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 2));
        lector.nextLine(); // Por alguna razon el siguiente nextLine el programa lo ignora y tuve que
                           // ponerlo 2 veces
        String nombreJugador = lector.nextLine();
        System.out.println();

        TableroTateti tableroTateti = new TableroTateti();
        System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 27) + " " + nombreJugador + ": X");
        System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 28) +": O");
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
                System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 29) + " " + nombreJugador);
            } else {
            	System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 30) + " u.u");
            }
        } else {
        	System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 12));
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
        	System.out.println(generarMensajeString(codigoLenguajeSeleccionado, miConexion, 31));
        }
    }

    private static boolean elJugadorEsGanador(TableroTateti tableroTateti) {
        return tableroTateti.getUltimaFichaJugada() == FICHA_JUGADOR;
    }

}

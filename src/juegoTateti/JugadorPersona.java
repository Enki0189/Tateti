package juegoTateti;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JugadorPersona extends Jugador {

    private String nombre;
    private Scanner lector = new Scanner(System.in);

    public JugadorPersona(char ficha, String nombre) {
        super(ficha);
        this.nombre = nombre;
    }

    @Override
    public void jugar(TableroTateti tableroTateti, Connection miConexion, int codigoLenguajeSeleccionado) throws SQLException {
    	Statement statement = miConexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    	ResultSet resultSetMensajes = statement.executeQuery("SELECT * FROM mensajes WHERE cod_idioma = " + codigoLenguajeSeleccionado);
    	resultSetMensajes.absolute(32);
    	System.out.println(resultSetMensajes.getString("mensaje") + nombre);
        System.out.println();
        boolean posicionJugada = true;
        do {
        	resultSetMensajes.absolute(5);
        	System.out.println(resultSetMensajes.getString("mensaje")+" (1 - 3):");
            int filaJugador = Integer.parseInt(lector.nextLine()) - 1; //Se resta 1 ya que en codigo se cuenta a partir de 0
            resultSetMensajes.absolute(6);
        	System.out.println(resultSetMensajes.getString("mensaje")+" (1 - 3):");
            int columnaJugada = Integer.parseInt(lector.nextLine()) - 1; //Se resta 1 ya que en codi go se cuenta a partir de 0

            if (filaJugador < 0 || filaJugador > 2 || columnaJugada < 0 || columnaJugada > 2) {
                resultSetMensajes.absolute(7);
                System.out.println(resultSetMensajes.getString("mensaje"));
                posicionJugada = false;
            } else {
                posicionJugada = tableroTateti.colocarFicha(new Posicion(filaJugador, columnaJugada), this.getFicha());
                if(!posicionJugada) {
                    resultSetMensajes.absolute(8);
                    System.out.println(resultSetMensajes.getString("mensaje"));
                }
            }

        }
        while(posicionJugada == false);
        System.out.println();
    }
}

package juegoTateti;

import java.sql.Connection;
import java.sql.SQLException;
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
        try {
            System.out.println(JuegoTateti.generarMensajeString(codigoLenguajeSeleccionado, miConexion, 32) + " " + nombre);
            System.out.println();
            boolean posicionJugada = true;
            do {
                System.out.println(JuegoTateti.generarMensajeString(codigoLenguajeSeleccionado, miConexion, 5) + " (1 - 3):");
                int filaJugador = Integer.parseInt(lector.nextLine()) - 1; //Se resta 1 ya que en codigo se cuenta a partir de 0
                System.out.println(JuegoTateti.generarMensajeString(codigoLenguajeSeleccionado, miConexion, 6)+" (1 - 3):");
                int columnaJugada = Integer.parseInt(lector.nextLine()) - 1; //Se resta 1 ya que en codi go se cuenta a partir de 0
                if (filaJugador < 0 || filaJugador > 2 || columnaJugada < 0 || columnaJugada > 2) {
                    System.out.println(JuegoTateti.generarMensajeString(codigoLenguajeSeleccionado, miConexion, 8));
                    posicionJugada = false;
                } else {
                    posicionJugada = tableroTateti.colocarFicha(new Posicion(filaJugador, columnaJugada), this.getFicha());
                    if (!posicionJugada) {
                        System.out.println(JuegoTateti.generarMensajeString(codigoLenguajeSeleccionado, miConexion, 8));
                    }
                }

            }
            while (posicionJugada == false);
            System.out.println();
        } catch (Exception e) {
            System.out.println("jugada erronea"); //no aparece el mensaje en consola
        }
    }
}

package juegoTateti;

import java.util.Scanner;

public class JugadorPersona extends Jugador {

    private String nombre;
    private Scanner lector = new Scanner(System.in);

    public JugadorPersona(char ficha, String nombre) {
        super(ficha);
        this.nombre = nombre;
    }

    @Override
    public void jugar(TableroTateti tableroTateti) {
        System.out.println("Turno de " + nombre);
        System.out.println();
        boolean posicionJugada = true;
        do {
            System.out.println("Ingrese fila de la jugada (1 - 3)");
            int filaJugador = Integer.parseInt(lector.nextLine()) - 1; //Se resta 1 ya que en codigo se cuenta a partir de 0
            System.out.println("Ingrese columna de la jugada (1 - 3)");
            int columnaJugada = Integer.parseInt(lector.nextLine()) - 1; //Se resta 1 ya que en codigo se cuenta a partir de 0
            posicionJugada = tableroTateti.colocarFicha(new Posicion(filaJugador, columnaJugada), this.getFicha());
            if(!posicionJugada) {
                System.out.println("La posicion jugada esta ocupada. Ingrese nuevamente en una posicion libre.");
            }
        }
        while(posicionJugada == false);
        System.out.println();
    }
}

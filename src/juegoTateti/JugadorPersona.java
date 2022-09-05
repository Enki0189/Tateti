package juegoTateti;

import java.util.Scanner;

public class JugadorPersona extends Jugador {

    private Scanner lector = new Scanner(System.in);

    public JugadorPersona(char ficha) {
        super(ficha);
    }

    @Override
    public void jugar(Tateti tateti) {
        System.out.println("Turno jugador");
        System.out.println();
        boolean posicionJugada = true;
        do {
            System.out.println("jugador ingrese fila de la jugada");
            int filaJugador = Integer.parseInt(lector.nextLine());
            System.out.println("jugador ingrese columna de la jugada");
            int columnaJugada = Integer.parseInt(lector.nextLine());
            posicionJugada = tateti.colocarFicha(new Posicion(filaJugador, columnaJugada), this.getFicha());
            if(!posicionJugada) {
                System.out.println("La posicion jugada esta ocupada. Ingrese nuevamente en una posicion libre.");
            }
        }
        while(posicionJugada == false);
        System.out.println();
    }
}

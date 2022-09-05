package juegoTateti;

import java.util.Random;

public class JugadorMaquina extends Jugador {

    private Random generadorNumeros = new Random();

    public JugadorMaquina(char ficha) {
        super(ficha);
    }

    @Override
    public void jugar(Tateti tateti) {
        System.out.println("Jugada de la computadora:");
        System.out.println();
        boolean posicionJugada = true;
        do {
            int filaJugador = generadorNumeros.nextInt(3);
            int columnaJugada = generadorNumeros.nextInt(3);
            posicionJugada = tateti.colocarFicha(new Posicion(filaJugador, columnaJugada), this.getFicha());
        }
        while(posicionJugada == false);
    }

}

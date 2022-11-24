package juegoTateti;

public abstract class Jugador {
    private char ficha;


    public Jugador(char ficha) {
        this.ficha = ficha;
    }

    public abstract void jugar(TableroTateti tableroTateti);

    public char getFicha() {
        return this.ficha;
    }
}

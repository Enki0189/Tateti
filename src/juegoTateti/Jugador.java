package juegoTateti;

public abstract class Jugador {
    private char ficha;


    public Jugador(char ficha) {
        this.ficha = ficha;
    }

    public abstract void jugar(Tateti tateti);

    public char getFicha() {
        return this.ficha;
    }
}

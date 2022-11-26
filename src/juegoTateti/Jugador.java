package juegoTateti;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Jugador {
    private char ficha;


    public Jugador(char ficha) {
        this.ficha = ficha;
    }

    public abstract void jugar(TableroTateti tableroTateti, Connection miConexion, int codigoLenguajeSeleccionado) throws SQLException ;

    public char getFicha() {
        return this.ficha;
    }
}

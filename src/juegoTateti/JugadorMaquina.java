package juegoTateti;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class JugadorMaquina extends Jugador {

    private Random generadorNumeros = new Random();

    public JugadorMaquina(char ficha) {
        super(ficha);
    }

	@Override
    public void jugar(TableroTateti tableroTateti, Connection miConexion, int codigoLenguajeSeleccionado) throws SQLException {
    	System.out.println(JuegoTateti.generarMensajeString(codigoLenguajeSeleccionado, miConexion, 9));
        System.out.println();
        boolean posicionJugada = true;
        do {
            int filaJugador = generadorNumeros.nextInt(3);
            int columnaJugada = generadorNumeros.nextInt(3);
            posicionJugada = tableroTateti.colocarFicha(new Posicion(filaJugador, columnaJugada), this.getFicha());
        }
        while(posicionJugada == false);
    }

}

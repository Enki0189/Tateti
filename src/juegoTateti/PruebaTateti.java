package juegoTateti;

public class PruebaTateti {

    public static void main(String[] args) {
        Tateti tateti = new Tateti();
        System.out.println("Ficha jugador: X");
        System.out.println("Ficha máquina: O");
        System.out.println();
        Jugador jugadorPersona = new JugadorPersona('X');
        Jugador jugadorComputadora = new JugadorMaquina('O');

        for(int i = 0; i < 9 && !tateti.hayGanador() ; i++) {
            System.out.println();
            tateti.mostrarTablero();
            System.out.println();
            if(i%2 == 0) {
                jugadorPersona.jugar(tateti);
            } else {
                jugadorComputadora.jugar(tateti);
            }
        }

        tateti.mostrarTablero();

        if(tateti.hayGanador()) {
            System.out.println("TATETI!!!");
            System.out.println("El ganador es " + mostrarNombreGanador(tateti));
        } else {
            System.out.println("Empate");
        }

    }

    private static String mostrarNombreGanador(Tateti tateti) {
        if(tateti.getUltimaFichaJugada() == 'X') {
            return "el jugador";
        }
        return "la computadora";
    }




}

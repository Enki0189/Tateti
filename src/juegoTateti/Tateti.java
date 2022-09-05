package juegoTateti;

public class Tateti {

    private static final int PERIMETRO_TATETI = 3;
    private char tablero [][];

    private char ultimaFichaJugada = ' ';

    public Tateti() {
        tablero = new char [][]{{'-','-','-'},{'-','-','-'},{'-','-','-'}};
    }

    public void mostrarTablero() {
        for (int i=0; i<tablero.length; i++) {
            for (int j=0; j<tablero[i].length; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println("");
        }

    }

    public boolean colocarFicha (Posicion posicion, char ficha) {
        //Si detecta poscion ocupada retorna falso.
        if(posicionOcupada(posicion))
            return false;

        //Inserta ficha en tablero y retorna verdadero ya que la fcha pudo ser colocarda
        tablero[posicion.getFila()][posicion.getColumna()] = ficha;
        ultimaFichaJugada = ficha;
        return true;
    }


    private boolean posicionOcupada(Posicion posicion) {
        if (posicion.getFila() >= PERIMETRO_TATETI || posicion.getColumna() >= PERIMETRO_TATETI ||
                this.tablero [posicion.getFila()][posicion.getColumna()] != '-') {
            return true;
        }
        return false;
    }

    public boolean hayGanador() {
        return verificarGanadorEnFilas() ||
            verificarGanadorEnColumnas() ||
            verificarGanadorEnDiagonales();
    }

    private boolean verificarGanadorEnDiagonales() {
        return verificarGanadorEnDiagonalPrincipal() ||
               verificarGanadorEnDiagonalInvertida();
    }

    private boolean verificarGanadorEnDiagonalInvertida() {
        return tablero[0][2] == ultimaFichaJugada &&
                tablero[1][1] == ultimaFichaJugada &&
                tablero[2][0] == ultimaFichaJugada;
    }

    private boolean verificarGanadorEnDiagonalPrincipal() {
        return tablero[0][0] == ultimaFichaJugada &&
                tablero[1][1] == ultimaFichaJugada &&
                tablero[2][2] == ultimaFichaJugada;
    }

    private boolean verificarGanadorEnColumnas() {
        for(int columna = 0; columna < PERIMETRO_TATETI; columna++) {
            if(tablero[0][columna] == ultimaFichaJugada &&
                    tablero[1][columna] == ultimaFichaJugada &&
                    tablero[2][columna] == ultimaFichaJugada) {
               return true;
            }
        }
        return false;
    }

    private boolean verificarGanadorEnFilas() {
        for(int fila = 0; fila < PERIMETRO_TATETI; fila++) {
            if(tablero[fila][0] == ultimaFichaJugada &&
                    tablero[fila][1] == ultimaFichaJugada &&
                    tablero[fila][2] == ultimaFichaJugada) {
                return true;
            }
        }
        return false;
    }

    public char getUltimaFichaJugada() {
        return ultimaFichaJugada;
    }
}

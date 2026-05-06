package gal.uvigo.esei.aed1.cubirds.core;

import es.uvigo.esei.aed1.tads.list.LinkedList;
import es.uvigo.esei.aed1.tads.list.List;

public class Table {

    /*
     * Se barajarán las cartas y se colocarán en la mesa 4 filas con 3 cartas de
     * pájaro cada una, no puede
     * haber pájaros de la misma especie en la misma fila. En tal caso, se siguen
     * sacando cartas hasta
     * conseguir 3 especies distintas, las cartas no utilizadas se devuelven de
     * nuevo al final de la baraja.
     */

    private List<Card>[] filas; // para filas y columnas en el inicializar()

    // Constructor
    public Table() {
        this.filas = new LinkedList[4];

        for (int i = 0; i < 4; i++) { // Crear cada fila de la mesa :D
            this.filas[i] = new LinkedList<>(); // Creamos cada fila individual
        }
    }

    public void inicializarMesa(DeckOfCards deck) {

        for (int i = 0; i < 4; i++) { // Por cada una de las 4 filas, le añadimos 3 cartas

            this.filas[i].clear();

            while (this.filas[i].size() < 3) { // Se pone 3 porque hay que añadir 3 cartas a cada fila.

                Card candidate = deck.takeFirstCard();

                if (!tipoRepetidoEnFila(i, candidate)) {
                    this.filas[i].addLast(candidate);
                } else {
                    deck.addLast(candidate); // Movemos candidate al otro lado de la baraja
                }
            }

        }
    }

    private boolean tipoRepetidoEnFila(int numFila, Card candidate) {
        boolean repetido = false;
        for (Card c : this.filas[numFila]) {
            if (c.getTypeBird() == candidate.getTypeBird()) {
                repetido = true;
            }
        }
        return repetido;
    }

    public int getRowCount() {
        return this.filas.length;
    }

    public List<Card> placeCardsOnRow(List<Card> cardsToPlay, int rowIndex, boolean placeLeft) {
        List<Card> capturedCards = new LinkedList<>();

        if (cardsToPlay == null || cardsToPlay.isEmpty()) {
            return capturedCards;
        }

        List<Card> row = filas[rowIndex];
        TypeBird species = cardsToPlay.get(0).getTypeBird();

        boolean hasSameTypeBird = false;
        for (int i = 0; i < row.size() && !hasSameTypeBird; i++) {
            if (row.get(i).getTypeBird() == species) {
                hasSameTypeBird = true;
            }
        }

        if (placeLeft) {
            for (int i = cardsToPlay.size() - 1; i >= 0; i--) {
                row.addFirst(cardsToPlay.get(i));
            }
        } else {
            for (int i = 0; i < cardsToPlay.size(); i++) {
                row.addLast(cardsToPlay.get(i));
            }
        }

        if (hasSameTypeBird) {
            int firstPos = -1;
            int lastPos = -1;

            for (int i = 0; i < row.size(); i++) {
                if (row.get(i).getTypeBird() == species) {
                    if (firstPos == -1) {
                        firstPos = i;
                    }
                    lastPos = i;
                }
            }

            if (firstPos + 1 < lastPos) {
                for (int i = lastPos - 1; i > firstPos; i--) {
                    capturedCards.addFirst(row.remove(i));
                }
            }
        }

        return capturedCards;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\nMesa: \n");
        for (int i = 0; i < filas.length; i++) { // Para todas las filas
            sb.append("Fila ").append(i + 1).append(": ");
            for (int j = 0; j < filas[i].size(); j++) { // En cada fila, todas las cartas
                sb.append(filas[i].get(j).toString()).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
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

    // Prepara las filas de la mesa para el inicio del juego.
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

    // Devuelve true si el tipo de pájaro de la carta candidata ya está presente en
    // la fila y false si no.
    private boolean tipoRepetidoEnFila(int numFila, Card candidate) {
        for (Card c : this.filas[numFila]) {
            if (c.getTypeBird() == candidate.getTypeBird()) {
                return true;
            }
        }
        return false;
    }

    // Devuelve el número de filas de la mesa.
    public int getRowCount() {
        return this.filas.length;
    }

    // Coloca una lista de cartas proporcionada en una fila y lado específicos.
    public List<Card> placeCardsOnRow(List<Card> cardsToPlay, int rowIndex, boolean placeLeft) {
        List<Card> capturedCards = new LinkedList<>();

        if (cardsToPlay == null || cardsToPlay.isEmpty())
            return capturedCards; // comprobamos invariantes de clase

        List<Card> row = filas[rowIndex]; // fila a la que colocamos las cartas

        TypeBird species = cardsToPlay.get(0).getTypeBird(); // comenzamos siempre por la primera carta

        int oldSize = row.size(); // el viejo tamaño de la fila antes de colocar las cartas, para saber hasta
                                  // dónde se extienden las cartas que colocamos
        int matchIndex = -1; // index inválido para que estemos seguros de que al salir se cambia

        if (placeLeft) {
            // Si colocamos a la izquierda, buscamos el primer match desde el principio de
            // la fila
            for (int i = 0; i < oldSize && matchIndex == -1; i++) {
                if (row.get(i).getTypeBird() == species) {
                    matchIndex = i;
                }
            }
        } else {
            // Si colocamos a la derecha, buscamos el primer match desde el final de la fila
            for (int i = oldSize - 1; i >= 0 && matchIndex == -1; i--) {
                if (row.get(i).getTypeBird() == species) {
                    matchIndex = i;
                }
            }
        }
        if (placeLeft) {
            for (int i = cardsToPlay.size() - 1; i >= 0; i--) {
                // Si colocamos a la izquierda, añadimos las cartas en orden inverso para que la
                // primera carta de cardsToPlay quede más a la izquierda
                row.addFirst(cardsToPlay.get(i));
            }
        } else {
            for (int i = 0; i < cardsToPlay.size(); i++) {
                // Si colocamos a la derecha, añadimos las cartas en orden normal para que la
                // primera carta de cardsToPlay quede más a la derecha
                row.addLast(cardsToPlay.get(i));
            }
        }

        if (matchIndex != -1) {
            // importancia del index erróneo
            if (placeLeft && matchIndex > 0) {
                // Si colocamos a la izquierda y hay cartas a la izquierda del match, las
                // capturamos
                int removeStart = cardsToPlay.size();
                int removeEnd = cardsToPlay.size() + matchIndex - 1;
                for (int i = removeEnd; i >= removeStart; i--) {
                    // for para el tema de las cartas que eliminamos, lo hacemos al revés para no
                    // tener problemas con los índices al eliminar
                    capturedCards.addFirst(row.remove(i));
                }
            }

            if (!placeLeft && matchIndex < oldSize - 1) {
                // Si colocamos a la derecha y hay cartas a la derecha del match, las capturamos
                int removeStart = matchIndex + 1;
                int removeEnd = oldSize - 1;
                for (int i = removeEnd; i >= removeStart; i--) {
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
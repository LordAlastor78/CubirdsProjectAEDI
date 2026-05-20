package gal.uvigo.esei.aed1.cubirds.core;

import es.uvigo.esei.aed1.tads.list.LinkedList;
import es.uvigo.esei.aed1.tads.list.List;

public class Table {

    // La mesa está conformada por 4 filas de cartas. Al inicializarla, cada una de
    // estas tendrá tres cartas de diferentes especies.

    private List<Card>[] filas; // para filas y columnas en el inicializar()

    // Constructor
    public Table() {
        this.filas = new LinkedList[4];

        for (int i = 0; i < 4; i++) { // Crear cada fila de la mesa :D
            this.filas[i] = new LinkedList<>(); // Creamos cada fila individual
        }
    }

    // Coloca tres cartas de diferentes especies en cada fila.
    public void inicializarMesa(DeckOfCards deck) {

        for (int i = 0; i < 4; i++) { // Por cada una de las 4 filas, le añadimos 3 cartas
            while (this.filas[i].size() < 3) { // Se pone 3 porque hay que añadir 3 cartas a cada fila.

                Card candidate = deck.takeFirstCard();

                if (!tipoRepetidoEnFila(i, candidate)) {
                    // Si el tipo de pájaro de candidate no está repetido en la fila i, se añade a
                    // la fila i.
                    this.filas[i].addLast(candidate);
                } else {
                    deck.addLast(candidate); // Movemos candidate al otro lado de la baraja
                }
            }

        }
    }

    // Devuelve un booleano que indica si el tipo de pájaro de la carta "candidate"
    // ya está presente en la fila "numFila".
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

    // Coloca las cartas de "cardsToPlay" en el lugar adecuado de la fila
    // "rowIndex", y devuelve las cartas que hayan sido capturadas.
    // Estas cartas capturadas son las que están entre las cartas colocadas y la
    // primera o última carta de la misma especie que haya en la fila.
    // (primera o última según placeLeft).
    public List<Card> placeCardsOnRow(List<Card> cardsToPlay, int rowIndex, boolean placeLeft) {

        // Lista de cartas capturadas, que se devuelve al final del método.
        List<Card> capturedCards = new LinkedList<>();

        if (cardsToPlay == null || cardsToPlay.isEmpty()) {
            return capturedCards;
        }

        List<Card> row = filas[rowIndex]; // Fila en las que colocamos
        TypeBird species = cardsToPlay.get(0).getTypeBird(); // especie que elegimos

        int oldSize = row.size(); // tamaño de la fila antes de colocar las cartas, para saber hasta dónde buscar
                                 // la carta de la misma especie
        int matchIndex = -1; // -1 si no se encuentra la carta de la misma especie

        // 
        if (placeLeft) {
            for (int i = 0; i < oldSize && matchIndex == -1; i++) {
                if (row.get(i).getTypeBird() == species) {
                    matchIndex = i;
                }
            }
        } else {
            for (int i = oldSize - 1; i >= 0 && matchIndex == -1; i--) {
                if (row.get(i).getTypeBird() == species) {
                    matchIndex = i;
                }
            }
        }

        //recorremos las cartas a colocar
        if (placeLeft) {
            for (int i = 0; i < cardsToPlay.size(); i++) {
                row.addFirst(cardsToPlay.get(i));
            }
        } else {
            for (int i = 0; i < cardsToPlay.size(); i++) {
                row.addLast(cardsToPlay.get(i));
            }
        }

        if (matchIndex != -1) {
            if (placeLeft && matchIndex > 0) {
                int removeStart = cardsToPlay.size();
                int removeEnd = cardsToPlay.size() + matchIndex - 1;
                for (int i = removeEnd; i >= removeStart; i--) {
                    capturedCards.addFirst(row.remove(i));
                }
            }

            if (!placeLeft && matchIndex < oldSize - 1) {
                int removeStart = matchIndex + 1;
                int removeEnd = oldSize - 1;
                for (int i = removeEnd; i >= removeStart; i--) {
                    capturedCards.addFirst(row.remove(i));
                }
            }
        }

        return capturedCards;
    }

    // Si la fila "rowIndex" tiene menos de dos especies diferentes, se añaden
    // cartas de la baraja a la fila hasta que tenga dos especies.
    public void ensureRowHasTwoSpecies(int rowIndex, DeckOfCards deck, DiscardedCards discardedCards) {
        List<Card> row = filas[rowIndex];
        boolean canDraw = true;

        // Mientras la fila tenga menos de dos especies diferentes, se añaden cartas de
        // la baraja a la fila.
        while (canDraw && !rowHasAtLeastTwoSpecies(row)) {
            if (deck.isEmpty() && !discardedCards.isEmpty()) {
                discardedCards.moveAllToDeck(deck);
            }
            // si la baraja está vacía y no tenemos descartadas, no robamos mas
            if (deck.isEmpty()) {
                canDraw = false;
            } else {
                row.addLast(deck.takeFirstCard());
            }
        }
    }

    // Devuelve un booleano que indica si la fila "row" tiene al menos dos especies
    // diferentes.
    private boolean rowHasAtLeastTwoSpecies(List<Card> row) {
        if (row.size() < 2) {
            return false;
        }

        TypeBird firstSpecies = row.get(0).getTypeBird();
        boolean different = false;

        for (int i = 1; i < row.size() && !different; i++) {
            if (row.get(i).getTypeBird() != firstSpecies) {
                different = true;
            }
        }

        return different;
    }

    // Método toString().
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
package gal.uvigo.esei.aed1.cubirds.core;

import es.uvigo.esei.aed1.tads.list.LinkedList;
import es.uvigo.esei.aed1.tads.list.List;

public class DeckOfCards {

    private final List<Card> deckOfCards;

    // Constructor de la baraja de cartas
    public DeckOfCards() {
        this.deckOfCards = new LinkedList<>();

        List<Card> allCards = new LinkedList<>();

        for (int i = 0; i < Card.values().length; i++) {
            allCards.addLast(Card.values()[i]);
        }

        // Barajamos las cartas
        while (allCards.size() > 0) {
            int randomCard = (int) (Math.random() * allCards.size());
            deckOfCards.addLast(allCards.remove(randomCard));
        }
    }

    // Elimina la primera carta de la baraja y la devuelve.
    public Card takeFirstCard() {
        return deckOfCards.removeFirst();
    }

    // Añade la carta dada al final de la baraja.
    public void addLast(Card card) {
        deckOfCards.addLast(card);
    }

    /*
     * 
     * Eliminados todos los métodos redundantes que no eran necesarios
     * 
     * public static Card takeFirstCard() { //
     * Card toReturn = deckOfCards.getFirst();
     * deckOfCards.removeFirst();
     * return toReturn;
     * }
     * 
     * public static Card removeLast() {
     * return deckOfCards.removeLast();
     * }
     * 
     * public static Card removeIndex(int i) {
     * return deckOfCards.remove(i);
     * }
     * 
     * public static void addFirst(Card card) {
     * deckOfCards.addFirst(card);
     * }
     * 
     * public static Card getFirstCard() {
     * return deckOfCards.get(0);
     * }
     * 
     * public static Card getLastCard() {
     * return deckOfCards.get(deckOfCards.size() - 1);
     * }
     * 
     */
}
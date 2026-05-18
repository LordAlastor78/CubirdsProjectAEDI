package gal.uvigo.esei.aed1.cubirds.core;

import es.uvigo.esei.aed1.tads.list.LinkedList;
import es.uvigo.esei.aed1.tads.list.List;

public class DeckOfCards {

    private final List<Card> deckOfCards;

    // Constructor de la baraja de cartas
    public DeckOfCards() {
        this.deckOfCards = new LinkedList<>();
        for (int i = 0; i < Card.values().length; i++) {
            deckOfCards.addLast(Card.values()[i]);
        }

        shuffle();
    }

    // Devuelve la primera carta de la baraja y la elimina de esta.
    public Card takeFirstCard() {
        return deckOfCards.removeFirst();
    }

    public boolean isEmpty() {
        return deckOfCards.isEmpty();
    }

    public int size() {
        return deckOfCards.size();
    }

    // Añade una carta al final de la baraja.
    public void addLast(Card card) {
        deckOfCards.addLast(card);
    }

    // Coloca aleatoriamente todas las cartas de la baraja.
    public void shuffle() {
        List<Card> shuffled = new LinkedList<>();

        while (!deckOfCards.isEmpty()) {
            int randomCard = (int) (Math.random() * deckOfCards.size());
            shuffled.addLast(deckOfCards.remove(randomCard));
        }

        while (!shuffled.isEmpty()) {
            deckOfCards.addLast(shuffled.removeFirst());
        }
    }
}
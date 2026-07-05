package gal.uvigo.esei.aed1.cubirds.core;

import es.uvigo.esei.aed1.tads.list.LinkedList;
import es.uvigo.esei.aed1.tads.list.List;

/**
 * CLASE Player — Representa UN jugador en la partida
 *
 * ESTRUCTURA DE LA MANO (MUY IMPORTANTE): - hand es una List<List<Card>> (lista de listas) - Cada
 * grupo contiene cartas del MISMO tipo (especie) - EJEMPLO: hand = [[FLAMENCO_1, FLAMENCO_2],
 * [TUCAN_3, TUCAN_5], [PATO_7]]
 *
 * ESTRUCTURA DE LA COLECCIÓN: - speciesCounters es un array int[] con 8 posiciones (uno por cada
 * especie) - Usa TypeBird.ordinal() como índice (FLAMENCO=0, TUCAN=1, etc.)
 */
public class Player {
    private String name;
    private List<List<Card>> hand; // Lista de grupos, cada grupo contiene cartas de la misma
                                   // especie.
    private int[] speciesCounters; // Contador de especies en la zona de juego.

    // Constructor
    public Player(String name) {
        this.name = name;
        this.hand = new LinkedList<>();
        this.speciesCounters = new int[TypeBird.values().length];
    }

    // Getters
    public String getName() {
        return this.name;
    }

    public int getHandSize() {
        int totalCards = 0;
        for (int i = 0; i < hand.size(); i++) {
            totalCards += hand.get(i).size();
        }
        return totalCards;
    }

    public int getCollectedSpeciesCount() {
        int total = 0;
        for (int i = 0; i < speciesCounters.length; i++) {
            if (speciesCounters[i] > 0) {
                total += 1;
            }
        }
        return total;
    }

    public int getCollectionSize() {
        int total = 0;
        for (int i = 0; i < speciesCounters.length; i++) {
            total += speciesCounters[i];
        }
        return total;
    }

    // Incrementa en uno el contador de una especie específica.
    public void incrementSpeciesCounter(TypeBird species) {

        int index = species.ordinal();
        /*
         * species.ordinal() devuelve el índice de la especie en el enum TypeBird, que coincide con
         * su posición en el array speciesCounters. por ejemplo FLAMENCO.ordinal() devuelve 0,
         * TUCAN.ordinal() devuelve 1, etc.
         */
        speciesCounters[index] = speciesCounters[index] + 1;

    }

    // Devuelve una lista con todas las especies presentes en la mano del jugador.
    public List<TypeBird> getPlayableSpecies() {
        List<TypeBird> species = new LinkedList<>();
        for (int i = 0; i < hand.size(); i++) {
            List<Card> group = hand.get(i);
            species.addLast(group.get(0).getTypeBird());
        }
        return species;
    }

    // Devuelve el número de cartas de una especie específica que tiene el jugador.
    public int getHandCountForSpecies(TypeBird species) {
        // vamos contnado el numero de especies que pasamos como parametro
        // que sería el tipo de pájaro de la carta que queremos jugar
        int index = findGroupIndex(species);
        if (index == -1) {
            return 0;
        }
        return hand.get(index).size();
    }

    // Devuelve el parámetro smallFlock de una especie específica que tiene el
    // jugador.
    public int getSmallFlockForSpecies(TypeBird species) {
        int index = findGroupIndex(species);
        if (index == -1) {
            return 0;
        }
        return hand.get(index).get(0).getSmallFlock();
    }

    // Añadir carta a la mano
    public void addCardToHand(Card card) {
        boolean added = false;

        for (int i = 0; i < hand.size() && !added; i++) {
            List<Card> group = hand.get(i);
            if (group.get(0).getTypeBird() == card.getTypeBird()) {
                group.addLast(card);
                added = true;
            }
        }

        if (!added) {
            List<Card> newGroup = new LinkedList<>();
            newGroup.addLast(card);
            hand.addLast(newGroup);
        }
    }

    // Añadir una lista de cartas a la mano.
    public void addCardsToHand(List<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            addCardToHand(cards.get(i));
        }
    }

    public void exchangeCard(Player other, int cardIndexOwn, int cardIndexOther) {
        if (other == null || other == this) {
            throw new IllegalArgumentException("El jugador destino no es válido.");
        }

        if (cardIndexOwn < 0 || cardIndexOwn >= this.getHandSize() || cardIndexOther < 0
                || cardIndexOther >= other.getHandSize()) {
            throw new IllegalArgumentException("Índices de carta fuera de rango.");
        }

        int[] posOwn = findGlobalIndexPosition(this.hand, cardIndexOwn);
        int[] posOther = findGlobalIndexPosition(other.hand, cardIndexOther);

        if (posOwn[0] == -1 || posOther[0] == -1) {
            throw new IllegalArgumentException(
                    "No se han podido localizar las cartas a intercambiar.");
        }

        Card ownCard = this.hand.get(posOwn[0]).remove(posOwn[1]);
        Card otherCard = other.hand.get(posOther[0]).remove(posOther[1]);

        if (this.hand.get(posOwn[0]).isEmpty()) {
            this.hand.remove(posOwn[0]);
        }

        if (other.hand.get(posOther[0]).isEmpty()) {
            other.hand.remove(posOther[0]);
        }

        this.addCardToHand(otherCard);
        other.addCardToHand(ownCard);
    }

    // Toma todas las cartas de una especie específica y las elimina de la mano.
    public List<Card> takeCardsOfSpecies(TypeBird species) {
        List<Card> toReturn = new LinkedList<>();
        int index = findGroupIndex(species);

        if (index != -1) {
            toReturn = hand.get(index);
            hand.remove(index);
        }

        return toReturn;
    }

    // Toma todas las cartas de la mano del jugador.
    public List<Card> takeAllCards() {
        List<Card> allCards = new LinkedList<>();

        for (int i = 0; i < hand.size(); i++) {
            List<Card> group = hand.get(i);
            for (int j = 0; j < group.size(); j++) {
                allCards.addLast(group.get(j));
            }
        }

        hand.clear();

        return allCards;
    }

    public boolean hasNoCards() {
        return hand.isEmpty();
    }

    // Devuelve el índice del grupo de cartas de una especie específica de la mano
    // del jugador.
    // Si no tiene cartas de esa especie, devuelve -1.
    private int findGroupIndex(TypeBird species) {
        int index = -1;
        for (int i = 0; i < hand.size() && index == -1; i++) {
            List<Card> group = hand.get(i);
            if (group.get(0).getTypeBird() == species) {
                index = i;
            }
        }
        return index;
    }

    private int[] findGlobalIndexPosition(List<List<Card>> playerHand, int globalIndex) {
        int currentAccumulator = 0;
        for (int i = 0; i < playerHand.size(); i++) {
            List<Card> speciesGroup = playerHand.get(i);
            if (globalIndex < currentAccumulator + speciesGroup.size()) {
                int internalIndex = globalIndex - currentAccumulator;
                return new int[] {i, internalIndex};
            }
            currentAccumulator += speciesGroup.size();
        }
        return new int[] {-1, -1};
    }

    public TypeBird getMostFrequentSpecies() {
        if (this.hand.isEmpty()) {
            return null;
        }

        TypeBird mostFrequent = null;
        int maxCards = -1;
        for (int i = 0; i < hand.size(); i++) {
            List<Card> group = hand.get(i);
            if (group.size() > maxCards) {
                maxCards = group.size();
                mostFrequent = group.get(0).getTypeBird();
            }
        }
        return mostFrequent;
    }

    // Método toString
    public String toString() {

        StringBuilder sb = new StringBuilder();
        if (this.hasNoCards()) {
            return "\nBaraja de " + this.getName() + ": Sin cartas!";
        } else {
            sb.append("\nBaraja de ").append(this.getName()).append(": ");

            for (int i = 0; i < hand.size(); i++) { // Por cada tipo de carta...
                List<Card> group = hand.get(i);
                for (int j = 0; j < group.size(); j++) {
                    Card card = group.get(j); // Me da una carta
                    sb.append("\n").append(card.toString());
                }
            }
        }

        return sb.toString();
    }

}

package gal.uvigo.esei.aed1.cubirds.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import es.uvigo.esei.aed1.tads.list.LinkedList;
import es.uvigo.esei.aed1.tads.list.List;

class ExamExtensionsTest {

    @Test
    void blockedRowPreventsPlacingCards() {
        Table table = new Table();
        List<Card> row = new LinkedList<>();
        row.addLast(Card.FLAMENCO_1);
        row.addLast(Card.TUCAN_1);
        table.setRow(0, row);
        table.blockRow(0);

        List<Card> played = new LinkedList<>();
        played.addLast(Card.FLAMENCO_2);

        List<Card> captured = table.placeCardsOnRow(played, 0, true);

        assertTrue(captured.isEmpty());
        assertEquals(2, table.getRow(0).size());
        assertTrue(table.isRowBlocked(0));
    }

    @Test
    void blockedRowsExpireAfterAdvancingTurns() {
        Table table = new Table();

        table.blockRow(1, 2);

        assertTrue(table.isRowBlocked(1));

        table.advanceBlockedRows();
        assertTrue(table.isRowBlocked(1));

        table.advanceBlockedRows();
        assertTrue(table.isRowBlocked(1));

        table.advanceBlockedRows();
        assertFalse(table.isRowBlocked(1));
    }

    @Test
    void countSpeciesOnTableCountsAllRows() {
        Table table = new Table();

        List<Card> row0 = new LinkedList<>();
        row0.addLast(Card.FLAMENCO_1);
        row0.addLast(Card.FLAMENCO_2);
        row0.addLast(Card.PATO_1);

        List<Card> row1 = new LinkedList<>();
        row1.addLast(Card.FLAMENCO_3);
        row1.addLast(Card.TUCAN_1);

        table.setRow(0, row0);
        table.setRow(1, row1);

        assertEquals(3, table.countSpeciesOnTable(TypeBird.FLAMENCO));
        assertEquals(1, table.countSpeciesOnTable(TypeBird.PATO));
        assertEquals(1, table.countSpeciesOnTable(TypeBird.TUCAN));
        assertEquals(0, table.countSpeciesOnTable(TypeBird.GUACAMAYO));
    }

    @Test
    void exchangeCardSwapsCardsAndRemovesEmptySpeciesGroups() {
        Player first = new Player("Ana");
        first.addCardToHand(Card.FLAMENCO_1);
        first.addCardToHand(Card.PATO_1);

        Player second = new Player("Luis");
        second.addCardToHand(Card.TUCAN_1);
        second.addCardToHand(Card.CURRUCA_1);

        first.exchangeCard(second, 0, 1);

        assertEquals(2, first.getHandSize());
        assertEquals(2, second.getHandSize());
        assertEquals(0, first.getHandCountForSpecies(TypeBird.FLAMENCO));
        assertEquals(0, second.getHandCountForSpecies(TypeBird.CURRUCA));
        assertEquals(1, first.getHandCountForSpecies(TypeBird.CURRUCA));
        assertEquals(1, second.getHandCountForSpecies(TypeBird.FLAMENCO));
    }

    @Test
    void mostFrequentSpeciesMatchesLargestGroup() {
        Player player = new Player("Marta");
        player.addCardToHand(Card.TUCAN_1);
        player.addCardToHand(Card.TUCAN_2);
        player.addCardToHand(Card.PATO_1);

        assertEquals(TypeBird.TUCAN, player.getMostFrequentSpecies());
    }
}

package gal.uvigo.esei.aed1.cubirds.core;

import es.uvigo.esei.aed1.tads.list.List;
// por fin arreglamos los imports :D
import gal.uvigo.esei.aed1.cubirds.iu.IU;

/**
 * CLASE Game — CONTROLADOR PRINCIPAL del juego CuBirds
 *
 * ¿QUÉ ES? - Gestiona el flujo completo de la partida - Controla jugadores,
 * mesa, baraja, descartes
 * - Ejecuta cada turno de cada jugador - Determina cuándo termina el juego y
 * quién gana
 *
 * FLUJO GENERAL DE LA PARTIDA: 1. play() se ejecuta (método principal) 2.
 * inicializarJugadores() →
 * pide nombres de jugadores 3. repartirCartas() → cada jugador recibe 5 cartas
 * (mesa ya
 * inicializada en constructor) 4. BUCLE PRINCIPAL: - Para cada jugador: -
 * executePlayerTurn() → el
 * jugador juega - ¿Ganó? (7 especies) → FIN - ¿Sin cartas en mano? →
 * handleEmptyHand() - ¿Hay
 * baraja? → Si no, el juego termina
 */
public class Game {

    // Atributos
    private IU iu;
    private DeckOfCards deck;
    private Table table;
    private Player[] players;
    private DiscardedCards discardedCards;

    public Game(IU iu) {
        this.iu = iu;
        this.deck = new DeckOfCards();
        this.table = new Table();
        this.table.inicializarMesa(this.deck);
        this.discardedCards = new DiscardedCards();
        this.players = null; // Se inicializa como null porque ya se convierte en un array en
                             // inicializarJugadores()
    }

    /**
     * INICIALIZA JUGADORES - Pide número y nombres al inicio del juego
     */
    private void inicializarJugadores() {
        int numJugadores;
        iu.displayMessage("  ¡Bienvenido a CuBirds!   ");
        iu.displayMessage("""

                    ░░░░░░░░▄▄▄▀▀▀▀▀▀▀▄▄███▄░░░░░░░░░░░░░░░░
                    ░░░░░▄▀▀░░░░░░░░░░▐░▀██▌░░░░░░░░░░░░░░░░
                    ░░░▄▀░░░░▄▄██████░▌▀▀░▀█░░░░░░░░░░░░░░░░
                    ░░▄█░░▄▀▀▒▒▒▒▒▒▒▒▄▐░░░░█▌░░░░░░░░░░░░░░░
                    ░▐█▀▄▀▄▄▄▄▀▀▀▀▀▀▀▀▌░░░░░▐█▄░░░░░░░░░░░░░
                    ░▌▄▄▀▀░░░░░░░░░░░░▌░░░░▄███████▄░░░░░░░░
                    ░░░░░░░░░░░░░░░░░▐░░░░▐███████████▄░░░░░
                    ░░░░░le░░░░░░░░░░░▐░░░░▐█████████████▄░░
                    ░░░░toucan░░░░░░░░░░▀▄░░░▐█████████████▄
                    ░░░░░░has░░░░░░░░░░░░▀▄▄███████████████░
                    ░░░░░arrivedd░░░░░░░░░░░░░░░█▀██████░░░░

                """); // toucan decorativo importante como símbolo del proyecto 𓅨

        do {
            numJugadores = iu.readNumber("¿Cuántos van a jugar? (2 a 5): ");
        } while (numJugadores < 2 || numJugadores > 5);

        this.players = new Player[numJugadores];
        for (int i = 0; i < numJugadores; i++) {
            String nombre;
            do {
                nombre = iu.readString("Nombre del jugador " + (i + 1) + ": ");
            } while (nombre == null || nombre.isBlank());
            this.players[i] = new Player(nombre.trim());
        }
    }

    /**
     * ¡¡ PUNTO DE ENTRADA PRINCIPAL DEL JUEGO !! Ejecuta el flujo completo:
     * inicializa, reparte,
     * bucle de turnos, determina ganador
     */
    public void play() {
        inicializarJugadores();
        repartirCartas();
        iu.displayMessage(table.toString());

        boolean gameFinished = false;
        int currentPlayerIndex = 0;

        while (!gameFinished) {
            Player currentPlayer = players[currentPlayerIndex];
            if (!currentPlayer.hasNoCards()) {
                executePlayerTurn(currentPlayer);
            }
            if (currentPlayer.getCollectedSpeciesCount() >= 7) {
                iu.displayMessage("\n¡¡" + currentPlayer.getName() + " ha ganado!!");
                gameFinished = true;
            } else if (currentPlayer.hasNoCards()) {
                boolean endGame = handleEmptyHand(currentPlayer);
                if (endGame)
                    gameFinished = true;
            }
            if (!gameFinished) {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
                iu.readString("\nPresiona una tecla para continuar...");
            }
        }
    }

    /**
     * Reparte 8 cartas a cada jugador al inicio del juego
     */
    private void repartirCartas() {
        for (Player player : this.players) {
            for (int i = 0; i < 8; i++) {
                player.addCardToHand(deck.takeFirstCard());
            }
        }
    }

    /**
     * Ejecuta UN TURNO COMPLETO de un jugador Proceso: elegir especie → elegir fila
     * → elegir lado →
     * colocar cartas → capturar → bajar especies → robar carta
     */
    public void executePlayerTurn(Player player) {
        iu.displayMessage("\nTurno de " + player.getName());
        iu.displayMessage(player.toString());
        iu.displayMessage(table.toString());

        List<TypeBird> playable = player.getPlayableSpecies();
        if (playable.isEmpty()) {
            iu.displayMessage("No tienes especies jugables. Turno saltado.");
            return;
        }

        TypeBird tipoElegido = iu.chooseBirdType(playable);
        int filaElegida = iu.chooseRow(table.getRowCount());
        boolean colocarIzquierda = iu.chooseSide();

        List<Card> cardsToPlay = player.takeCardsOfSpecies(tipoElegido);
        List<Card> capturedCards = table.placeCardsOnRow(cardsToPlay, filaElegida, colocarIzquierda);

        player.addCardsToHand(capturedCards);

        if (!capturedCards.isEmpty()) {
            table.ensureRowHasTwoSpecies(filaElegida, deck, discardedCards);
        }

        iu.displayMessage(table.toString());

        if (!player.hasNoCards()) {
            handleCollectionChoice(player);
        }
    }

    // Si el jugador quiere añadir una especie a su zona de juego, se verifica que
    // tiene suficientes cartas de esa especie,
    // las elimina de su mano, y suma uno a su contador de esa especie.
    private void handleCollectionChoice(Player player) {
        boolean wantsToCollect = iu.chooseYesNo("¿Deseas añadir una especie a tu zona de juego?");

        if (wantsToCollect) {
            TypeBird chosenSpecies = iu.chooseBirdType(player.getPlayableSpecies());
            int availableCards = player.getHandCountForSpecies(chosenSpecies);
            int requiredCards = player.getSmallFlockForSpecies(chosenSpecies);

            if (availableCards >= requiredCards) {
                player.incrementSpeciesCounter(chosenSpecies);
                List<Card> discarded = player.takeCardsOfSpecies(chosenSpecies);
                discardedCards.addCards(discarded);
                iu.displayMessage("Especie añadida a la zona de juego.");
            } else {
                iu.displayMessage("No es posible bajar esa especie a la zona de juego.");
            }
        }
    }

    // Si el jugador se queda sin cartas, se descartan todas las cartas de los demás
    // jugadores, se devuelven al mazo, y se reparten nuevas manos.
    // Si no es posible repartir nuevas manos, el jugador con más cartas en su
    // colección gana.
    private boolean handleEmptyHand(Player currentPlayer) {
        if (!currentPlayer.hasNoCards()) {
            return false;
        }

        iu.displayMessage(currentPlayer.getName() + " se ha quedado sin cartas.");
        iu.displayMessage("Los demas jugadores se descartan por completo.");

        for (int i = 0; i < players.length; i++) {
            if (players[i] != currentPlayer) {
                List<Card> discarded = players[i].takeAllCards();
                discardedCards.addCards(discarded);
            }
        }

        discardedCards.moveAllToDeck(deck);

        if (!canDealCards(8)) {
            Player winner = getWinnerByCollection();
            iu.displayMessage(winner.getName()
                    + " ha ganado la partida porque no ha sido posible realizar el reparto de cartas.");
            return true;
        }

        iu.displayMessage("Repartiendo nuevas manos...");
        for (Player p : this.players) {
            for (int i = 0; i < 8; i++) {
                p.addCardToHand(deck.takeFirstCard());
            }
        }
        iu.displayMessage("Reparto completado. Cada jugador tiene 8 cartas.");
        return false;
    }

    // Verifica si hay suficientes cartas en el mazo para repartir una nueva mano a
    // cada jugador.
    private boolean canDealCards(int cardsPerPlayer) {
        int totalCardsNeeded = cardsPerPlayer * players.length;
        return deck.size() >= totalCardsNeeded;
    }

    // Devuelve el jugador con más cartas en su colección.
    // En caso de empate, devuelve el primero de estos que se encuentre.
    private Player getWinnerByCollection() {
        Player winner = players[0];
        int bestScore = winner.getCollectionSize();

        for (int i = 1; i < players.length; i++) {
            int currentScore = players[i].getCollectionSize();
            if (currentScore > bestScore) {
                winner = players[i];
                bestScore = currentScore;
            }
        }

        return winner;
    }
}

package gal.uvigo.esei.aed1.cubirds.core;

import es.uvigo.esei.aed1.tads.list.List;
//por fin arreglamos los imports :D
import gal.uvigo.esei.aed1.cubirds.iu.IU;

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

    // Inicializa los jugadores preguntando cantidad y nombres.
    // Los jugadores se almacenan directamente en this.players.
    private void inicializarJugadores() {
        int numJugadores;

        // Mostrar tucán decorativo antes de preguntar el número de jugadores
        iu.displayMessage("       Cubirds CLI | GrupoG        ");
        iu.displayMessage("                                    ");
        iu.displayMessage("░░░░░░░░▄▄▄▀▀▀▄▄███▄░░░░░░░░░░░░░░░░");
        iu.displayMessage("░░░░░▄▀▀░░░░░░░▐░▀██▌░░░░░░░░░░░░░░░");
        iu.displayMessage("░░░▄▀░░░░▄▄███░▌▀▀░▀█░░░░░░░░░░░░░░░");
        iu.displayMessage("░░▄█░░▄▀▀▒▒▒▒▒▄▐░░░░█▌░░░░░░░░░░░░░░");
        iu.displayMessage("░▐█▀▄▀▄▄▄▄▀▀▀▀▌░░░░░▐█▄░░░░░░░░░░░░░");
        iu.displayMessage("░▌▄▄▀▀░░░░░░░░▌░░░░▄███████▄░░░░░░░░");
        iu.displayMessage("░░░░░░░░░░░░░▐░░░░▐███████████▄░░░░░");
        iu.displayMessage("░░░░░le░░░░░░░▐░░░░▐█████████████▄░░");
        iu.displayMessage("░░░░toucan░░░░░░▀▄░░░▐█████████████▄");
        iu.displayMessage("░░░░░░has░░░░░░░░▀▄▄███████████████");
        iu.displayMessage("░░░░░arrived░░░░░░░░░░░░█▀██████░░░░");
        iu.displayMessage("                                    ");

        // Pedir número válido de jugadores (2-5)
        do {
            numJugadores = iu.readNumber("¿Cuántos van a jugar? (2 a 5): ");
            if (numJugadores < 2 || numJugadores > 5) {
                iu.displayMessage("Número inválido. Debe estar entre 2 y 5.");
            }
        } while (numJugadores < 2 || numJugadores > 5);

        // Crear array con tamaño exacto
        this.players = new Player[numJugadores];

        // Pedir nombre para cada jugador
        for (int i = 0; i < numJugadores; i++) {
            String nombre;
            do {
                nombre = iu.readString("Nombre del jugador " + (i + 1) + ": ");
                if (nombre == null || nombre.isBlank()) {
                    iu.displayMessage("El nombre no puede estar vacío o ser nulo.");
                }
            } while (nombre == null || nombre.isBlank());

            this.players[i] = new Player(nombre.trim());
        }

        iu.displayMessage(numJugadores + " jugadores creados. ");
    }

    // Reparte 8 cartas a cada jugador desde el mazo y ordena sus manos por especie.
    private void repartirCartas() {
        iu.displayMessage("Repartiendo cartas...");

        for (Player jugador : this.players) {
            // Dar exactamente 8 cartas
            for (int i = 0; i < 8; i++) {
                jugador.addCardToHand(deck.takeFirstCard());
            }
        }

        iu.displayMessage("Reparto completado. Cada jugador tiene 8 cartas.");
    }

    // Muestra el estado inicial: mesa y manos de todos los jugadores.
    private void mostrarEstadoInicial() {
        iu.displayMessage("\n========================================");
        iu.displayMessage("ESTADO INICIAL DEL JUEGO");
        iu.displayMessage("========================================\n");

        // Mostrar mesa
        iu.displayMessage(table.toString());

        // Mostrar mano de cada jugador
        for (Player jugador : this.players) {
            iu.displayMessage(jugador.toString());
            iu.displayMessage("");
        }

        iu.displayMessage("========================================\n");
    }

    // Los tres métodos siguientes llaman a la iu, con elegirTipo recibiendo el
    // jugador como parámetro.
    private TypeBird elegirTipo(Player player) {
        List<TypeBird> playable = player.getPlayableSpecies();

        if (playable.isEmpty()) {
            iu.displayMessage("ERROR: El jugador no tiene especies jugables. Saltando turno.");
            return null;
        }

        return iu.chooseBirdType(playable);
    }

    private int elegirFila() {
        return iu.chooseRow(table.getRowCount());
    }

    private boolean elegirLado() {
        return iu.chooseSide();
    }

    // Recibe un jugador como parámetro y llama a todas las funciones necesarias
    // para ejecutar su turno.
    public void executePlayerTurn(Player player) {
        iu.displayMessage("Turno del jugador " + player.getName() + ": ");
        // Mostrar baraja del jugador
        iu.displayMessage(player.toString()); // :D

        iu.displayMessage(table.toString());

        TypeBird tipoElegido = elegirTipo(player);

        // Si no hay especies jugables, saltar turno
        if (tipoElegido == null) {
            iu.displayMessage("No es posible jugar. Turno saltado.");
            return;
        }

        int filaElegida = elegirFila();
        boolean colocarIzquierda = elegirLado();

        List<Card> cardsToPlay = player.takeCardsOfSpecies(tipoElegido);
        List<Card> capturedCards = table.placeCardsOnRow(cardsToPlay, filaElegida, colocarIzquierda);

        player.addCardsToHand(capturedCards);

        if (!capturedCards.isEmpty()) {
            table.ensureRowHasTwoSpecies(filaElegida, deck, discardedCards);
        }

        // Mostrar la mano del jugador después de la acción
        iu.displayMessage("\n--- Estado después de jugar cartas ---");
        iu.displayMessage(player.toString());

        // Mostrar la mesa actualizada
        iu.displayMessage(table.toString());

        if (!player.hasNoCards()) {
            handleCollectionChoice(player);
        }

    }

    // Método play para la ejecución de todo el juego.
    public void play() {

        // Inicializar el juego
        inicializarJugadores();
        repartirCartas();
        mostrarEstadoInicial();

        // Bucle principal
        boolean gameFinished = false;
        int currentPlayerIndex = 0;

        do {
            Player currentPlayer = players[currentPlayerIndex];

            // Si el jugador actual tiene cartas, ejecuta su turno.
            if (!currentPlayer.hasNoCards()) {
                executePlayerTurn(currentPlayer);
            }

            if (!gameFinished) {
                // Comprobación de si el jugador actual ha ganado por conseguir hacer 7 bandadas
                // pequeñas distintas.
                if (currentPlayer.getCollectedSpeciesCount() >= 7) {
                    iu.displayMessage(currentPlayer.getName()
                            + " Ha ganado la partida! Ha conseguido 7 especies de pájaros.");
                    gameFinished = true;
                } else {
                    // Si el jugador actual no tiene cartas, se comprueba si el juego termina por no
                    // poder repartir nuevas cartas.
                    boolean endByNoDeal = handleEmptyHand(currentPlayer);
                    if (endByNoDeal) {
                        gameFinished = true;
                    }
                }
            }

            if (!gameFinished) {
                // Actualizamos el índice de currentPlayer para el siguiente turno.
                currentPlayerIndex = (currentPlayerIndex + 1) % players.length;

                // Pausa para que el jugador pueda ver el resultado de su turno antes de que el
                // siguiente jugador comience
                iu.readString("\nPresiona cualquiera tecla para continuar...");
            }
        } while (!gameFinished);

        // Resultados
        iu.displayMessage("\n=== RESULTADOS FINALES ===");
        for (Player p : players) {
            iu.displayMessage(p.getName() + ": " + p.getCollectedSpeciesCount()
                    + " especies, " + p.getCollectionSize() + " cartas en coleccion.");
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
                List<Card> discarded = player.takeNCardsOfSpecies(chosenSpecies, requiredCards);
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
        for (int i = 0; i < players.length; i++) {
            for (int j = 0; j < 8; j++) {
                players[i].addCardToHand(deck.takeFirstCard());
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
package gal.uvigo.esei.aed1.cubirds.core;

//por fin arreglamos los imports :D
import gal.uvigo.esei.aed1.cubirds.iu.IU;
import es.uvigo.esei.aed1.tads.list.List;

public class Game {

    // Atributos
    private IU iu;
    private DeckOfCards deck;
    private Table table;
    private Player[] players;

    public Game(IU iu) {
        this.iu = iu;
        this.deck = new DeckOfCards();
        this.table = new Table();
        this.table.inicializarMesa(this.deck);
        this.players = null; // Se inicializa como null porque ya se convierte en un array en
                             // inicializarJugadores()
    }

    /**
     * brief
     * Inicializa los jugadores preguntando cantidad y nombres.
     * Los jugadores se almacenan directamente en this.players.
     */

    private void inicializarJugadores() {
        int numJugadores;

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

    /**
     * Reparte 8 cartas a cada jugador desde el mazo y ordena sus manos por especie.
     */
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

    /**
     * Muestra el estado inicial: mesa y manos de todos los jugadores.
     */
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

    private TypeBird elegirTipo(Player player) {
        return iu.chooseBirdType(player.getPlayableSpecies());
    }

    private int elegirFila() {
        return iu.chooseRow(table.getRowCount());
    }

    private boolean elegirLado() {
        return iu.chooseSide();
    }

    public void executePlayerTurn(Player player) {
        iu.displayMessage("Turno del jugador " + player.getName() + ": ");
        // Mostrar baraja del jugador
        iu.displayMessage(player.toString()); // :D

        iu.displayMessage(table.toString());

        TypeBird tipoElegido = elegirTipo(player);
        int filaElegida = elegirFila();
        boolean colocarIzquierda = elegirLado();

        List<Card> cardsToPlay = player.takeCardsOfSpecies(tipoElegido);
        List<Card> capturedCards = table.placeCardsOnRow(cardsToPlay, filaElegida, colocarIzquierda);

        player.addCardsToHand(capturedCards);

        // Mostrar la mano del jugador después de la acción
        iu.displayMessage("\n--- Estado después de jugar cartas ---");
        iu.displayMessage(player.toString());

        // Mostrar la mesa actualizada
        iu.displayMessage(table.toString());

    }

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

            if (currentPlayer.hasNoCards()) {
                iu.displayMessage(currentPlayer.getName() + " Ha ganado la partida!");
                gameFinished = true;
            } else {
                // Ejecutar turno del jugador actual
                executePlayerTurn(currentPlayer);

                // Verificar condición de fin de juego (ej: un jugador se queda sin sus oniichan
                // cartas)
                if (currentPlayer.hasNoCards()) {
                    iu.displayMessage(currentPlayer.getName() + " Ha ganado la partida!");
                    gameFinished = true;
                }
            }

            if (!gameFinished) {
                // El cálculo del siguiente índice funciona igual con .size() ,
                currentPlayerIndex = (currentPlayerIndex + 1) % players.length;

                // Pausa para que el jugador pueda ver el resultado de su turno antes de que el
                // siguiente jugador comience
                iu.readString("\nPresiona cualquiera tecla para continuar...");
            }
        } while (!gameFinished);

        // Resultados
        iu.displayMessage("\n=== RESULTADOS FINALES ===");
        for (Player p : players) {
            iu.displayMessage(p.getName() + ": " + p.getHandSize() + " cartas.");
        }

    }
}
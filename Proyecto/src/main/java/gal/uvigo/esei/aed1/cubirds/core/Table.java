package gal.uvigo.esei.aed1.cubirds.core;

import java.util.Objects;
import es.uvigo.esei.aed1.tads.list.LinkedList;
import es.uvigo.esei.aed1.tads.list.List;

/**
 * CLASE Table — El tablero con 4 filas de cartas
 *
 * ¿QUÉ ES? - La mesa tiene 4 filas (vallas) boca arriba - Los jugadores juegan sus cartas sobre
 * estas filas - Cuando un jugador rodea cartas, las captura
 *
 * ESTRUCTURA: - filas es un array de 4 LinkedList<Card> - Cada fila es una lista de cartas
 *
 * EJEMPLO VISUAL: Mesa: Fila 1: [FLAMENCO] [TUCAN] [PATO] Fila 2: [URRACA] [PETIRROJO] [LECHUZA]
 * Fila 3: [CURRUCA] [GUACAMAYO] [FLAMENCO] Fila 4: [TUCAN] [PATO] [URRACA]
 */
public class Table {

    // ========================================================================
    // ATRIBUTO PRIVADO
    // ========================================================================

    /** Array de 4 filas. Cada fila es una lista de cartas. */
    private List<Card>[] filas;

    // ========================================================================
    // CONSTRUCTOR
    // ========================================================================

    /**
     * Constructor — Crea la mesa con 4 filas vacías.
     */
    public Table() {
        this.filas = new LinkedList[4];
        for (int i = 0; i < 4; i++) {
            this.filas[i] = new LinkedList<>();
        }
    }

    // ========================================================================
    // INICIALIZACIÓN: llenar la mesa al empezar
    // ========================================================================

    /**
     * Coloca 3 cartas en cada fila al INICIO del juego.
     *
     * IMPORTANTE: Asegura que NO hay cartas del mismo tipo en la misma fila.
     *
     * PROCESO: 1. Para cada fila (0, 1, 2, 3): - Mientras la fila tenga < 3 cartas: - Saca una
     * carta del mazo - ¿Es del mismo tipo de las que ya hay en la fila? - SÍ: Devuelve la carta al
     * FINAL de la baraja - NO: Añade la carta a la fila
     *
     * EJEMPLO: Fila vacía: [] - Saca FLAMENCO → lo añade: [FLAMENCO] - Saca TUCAN → lo añade:
     * [FLAMENCO, TUCAN] - Saca FLAMENCO (¡duplicado!) → lo devuelve al final del mazo - Saca PATO →
     * lo añade: [FLAMENCO, TUCAN, PATO]
     *
     * ¿QUIÉN LA USA? - Game (en el constructor) → llama a inicializarMesa(deck)
     *
     * @param deck La baraja de la que sacar cartas
     */
    public void inicializarMesa(DeckOfCards deck) {
        for (int i = 0; i < 4; i++) {
            while (this.filas[i].size() < 3) {
                Card candidate = deck.takeFirstCard();
                if (!tipoRepetidoEnFila(i, candidate)) {
                    // La carta NO es del mismo tipo, la añadimos
                    this.filas[i].addLast(candidate);
                } else {
                    // La carta ES del mismo tipo, la devolvemos al final de la baraja
                    deck.addLast(candidate);
                }
            }
        }
    }

    /**
     * Comprueba si una carta es del MISMO TIPO que las ya en la fila.
     *
     * EJEMPLO: Fila: [FLAMENCO, TUCAN] ¿tipoRepetidoEnFila(0, FLAMENCO)? → true (FLAMENCO ya
     * existe) ¿tipoRepetidoEnFila(0, PATO)? → false (PATO no existe)
     *
     * @param numFila El índice de la fila (0, 1, 2, o 3)
     * @param candidate La carta a comprobar
     * @return true si el tipo ya existe en la fila, false si no
     */
    private boolean tipoRepetidoEnFila(int numFila, Card candidate) {
        boolean repetido = false;
        for (Card c : this.filas[numFila]) {
            if (c.getTypeBird() == candidate.getTypeBird()) {
                repetido = true;
            }
        }
        return repetido;
    }

    // ========================================================================
    // CONSULTAS
    // ========================================================================

    /**
     * Devuelve el número de filas (4 en esta versión).
     *
     * @return 4 (número de filas)
     */
    public int getRowCount() {
        return this.filas.length;
    }

    // ========================================================================
    // OPERACIÓN PRINCIPAL: Colocar cartas y capturar
    // ========================================================================

    /**
     * Coloca cartas en una fila Y DEVUELVE las cartas capturadas.
     *
     * ¿QUÉ PASA? 1. Coloca las cartas a la izquierda o derecha de la fila 2. Busca la
     * primera/última carta del MISMO tipo que está jugando 3. Captura todo lo que esté entre sus
     * cartas y esa carta del mismo tipo 4. Devuelve las cartas capturadas (como List<Card>)
     *
     * EJEMPLO VISUAL (placeLeft=true): ANTES: [A] [B] [B] [C] Jugador coloca: 2x[B] DESPUÉS: [B]
     * [B] [A] [B] [B] [C] CAPTURADA: [A] ← estaba entre los dos grupos de [B]
     *
     * OTRO EJEMPLO (placeLeft=false): ANTES: [A] [B] [B] [C] Jugador coloca: 2x[B] a la derecha
     * DESPUÉS: [A] [B] [B] [C] [B] [B] CAPTURADA: [C] ← estaba entre el grupo original de [B] y los
     * nuevos
     *
     * ¿QUIÉN LA USA? - Game.executePlayerTurn() → cuando el jugador juega sus cartas
     *
     * @param cardsToPlay Las cartas a colocar (todos del mismo tipo)
     * @param rowIndex El índice de la fila (0, 1, 2, o 3)
     * @param placeLeft true si colocar a la izquierda, false si a la derecha
     * @return Las cartas capturadas (o vacío si no captura nada)
     */
    public List<Card> placeCardsOnRow(List<Card> cardsToPlay, int rowIndex, boolean placeLeft) {
        List<Card> capturedCards = new LinkedList<>();

        if (cardsToPlay == null || cardsToPlay.isEmpty()) {
            return capturedCards;
        }

        List<Card> row = filas[rowIndex];
        TypeBird species = cardsToPlay.get(0).getTypeBird(); // Qué tipo estoy jugando
        int oldSize = row.size(); // Tamaño ANTES de colocar, para saber dónde buscar
        int matchIndex = -1; // -1 si no encuentra carta del mismo tipo

        // Busca la PRIMERA o ÚLTIMA carta del mismo tipo que está jugando
        if (placeLeft) {
            // Busca la PRIMERA aparición
            for (int i = 0; i < oldSize && matchIndex == -1; i++) {
                if (row.get(i).getTypeBird() == species) {
                    matchIndex = i;
                }
            }
        } else {
            // Busca la ÚLTIMA aparición
            for (int i = oldSize - 1; i >= 0 && matchIndex == -1; i--) {
                if (row.get(i).getTypeBird() == species) {
                    matchIndex = i;
                }
            }
        }

        // Coloca las cartas a la izquierda o derecha
        if (placeLeft) {
            for (int i = 0; i < cardsToPlay.size(); i++) {
                row.addFirst(cardsToPlay.get(i));
            }
        } else {
            for (int i = 0; i < cardsToPlay.size(); i++) {
                row.addLast(cardsToPlay.get(i));
            }
        }

        // Si encontró una carta del mismo tipo, captura lo que está entre
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

    // ========================================================================
    // RELLENO AUTOMÁTICO: Si una fila queda con 1 especie
    // ========================================================================

    /**
     * Si una fila tiene menos de 2 especies DIFERENTES, rellena con cartas.
     *
     * ¿CUÁNDO SE LLAMA? - Después de que un jugador coloca cartas y captura otras - Si la fila
     * quedó con una sola especie (ej: [TUCAN, TUCAN])
     *
     * PROCESO: 1. Mientras la fila tenga < 2 especies: - Intenta sacar una carta del mazo - Si el
     * mazo está vacío y hay descartes → devuelve descartes al mazo - Si no hay nada más, DETIENE el
     * relleno - Añade la carta a la fila
     *
     * RESULTADO: La fila tiene al menos 2 especies diferentes (o termina con lo que hay)
     *
     * ¿QUIÉN LA USA? - Game.executePlayerTurn() → después de capturar cartas
     *
     * @param rowIndex El índice de la fila a verificar
     * @param deck La baraja (para sacar cartas)
     * @param discardedCards Los descartes (en caso de que la baraja se vacíe)
     */
    public void ensureRowHasTwoSpecies(int rowIndex, DeckOfCards deck,
            DiscardedCards discardedCards) {
        List<Card> row = filas[rowIndex];
        boolean canDraw = true;

        while (canDraw && !rowHasAtLeastTwoSpecies(row)) {
            // Si la baraja está vacía pero hay descartes, devuelve los descartes
            if (deck.isEmpty() && !discardedCards.isEmpty()) {
                discardedCards.moveAllToDeck(deck);
            }

            // Si la baraja sigue vacía, no podemos sacar más cartas
            if (deck.isEmpty()) {
                canDraw = false;
            } else {
                // Saca una carta y la añade a la fila
                row.addLast(deck.takeFirstCard());
            }
        }
    }

    /**
     * Comprueba si una fila tiene al MENOS 2 especies DIFERENTES.
     *
     * EJEMPLO: [TUCAN, TUCAN, TUCAN] → false (solo 1 especie) [TUCAN, PATO] → true (2 especies
     * diferentes) [TUCAN, TUCAN, PATO, PATO] → true (2 especies)
     *
     * @param row La fila a comprobar
     * @return true si tiene >= 2 especies, false si tiene < 2
     */
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

    // ========================================================================
    // VALIDACIONES PRIVADAS
    // ========================================================================

    /**
     * Valida que el índice de fila está entre 0 y 3. Lanza IllegalArgumentException con mensaje en
     * español si no es válido.
     *
     * @param rowIndex índice de la fila
     */
    private void validateRowIndex(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= this.filas.length) {
            throw new IllegalArgumentException("Índice de fila inválido: " + rowIndex
                    + ". Debe estar entre 0 y " + (this.filas.length - 1) + ".");
        }
    }

    /**
     * Valida que el índice de carta es válido para la fila indicada. Lanza IllegalArgumentException
     * con mensaje en español si no es válido.
     *
     * @param rowIndex índice de la fila
     * @param cardIndex índice de la carta dentro de la fila
     */
    private void validateCardIndex(int rowIndex, int cardIndex) {
        validateRowIndex(rowIndex);
        int sz = this.filas[rowIndex].size();
        if (cardIndex < 0 || cardIndex >= sz) {
            throw new IllegalArgumentException(
                    "Índice de carta inválido: " + cardIndex + ". Debe estar entre 0 y "
                            + Math.max(0, sz - 1) + " para la fila " + rowIndex + ".");
        }
    }

    // ========================================================================
    // ACCESO A CARTAS INDIVIDUALES
    // ========================================================================

    /**
     * Devuelve la carta en la posición {@code cardIndex} de la fila {@code rowIndex}. Valida
     * índices y lanza {@link IllegalArgumentException} si son inválidos.
     *
     * Ejemplo de uso:
     * 
     * <pre>
     * Table table = new Table();
     * DeckOfCards deck = new DeckOfCards();
     * table.inicializarMesa(deck);
     * Card carta = table.getCard(0, 0);
     * </pre>
     *
     * @param rowIndex índice de la fila (0..3)
     * @param cardIndex índice de la carta dentro de la fila (0..size-1)
     * @return la carta solicitada
     */
    public Card getCard(int rowIndex, int cardIndex) {
        validateCardIndex(rowIndex, cardIndex);
        return this.filas[rowIndex].get(cardIndex);
    }

    /**
     * Reemplaza la carta en la posición indicada por {@code newCard}. Valida índices y parámetros;
     * lanza {@link IllegalArgumentException} si son inválidos.
     *
     * Ejemplo de uso:
     * 
     * <pre>
     * Table table = new Table();
     * DeckOfCards deck = new DeckOfCards();
     * table.inicializarMesa(deck);
     * Card nueva = deck.takeFirstCard();
     * table.setCard(0, 1, nueva);
     * </pre>
     *
     * @param rowIndex índice de la fila (0..3)
     * @param cardIndex índice de la carta dentro de la fila (0..size-1)
     * @param newCard nueva carta (no puede ser null)
     */
    public void setCard(int rowIndex, int cardIndex, Card newCard) {
        validateCardIndex(rowIndex, cardIndex);
        if (newCard == null) {
            throw new IllegalArgumentException("La nueva carta no puede ser null.");
        }
        this.filas[rowIndex].set(cardIndex, newCard);
    }

    // ========================================================================
    // ACCESO A FILAS COMPLETAS
    // ========================================================================

    /**
     * Devuelve una COPIA (nueva lista) de toda la fila {@code rowIndex}. No devuelve la referencia
     * interna.
     *
     * Ejemplo de uso:
     * 
     * <pre>
     * Table table = new Table();
     * DeckOfCards deck = new DeckOfCards();
     * table.inicializarMesa(deck);
     * List<Card> fila = table.getRow(2); // copia de la fila 2
     * </pre>
     *
     * @param rowIndex índice de la fila (0..3)
     * @return nueva lista con las cartas de la fila
     */
    public List<Card> getRow(int rowIndex) {
        validateRowIndex(rowIndex);
        List<Card> copy = new LinkedList<>();
        List<Card> original = this.filas[rowIndex];
        for (int i = 0; i < original.size(); i++) {
            copy.addLast(original.get(i));
        }
        return copy;
    }

    /**
     * Reemplaza toda la fila {@code rowIndex} por {@code newRow}. No realiza validaciones de
     * negocio (p.ej. duplicados de tipo), solo valida argumentos y copia los elementos.
     *
     * Ejemplo de uso:
     * 
     * <pre>
     * Table table = new Table();
     * List<Card> nuevaFila = new LinkedList<>();
     * nuevaFila.addLast(deck.takeFirstCard());
     * table.setRow(1, nuevaFila);
     * </pre>
     *
     * @param rowIndex índice de la fila (0..3)
     * @param newRow nueva lista de cartas que reemplaza la fila (no-null)
     */
    public void setRow(int rowIndex, List<Card> newRow) {
        validateRowIndex(rowIndex);
        if (newRow == null) {
            throw new IllegalArgumentException("La nueva fila no puede ser null.");
        }
        // Comprueba que no haya elementos null en newRow
        for (int i = 0; i < newRow.size(); i++) {
            if (newRow.get(i) == null) {
                throw new IllegalArgumentException(
                        "La nueva fila contiene una carta null en la posición " + i + ".");
            }
        }

        // Copiamos los elementos a una nueva LinkedList y sustituimos
        List<Card> replacement = new LinkedList<>();
        for (int i = 0; i < newRow.size(); i++) {
            replacement.addLast(newRow.get(i));
        }
        this.filas[rowIndex] = replacement;
    }

    // ========================================================================
    // ACCESO POR POSICIÓN (IMPARES / PARES / CENTRO / EXTREMOS)
    // ========================================================================

    /**
     * Devuelve una nueva lista con las cartas en posiciones impares (1,3,5...) de la fila indicada.
     * No modifica la fila original.
     *
     * Ejemplo de uso:
     * 
     * <pre>
     * List<Card> impares = table.getOddPositionCards(0);
     * </pre>
     *
     * @param rowIndex índice de la fila (0..3)
     * @return nueva lista con cartas en posiciones impares
     */
    public List<Card> getOddPositionCards(int rowIndex) {
        validateRowIndex(rowIndex);
        List<Card> result = new LinkedList<>();
        List<Card> row = this.filas[rowIndex];
        for (int i = 0; i < row.size(); i++) {
            if (i % 2 == 1) {
                result.addLast(row.get(i));
            }
        }
        return result;
    }

    /**
     * Devuelve una nueva lista con las cartas en posiciones pares (0,2,4...) de la fila indicada.
     * No modifica la fila original.
     *
     * Ejemplo de uso:
     * 
     * <pre>
     * List<Card> pares = table.getEvenPositionCards(0);
     * </pre>
     *
     * @param rowIndex índice de la fila (0..3)
     * @return nueva lista con cartas en posiciones pares
     */
    public List<Card> getEvenPositionCards(int rowIndex) {
        validateRowIndex(rowIndex);
        List<Card> result = new LinkedList<>();
        List<Card> row = this.filas[rowIndex];
        for (int i = 0; i < row.size(); i++) {
            if (i % 2 == 0) {
                result.addLast(row.get(i));
            }
        }
        return result;
    }

    /**
     * Devuelve una nueva lista con las cartas del centro de la fila (excluye la primera y la
     * última). Si la fila tiene <= 2 cartas devuelve vacía.
     *
     * Ejemplo de uso:
     * 
     * <pre>
     * List<Card> centro = table.getMiddleCards(0);
     * </pre>
     *
     * @param rowIndex índice de la fila (0..3)
     * @return nueva lista con las cartas del centro
     */
    public List<Card> getMiddleCards(int rowIndex) {
        validateRowIndex(rowIndex);
        List<Card> result = new LinkedList<>();
        List<Card> row = this.filas[rowIndex];
        int sz = row.size();
        if (sz <= 2) {
            return result;
        }
        for (int i = 1; i <= sz - 2; i++) {
            result.addLast(row.get(i));
        }
        return result;
    }

    /**
     * Devuelve una nueva lista con la primera y la última carta de la fila. Si solo hay una carta
     * devuelve solo esa. Si no hay cartas devuelve vacía.
     *
     * Ejemplo de uso:
     * 
     * <pre>
     * List<Card> extremos = table.getEdgeCards(0);
     * </pre>
     *
     * @param rowIndex índice de la fila (0..3)
     * @return nueva lista con la(s) carta(s) de borde
     */
    public List<Card> getEdgeCards(int rowIndex) {
        validateRowIndex(rowIndex);
        List<Card> result = new LinkedList<>();
        List<Card> row = this.filas[rowIndex];
        int sz = row.size();
        if (sz == 0) {
            return result;
        }
        if (sz == 1) {
            result.addLast(row.get(0));
            return result;
        }
        result.addLast(row.get(0));
        result.addLast(row.get(sz - 1));
        return result;
    }

    // ========================================================================
    // MÉTODOS AUXILIARES ÚTILES
    // ========================================================================

    /**
     * Devuelve cuántas cartas hay en la fila indicada.
     *
     * Ejemplo de uso:
     * 
     * <pre>
     * int tam = table.getRowSize(0);
     * </pre>
     *
     * @param rowIndex índice de la fila (0..3)
     * @return tamaño de la fila
     */
    public int getRowSize(int rowIndex) {
        validateRowIndex(rowIndex);
        return this.filas[rowIndex].size();
    }

    /**
     * Devuelve la primera carta de la fila. Lanza IllegalArgumentException si la fila está vacía o
     * el índice de fila es inválido.
     *
     * Ejemplo de uso:
     * 
     * <pre>
     * Card primera = table.getFirstCard(0);
     * </pre>
     *
     * @param rowIndex índice de la fila (0..3)
     * @return primera carta de la fila
     */
    public Card getFirstCard(int rowIndex) {
        validateRowIndex(rowIndex);
        if (this.filas[rowIndex].size() == 0) {
            throw new IllegalArgumentException(
                    "La fila " + rowIndex + " está vacía (no hay primera carta).");
        }
        return this.filas[rowIndex].get(0);
    }

    /**
     * Devuelve la última carta de la fila. Lanza IllegalArgumentException si la fila está vacía o
     * el índice de fila es inválido.
     *
     * Ejemplo de uso:
     * 
     * <pre>
     * Card ultima = table.getLastCard(0);
     * </pre>
     *
     * @param rowIndex índice de la fila (0..3)
     * @return última carta de la fila
     */
    public Card getLastCard(int rowIndex) {
        validateRowIndex(rowIndex);
        int sz = this.filas[rowIndex].size();
        if (sz == 0) {
            throw new IllegalArgumentException(
                    "La fila " + rowIndex + " está vacía (no hay última carta).");
        }
        return this.filas[rowIndex].get(sz - 1);
    }

    /**
     * Añade una carta al PRINCIPIO (atStart=true) o al FINAL (atStart=false) de la fila indicada.
     * Valida parámetros.
     *
     * Ejemplo de uso:
     * 
     * <pre>
     * table.addCardToRow(0, deck.takeFirstCard(), true); // añade al principio
     * </pre>
     *
     * @param rowIndex índice de la fila (0..3)
     * @param card carta a añadir (no puede ser null)
     * @param atStart true para añadir al principio, false para añadir al final
     */
    public void addCardToRow(int rowIndex, Card card, boolean atStart) {
        validateRowIndex(rowIndex);
        if (card == null) {
            throw new IllegalArgumentException("La carta a añadir no puede ser null.");
        }
        if (atStart) {
            this.filas[rowIndex].addFirst(card);
        } else {
            this.filas[rowIndex].addLast(card);
        }
    }

    /**
     * Elimina y devuelve la carta en la posición indicada. Valida índices.
     *
     * Ejemplo de uso:
     * 
     * <pre>
     * Card borrada = table.removeCard(0, 1);
     * </pre>
     *
     * @param rowIndex índice de la fila (0..3)
     * @param cardIndex índice de la carta dentro de la fila (0..size-1)
     * @return carta eliminada
     */
    public Card removeCard(int rowIndex, int cardIndex) {
        validateCardIndex(rowIndex, cardIndex);
        return this.filas[rowIndex].remove(cardIndex);
    }

    // ========================================================================
    // BÚSQUEDA
    // ========================================================================

    /**
     * Devuelve el índice de la carta en la fila, o -1 si no existe. Valida índice de fila y
     * parámetro carta.
     *
     * Ejemplo de uso:
     * 
     * <pre>
     * int idx = table.findCardIndex(0, someCard);
     * </pre>
     *
     * @param rowIndex índice de la fila (0..3)
     * @param card carta a buscar (no puede ser null)
     * @return índice de la carta o -1
     */
    public int findCardIndex(int rowIndex, Card card) {
        validateRowIndex(rowIndex);
        if (card == null) {
            throw new IllegalArgumentException("La carta a buscar no puede ser null.");
        }
        List<Card> row = this.filas[rowIndex];
        for (int i = 0; i < row.size(); i++) {
            if (Objects.equals(row.get(i), card)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Devuelve el índice de la PRIMERA carta del tipo indicado en la fila, o -1 si no existe.
     * Valida parámetros.
     *
     * Ejemplo de uso:
     * 
     * <pre>
     * int first = table.findFirstCardOfType(0, TypeBird.TUCAN);
     * </pre>
     *
     * @param rowIndex índice de la fila (0..3)
     * @param type tipo de ave a buscar (no-null)
     * @return índice de la primera coincidencia o -1
     */
    public int findFirstCardOfType(int rowIndex, TypeBird type) {
        validateRowIndex(rowIndex);
        if (type == null) {
            throw new IllegalArgumentException("El tipo a buscar no puede ser null.");
        }
        List<Card> row = this.filas[rowIndex];
        for (int i = 0; i < row.size(); i++) {
            if (row.get(i).getTypeBird() == type) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Devuelve el índice de la ÚLTIMA carta del tipo indicado en la fila, o -1 si no existe. Valida
     * parámetros.
     *
     * Ejemplo de uso:
     * 
     * <pre>
     * int last = table.findLastCardOfType(0, TypeBird.PATO);
     * </pre>
     *
     * @param rowIndex índice de la fila (0..3)
     * @param type tipo de ave a buscar (no-null)
     * @return índice de la última coincidencia o -1
     */
    public int findLastCardOfType(int rowIndex, TypeBird type) {
        validateRowIndex(rowIndex);
        if (type == null) {
            throw new IllegalArgumentException("El tipo a buscar no puede ser null.");
        }
        List<Card> row = this.filas[rowIndex];
        for (int i = row.size() - 1; i >= 0; i--) {
            if (row.get(i).getTypeBird() == type) {
                return i;
            }
        }
        return -1;
    }

    // ========================================================================
    // REPRESENTACIÓN: toString()
    // ========================================================================

    /**
     * Devuelve una representación visual de la mesa.
     *
     * EJEMPLO: Mesa: Fila 1: [FLAMENCO 2/3] [TUCAN 3/4] [PATO 4/6] Fila 2: [URRACA 5/7] [PETIRROJO
     * 6/9] ... ...
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nMesa: \n");
        for (int i = 0; i < filas.length; i++) {
            sb.append("Fila ").append(i + 1).append(": ");
            for (int j = 0; j < filas[i].size(); j++) {
                sb.append(filas[i].get(j).toString()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

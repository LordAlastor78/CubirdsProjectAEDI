# 📚 Documentación Completa — TADS (Estructuras de Datos Abstractas) — AED I

**Para alguien que debe modificar o usar estas estructuras sin conocerlas: Lee esta guía de arriba a abajo. Cada sección explica qué es, cómo funciona, y dónde cambiar si necesitas modificar algo.**

---

## 📑 ÍNDICE (Pulsa Ctrl+F para buscar)

1. [**¿QUÉ SON LOS TADS?**](#qué-son-los-tads) — Conceptos básicos
2. [**ESTRUCTURA DEL PROYECTO**](#estructura-del-proyecto) — Dónde están los archivos
3. [**LISTA (List)**](#lista-list) — `List.java` y `LinkedList.java`
4. [**COLA (Queue)**](#cola-queue) — `Queue.java` y `LinkedQueue.java`
5. [**PILA (Stack)**](#pila-stack) — `Stack.java` y `LinkedStack.java`
6. [**NODOS ENLAZADOS**](#nodos-enlazados) — Cómo funciona el enlazamiento
7. [**ITERADORES**](#iteradores) — Recorrer estructuras con `Iterator`
8. [**COMPARACIÓN DE TADS**](#comparación-de-tads) — Cuándo usar cada una
9. [**GUÍA: Cómo Modificar**](#guía-para-modificar) — Ejemplos prácticos
10. [**GUÍA: Compilar y Ejecutar**](#compilar-y-ejecutar) — Paso a paso
11. [**CASOS DE USO**](#casos-de-uso) — Ejemplos reales en CuBirds

---

## ¿QUÉ SON LOS TADS?

**TAD = Tipo Abstracto de Datos** — Es una forma de organizar y almacenar datos con operaciones específicas.

No es lo mismo que las clases de Java. Un TAD es un **concepto** (qué operaciones puedo hacer), y la **implementación** es cómo lo construyo (nodos, arrays, etc.).

### Las 3 Estructuras Principales

| Estructura | Característica | Operaciones Principales | Ejemplo |
|-----------|---|---|---|
| **List** | Acceso en cualquier posición | `get(i)`, `add()`, `remove()`, `iterator` | Lista de compra |
| **Queue** | FIFO (primero entra, primero sale) | `add()`, `remove()` desde extremos | Cola en banco |
| **Stack** | LIFO (último entra, primero sale) | `push()`, `pop()` desde la cima | Pila de libros |

---

## ESTRUCTURA DEL PROYECTO

```
Proyecto/src/main/java/es/uvigo/esei/aed1/tads/
├── list/                           (ESTRUCTURA: Lista)
│   ├── List.java                   (Interfaz)
│   ├── LinkedList.java             (Implementación con nodos)
│   ├── IteratorList.java           (Iterador bidireccional)
│   └── EmptyException.java         (Excepción)
├── queue/                          (ESTRUCTURA: Cola)
│   ├── Queue.java                  (Interfaz)
│   ├── LinkedQueue.java            (Implementación con nodos)
│   └── EmptyException.java         (Excepción)
├── stack/                          (ESTRUCTURA: Pila)
│   ├── Stack.java                  (Interfaz)
│   ├── LinkedStack.java            (Implementación con nodos)
│   └── EmptyException.java         (Excepción)
└── docs/
    ├── TADS.md                     (Documentación técnica)
    └── TADS_explained.md           (ESTA DOCUMENTACIÓN)
```

### 🔑 Lo Más Importante

- **Interfaz** = Qué operaciones tiene la estructura (firma de métodos)
- **Implementación** = Cómo lo hago por dentro (nodos enlazados)
- **Excepción** = Qué error lanzar si algo no funciona (`EmptyException`)

---

## LISTA (List)

### ¿Qué es una Lista?

Una **lista es una colección ordenada** de elementos donde puedo:
- Acceder a cualquier elemento por índice
- Añadir o quitar elementos en cualquier posición
- Recorrer todos los elementos

```
LISTA: [A] → [B] → [C] → [D]
       ↑     ↑     ↑     ↑
       0     1     2     3  (índices)
```

**En CuBirds se usa para**:
- La mano del jugador (agrupada por especies)
- Las cartas en cada fila de la mesa
- La baraja

### Interfaz List<T>

```java
public interface List<T> extends Iterable<T> {
    boolean isEmpty();
    int size();
    boolean contains(T value);

    T getFirst();      // Obtiene el primero
    T getLast();       // Obtiene el último
    T get(int index);  // Obtiene por índice (0, 1, 2...)

    void addFirst(T value);      // Añade al inicio
    void addLast(T value);       // Añade al final
    void add(int index, T value); // Añade en posición

    T removeFirst();             // Quita el primero
    T removeLast();              // Quita el último
    T remove(int index);         // Quita por índice
    boolean removeValue(T value); // Quita por valor

    void clear();
    Iterator<T> iterator();
}
```

### Implementación: LinkedList<T>

**Concepto**: Una lista de **nodos enlazados** (cada nodo apunta al siguiente).

```
[Nodo 1: A] → [Nodo 2: B] → [Nodo 3: C] → null
     ↑                            ↑
   first                        last
```

#### Estructura Interna

```java
private static class Node<T> {
    T value;          // El dato que guarda
    Node<T> previous; // Apunta al anterior
    Node<T> next;     // Apunta al siguiente
}

private Node<T> first;  // Primer nodo
private Node<T> last;   // Último nodo
private int size;       // Cantidad de elementos
```

#### Por qué "anterior" y "siguiente"?

Porque `LinkedList` es una **lista doblemente enlazada**:
- Puedo ir hacia adelante (siguiente) o hacia atrás (anterior)
- Cuando busco un elemento, puedo empezar desde el inicio O el final (lo que esté más cerca)

**Ejemplo de búsqueda optimizada**:
```java
// Si busco el índice 2 en una lista de 5 elementos:
// - Desde el inicio: 2 saltos (0→1→2) ✓ Mejor
// - Desde el final: 3 saltos (4→3→2)

// Si busco el índice 4 en una lista de 5 elementos:
// - Desde el inicio: 4 saltos
// - Desde el final: 1 salto (4) ✓ Mejor
```

#### Métodos Clave Explicados

##### `addFirst(T value)` — Añadir al inicio

```java
addFirst("NUEVO");

// ANTES: [A] → [B] → [C]
// DESPUÉS: [NUEVO] → [A] → [B] → [C]
```

**¿Cómo funciona?**
1. Crea un nodo nuevo
2. Lo enlaza al `first` actual
3. Lo convierte en el nuevo `first`
4. Incrementa el tamaño

##### `addLast(T value)` — Añadir al final

```java
addLast("NUEVO");

// ANTES: [A] → [B] → [C]
// DESPUÉS: [A] → [B] → [C] → [NUEVO]
```

##### `add(int index, T value)` — Añadir en posición específica

```java
add(1, "NUEVO");  // Añadir en la posición 1

// ANTES: [A] → [B] → [C]
// DESPUÉS: [A] → [NUEVO] → [B] → [C]
```

##### `get(int index)` — Obtener elemento

```java
T elemento = lista.get(2);  // Obtiene el elemento en índice 2
// [A] → [B] → [C]
//       0    1    2
// Devuelve: C
```

**Optimización**: Si el índice está más cerca del final, comienza desde el final.

##### `removeFirst()` y `removeLast()`

```java
lista.removeFirst();  // Quita [A]
// ANTES: [A] → [B] → [C]
// DESPUÉS: [B] → [C]

lista.removeLast();   // Quita [C]
// ANTES: [B] → [C]
// DESPUÉS: [B]
```

##### `remove(int index)` — Quitar por índice

```java
lista.remove(1);  // Quita elemento en índice 1

// ANTES: [A] → [B] → [C]
// DESPUÉS: [A] → [C]
```

##### `contains(T value)` — Buscar si existe

```java
boolean existe = lista.contains("B");  // true o false
// Recorre toda la lista comparando
```

##### `iterator()` — Recorrer

```java
for (String s : lista) {  // Usa iterator internamente
    System.out.println(s);
}
// Imprime: A, B, C
```

### Complejidad Temporal

| Operación | Tiempo | Explicación |
|-----------|--------|-------------|
| `get(i)` | O(n) | En el peor caso, recorre n elementos |
| `addFirst()` | O(1) | Solo cambia el primer nodo |
| `addLast()` | O(1) | Solo cambia el último nodo |
| `add(i, x)` | O(n) | Buscar posición + insertar |
| `removeFirst()` | O(1) | Solo cambia el primer nodo |
| `removeLast()` | O(1) | Solo cambia el último nodo |
| `remove(i)` | O(n) | Buscar posición + eliminar |
| `contains()` | O(n) | Recorre toda la lista |

---

## COLA (Queue)

### ¿Qué es una Cola?

Una **cola es FIFO** (First In, First Out): el primero que entra es el primero que sale.

```
ENTRADA (add):          SALIDA (remove):
    ↓                        ↑
[A] → [B] → [C] → [D]
  └────────────────┘
```

**En la vida real**: Cola en un banco, en el supermercado, etc.

**En CuBirds se usa para**:
- La baraja (aunque aquí usamos List directamente)
- Procesar turnos de jugadores

### Interfaz Queue<T>

```java
public interface Queue<T> {
    boolean isEmpty();
    int size();
    T first() throws EmptyException;      // Obtiene el primero (sin quitar)
    void add(T value) throws NullPointerException;  // Añade al final
    T remove() throws EmptyException;     // Quita y devuelve el primero
    void clear();
}
```

### Implementación: LinkedQueue<T>

**Concepto**: Una lista enlazada **unidireccional** (solo apunta hacia adelante) optimizada para operaciones FIFO.

```
[Nodo 1: A] → [Nodo 2: B] → [Nodo 3: C] → null
     ↑                           ↑
   first                       last
   (sacamos aquí)        (añadimos aquí)
```

#### Estructura Interna

```java
private static class Node<T> {
    T value;      // El dato
    Node<T> next; // Solo apunta hacia adelante
}

private Node<T> first;  // Por donde sacamos
private Node<T> last;   // Por donde añadimos
private int size;
```

#### Métodos Explicados

##### `add(T value)` — Encolar (añadir al final)

```java
queue.add("A");
queue.add("B");
queue.add("C");

// RESULTADO: [A] → [B] → [C]
```

**¿Cómo funciona?**
1. Crea un nodo nuevo con el valor
2. Si la cola está vacía: `first = nodo` y `last = nodo`
3. Si tiene elementos: `last.next = nodo` y `last = nodo` (actualiza el final)
4. Incrementa tamaño

##### `remove()` — Desencolar (quitar del inicio)

```java
String primero = queue.remove();  // Devuelve "A"

// ANTES: [A] → [B] → [C]
// DESPUÉS: [B] → [C]
```

**¿Cómo funciona?**
1. Si está vacía, lanza `EmptyException`
2. Guarda el valor de `first.value`
3. Avanza: `first = first.next`
4. Si `first == null` (quedó vacía), también `last = null`
5. Decrementa tamaño
6. Devuelve el valor guardado

##### `first()` — Mirar el primero (sin quitar)

```java
String primero = queue.first();  // Devuelve "A"

// ANTES: [A] → [B] → [C]
// DESPUÉS: [A] → [B] → [C]  (no cambia)
```

### Complejidad Temporal

| Operación | Tiempo | Explicación |
|-----------|--------|-------------|
| `add()` | O(1) | Solo cambia el último |
| `remove()` | O(1) | Solo cambia el primero |
| `first()` | O(1) | Solo lee el primero |
| `isEmpty()` | O(1) | Compara con null |
| `size()` | O(1) | Lee la variable |

---

## PILA (Stack)

### ¿Qué es una Pila?

Una **pila es LIFO** (Last In, First Out): el último que entra es el primero que sale.

```
      ↓ (push/pop aquí)
    [D]
    [C]
    [B]
    [A]
    ───
```

**En la vida real**: Pila de platos, pila de libros, historial "deshacer" en un editor.

**En CuBirds se usa para**:
- Podría usarse para el historial de movimientos
- Validación de paréntesis (en otros programas)

### Interfaz Stack<T>

```java
public interface Stack<T> {
    boolean isEmpty();
    int size();
    void push(T value) throws NullPointerException;  // Añade arriba
    T pop();                                         // Quita y devuelve la cima
    void clear();
    boolean contains(char character);                // (No implementado)
}
```

### Implementación: LinkedStack<T>

**Concepto**: Una lista enlazada **unidireccional** optimizada para operaciones LIFO (última posición).

```
      top
       ↓
[Nodo 1: D] → [Nodo 2: C] → [Nodo 3: B] → [Nodo 4: A] → null
```

#### Estructura Interna

```java
private static class Node<T> {
    T value;      // El dato
    Node<T> next; // Apunta al siguiente en la pila
}

private Node<T> top;   // La cima de la pila
private int size;
```

#### Métodos Explicados

##### `push(T value)` — Apilar

```java
stack.push("A");
stack.push("B");
stack.push("C");

// RESULTADO (top a arriba):
// C ← top
// B
// A
```

**¿Cómo funciona?**
1. Crea un nodo nuevo
2. Lo enlaza al `top` actual: `next = top`
3. Lo convierte en el nuevo `top`
4. Incrementa tamaño

**Código**:
```java
this.top = new Node<>(value, this.top);
this.size++;
```

Es elegante: el nuevo nodo apunta al anterior `top` automáticamente.

##### `pop()` — Desapilar

```java
String cima = stack.pop();  // Devuelve "C"

// ANTES (top a arriba):
// C ← top
// B
// A

// DESPUÉS:
// B ← top
// A
```

**¿Cómo funciona?**
1. Si está vacía, lanza `NoSuchElementException`
2. Guarda el valor: `value = top.value`
3. Sube a la siguiente: `top = top.next`
4. Decrementa tamaño
5. Devuelve el valor

### Complejidad Temporal

| Operación | Tiempo | Explicación |
|-----------|--------|-------------|
| `push()` | O(1) | Solo cambia la cima |
| `pop()` | O(1) | Solo cambia la cima |
| `isEmpty()` | O(1) | Compara con null |
| `size()` | O(1) | Lee la variable |

---

## NODOS ENLAZADOS

### ¿Cómo funciona un Nodo?

Un **nodo es una caja** que contiene:
1. **El dato** (`value`)
2. **Referencias a otros nodos** (`next`, `previous`)

```
┌─────────────────┐
│   Node<T>       │
├─────────────────┤
│ T value         │  ← El dato real
│ Node next       │  ← Apunta al siguiente
│ Node previous   │  ← Apunta al anterior (solo en List)
└─────────────────┘
```

### Enlazamiento Unidireccional (Queue, Stack)

```
[Nodo A] → [Nodo B] → [Nodo C] → null
    ↑           ↑           ↑
  next=B      next=C     next=null
```

**Ventaja**: Menos memoria (solo un apuntador)
**Desventaja**: No puedo ir hacia atrás

### Enlazamiento Bidireccional (List)

```
null ← [Nodo A] ↔ [Nodo B] ↔ [Nodo C] → null
         ↑         ↑         ↑
      prev/next   prev/next prev/next
```

**Ventaja**: Puedo navegar en ambas direcciones (optimización)
**Desventaja**: Más memoria (dos apuntadores)

### Por qué Enlazados y no Arrays?

| Aspecto | Arrays | Nodos Enlazados |
|--------|--------|---|
| Acceso `get(i)` | O(1) | O(n) |
| Añadir/Quitar inicio | O(n) | O(1) |
| Memoria | Fija | Dinámica |
| Redimensionar | Caro | No necesario |

**En CuBirds**: Usamos nodos porque frecuentemente añadimos/quitamos, y el acceso aleatorio no es crítico.

---

## ITERADORES

### ¿Qué es un Iterador?

Un **iterador es un objeto que recorre** una colección elemento a elemento.

```java
for (String s : lista) {  // Usa iterator internamente
    System.out.println(s);
}
```

**Es equivalente a**:
```java
Iterator<String> it = lista.iterator();
while (it.hasNext()) {
    String s = it.next();
    System.out.println(s);
}
```

### Interfaz Iterator<T> (de Java)

```java
public interface Iterator<T> {
    boolean hasNext();  // ¿Hay más elementos?
    T next();          // Devuelve el siguiente
}
```

### IteratorList<T> (Custom)

`IteratorList` es un iterador **bidireccional** personalizado para `LinkedList`:

```java
public interface IteratorList<T> extends Iterator<T> {
    boolean hasPrevious();    // ¿Hay anterior?
    T previous();             // Devuelve el anterior
    void forward();           // Ir al siguiente sin obtener
    void backward();          // Ir al anterior sin obtener
    T current();              // Obtiene el actual sin mover
}
```

### Ejemplo de Uso

```java
List<String> lista = new LinkedList<>();
lista.addLast("A");
lista.addLast("B");
lista.addLast("C");

// Iterador bidireccional
IteratorList<String> it = lista.iteratorList();

// Ir hacia adelante
while (it.hasNext()) {
    System.out.println(it.next());  // A, B, C
}

// Volver atrás
while (it.hasPrevious()) {
    System.out.println(it.previous());  // C, B, A
}
```

### En CuBirds

Se usa para recorrer la mano del jugador:

```java
for (Card carta : manoJugador) {
    System.out.println(carta);
}
```

---

## COMPARACIÓN DE TADS

### Cuándo Usar Cada Una

| Necesidad | Estructura | Por qué |
|-----------|-----------|--------|
| Acceso frecuente por índice | **List** | `get(i)` es rápido si está bien implementado |
| Procesar datos en orden FIFO | **Queue** | Diseñada para eso, O(1) en todas operaciones |
| Procesar datos en orden LIFO | **Stack** | Diseñada para eso, O(1) en todas operaciones |
| Deshacer/Rehacer | **Stack** | Guarda el historial |
| Esperar en línea | **Queue** | Simula una cola real |
| Búsqueda en el medio | **List** | Puedo buscar en cualquier posición |
| Agrupar datos | **List** | Puedo hacer `add(indice, dato)` |

### Tabla de Operaciones

| Operación | List | Queue | Stack |
|-----------|------|-------|-------|
| Insertar inicio | O(1) ✓ | O(1) ✓ | X |
| Insertar final | O(1) ✓ | O(1) ✓ | O(1) ✓ |
| Obtener cualquier | O(n) | X | X |
| Obtener primero | O(1) ✓ | O(1) ✓ | X |
| Obtener último | O(1) ✓ | X | O(1) ✓ |
| Quitar cualquier | O(n) | X | X |
| Quitar primero | O(1) ✓ | O(1) ✓ | X |
| Quitar último | O(1) ✓ | X | O(1) ✓ |

---

## 🔧 GUÍA PARA MODIFICAR

### Modificación 1: LIMITAR TAMAÑO MÁXIMO

**Situación**: Quieres que una `Queue` no pueda tener más de 10 elementos.

**Archivos**: `LinkedQueue.java`

**Paso a paso**:

```java
@Override
public void add(T value) throws NullPointerException {
    if (value == null) {
        throw new NullPointerException();
    }

    // ← AÑADE ESTO
    if (this.size >= 10) {
        throw new IllegalStateException("Cola llena (máximo 10)");
    }

    Node<T> newNode = new Node<>(value);
    if (this.last == null) {
        this.first = newNode;
        this.last = newNode;
    } else {
        this.last.next = newNode;
        this.last = newNode;
    }
    this.size++;
}
```

### Modificación 2: VISUALIZAR LA ESTRUCTURA

**Situación**: Quieres un método `toString()` que muestre la lista como `[A] → [B] → [C]`.

**Archivos**: `LinkedList.java`

**Añadir método**:

```java
@Override
public String toString() {
    if (isEmpty()) {
        return "[]";
    }
    StringBuilder sb = new StringBuilder();
    Node<T> current = this.first;
    while (current != null) {
        sb.append("[").append(current.value).append("]");
        if (current.next != null) {
            sb.append(" → ");
        }
        current = current.next;
    }
    return sb.toString();
}
```

**Uso**:
```java
LinkedList<String> lista = new LinkedList<>();
lista.addLast("A");
lista.addLast("B");
lista.addLast("C");
System.out.println(lista);  // [A] → [B] → [C]
```

### Modificación 3: BUSCAR UN ELEMENTO Y SU ÍNDICE

**Situación**: Quieres `indexOf(T value)` que devuelva el índice de un elemento, o -1 si no existe.

**Archivos**: `LinkedList.java`

**Añadir método**:

```java
public int indexOf(T value) throws NullPointerException {
    if (value == null) {
        throw new NullPointerException();
    }
    Node<T> current = this.first;
    int index = 0;
    while (current != null) {
        if (Objects.equals(current.value, value)) {
            return index;
        }
        current = current.next;
        index++;
    }
    return -1;  // No encontrado
}
```

**Uso**:
```java
int indice = lista.indexOf("B");  // 1
int indice = lista.indexOf("Z");  // -1 (no existe)
```

### Modificación 4: INVERTIR UNA LISTA

**Situación**: Quieres `reverse()` que invierta la lista.

**Archivos**: `LinkedList.java`

**Añadir método**:

```java
public void reverse() {
    Node<T> current = this.first;
    Node<T> temp;

    // Intercambiar las referencias previous/next de cada nodo
    while (current != null) {
        temp = current.next;
        current.next = current.previous;
        current.previous = temp;
        current = temp;  // Ir al siguiente (que ahora está en previous)
    }

    // Intercambiar first y last
    temp = this.first;
    this.first = this.last;
    this.last = (Node<T>) temp;
}
```

**Ejemplo**:
```java
// ANTES: [A] → [B] → [C]
lista.reverse();
// DESPUÉS: [C] → [B] → [A]
```

### Modificación 5: COMPARAR DOS LISTAS

**Situación**: Quieres comprobar si dos listas tienen los mismos elementos en el mismo orden.

**Archivos**: `LinkedList.java`

**Añadir método**:

```java
@Override
public boolean equals(Object obj) {
    if (!(obj instanceof LinkedList)) {
        return false;
    }
    LinkedList<?> other = (LinkedList<?>) obj;

    if (this.size != other.size) {
        return false;
    }

    Node<?> current1 = this.first;
    Node<?> current2 = other.first;

    while (current1 != null) {
        if (!Objects.equals(current1.value, current2.value)) {
            return false;
        }
        current1 = current1.next;
        current2 = current2.next;
    }

    return true;
}
```

---

## ▶️ COMPILAR Y EJECUTAR

### Compilar TADS

```powershell
cd "Proyecto"
mvn clean compile
```

### Verificar que compila sin errores

```powershell
mvn compile
```

### Ejecutar Tests (si existen)

```powershell
mvn test
```

### Crear JAR

```powershell
mvn package
```

---

## CASOS DE USO

### En CuBirds

#### 1. **Baraja (DeckOfCards)**

```java
DeckOfCards deck = new DeckOfCards();
// Usa LinkedList internamente

// Estructura: [Card1] → [Card2] → [Card3] ...
// Operaciones:
Card primera = deck.takeFirstCard();  // Saca de inicio (rápido)
deck.addLast(nuevaCarta);             // Añade al final (rápido)
deck.shuffle();                       // Mezcla todo
```

**¿Por qué List?** Necesitamos acceso frecuente al inicio (`takeFirstCard`) y al final (`addLast`).

#### 2. **Mano del Jugador (List<List<Card>>)**

```java
List<List<Card>> mano = new LinkedList<>();

// Estructura: [[Flamenco_1, Flamenco_2], [Tucan_1, Tucan_2, Tucan_3]]
//             grupo 0                      grupo 1

// Operaciones:
List<Card> cartas_flamenco = mano.get(0);  // Obtener grupo
mano.add(1, new LinkedList<>());            // Añadir nueva especie
```

**¿Por qué List?** Necesitamos acceso por índice de especie.

#### 3. **Filas de la Mesa (Array de List)**

```java
List<Card>[] filas = new LinkedList[4];

// Estructura:
// Fila 0: [Flamenco] → [Tucan] → [Pato]
// Fila 1: [Urraca] → [Petirrojo] → [Lechuza]
// ...

// Operaciones:
Card primera = filas[1].getFirst();     // Obtener primer elemento de fila
filas[1].add(0, nuevaCarta);            // Añadir al inicio de fila
```

**¿Por qué List?** Las filas necesitan reordenarse durante el juego (captura de cartas).

#### 4. **Descartes (DiscardedCards)**

```java
DiscardedCards descartes = new DiscardedCards();
// Usa LinkedList internamente

// Estructura: [Card1] → [Card2] → [Card3] ...

// Operaciones:
descartes.addCard(carta);            // Añadir descarte
descartes.moveAllToDeck(deck);       // Vaciar en baraja
```

**¿Por qué List?** Solo añadimos al final y vaciamos todo. Queue sería más eficiente, pero aquí no se optimiza.

---

## 📊 DIAGRAMA: Relación entre TADS en CuBirds

```
┌─────────────────────────────────────────────────┐
│              Game (Control del Juego)           │
├─────────────────────────────────────────────────┤
│                                                 │
│  ┌──────────────┐  ┌──────────────┐            │
│  │ DeckOfCards  │  │   Table      │            │
│  │ (LinkedList) │  │ (Array de    │            │
│  │              │  │  LinkedList) │            │
│  └──────────────┘  └──────────────┘            │
│                                                 │
│  ┌──────────────┐  ┌──────────────┐            │
│  │   Player     │  │ DiscardedCards           │
│  │ (List<List>) │  │ (LinkedList) │           │
│  └──────────────┘  └──────────────┘            │
│                                                 │
└─────────────────────────────────────────────────┘
```

---

## 📝 NOTAS FINALES

### Qué Está Implementado
✅ `LinkedList` — Doblemente enlazada con optimización bidireccional
✅ `LinkedQueue` — FIFO con acceso O(1)
✅ `LinkedStack` — LIFO con acceso O(1)
✅ `IteratorList` — Iterador bidireccional personalizado
✅ Excepciones personalizadas (`EmptyException`)

### Qué NO Está Implementado
❌ `ArrayList` (implementación con arrays redimensionables)
❌ `CircularQueue` (cola circular)
❌ Métodos avanzados como `clone()`, `serializar()`

### Genéricos en Java

Las estructuras usan **genéricos** `<T>`:

```java
List<String> lista = new LinkedList<>();
List<Card> cartas = new LinkedList<>();
Queue<Integer> numeros = new LinkedQueue<>();
```

`<T>` es un **placeholder** — "cualquier tipo".

**Ventaja**: Reutilización de código
**Desventaja**: Sin comprobar tipos en tiempo de compilación (es por eso la advertencia de casting)

---

**Fin de la Documentación de TADS. ¡Ahora usa las estructuras con confianza!** 🚀

# CuBirds — Proyecto AED I (Tercera Entrega) 𓅫

░░░░░░░░▄▄▄▀▀▀▄▄███▄░░░░░░░░░░░░░░
░░░░░▄▀▀░░░░░░░▐░▀██▌░░░░░░░░░░░░░
░░░▄▀░░░░▄▄███░▌▀▀░▀█░░░░░░░░░░░░░
░░▄█░░▄▀▀▒▒▒▒▒▄▐░░░░█▌░░░░░░░░░░░░
░▐█▀▄▀▄▄▄▄▀▀▀▀▌░░░░░▐█▄░░░░░░░░░░░
░▌▄▄▀▀░░░░░░░░▌░░░░▄███████▄░░░░░░
░░░░░░░░░░░░░▐░░░░▐███████████▄░░░
░░░░░le░░░░░░░▐░░░░▐█████████████▄
░░░░toucan░░░░░░▀▄░░░▐█████████████▄
░░░░░░has░░░░░░░░▀▄▄███████████████
░░░░░arrived░░░░░░░░░░░░█▀██████░░

Proyecto en Java del juego de cartas **CuBirds**, desarrollado para la asignatura de Algoritmos y Estructuras de Datos I.

Este repositorio contiene la **tercera entrega** del proyecto, con la lógica completa de juego, la gestión de descartes, el relleno automático de filas de la mesa y la zona de juego de cada jugador.

## Qué hace el proyecto 𓅫

El programa permite jugar a CuBirds por consola para **2 a 5 jugadores**.

El objetivo es ganar la partida consiguiendo:
- **7 especies distintas** en la zona de juego, o
- en caso de final por falta de cartas, tener la **mayor colección total**.

Durante la partida el sistema gestiona:
- la baraja de cartas,
- el montón de descartes,
- la mesa con 4 filas,
- la mano de cada jugador,
- la zona de juego mediante contador por especie,
- las capturas de cartas rodeadas,
- el relleno de filas cuando quedan con una única especie,
- el reciclado de descartes cuando la baraja se agota.

## Estructura del proyecto 𓅫

La carpeta principal del código es `Proyecto/`.

```text
Proyecto/
├─ pom.xml
├─ explained.md
└─ src/
   ├─ main/java/
   │  ├─ es/uvigo/esei/aed1/tads/...
   │  └─ gal/uvigo/esei/aed1/cubirds/
   │     ├─ core/
   │     └─ iu/
   └─ test/java/   (si se añaden pruebas)
```

Paquetes principales:
- `gal.uvigo.esei.aed1.cubirds.core` — lógica del juego.
- `gal.uvigo.esei.aed1.cubirds.iu` — interfaz por consola.
- `es.uvigo.esei.aed1.tads` — TADs auxiliares usados por el proyecto.

## Compilación a JAR 𓅫

### Requisitos previos
- **Java 17+** instalado (verifica con `java -version`)
- **Maven 3.6+** instalado (verifica con `mvn -version`)

### Pasos para compilar

1. **Navega a la carpeta del proyecto:**
   ```powershell
   cd Proyecto
   ```

2. **Limpia compilaciones anteriores (opcional pero recomendado):**
   ```powershell
   mvn clean
   ```

3. **Compila y genera el JAR:**
   ```powershell
   mvn package
   ```

   Si quieres omitir las pruebas (si las hay):
   ```powershell
   mvn package -DskipTests
   ```

4. **Localiza el JAR generado:**
   El archivo JAR estará en:
   ```
   target/cubirds_stable-1.3.1.jar
   ```

### Ejecución del JAR

**Opción 1: Ejecutando directamente el JAR:**
```powershell
java -jar target/cubirds_stable-1.3.1.jar
```

**Opción 2: Especificando la clase principal (recomendado):**
```powershell
java -cp target/cubirds_stable-1.3.1.jar gal.uvigo.esei.aed1.cubirds.iu.Main
```

### Ejecución directa con Maven (sin generar JAR)

Si prefieres no generar el JAR y ejecutar directamente:
```powershell
mvn exec:java -Dexec.mainClass="gal.uvigo.esei.aed1.cubirds.iu.Main"
```

## Clases principales 𓅫

### `Card`
Representa una carta de pájaro.

Contiene información de:
- especie (`TypeBird`),
- tamaño de bandada pequeña,
- tamaño de bandada grande,
- representación textual.

### `TypeBird`
Enumeración de las especies del juego.

Se usa para:
- identificar cartas,
- agrupar cartas iguales,
- comparar especies,
- calcular condiciones de juego.

### `DeckOfCards`
Representa la baraja principal.

Funciones típicas:
- crear y barajar el mazo,
- sacar la primera carta,
- añadir cartas al final,
- comprobar si está vacía,
- obtener su tamaño,
- mezclarla.

### `DiscardedCards`
Representa el montón de descartes.

Permite:
- añadir una carta,
- añadir varias cartas,
- comprobar si está vacío,
- conocer su tamaño,
- pasar todo su contenido a la baraja y barajar.

### `Table`
Representa la mesa de juego con 4 filas.

Responsabilidades:
- inicializar las 4 filas con 3 cartas distintas cada una,
- colocar cartas en un extremo de una fila,
- detectar capturas cuando una especie queda rodeando cartas,
- rellenar una fila si queda con una sola especie,
- mostrar el estado de la mesa.

### `Player`
Representa a un jugador.

Gestiona:
- el nombre,
- la mano de cartas agrupada por especie,
- la zona de juego como contador por especie,
- las cartas que puede jugar,
- las cartas que puede bajar a su colección,
- las cartas que debe descartar o recuperar.

### `Game`
Controla el flujo completo de la partida.

Se encarga de:
- pedir jugadores y nombres,
- repartir cartas,
- ejecutar turnos,
- comprobar la victoria,
- reciclar descartes,
- repartir cartas nuevas cuando un jugador se queda sin mano,
- finalizar la partida si no se puede repartir.

### `IU`
Interfaz de usuario por consola.

Permite:
- leer números,
- leer texto,
- mostrar mensajes,
- elegir especie,
- elegir fila,
- elegir lado,
- responder sí/no.

## Cómo se juega 𓅫

### 1. Inicio

Al arrancar el programa:
1. Se pregunta cuántos jugadores van a participar.
2. Se pide el nombre de cada jugador.
3. Se crea la baraja y se baraja.
4. Se inicializa la mesa con 4 filas de 3 cartas.
5. Se reparten 8 cartas a cada jugador.

### 2. Turno de un jugador
En cada turno el jugador activo:

1. Ve su mano y la mesa.
2. Elige una especie que tenga en la mano.
3. Elige una fila (1-4).
4. Elige si coloca a la izquierda o a la derecha.
5. Baja todas las cartas de esa especie.
6. Si rodea cartas de la fila, esas cartas se incorporan a su mano.
7. Si la fila queda con una sola especie, se rellena hasta volver a tener al menos dos especies.
8. Se comprueba si desea bajar una especie a su zona de juego.
9. Si ha llegado a 7 especies distintas, gana la partida.

### 3. Cuando un jugador se queda sin cartas
Si el jugador activo termina su turno y se queda sin cartas:
- los demás jugadores descartan toda su mano,
- todos esos descartes pasan al montón de descartes,
- el montón de descartes se devuelve a la baraja y se baraja,
- se reparten 8 cartas a cada jugador otra vez.

Si no hay cartas suficientes para repartir 8 a cada jugador:
- finaliza la partida,
- gana el jugador con más cartas en la zona de juego.

## Reglas implementadas en código

El proyecto ya incorpora la lógica de la tercera entrega:
- `DiscardedCards` almacena descartes y los devuelve al mazo cuando hace falta.
- `Player` mantiene el contador de especies de la zona de juego.
- `Game` comprueba la victoria y la condición de fin por falta de cartas.
- `Table` rellena filas cuando quedan con una sola especie.

Además, se aplicaron correcciones pedidas en tutoría:
- se eliminó el `clear()` innecesario al inicializar la mesa,
- se simplificó la inserción de cartas al colocar por la izquierda,
- se eliminó la comprobación redundante de grupos vacíos en `Player`.

## Cómo compilar y ejecutar 𓅫

usamos Maven para gestionar el proyecto, así como los errores.

Desde la carpeta `Proyecto`:

```powershell
mvn clean package
```

Para ejecutar la aplicación desde Maven, si tu entorno tiene el plugin configurado:

```powershell
mvn exec:java -Dexec.mainClass="gal.uvigo.esei.aed1.cubirds.iu.Main"
```




## Autores 𓅫

- Alan Fernández Teijeiro
- Diego Feijóo Ángel
- Lois Feijóo Lorenzo
- Mateo Fernández Rodríguez
- Andrés González Hermida


𓅫 — ¡Gracias por leer! Esperamos que disfrutes jugando a CuBirds tanto como nosotros hemos disfrutado programándolo. 🐦✨


@Cubirds, juego de maldito games:

https://www.malditogames.com/juegos/cubirds-juego/



---------------

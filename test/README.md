## Bingo

Bingo es un juego de mesa en el que se juega con un cartón de números. Los jugadores deben llamar a los números del cartón para ganar el juego. El objetivo del juego es llegar a la línea de 75 números llamados.

### Características del juego

- Jugadores: Los jugadores pueden ser cualquier persona o grupo de personas.
- Cartón: El cartón es un conjunto de números que se utiliza para jugar al Bingo.
- Números: Los números que se utilizan en el juego son de 1 a 75.
- Bingo: El objetivo del juego es llegar a la línea de 75 números llamados.
- Llamado: Los jugadores deben llamar a los números del cartón para ganar el juego.

### Estructura del proyecto

- Clase `BallCaller`: Representa el objeto que llama números aleatorios y mantiene una lista de números llamados.
- Clase `Card`: Representa el cartón que contiene los números que se utilizan en el juego.
- Clase `CardFactory`: Representa la fábrica de cartón que crea cartón con números aleatorios.
- Clase `Game`: Representa el juego de Bingo que contiene un objeto `BallCaller` y una lista de jugadores.
- Clase `Player`: Representa un jugador que contiene un nombre y un cartón.
- Clase `Players`: Representa una clase que contiene una lista de jugadores.
- Clase `Observer`: Representa un observador que se notifica cuando se llama a un número en el cartón.
- Clase `Subject`: Representa un objeto que tiene una lista de observadores y se notifica a los observadores cuando se llama a un número en el cartón.

### Código fuente

El código fuente del proyecto se encuentra en el archivo `Bingo.java`.

### Ejecución del proyecto

Para ejecutar el proyecto, siga los siguientes pasos:

1. Abra una terminal o línea de comandos.
2. Cambia el directorio hasta donde se encuentra el archivo `Bingo.java`.
3. Ejecute el siguiente comando para compilar el código fuente: `javac Bingo.java`.
4. Ejecute el siguiente comando para ejecutar el programa: `java Bingo`.

### Ejemplo de uso

Para ejecutar el juego de Bingo, siga los siguientes pasos:

1. Abra una terminal o línea de comandos.
2. Cambia el directorio hasta donde se encuentra el archivo `Bingo.java`.
3. Ejecute el siguiente comando para compilar el código fuente: `javac Bingo.java`.
4. Ejecute el siguiente comando para ejecutar el programa: `java Bingo`.

### Ejemplo de salida

```
¡Comienza el juego de Bingo!
¡Bola cantada: 2!
¡Bola cantada: 11!
¡Bola cantada: 75!

Jugador 1: ¡El número 75 está en mi cartón!
19  68  24  44  67 
16  69  43   5  30
52  41  75* 20   1
46  64  15  73  33
71  47  38  36  58

...

¡Bola cantada: 73!
Jugador 1: ¡El número 73 está en mi cartón!
19* 68* 24* 44  67*
16  69  43*  5* 30
52  41  75* 20   1*
46* 64* 15* 73* 33*
71  47  38* 36* 58*

¡BINGO! El jugador Jugador 1 ha ganado.
¡Juego terminado!
```

## Autores ✒️

-Andres Cerdas Padilla - *20231020053*
-Ana Karina Roa Mora - *20232020118*
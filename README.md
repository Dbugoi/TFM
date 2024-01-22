# TFM - Análisis comparativo de la capacidad de aprendizaje del paradigma CBR respecto a otros modelos de aprendizaje máquina.

Clases para la evaluación de los diferentes modelos de MsPacMan y ghosts en el juego MsPacMan vs Ghosts.
Implementacion de clases evaluadoras que obtienen información de las evaluaciones realizadas. Posteriormente con esta información se pueden producir gráficas que ayuden a analizar los resultados obtenidos.

### Clases Evaluadoras
* PacManEvaluatorTFM: clase que realiza las evaluaciones entre las diferentes implementaciones que se encuentran en el archivo *config.properties*. Permite realizar evaluaciones entre las técnicas CBR, Algorítmica, FSM y Rules, tanto para MsPacMan como para ghosts. Estas evaluaciones se realizan en un único proceso de ejecución.


* PacManEvaluatorTFMBash: clase que realiza las evaluaciones entre las diferentes implementaciones que se encuentrarn en el archivo *config.properties*. Permite realizar las evaluaciones entre las técnicas CBR, Algorítmica, FSM y Rules.  Estas evaluaciones se realizan en varios procesos de ejecución que van siendo creados desde un script de bash.  Está pensado para usarlo en servidor y de esta manera si falla un proceso no se pierda toda la información de las evaluaciones previas.



* PacManEvaluatorQLearnTFM: clase que realiza las evaluaciones entre las diferentes implementaciones Q-learning contra los ghosts que se encuentran en el archivo *config.properties*. Permite realizar las evaluaciones contra ghosts que usen las técnicas CBR, Algorítmica, FSM y Rules. Estas evaluaciones se realizan en un único proceso de ejecución.



* PacManEvaluatorTFMBash: clase que realiza las evaluaciones entre las diferentes implementaciones con Q-learning contra los ghosts que se encuentran en el archivo *config.properties*. Permite realizar las evaluaciones contra ghosts que usen las técnicas CBR, Algorítmica, FSM y Rules. Estas evaluaciones se realizan en varios procesos de ejecución que van siendo creados desde un script de bash. Está pensado para usarlo en servidor y de esta manera si falla un proceso no se pierda toda la información de las evaluaciones previas.


### Localización
Las clases evaluadoras se encuentran en el paquete *es.ucm.fdi.ici*.
La ruta es la siguiente: src/main/java/es/ucm/fdi/ici


### config.properties
 Para evaluar una implementación se debe poner la localización de la implementación en el archivo *config.properties*.
 #### Ejemplo
 ```trials = 50
file = results.txt

MsPacManTeams = \
es.ucm.fdi.ici.c2122.practica5.grupo04.MsPacMan,\
es.ucm.fdi.ici.c2122.practica5.grupo08.MsPacMan,\
es.ucm.fdi.ici.c2122.practica5.grupo10.MsPacMan

GhostsTeams = \
es.ucm.fdi.ici.c2122.practica3.grupo02.Ghosts,\
es.ucm.fdi.ici.c2122.practica3.grupo03.Ghosts,\
es.ucm.fdi.ici.c2122.practica3.grupo04.Ghosts,\
es.ucm.fdi.ici.c2122.practica3.grupo05.Ghosts,\
es.ucm.fdi.ici.c2122.practica3.grupo06.Ghosts,\
es.ucm.fdi.ici.c2122.practica3.grupo07.Ghosts,\
es.ucm.fdi.ici.c2122.practica3.grupo08.Ghosts,\
es.ucm.fdi.ici.c2122.practica3.grupo09.Ghosts,\
es.ucm.fdi.ici.c2122.practica3.grupo10.Ghosts
```
Cuando queremos evaluar los MsPacMan con Q-learning debemos usar el evaluador correspondiente y no poner nada en el apartado MsPacMans. En el apartado GhostsTeams debemos poner los ghosts que queremos que se enfrenten a los MsPacMan con Q-learning.


### Ejecución con script de bash
Para ejecutar el evaluador con bash tenemos que tener un script de bash. A continuación se muestra unos ejemplo de script de bash para cada evaluador.

#### Para PacManEvaluatorTFMBash:
```#!/bin/bash

# Ruta al archivo JAR que deseas ejecutar
JAR_FILE="evaluatorTFMBash.jar"

# Número total de ejecuciones
TOTAL_EJECUCIONES=1000

#Ciclos por ejecucion
CICLOS=50

# Loop para ejecutar el JAR 1000 veces pasando el valor de 'i' como argumento de 50 en 50
for ((i = $CICLOS; i <= $TOTAL_EJECUCIONES; i+=$CICLOS)); do
  java -jar "$JAR_FILE" "$i"
done
```

#### Para PacManEvaluatorTFMBash:
```#!/bin/bash

# Ruta al archivo JAR que deseas ejecutar
JAR_FILE="evaluatorQLearnTFMBash.jar"

# Número total de ejecuciones
TOTAL_EJECUCIONES=10

#Ciclos por ejecucion
CICLOS=1

# Loop para ejecutar el JAR 1000 veces pasando el valor de 'i' como argumento de 50 en 50
for ((i = $CICLOS; i <= $TOTAL_EJECUCIONES; i+=$CICLOS)); do
  java -jar "$JAR_FILE" "$i"
done
```



#!/bin/bash

# Ruta al archivo JAR que deseas ejecutar
JAR_FILE="evaluatorTFMBash.jar"

# Número total de ejecuciones
TOTAL_EJECUCIONES=1000

#Ciclos por ejecución´
CICLOS=50

# Loop para ejecutar el JAR 1000 veces pasando el valor de 'i' como argumento
for ((i = $CICLOS; i <= $TOTAL_EJECUCIONES; i+=$CICLOS)); do
  java -jar "$JAR_FILE" "$i"
done
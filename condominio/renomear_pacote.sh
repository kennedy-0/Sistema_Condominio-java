#!/bin/bash
# Uso: ./renomear_pacote.sh 1234   (os 4 últimos dígitos da sua matrícula)
set -e
if [[ ! "$1" =~ ^[0-9]{4}$ ]]; then
  echo "Informe os 4 últimos dígitos da matrícula. Ex: ./renomear_pacote.sh 5678"; exit 1
fi
NOVO="m$1"
[ "$NOVO" = "5678" ] && { echo "Já está como m1234."; exit 0; }
mv src/main/java/br/edu/poo/m1234 src/main/java/br/edu/poo/$NOVO
grep -rl "m1234" src pom.xml README.md | xargs sed -i "s/br\.edu\.poo\.m1234/br.edu.poo.$NOVO/g; s#br/edu/poo/m1234#br/edu/poo/$NOVO#g"
echo "Pacote renomeado para br.edu.poo.$NOVO"

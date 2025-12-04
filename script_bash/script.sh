#!/bin/bash

#On vérifie si un argument est passé lors de l'exécution du script
if [ $# -eq 0 ]; then
    echo "Veuillez spécifier la taille de la grille et la profondeur en tant qu'argument."
    exit 1
fi

# Profondeur et taille passée en argument lors de l'exécution du script
taille=$1
depth=$2

#taille=$1
# Nombre d'expérimentations à effectuer
num_experiments=100

# Chemin vers le programme Java compilé
java_program="java -cp ../build model.Main $taille $depth 4"


# Fichier de sortie pour enregistrer les résultats
output_file="results.txt"

# Initialisation du tableau associatif pour compter les victoires de chaque joueur
declare -A player_wins
player_wins=()

# Boucle pour exécuter plusieurs expérimentations
for ((i=1; i<=$num_experiments; i++)); do
    echo "Expérimentation $i:"
    
    # Exécution de programme Java et capture de la sortie
    output=$($java_program)

    # Extraction du joueur gagnant à partir de la sortie
    winner_name=$(echo "$output" | awk '/Le joueur gagnant est :/{print $5}')

    # Vérification si la chaîne n'est pas vide
    if [ -n "$winner_name" ]; then
        # Affichage du joueur gagnant
        echo "Le joueur gagnant est : $winner_name"

        # Incrémentation du nombre de victoires pour le joueur
        ((player_wins["$winner_name"]++))
    else
        echo "Aucun gagnant trouvé dans l'expérimentation $i"
    fi

done

# Enregistrement des résultats dans le fichier de sortie
echo "Résultats des expérimentations:" > "$output_file"
for player in "${!player_wins[@]}"; do
    echo "Joueur $player : ${player_wins[$player]} victoires" >> "$output_file"
done

echo "Les résultats ont été enregistrés dans le fichier $output_file"
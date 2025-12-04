#!/bin/bash

# Assurez-vous qu'un argument est passé lors de l'exécution du script
if [ $# -eq 0 ]; then
    echo "Veuillez spécifier la taille de la grille , la profondeur et le nombre de joueurs en tant qu'argument."
    exit 1
fi

# Taille de la grille, profondeur de recherche et nombre de joueurs passées en argument lors de l'exécution du script
#grid_size=$1
depth=$1
nb_players_per_team=$2


# Nombre d'expérimentations à effectuer
num_experiments=100

# Chemin vers le programme Java compilé
java_program="java -cp ../build model.main.MainSos 11 $depth $nb_players_per_team"


# Fichier de sortie pour enregistrer les résultats
output_file="resultsSOSTaileFixe.txt"

# Initialisation du tableau associatif pour compter les victoires de chaque joueur
declare -A team_wins
team_wins=()

# Boucle pour exécuter plusieurs expérimentations
for ((i=1; i<=$num_experiments; i++)); do
    echo "Expérimentation $i:"
    
    # Exécution de programme Java et capture de la sortie
    output=$($java_program)

    # Extraction du joueur gagnant à partir de la sortie
    winner_name=$(echo "$output" | awk '/La team gagnante est : /{print $6}')

    # Vérification si la chaîne n'est pas vide
    if [ -n "$winner_name" ]; then
        # Affichage du joueur gagnant
        echo "L'equipe gagnante est : $winner_name"

        # Incrémentation du nombre de victoires pour le joueur
        ((team_wins["$winner_name"]++))
    else
        echo "Aucune equipe gagnante trouvé dans l'expérimentation $i"
    fi

done

# Enregistrement des résultats dans le fichier de sortie
echo "Résultats des expérimentations:" > "$output_file"
for player in "${!team_wins[@]}"; do
    echo "Equipe $player : ${team_wins[$player]} victoires" >> "$output_file"
done

echo "Les résultats ont été enregistrés dans le fichier $output_file"

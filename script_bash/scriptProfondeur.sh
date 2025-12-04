#!/bin/bash

# Liste des profondeurs à tester
depths=(2 3 4 5 6)

# Liste des tailles à tester
tailles=(4 5 6 7 8 9 10 11 12)

# Litse de taille d'équipe a tester
equipe=(2 3 4 5)

# Fichier de sortie pour enregistrer les résultats
output_file="resultsProfondeurEquipeSOSMaxn.txt"

# Boucle pour chaque profondeur
for depth in "${depths[@]}"; do
    
    echo -e "Profondeur $depth :" >> "$output_file"
    
    # Boucle pour chaque taille
    for taille in "${equipe[@]}"; do

        echo -e "Equipe de taille $taille :" >> "$output_file"

        # Exécuter le script pour chaque combinaison de taille et de profondeur
        ./ScriptSOS.sh "$depth" "$taille"
        
        # Récupérer les résultats de l'exécution précédente et les ajouter au fichier de sortie
        cat resultsSOSTaileFixe.txt >> "$output_file"
    done
done

echo "Toutes les expérimentations ont été effectuées et les résultats ont été enregistrés dans le fichier $output_file"
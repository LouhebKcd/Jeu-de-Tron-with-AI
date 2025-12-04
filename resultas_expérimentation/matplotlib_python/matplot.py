import sys
import re
import matplotlib.pyplot as plt

# Fonction pour lire un fichier texte et retourner ses lignes
def lire_fichier_texte(nom_fichier):
    with open(nom_fichier, 'r') as fichier:
        lignes = fichier.readlines()
    return lignes

# Fonction pour extraire les résultats à partir des lignes du fichier
def extraire_resultats(lignes):
    resultats = {}
    profondeur = None

    for ligne in lignes:
        if "Profondeur" in ligne:
            profondeur = int(re.findall(r'\d+', ligne)[0])
            resultats[profondeur] = {}
        elif "Joueur" in ligne:
            joueur, victoires = re.findall(r':([^:]+)\s*:\s*(\d+)', ligne)[0]
            resultats[profondeur][joueur.strip()] = int(victoires)
    
    return resultats

# Fonction pour dessiner le graphe des résultats
def dessiner_graphe(resultats):
    profondeurs = sorted(resultats.keys())
    joueurs = list(resultats[profondeurs[0]].keys())

    # Parcourir chaque joueur pour dessiner une courbe correspondant à ses victoires en fonction de la profondeur
    for joueur in joueurs:
        scores = [resultats[profondeur].get(joueur, 0) for profondeur in profondeurs]
        if joueur == "robot1":
            plt.plot(profondeurs, scores, marker='o', label=joueur + " (sos)")
        elif joueur == "robot2":
            plt.plot(profondeurs, scores, marker='o', label=joueur + " (paranoid)")
        else:
            plt.plot(profondeurs, scores, marker='o', label=joueur + " (random)")

    plt.xlabel('Profondeur')
    plt.ylabel('Nombre de victoires')
    plt.title("Résultats des expérimentations par profondeur pour robot1 sos et robot2 maxn avec robot 3 et 4 Random")
    plt.legend()
    plt.grid(True)
    plt.gcf().set_size_inches(10, 6)  
    plt.savefig("resultats_exp.png")
    plt.show()



if __name__ == "__main__":
   # Vérification que l'utilisateur fournit le nom du fichier en argument
    if len(sys.argv) != 2:
        print("Veuillez saisir le nom de fichiers qui contient les resultas")
        sys.exit(1)

    # Récupérer le nom du fichier à partir des arguments de la ligne de commande
    nom_fichier = sys.argv[1]
    lignes = lire_fichier_texte(nom_fichier)
    resultats = extraire_resultats(lignes)
    dessiner_graphe(resultats)

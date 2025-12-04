import sys  

# Importation du module re pour traiter les expressions régulières
import re

# Importation du module matplotlib.pyplot pour la visualisation des données
import matplotlib.pyplot as plt

# Fonction pour lire un fichier texte et renvoyer ses lignes
def lire_fichier_texte(nom_fichier):
    # Ouverture du fichier en mode lecture ('r')
    with open(nom_fichier, 'r') as fichier:
         # Lecture de toutes les lignes du fichier et stockage dans une liste
        lignes = fichier.readlines()
        # On retourne la liste des lignes lues
    return lignes

# Fonction pour extraire les résultats des expérimentations à partir des lignes du fichier
def extraire_resultats(lignes):
    # Initialisation du dictionnaire et de la variable
    resultats = {}
    profondeur = None

    # Parcours de chaque ligne du fichier
    for ligne in lignes:
        # Vérification si la ligne contient le mot "Profondeur"
        if "Profondeur" in ligne:
            # Extraction de la profondeur à partir de la ligne en utilisant une expression régulière
            profondeur = int(re.findall(r'\d+', ligne)[0])
            # Initialisation d'un dictionnaire vide pour stocker les résultats de cette profondeur
            resultats[profondeur] = {}
        # Vérification si la ligne contient le mot "Joueur"
        elif "Joueur" in ligne:
            # Extraction du nom du joueur et du nombre de victoires à partir de la ligne
            joueur, victoires = re.findall(r':([^:]+)\s*:\s*(\d+)', ligne)[0]
            # Ajout du nombre de victoires du joueur dans le dictionnaire des résultats
            resultats[profondeur][joueur.strip()] = int(victoires)
    
    return resultats

# Fonction pour dessiner un histogramme des résultats par profondeur
def dessiner_histogramme(resultats):
    # Récupération des profondeurs triées
    profondeurs = sorted(resultats.keys())
    # Récupération des noms des joueurs
    joueurs = list(resultats[profondeurs[0]].keys())

    # Création d'une nouvelle figure et de ses axes
    fig, ax = plt.subplots()

    # Parcours de chaque joueur pour dessiner les barres d'histogramme correspondantes
    for i, joueur in enumerate(joueurs):
        scores = [resultats[profondeur].get(joueur, 0) for profondeur in profondeurs]
        ax.bar([profondeur + i * 0.1 for profondeur in profondeurs], scores, width=0.1, label=joueur)

    # Ajout des titres et des labels aux axes
    plt.xlabel('Profondeur')
    plt.ylabel('Nombre de victoires')
    plt.title("Résultats des expérimentations par profondeur pour robot1 sos et robot2 maxn avec robot 3 et 4 Random")
    plt.legend()
    plt.grid(True)
    plt.gcf().set_size_inches(10, 6)  

    # Sauvegarde de la figure en tant qu'image png
    plt.savefig("histogramme.png")
    plt.show()



if __name__ == "__main__":
    # Vérification que l nom du fichier est passé en argument
    if len(sys.argv) != 2:
        print("Veuillez saisir le nom de fichiers qui contient les resultas")
        sys.exit(1)
    
    nom_fichier = sys.argv[1]
    lignes = lire_fichier_texte(nom_fichier)
    resultats = extraire_resultats(lignes)
    dessiner_histogramme(resultats)

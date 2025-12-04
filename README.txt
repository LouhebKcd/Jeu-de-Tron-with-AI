-----------------------------------------------------------------------
    README - Rendu Final - Projet 2
-----------------------------------------------------------------------

* Le travail consiste à la réalisation du Jeu de Tron sans joueurs humains.

------------------------------------------------------------------------
Pour lancer le projet, suivez ces étapes :
1. Compilation des classes :
	Pour compiler toutes les classes, exécutez la commande suivante à partir du répertoire racine   du projet :
	  
	javac -d build src/model/algorithmes/*.java src/model/evaluation/*.java
	src/model/jeu/*.java src/model/main/*.java src/vue/*.java

2. Exécution des classes exécutables : Une fois que toutes les classes ont été compilées, vous pouvez exécuter chaque classe exécutable  :

	— Exécution de la classe principale DemoParametrable :

	java -cp build model.main.DemoParametrable

	— Exécution de la classe principale Main :
	Pour exécuter la classe principale ‘Main‘ en spécifiant les arguments nécessaires, utilisez la commande suivante :

	java -cp build model.main.Main <taille_grille> <profondeur_recherche> <nombre_joueurs>

	— Exécution de la classe principale MainSos :
	Pour exécuter la classe principale MainSos en spécifiant les arguments nécessaires, utilisez la commande suivante :

	java -cp build model.main.MainSos <taille_grille> <profondeur_recherche> <nombre_joueurs_par équipe>

	— Exécution de la classe principale MainInter : 
	Pour lancer l’interface graphique du jeu, exécutez la classe principale MainInter. Utilisez la commande suivante :

	java -cp build vue.MainInter


3. Utilisation des fichiers JAR : 
	Vous trouverez les fichiers JAR dans le dossier dist.
	Une fois que vous avez les fichiers JAR, vous pouvez lancer le projet en utilisant les commandes suivante :

	java -jar JeuTronConsole.jar

	Cette commande exécutera le jeu sur le terminal en utilisant le fichier JAR
	pour la version console du jeu.
	
	
	java -jar JeuTronVueGraphique.jar
	
	Cette commande exécutera une partie du jeu avec une interface graphique en
	utilisant le fichier JAR pour la version avec visualisation graphique du jeu.
	
	
	
	
	
	


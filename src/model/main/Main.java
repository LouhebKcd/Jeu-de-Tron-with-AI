package model.main;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import model.algorithmes.*;
import model.evaluation.*;
import model.jeu.*;

public class Main {
	public static void main(String[] args) {
		
		if (args.length < 3) {
            System.out.println("Veuillez spécifier la taille de la grille, la profondeur de recherche et le nombre de joueurs.");
            return;
        }

        int gridSize = Integer.parseInt(args[0]);
        int depth = Integer.parseInt(args[1]);
        int numPlayers = Integer.parseInt(args[2]);

        if (gridSize <= 0 || depth <= 0 || numPlayers <= 0) {
            System.out.println("La taille de la grille, la profondeur de recherche et le nombre de joueurs doivent être supérieurs à zéro.");
            return;
        }

		//tableau pour stocker les joueurs
        Player[] players = new Player[numPlayers];

		// Création des joueurs
        for (int i = 0; i < numPlayers; i++) {
            String name = "robot" + (i + 1);
            char symbol = (char) ('1' + i);
            players[i] = new Player(name, i, symbol);
        }

		//initialisation du joueur courrant
		Player currentPlayer = players[0];

		//initialisation de la grille de jeu 
		Player[][] grid = new Player[gridSize][gridSize];

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				grid[i][j] = null;
			}
		}
		
		// Positionnement aléatoire des joueurs sur la grille
        Random random = new Random();
        Map<Player, Point> playerPosition = new HashMap<>();
        for (Player player : players) {
            int x = random.nextInt(gridSize);
            int y = random.nextInt(gridSize);
            while (grid[x][y] != null) { // Vérifie si la case est déjà occupée
                x = random.nextInt(gridSize);
                y = random.nextInt(gridSize);
            }
            grid[x][y] = player;
            playerPosition.put(player, new Point(x, y));
        }


		//creation et affichage de l'etat initiale 
		StateGame initialState = new StateGame(grid, playerPosition,currentPlayer);
		initialState.showGrid();


		Action action = new Action();
		Voronoi voronoi = new Voronoi();
		//PlayersScores scores = new PlayersScores();
		StateEvaluation voronoiEvaluation = new VoronoiStateEvaluation();
		//BasicStateEvaluation basicStateEvaluation = new BasicStateEvaluation(scores, ponderation);
		MaxNAlgorithme maxn = new MaxNAlgorithme(voronoiEvaluation, action);
		//ParanoidAlgorithm paranoidAlgorithm = new ParanoidAlgorithm(voronoiEvaluation, action);
		//SOSAlgorithm sos = new SOSAlgorithm(voronoiEvaluation, action);



		//boucle principal qui permet de lancer le jeu
		while (!maxn.isTerminal(initialState)) {
			String ch = null;
			Map<Player, String> playerActions = new HashMap<>();

			for (Player player : players) {
				//on a choisi de faire jouer le joueur robot1 avec maxN
				//et les autres joueur aléatoirement pour voir les performances de maxN
				if (player.getIsAlive()) {
					if (player.getId() == 0 || player.getId() == 1){
						initialState.setCurrentPlayer(player);
						ch = maxn.getBestActionForPlayer(initialState,voronoi ,currentPlayer, depth);
					}else {
						initialState.setCurrentPlayer(player);
						ch = action.getRandomAction(initialState, currentPlayer);
						//ch = paranoidAlgorithm.getBestActionForPlayer(initialState,voronoi ,currentPlayer, depth);
					}
				}
				
				//Si une action est disponible et applicable on la stocke dans notre map pour l'appliquer apres
				if (ch != null && action.isApplicableAction(initialState, ch, player)) {
					playerActions.put(player, ch);
					currentPlayer = maxn.nextPlayer(initialState, player);
				} else {
					// Si aucune action n'est disponible ou si l'action n'est pas applicable, on passe au joueur suivant
					player.setAlive(false);
					currentPlayer = maxn.nextPlayer(initialState, player);
				}
			}
		
			//on appliques l'action de chaque joueur et on affiche la grille du jeu
            initialState = action.applyActions(initialState, playerActions);
            initialState.showGrid();
		}
		
		//affichage du joueur gagnant
		if (currentPlayer != null) {
			System.out.println("GameOver\nLe joueur gagnant est :" + currentPlayer);
		}else{
			System.out.println("Partie null");
		}
	}
}
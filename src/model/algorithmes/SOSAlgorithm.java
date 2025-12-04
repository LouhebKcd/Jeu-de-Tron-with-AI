package model.algorithmes;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.jeu.*;
import model.evaluation.*;

/**
 * Cette class implémente l'algorithme de recherche SOS qui permet de faire jouer avec des équipes.
 * Elle hérite de la class AbstractAlgorithmeSearch.
 */
public class SOSAlgorithm extends AbstractAlgorithmeSearch{
	
	/**
     * Constructeur de la classe SOSAlgorithm.
     * @param evaluation L'interface de l'évaluation du jeu.
     * @param ac Objet de la class Action utilisé pour manipuler les actions dans le jeu.
     */
	public SOSAlgorithm(StateEvaluation evaluation , Action ac) {
		super(evaluation,ac);
	}
	
	/**
     * Génère une matrice de relation entre les joueurs basée sur leurs liens (soit dans la meme equipe ou pas).
     * @param players Un dictionaire associant chaque joueur à un ensemble de joueurs qui sont dans son equipes.
     * @return La matrice des liens entre les joueurs.
     */
	public int[][] generateSocialRangeMatrix(Map<Player, Set<Player>> players) {
	    int numPlayers = players.size();
	    int[][] matrix = new int[numPlayers][numPlayers];
	        
	    // Parcourir tous les joueurs et leurs liens sociaux
	    for (Player player1 : players.keySet()) {
	        for (Player player2 : players.keySet()) {
	            if (players.get(player1).contains(player2)) {
	                matrix[player1.getId()][player2.getId()] = 1; 
	            } else {
	                matrix[player1.getId()][player2.getId()] = 0; 
	            }
	        }
	    }   
	    return matrix;
	}
	   
	 /**
     * Effectue la multiplication d'une matrice par un vecteur.
     * @param matrix La matrice à multiplier.
     * @param vector Le vecteur à multiplier.
     * @return Le résultat de la multiplication.
     */  	
	int[] multiplierMatrice(int[][] matrix, int[] vector) {
		if(matrix.length == 0){
			System.out.println("matrice vide");
		}
		int m = matrix.length;
		int n = matrix[0].length;
		if (n != vector.length) {
			System.out.println("La taille de la matrice et du vecteur n'est pas compatible pour une multiplication.");
			return null;
		}

		int[] result = new int[m];

		for (int i = 0; i < m; i++) {
			int sum = 0;
			for (int j = 0; j < n; j++) {
				sum += matrix[i][j] * vector[j];
			}
			result[i] = sum;
		}

		return result;
	}
    
	/**
     * Méthode principale de l'algorithme de recherche SOS.
     * @param state 		L'état actuel du jeu.
     * @param action 		Objet de la class Action utilisé pour manipuler les actions dans le jeu.
     * @param voronoi       objet de la class Voronoi qui permet de calculer la région de chauqe joueur.
     * @param depth			La profondeur de recherche.
     * @param currentPlayer Le joueur courant.
     * @return 				Le tableau qui contient le meilleure score pour le joueur courrant
     */
	@Override
	public int[] algorithmeSearch(StateGame state , Action action, Voronoi voronoi, int depth , Player currentPlayer) {
	    // Etat terminal ou profondeur atteinte : retourne l'évaluation de l'état actuel
		if (isTerminalSos(state) || depth == 0) {
			voronoi.assignVoronoiRegions(state);
			return this.multiplierMatrice(this.generateSocialRangeMatrix(state.getTeamPlayers()),evaluation.evaluate(state));
		}

		Map<Player, Point> playerPositions = state.getPlayerPosition();
		List<String> possibleActions = action.actionsPossible(state, currentPlayer);
		int numPlayers = playerPositions.size();
		int[] bestValue = new int[numPlayers];

		//Parcours des actions possibles
		for (String possibleAction : possibleActions) {
			StateGame nextState = state.copy();

			//Applique l'action 
			nextState = action.applyAction(nextState, possibleAction, currentPlayer);
			nextState.setTeamPlayers(state.getTeamPlayers());
			
			// Appel récursif pour évaluer l'état suivant
			int[] value = algorithmeSearch(nextState, action , voronoi,depth - 1, nextPlayer(nextState, currentPlayer));

			// Mise à jour des meilleures valeurs si nécessaire
			if (bestValue[currentPlayer.getId()] <= value[currentPlayer.getId()]) {
				for (int i = 0; i < numPlayers; i++) {
					bestValue[i] = value[i];
				}
			}

		}
		return bestValue;
	}

	/**
     * Vérifie si l'état du jeu avec l'utilisation de SOS est terminal.
     * @param state L'état actuel du jeu.
     * @return True si l'état est terminal, False sinon.
     */
	public boolean isTerminalSos(StateGame state) {
		Map<Player, Set<Player>> players = state.getTeamPlayers();
		
		for (Set<Player> team : players.values()) {
			boolean allPlayersDead = true;
			for (Player player : team) {
				if (player.getIsAlive()) {
					allPlayersDead = false;
					break; 
				}
			}
			if (allPlayersDead) {
				return true;
			}
		}
		
		return false;
	}
			
}

	

package model.algorithmes;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import model.evaluation.*;
import model.jeu.*;


/**
 * Class abstraite représentant un algorithme de recherche générique
 */
public abstract class AbstractAlgorithmeSearch {
	protected StateEvaluation evaluation;
	protected Action action;

	/**
     * Constructeur de la class Action.
     * 
     * @param evaluation    Interface qui contient la methode de l'evaluation du jeu.
     * @param ac            L'objet Action utilisé pour manipuler les actions dans le jeu.
     */
	public AbstractAlgorithmeSearch(StateEvaluation evaluation, Action ac) {
		this.evaluation = evaluation;
		this.action = ac;
	}

	/**
     * Fonction abstraite pour effectuer une recherche dans l'espace de jeu.
     * 
     * @param state         L'état actuel du jeu.
     * @param action        objet Action utilisé pour manipuler les actions dans le jeu.
     * @param voronoi       objet de la class Voronoi qui permet de calculer la région de chauqe joueur
	 * @param depth         La profondeur de recherche.
     * @param currentPlayer Le joueur courant.
     * @return              Le tableau de scores pour chaque joueur après la recherche.
     */
	public abstract int[] algorithmeSearch(StateGame state, Action action,Voronoi voronoi ,int depth, Player currentPlayer);

	 /**
     * Fonction qui permet d'obtenir le joueur suivant dans l'ordre de jeu.
     * 
     * @param state             L'état actuel du jeu.
     * @param currentPlayer     Le joueur courant.
     * @return                  Le prochain joueur dans l'ordre de jeu.
     */
	public Player nextPlayer(StateGame state, Player currentPlayer) {
		Map<Player, Point> playerPositions = state.getPlayerPosition();
		List<Player> players = new ArrayList<>(playerPositions.keySet());
	
		// Tri des joueurs en fonction de leur ID
		players.sort(Comparator.comparingInt(Player::getId));
	
		int currentIndex = players.indexOf(currentPlayer);
	
		// Vérifie qu'il y a au moins un joueur vivant
		boolean anyPlayerAlive = players.stream().anyMatch(Player::getIsAlive);
		if (!anyPlayerAlive) {
			return null;
		}
	
		// Trouver le prochain joueur en vie
		int nextIndex = (currentIndex + 1) % players.size();
		while (!players.get(nextIndex).getIsAlive()) {
			nextIndex = (nextIndex + 1) % players.size();
		}
	
		return players.get(nextIndex);
	}
	

	/**
     * Fonction abstraite pour obtenir la meilleure action possible pour un joueur.
     * 
     * @param state             L'état actuel du jeu.
     * @param voronoi       	objet de la class Voronoi qui permet de calculer la région de chauqe joueur.
	 * @param currentPlayer     Le joueur courant.
     * @param depth             La profondeur de recherche.
     * @return                  La meilleure action possible pour le joueur.
     */
	public String getBestActionForPlayer(StateGame state,Voronoi voronoi ,Player currentPlayer, int depth) {

		List<String> possibleActions = action.actionsPossible(state, currentPlayer);
		int numPlayers = state.getPlayerPosition().size();

		// Initialisation des meilleures valeurs avec des valeurs minimales
		int[] bestValues = new int[numPlayers];
		Arrays.fill(bestValues, Integer.MIN_VALUE);

		String bestAction = null;

		if (possibleActions.isEmpty()) {
			//si y'a aucune action possible pour le joueur donc il est déclarer mort
			currentPlayer.setAlive(false);
			return null;
		} else {
			// Parcours des actions possibles
			for (String possibleAction : possibleActions) {
				StateGame nextState = state.copy();
				
				// Applique l'action et retourne l'etat suivant
				nextState = action.applyAction(nextState, possibleAction, currentPlayer);
				nextState.setTeamPlayers(state.getTeamPlayers());
				
				// Appel récursif de notre algorithme de recherche
				int[] values = algorithmeSearch(nextState, action,voronoi ,depth - 1, nextPlayer(nextState, currentPlayer));

				// Mise à jour des meilleures valeurs si nécessaire
				if (bestValues[currentPlayer.getId()] < values[currentPlayer.getId()]) {
					bestValues = values;
					bestAction = possibleAction;
				}
			}
			return bestAction;
		}
	}

	/**
     * Vérifie si l'état actuel du jeu est terminal, c'est-à-dire s'il représente une fin de partie.
     * 
     * @param state     L'état actuel du jeu.
     * @return          true si l'état est terminal, sinon false.
     */
	public boolean isTerminal(StateGame state) {
		Map<Player, Point> playerPositions = state.getPlayerPosition();
		int alivePlayers = 0;
		for (Map.Entry<Player, Point> entry : playerPositions.entrySet()) {
			Player player = entry.getKey();
			if (player.getIsAlive()) {
				alivePlayers++;
			}
		}

		// L'état est terminal si qu'un joueur ou aucun joueur n'est en vie
		return alivePlayers <= 1;
	}

	/**
     * Affiche le tableau de scores.
     * 
     * @param tab   Le tableau de scores à afficher.
     */
	public void showScore(int[] tab) {
		for (int i = 0; i < tab.length; i++) {
			System.out.print(tab[i]+"\t");
		}
		System.out.println();
	}

}

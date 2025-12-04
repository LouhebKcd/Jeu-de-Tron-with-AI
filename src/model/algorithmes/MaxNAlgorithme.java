package model.algorithmes;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import model.jeu.*;
import model.evaluation.*;
/**
 * Implémentation de l'algorithme Max-N pour la recherche de la meilleure action dans un jeu.
 */
public class MaxNAlgorithme extends AbstractAlgorithmeSearch {
	
	/**
     * Constructeur de la class.
     * 
     * @param evaluation Interface qui contient la methode de l'evaluation du jeu.
     * @param ac         Objet Action utilisé pour manipuler les actions dans le jeu.
     */
	public MaxNAlgorithme(StateEvaluation evaluation, Action ac) {
		super(evaluation, ac);
	}

	/**
     * Algorithme de recherche récursif MaxN.
     * 
     * @param state         L'état actuel du jeu.
     * @param action        Objet de la class Action utilisé pour manipuler les actions dans le jeu.
     * @param voronoi       objet de la class Voronoi qui permet de calculer la région de chauqe joueur
	 * @param depth         La profondeur de la recherche.
     * @param currentPlayer Le joueur courant.
     * @return              Les valeurs évaluées pour chaque joueur.
     */
	@Override
	public int[] algorithmeSearch(StateGame state, Action action,Voronoi voronoi ,int depth, Player currentPlayer) {
		
		// Etat terminal ou profondeur atteinte : retourne l'évaluation de l'état
		if (isTerminal(state) || depth == 0) {
			voronoi.assignVoronoiRegions(state);
			return evaluation.evaluate(state);
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
			
			// Appel récursif pour évaluer l'état suivant
			int[] value = algorithmeSearch(nextState, action,voronoi ,depth - 1, nextPlayer(nextState, currentPlayer));

			// Mise à jour des meilleures valeurs si nécessaire
			if (bestValue[currentPlayer.getId()] <= value[currentPlayer.getId()]) {
				for (int i = 0; i < numPlayers; i++) {
					bestValue[i] = value[i];
				}
			}

		}
		return bestValue;
	}
}

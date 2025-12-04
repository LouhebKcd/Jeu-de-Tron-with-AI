package model.evaluation;

import java.awt.Point;
import java.util.Map;
import model.jeu.*;
/**
 * La class BasicStateEvaluation implémente l'interface StateEvaluation en évaluant l'état actuel du jeu
 * selon des heuristique de base.
 */
public class BasicStateEvaluation implements StateEvaluation {
    private PlayersScores scores; 
    private int[] ponderationScore;

	/**
     * Constructeur de la class BasicStateEvaluation.
     * 
     * @param scores           Gestionnaire des scores pour différentes heuristique liées au jeu
     * @param ponderationScore Ponderation des différentes heuristiques de score
     */
    public BasicStateEvaluation(PlayersScores scores, int[] ponderationScore) {
        this.scores = scores;
        this.ponderationScore = ponderationScore;
    }

	/**
     * Évalue l'état actuel du jeu et retourne les scores des joueurs en fonction des heuristiques de base.
     * 
     * @param state L'état actuel du jeu à évaluer.
     * @return Un tableau d'entiers représentant les scores des joueurs.
     */
    @Override
    public int[] evaluate(StateGame state) {
        Map<Player, Point> playerPositions = state.getPlayerPosition();
		int[] scoresPlayer = new int[playerPositions.size()];

		for (Player player : playerPositions.keySet()) {
			int distanceOpnnetScore = this.scores.distanceToOpponentPlayerScore(state, player);
				int emptyCellScore = this.scores.totalEmptySurroundingScore(state, player);
				int distanceMursScore = this.scores.distanceMinimaleAuxMursScore(state, player);
				int bordScore = this.scores.distanceMinimalAuxBordsScore(state, player);
				int centreScore = this.scores.distanceCentreScore(state, player);

				scoresPlayer[player.getId()] = ponderationScore[0] * centreScore + ponderationScore[1] * bordScore + ponderationScore[2] * distanceMursScore + ponderationScore[3]*distanceOpnnetScore
					+ ponderationScore[4] * emptyCellScore;
			if (!player.getIsAlive()) {
				scoresPlayer[player.getId()] = -1;
			} 
		}
		return scoresPlayer;
    }
}

package model.evaluation;

import java.awt.Point;
import model.jeu.*;
/**
 * Class qui gère le calcul des scores pour différentes heuristiques liées au jeu.
 */
public class PlayersScores {

	/**
     * Calcule la distance euclidienne entre deux points.
     * 
     * @param p1 Le premier point.
     * @param p2 Le deuxième point.
     * @return La distance euclidienne entre les deux points.
     */
	public int calculateDistance(Point p1, Point p2) {
		return (int) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}

	/**
     * Calcule le score du joueur actuel selon la distance par rapport aux autres joueurs.
     * 
     * @param state        L'état actuel du jeu.
     * @param currentPlayer Le joueur actuel.
     * @return Le score du joueur actuel en fonction de la distance par rapport aux autres joueurs.
     */
	public int distanceToOpponentPlayerScore(StateGame state, Player currentPlayer) {
		int taille = state.getGrid().length;
		Point currentPlayerPosition = state.getPlayerPosition().get(currentPlayer);

		double scoreFinal = Double.MAX_VALUE;
		double maxDistance = calculateDistance(new Point(0, 0), new Point(taille - 1,taille - 1));

		for (Player opponent : state.getPlayerPosition().keySet()) {
			if (!opponent.equals(currentPlayer)) {
				Point opponentPosition = state.getPlayerPosition().get(opponent);
				double distance = calculateDistance(currentPlayerPosition, opponentPosition);
				
				// Si les joueurs sont adjacents le score est 0
				if (distance < 2) {
					return 0;
				}

				double score = (distance *taille) / maxDistance;
				scoreFinal = Math.min(scoreFinal, score);
			}
		}

		return (int) scoreFinal;
	}

	/**
     * Calcule le score du joueur actuel selon le nombre total des cases vides qui l'entourent.
     * 
     * @param state        L'état actuel du jeu.
     * @param currentPlayer Le joueur actuel.
     * @return Le score du joueur actuel en fonction du nombre total des cases vides qui l'entourent.
     */
	public int totalEmptySurroundingScore(StateGame state, Player currentPlayer) {
		int emptyCount = 0;
		int taille = state.getGrid().length;
		Point currentPosition = state.getPlayerPosition().get(currentPlayer);
	
		// Définition des directions à explorer
		int[][] directions = { 
			{ -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, // Horizontales et verticales
			{ -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } // Diagonales
		};
	
		// Parcours des directions
		for (int[] direction : directions) {
			int x = currentPosition.x + direction[0];
			int y = currentPosition.y + direction[1];
			// Vérifier si la cellule adjacente est valide et vide
			if (isValidCell(state, x, y) && state.getGrid()[x][y] == null) {
				emptyCount++;
			}
		}

		return emptyCount*taille / 8;
	}

	/**
     * Vérifie si une cellule aux coordonnées spécifiées est valide dans la grille de jeu.
     * 
     * @param state L'état actuel du jeu.
     * @param x     Coordonnée x de la cellule.
     * @param y     Coordonnée y de la cellule.
     * @return true si la cellule est valide, sinon false.
     */
	public boolean isValidCell(StateGame state, int x, int y) {
		int taille = state.getGrid().length;
		return x >= 0 && x < taille && y >= 0 && y < taille;
	}

	/**
     * Calcule le score du joueur actuel selon la distance minimale par rapport aux murs.
     * 
     * @param state        L'état actuel du jeu.
     * @param currentPlayer Le joueur actuel.
     * @return Le score du joueur actuel en fonction de la distance minimale par rapport aux murs.
     */
	public int distanceMinimaleAuxMursScore(StateGame state, Player currentPlayer) {
		int taille = state.getGrid().length;
		Point currentPlayerPosition = state.getPlayerPosition().get(currentPlayer);
		int Scoredistance = Integer.MAX_VALUE;
	
		int maxDistance = calculateDistance(new Point(0, 0), new Point(taille - 1, taille - 1));
		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille; j++) {
				Point mur = new Point(i, j);
				if (state.getGrid()[i][j] != null && !state.getGrid()[i][j].equals(currentPlayer)) {
					int distance = calculateDistance(currentPlayerPosition, mur);

					// Si le mur est adjacent au joueur le score est considéré comme 0
					if (areAdjacent(mur,currentPlayerPosition)) {
						return 0;
					}

					Scoredistance = Math.min(Scoredistance, distance);
				}
			}
		}
	
		return (int) (Scoredistance * taille / maxDistance);
	}

	/**
     * Vérifie si deux points sont adjacents.
     * 
     * @param p1 Le premier point.
     * @param p2 Le deuxième point.
     * @return true si les points sont adjacents, sinon false.
     */
	public boolean areAdjacent(Point p1, Point p2) {
		// Vérifie si les coordonnées des points diffèrent d'au plus un dans une seule dimension (x ou y)
		return Math.abs(p1.x - p2.x) <= 1 && Math.abs(p1.y - p2.y) <= 1 && (p1.x == p2.x || p1.y == p2.y);
	}
	

	/**
     * Calcule le score du joueur actuel selon la distance au centre.
     * 
     * @param state        L'état actuel du jeu.
     * @param currentPlayer Le joueur actuel.
     * @return Le score du joueur actuel en fonction de la distance au centre.
     */
	public int distanceCentreScore(StateGame state, Player currentPlayer) {
		int taille = state.getGrid().length;
		Point currentPlayerPosition = state.getPlayerPosition().get(currentPlayer);
		int score;
		Point centre = new Point(taille / 2, taille / 2);
		int maxDistanceCentre = calculateDistance(new Point(0,0), centre);

		//taille de la grille pair 
		if (taille % 2 == 0) {
			int[][] centerCells = {{centre.x, centre.y}, {centre.x - 1, centre.y}, {centre.x, centre.y - 1}, {centre.x - 1, centre.y - 1}};

			// Calculer la distance moyenne à ces cellules
			int totalDistance = 0;
			for (int[] centerCell : centerCells) {
				Point cell = new Point(centerCell[0], centerCell[1]);
				totalDistance += calculateDistance(currentPlayerPosition, cell);
			}
			int distanceMoyenne = totalDistance / 4;
			score = distanceMoyenne * taille / maxDistanceCentre;

		} else {
			
			int distance = calculateDistance(currentPlayerPosition, centre);
			score = distance * taille / maxDistanceCentre;
		}
		
		return score;
	}

	/**
     * Calcule le score du joueur actuel selon la distance minimale par rapport aux bords.
     * 
     * @param state        L'état actuel du jeu.
     * @param currentPlayer Le joueur actuel.
     * @return Le score du joueur actuel en fonction de la distance minimale par rapport aux bords.
     */
	public int distanceMinimalAuxBordsScore(StateGame state, Player currentPlayer) {
		int taille = state.getGrid().length;
		Point currentPlayerPosition = state.getPlayerPosition().get(currentPlayer);
		int distanceMinimaleAuxBords = Math.min(
				Math.min(currentPlayerPosition.x, taille - currentPlayerPosition.x - 1),
				Math.min(currentPlayerPosition.y, taille - currentPlayerPosition.y - 1));

		return distanceMinimaleAuxBords;
	}

}

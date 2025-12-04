package model.jeu;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * La class Action permet la manipulation des actions dans le jeu.
 */
public class Action {

	/**
     * Vérifie si une position est valide sur la grille.
     * 
     * @param x     La coordonnée x de la position à vérifier.
     * @param y     La coordonnée y de la position à vérifier.
     * @param grid  La grille du jeu.
     * @return      true si la position est valide, sinon false.
     */
	public static boolean isValidPosition(int x, int y, Player[][] grid) {
		return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y] == null;
	}

	/**
     * Retourne la liste des actions possibles pour un joueur dans l'état actuel du jeu.
     * 
     * @param state         L'état actuel du jeu.
     * @param currentPlayer Le joueur pour lequel les actions sont évaluées.
     * @return              La liste des actions possibles pour le joueur.
     */
	public List<String> actionsPossible(StateGame state, Player currentPlayer) {
		List<String> actions = new ArrayList<>();
		Player[][] currentGrid = state.getGrid();
		Map<Player, Point> currentPlayerPositions = state.getPlayerPosition();
		Point currentPosition = currentPlayerPositions.get(currentPlayer);

		if (isValidPosition(currentPosition.x - 1, currentPosition.y, currentGrid)) {
			actions.add("up");
		}
		if (isValidPosition(currentPosition.x + 1, currentPosition.y, currentGrid)) {
			actions.add("down");
		}
		if (isValidPosition(currentPosition.x, currentPosition.y - 1, currentGrid)) {
			actions.add("left");
		}
		if (isValidPosition(currentPosition.x, currentPosition.y + 1, currentGrid)) {
			actions.add("right");
		}
		return actions;
	}
	
	/**
     * Applique une action du joueur dans l'état actuel du jeu.
     * 
     * @param state         L'état actuel du jeu.
     * @param action        L'action à appliquer.
     * @param currentPlayer Le joueur effectuant l'action.
     * @return              Le nouvel état du jeu après avoir appliqué l'action.
     */
	public StateGame applyAction(StateGame state, String action, Player currentPlayer) {
		Player[][] currentGrid = state.getGrid();
		Map<Player, Point> currentPlayerPositions = state.getPlayerPosition();
		Point currentPosition = currentPlayerPositions.get(currentPlayer);

		// Creer une copie de la grille pour la modification
		Player[][] newGrid = new Player[currentGrid.length][currentGrid[0].length];
		for (int i = 0; i < currentGrid.length; i++) {
			newGrid[i] = Arrays.copyOf(currentGrid[i], currentGrid[i].length);
		}

		Point newPosition = new Point(currentPosition);

		switch (action) {
		case "up":
			newPosition.x -= 1;
			break;
		case "down":
			newPosition.x += 1;
			break;
		case "left":
			newPosition.y -= 1;
			break;
		case "right":
			newPosition.y += 1;
			break;
		default:
			System.out.println("Action non reconnue.");
			return state; // Retourner l'etat d'origine si l'action est invalide
		}

		// Verifier si la nouvelle position est valide avant de mettre à jour la grille
		if (isValidPosition(newPosition.x, newPosition.y, currentGrid)) {
			newGrid[currentPosition.x][currentPosition.y] = currentPlayer;
			newGrid[newPosition.x][newPosition.y] = currentPlayer;
		} else {
			System.out.println("Action non applicable. Position invalide.");
		}

		// Mettre à jour les positions des joueurs dans une nouvelle carte
		Map<Player, Point> newPlayerPositions = new HashMap<>(currentPlayerPositions);
		newPlayerPositions.put(currentPlayer, newPosition);

		return new StateGame(newGrid, newPlayerPositions,currentPlayer);
	}

	/**
	 * Applique les actions simultanément à l'état du jeu.
	 * 
	 * @param state          L'état actuel du jeu.
	 * @param playerActions  Map associant chaque joueur à son action à appliquer.
	 * @return               L'état du jeu mis à jour après l'application des actions.
	 */
	public StateGame applyActions(StateGame state, Map<Player, String> playerActions) {
		StateGame nextState = state.copy(); // Copie de l'état actuel du jeu
		Action action = new Action(); // Initialisation de l'objet Action
		
		// Parcours de la map des actions de chaque joueur
		for (Map.Entry<Player, String> entry : playerActions.entrySet()) {
			Player player = entry.getKey(); // Joueur associé à l'action
			String ch = entry.getValue(); // Action à appliquer
			
			// Vérification si l'action est non nulle et applicable
			if (ch != null && action.isApplicableAction(nextState, ch, player)) {
				// Application de l'action et mise à jour de l'état du jeu
				System.out.println("joueur : "+player+" action : "+ch);
				nextState = action.applyAction(nextState, ch, player);
			}
		}
		return nextState; // Retourne l'état du jeu mis à jour
	}


	/**
     * Vérifie si une action est applicable avant de l'exécuter.
     * 
     * @param state         L'état actuel du jeu.
     * @param action        L'action à vérifier.
     * @param currentPlayer Le joueur effectuant l'action.
     * @return              true si l'action est applicable, sinon false.
     */
	public boolean isApplicableAction(StateGame state, String action, Player currentPlayer) {
		// Obtenir la position actuelle du joueur
		Point currentPosition = state.getPlayerPosition().get(currentPlayer);

		// Vérifier si la nouvelle position après l'action est valide
		switch (action) {
		case "up":
			return isValidPosition(currentPosition.x - 1, currentPosition.y, state.getGrid());
		case "down":
			return isValidPosition(currentPosition.x + 1, currentPosition.y, state.getGrid());
		case "left":
			return isValidPosition(currentPosition.x, currentPosition.y - 1, state.getGrid());
		case "right":
			return isValidPosition(currentPosition.x, currentPosition.y + 1, state.getGrid());
		default:
			return false;
		}
	}

	/**
     * Retourne une action aléatoire pour un joueur dans l'état actuel du jeu.
     * 
     * @param state         L'état actuel du jeu.
     * @param currentPlayer Le joueur pour lequel l'action est choisie aléatoirement.
     * @return              L'action choisie aléatoirement, ou null si aucune action possible.
     */
	public String getRandomAction(StateGame state, Player currentPlayer) {
		List<String> possibleActions = actionsPossible(state, currentPlayer);

		// Sélectionner une action aléatoire parmi les actions possible
		if (!possibleActions.isEmpty()) {
			Random random = new Random();
			int randomIndex = random.nextInt(possibleActions.size());
			return possibleActions.get(randomIndex);
		} else {
			// Aucune action applicable, retourner null
			currentPlayer.setAlive(false);
			return null;
		}
	}
}

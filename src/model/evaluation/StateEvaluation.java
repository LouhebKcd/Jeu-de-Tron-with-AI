package model.evaluation;

import model.jeu.*;
/**
 * L'interface StateEvaluation définit une méthode pour évaluer l'état actuel du jeu.
 */
public interface StateEvaluation {
    /**
     * Évalue l'état actuel du jeu et retourne un tableau d'entiers représentant les scores des joueurs.
     * 
     * @param state L'état actuel du jeu à évaluer.
     * @return Un tableau d'entiers représentant les scores des joueurs.
     */
    int[] evaluate(StateGame state);
}

package model.algorithmes;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import model.jeu.*;
import model.evaluation.*;

/**
 * Implémentation de base de l'algorithme Paranoid pour la recherche de la meilleure action dans un jeu.
 */
public class ParanoidAlgorithm extends AbstractAlgorithmeSearch{

    /**
     * Constructeur de la class ParanoidAlgorithm.
     * 
     * @param evaluation Interface qui contient la methode de l'evaluation du jeu.
     * @param ac         Objet de la class Action utilisé pour manipuler les actions dans le jeu.
     */
    public ParanoidAlgorithm(StateEvaluation evaluation, Action ac) {
        super(evaluation,ac);
    }

    /**
     * Algorithme de recherche récursif paranoid.
     * 
     * @param state         L'état du jeu actuel.
     * @param action        objet de la class Action pour manipuler les actions dans le jeu.
     * @param voronoi       objet de la class Voronoi qui permet de calculer la région de chauqe joueur.
     * @param depth         La profondeur de la recherche.
     * @param currentPlayer Le joueur actuel pour lequel l'algorithme est appliqué.
     * @return              Un tableau d'entiers représentant les scores pour chaque joueur.
     */
    @Override
    public int[] algorithmeSearch(StateGame state, Action action,Voronoi voronoi ,int depth, Player currentPlayer ) {
        
        // Etat terminal ou profondeur atteinte : retourne l'évaluation de l'état
        if (isTerminal(state) || depth == 0) {
            voronoi.assignVoronoiRegions(state);
            return evaluation.evaluate(state);
        }

        Map<Player, Point> playerPositions = state.getPlayerPosition();
        List<String> possibleActions = action.actionsPossible(state, currentPlayer);
        int numPlayers = playerPositions.size();
        int[] bestValuesMaxPlayer = new int[numPlayers];
        int[] bestValuesMinPlayer = new int[numPlayers];

        Arrays.fill(bestValuesMaxPlayer, Integer.MIN_VALUE);
        Arrays.fill(bestValuesMinPlayer, Integer.MAX_VALUE);

        //Parcours des actions possibles
        for (String possibleAction : possibleActions) {
            StateGame nextState = state.copy();

            //Applique l'action
            nextState = action.applyAction(nextState, possibleAction, currentPlayer);

            int[] value = algorithmeSearch(nextState, action,voronoi ,depth - 1, nextPlayer(nextState, currentPlayer));
            
            // Mise à jour des meilleures valeurs si nécessaire
            if (currentPlayer.equals(state.getCurrentPlayer())) {
                if (bestValuesMaxPlayer[currentPlayer.getId()] < value[currentPlayer.getId()]) {
                    bestValuesMaxPlayer = Arrays.copyOf(value, value.length);
                }
            } else {
                if (bestValuesMinPlayer[currentPlayer.getId()] > value[currentPlayer.getId()]) {
                    bestValuesMinPlayer = Arrays.copyOf(value, value.length);
                }
            }
        }

        // On maximise quand c'est le joueur max et on minimise quand c'est le joueur adversaire
        if (currentPlayer.equals(state.getCurrentPlayer())) {
            return bestValuesMaxPlayer;
        }else{
            return bestValuesMinPlayer;
        }

    }
    
}
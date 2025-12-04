package model.evaluation;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import model.jeu.*;
/**
 * La class VoronoiStateEvaluation fournit une évaluation de l'état du jeu basée sur les tailles des régions de Voronoi
 */
public class VoronoiStateEvaluation implements StateEvaluation {

    /**
     * Évalue l'état actuel du jeu en attribuant des scores aux joueurs basés sur les tailles des régions de Voronoi.
     * 
     * @param state L'état actuel du jeu.
     * @return      Un tableau d'entiers contenant les scores attribués à chaque joueur.
     */
    @Override
    public int[] evaluate(StateGame state) {
        Map<Integer, Integer> regionSizes = calculateRegionSizes(state);
        int[] scoresPlayer = new int[state.getPlayerPosition().size()];
        for (Player player : state.getPlayerPosition().keySet()) {
            Point playerPosition = state.getPlayerPosition().get(player);
            int regionOwner = state.getRegionOwner(playerPosition.x, playerPosition.y);
            int regionSize = regionSizes.getOrDefault(regionOwner, 0);

            if (!player.getIsAlive()) {
                scoresPlayer[player.getId()] = -1;
            }else{
                scoresPlayer[player.getId()] = regionSize;
            }
            
        }
        return scoresPlayer;
    }

    /**
     * Calcule les tailles des régions de Voronoi pour chaque joueur sur la grille de jeu.
     * 
     * @param state L'état actuel du jeu.
     * @return      Une map contenant la taille de chaque région de Voronoi pour chaque joueur.
     */
    public Map<Integer, Integer> calculateRegionSizes(StateGame state) {
        Map<Integer, Integer> regionSizes = new HashMap<>();
        for (int row = 0; row < state.getGrid().length; row++) {
            for (int col = 0; col < state.getGrid()[0].length; col++) {
                int regionOwner = state.getRegionOwner(row, col);
                if (regionOwner != -1) {
                    regionSizes.put(regionOwner, regionSizes.getOrDefault(regionOwner, 0) + 1);
                }
            }
        }
        return regionSizes;
    }

}

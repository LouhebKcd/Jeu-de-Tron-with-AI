package model.evaluation;

import java.util.LinkedList;
import java.util.Queue;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import model.jeu.*;
/**
 * La class Voronoi représente un système de calcul de régions de Voronoi pour chaque joueur sur la grille de jeu.
 */
public class Voronoi {

   /**
     * Assigner des régions de Voronoi à chaque joueur sur la grille de jeu.
     * 
     * @param state L'état actuel du jeu.
     */
    public void assignVoronoiRegions(StateGame state) {
        // Parcours de chaque case du plateau de jeu
        for (int row = 0; row < state.getGrid().length; row++) {
            for (int col = 0; col < state.getGrid()[0].length; col++) {
                double minDistance = Double.MAX_VALUE;
                int regionOwner = -1;
                List<Player> minDistancePlayers = new ArrayList<>(); // Liste des joueurs avec la distance minimale

                // Calcul de la distance entre la case actuelle et chaque joueur
                if (state.getGrid()[row][col] == null) {
                    for (Player player : state.getPlayerPosition().keySet()) {
                        int[][] distances = dijkstra(state, player);
                        double distance = distances[row][col];
                        if (distance < minDistance) {
                            minDistance = distance;
                            minDistancePlayers.clear(); // Réinitialiser la liste s'il y a une nouvelle distance
                                                        // minimale
                            minDistancePlayers.add(player);
                        } else if (distance == minDistance) {
                            minDistancePlayers.add(player); // Ajouter le joueur si sa distance est égale à la distance
                                                            // minimale
                        }
                    }

                    // Vérifier s'il y a plusieurs joueurs à égale distance
                    if (minDistancePlayers.size() == 1) {
                        regionOwner = minDistancePlayers.get(0).getId(); // Un seul joueur avec la distance minimale,
                                                                         // donc attribuer la région
                    } else {
                        // Plusieurs joueurs avec la même distance, ne pas attribuer de région
                        regionOwner = -1;
                    }

                    // Attribue à la case le propriétaire de la région de Voronoi (ou -1 si aucun
                    // joueur n'est clairement le plus proche)
                    state.setRegionOwner(row, col, regionOwner);

                    // Parcours de chaque joueur pour attribuer sa propre position à sa région
                    for (Player player : state.getPlayerPosition().keySet()) {
                        Point playerPosition = state.getPlayerPosition().get(player);
                        state.setRegionOwner(playerPosition.x, playerPosition.y, player.getId());
                    }
                }
            }
        }
    }

    /**
     * Calculer les distances de Dijkstra à partir de la position d'un joueur sur la grille de jeu.
     * 
     * @param state  L'état actuel du jeu.
     * @param player Le joueur dont on calcule les distances.
     * @return       Une matrice contenant les distances de Dijkstra depuis la position du joueur.
     */
    public int[][] dijkstra(StateGame state, Player player) {
        int[][] distances = new int[state.getGrid().length][state.getGrid()[0].length]; // Initialisation de la matrice
                                                                                        // des distances

        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }; // Déplacements correspondant à haut, bas,
                                                                           // gauche, droite
        Player[][] currentGrid = state.getGrid();

        Point currentPosition = state.getPlayerPosition().get(player);
        int row = currentPosition.x;
        int col = currentPosition.y;

        // Initialisation des distances à l'infini pour toutes les cases
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances[0].length; j++) {
                distances[i][j] = Integer.MAX_VALUE;
            }
        }
        distances[row][col] = 0;

        // File d'attente pour le parcours en largeur
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(row, col));

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            int x = current.x;
            int y = current.y;
            int distance = distances[x][y];

            for (int[] direction : directions) {
                int newX = x + direction[0];
                int newY = y + direction[1];

                // Vérification si la case voisine est valide et non bloquée par un mur ou un
                // autre joueur
                if (Action.isValidPosition(newX, newY, currentGrid) && distances[newX][newY] == Integer.MAX_VALUE) {
                    // Mise à jour de la distance si elle est plus courte
                    distances[newX][newY] = distance + 1;
                    queue.add(new Point(newX, newY));
                }
            }
        }
        return distances;
    }

}

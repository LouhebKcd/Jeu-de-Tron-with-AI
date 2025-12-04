package model.jeu;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * La class StateGame représente l'état actuel du jeu, avec la position des joueurs sur la grille.
 */
public class StateGame {

    private Player[][] grid; // Matrice représentant la grille du jeu
    private Map<Player, Point> playerPosition; // Map associant chaque joueur à sa position sur la grille
    private Player currentPlayer; // Joueur actuel dans l'état
    private Map<Point, Integer> regionOwners; // Structure de données pour stocker les propriétaires de région
    public Map<Player, Set<Player>> teamPlayers;// map qui permet de stocker chaque joueur avec ces co-équipiers

    /**
     * Constructeur de la class StateGame.
     * 
     * @param grille        Matrice représentant la grille du jeu.
     * @param playerPos     Map associant chaque joueur à sa position sur la grille.
     * @param currentPlayer Joueur actuel dans l'état.
     */
    public StateGame(Player[][] grille, Map<Player, Point> playerPos, Player currentPlayer) {
        this.grid = grille;
        this.playerPosition = playerPos;
        this.currentPlayer = currentPlayer;
        this.regionOwners = new HashMap<>(); // Initialisation de la structure de données
        this.teamPlayers = new HashMap<>(); // Initialisation du map pour représenter les équipes
    }

    /**
     * Méthode pour attribuer le propriétaire de la région à une case donnée.
     * 
     * @param row          L'index de la ligne de la case.
     * @param col          L'index de la colonne de la case.
     * @param regionOwner  L'identifiant du joueur propriétaire de la région.
     */
    public void setRegionOwner(int row, int col, int regionOwner) {
        regionOwners.put(new Point(row, col), regionOwner);
    }

    /**
     * Méthode pour récupérer le propriétaire de la région d'une case donnée.
     * 
     * @param row  L'index de la ligne de la case.
     * @param col  L'index de la colonne de la case.
     * @return L'identifiant du joueur propriétaire de la région de la case spécifiée, ou -1 si la case n'a pas de propriétaire défini.
     */
    public int getRegionOwner(int row, int col) {
        return regionOwners.getOrDefault(new Point(row, col), -1);
    }


    /**
     * Obtient la grille du jeu.
     * 
     * @return La matrice représentant la grille du jeu.
     */
    public Player[][] getGrid() {
        return grid;
    }

    /**
     * Obtient la position des joueurs sur la grille.
     * 
     * @return La map associant chaque joueur à sa position sur la grille.
     */
    public Map<Player, Point> getPlayerPosition() {
        return playerPosition;
    }

    /**
     * Obtient le joueur courant dans l'état.
     * 
     * @return Le joueur courant.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Modifie le joueur courant dans l'état.
     * 
     * @param currentPlayer Le nouveau joueur courant.
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

     /**
     * Retourne le map représentant les équipes.
     * 
     * @return map représentant les équipes.
     */
    public Map<Player, Set<Player>> getTeamPlayers() {
        return this.teamPlayers;
    }

     /**
     * Pour metre a jour le map représentant les équipes.
     * 
     * @param map représentant les équipes.
     */
    public void setTeamPlayers(Map<Player, Set<Player>> teamPlayers) {
        this.teamPlayers = teamPlayers;
    }

    /**
     * Crée une copie de l'état actuel du jeu.
     * 
     * @return Une copie de l'état actuel du jeu.
     */
    public StateGame copy() {
        StateGame newState = new StateGame(this.grid, this.playerPosition, this.currentPlayer);
        return newState;
    }

    /**
     * Affiche la grille du jeu.
     */
    public void showGrid() {
        for (int row = 0; row < this.grid.length; row++) {
            for (int col = 0; col < this.grid.length; col++) {
                if (this.grid[row][col] != null) {
                    System.out.print(" " + this.grid[row][col].getSymbole() + " ");
                } else {
                    System.out.print(" . ");
                }
            }
            System.out.println(); // Saut de ligne après chaque ligne de la grille
        }
    }
}

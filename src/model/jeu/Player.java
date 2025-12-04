package model.jeu;

/**
 * La class Player représente un joueur dans le jeu.
 */
public class Player {

    private String nom; // Nom du joueur
    private int id; // Identifiant unique du joueur
    private char symbole; // Symbole associé au joueur sur la grille du jeu
    private boolean isAlive; // Indique si le joueur est en vie ou non

    /**
     * Constructeur de la class Player.
     * 
     * @param nom     Nom du joueur
     * @param id      Identifiant unique du joueur
     * @param symbole Symbole associé au joueur sur la grille du jeu
     */
    public Player(String nom, int id, char symbole) {
        this.nom = nom;
        this.id = id;
        this.symbole = symbole;
        this.isAlive = true; // Le joueur est initialement en vie.
    }

    /**
     * Obtient le nom du joueur.
     * 
     * @return Le nom du joueur.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Obtient l'identifiant unique du joueur.
     * 
     * @return L'identifiant unique du joueur.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtient le symbole associé au joueur.
     * 
     * @return Le symbole associé au joueur.
     */
    public char getSymbole() {
        return symbole;
    }

    /**
     * Obtient l'état de vie du joueur.
     * 
     * @return true si le joueur est en vie, sinon false.
     */
    public boolean getIsAlive() {
        return isAlive;
    }

    /**
     * Définit l'état de vie du joueur.
     * 
     * @param isAlive true si le joueur est en vie, sinon false.
     */
    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    /**
     * Retourne une représentation textuelle du joueur.
     * 
     * @return Le nom du joueur.
     */
    @Override
    public String toString() {
        return this.nom;
    }
}

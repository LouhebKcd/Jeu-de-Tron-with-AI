package vue;

/**
 * Un Main pour lancer l'interface graphique du jeu
 */
public class MainInter {

    public static void main(String[] args) {
        // une instance de SplashScreen pour afficher l'écran de démarrage
        new SplashScreen();
        // une instance de GameInterface pour afficher l'interface graphique du jeu
        new GameInterface();

    }
}

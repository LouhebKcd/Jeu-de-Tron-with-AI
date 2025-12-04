package vue;

import java.io.File;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Une classe représentant un écran de démarrage avec une image et une barre
 * de progression.
 */
public class SplashScreen extends JFrame {

    private JLabel imageLabel;
    private JProgressBar progressBar;
    private int progress = 0;

    /**
     * Constructeur de la classe SplashScreen.
     * Crée un écran de démarrage avec une image et une barre de progression.
     */
    public SplashScreen() {
        // Creer une image pour la splash screen
        String filePath = new File("").getAbsolutePath();
        ImageIcon imageIcon = new ImageIcon(filePath + "/ressources/image/splash.jpg");
        imageLabel = new JLabel(imageIcon);

        // Creer une barre de progression
        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setForeground(Color.green);

        // Creer un conteneur pour l'image et la barre de progression
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(imageLabel, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);

        // Ajouter le conteneur la fenetre
        add(panel, BorderLayout.CENTER);

        // Taille de la fenetre
        setSize(0, 100);

        // Centrer la fenetre sur l'ecran
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 4 - getWidth() / 4, dim.height / 4 - getHeight() / 2);

        // Afficher la fenetre
        setUndecorated(true);
        pack();
        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Simuler une tache longue
        for (int i = 0; i <= 100; i++) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setProgress(i);
        }

        // Fermer la fenetre
        dispose();
    }

    /**
     * Méthode pour définir la valeur de la barre de progression.
     *
     * @param value La valeur de progression de la barre (0-100).
     */
    public void setProgress(int value) {
        progress = value;
        progressBar.setValue(progress);
        progressBar.setString(progress + "%");
    }
}
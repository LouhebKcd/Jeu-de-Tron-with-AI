package vue;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.algorithmes.*;
import model.evaluation.*;
import model.jeu.*;

/**
 * Cette classe représente l'interface graphique du jeu.
 */
public class GameInterface extends JFrame {

    private final int gridSizelength;
    private final int gridSizeWidth;
    private final int cellSize = 40;
    private JButton startButton;
    private JButton resetButton;
    private JButton stopButton;
    private JButton replaceButton;
    private JButton exitButton;
    JPanel legendPanel;
    private JPanel boardPanel;
    private StateGame stateGame;
    // Variables de contrôle
    boolean stop = false;
    int reset = 0;
    boolean replace = true;
    // Étiquette pour afficher le gagnant
    private JLabel winnerLabel;

    /**
     * Constructeur de l'interface graphique du jeu.
     */
    public GameInterface() {
        this.stateGame = randomStateGame();
        this.gridSizelength = stateGame.getGrid().length;
        this.gridSizeWidth = stateGame.getGrid()[0].length;

        setTitle("Game Interface");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        legendPanel = createLegendPanel();
        winnerLabel = new JLabel(" ");
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 30));

        addComponentsToFrame();
        setVisible(true);
    }

    /**
     * Ajoute les composants à la fenêtre.
     */
    private void addComponentsToFrame() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(winnerLabel, BorderLayout.SOUTH);
        contentPane.add(createBoardPanel(stateGame), BorderLayout.CENTER);
        contentPane.add(createControlPanel(), BorderLayout.NORTH);
        contentPane.add(legendPanel, BorderLayout.WEST);
        contentPane.setBackground(Color.BLACK);
    }

    /**
     * Initialise les composants de l'interface graphique.
     */
    private void initComponents() {
        startButton = createButton("Start", this::startGameAction);
        resetButton = createButton("Restart", this::resetGameAction);
        stopButton = createButton("Stop", this::togglePlayAction);
        replaceButton = createButton("Replace", this::replaceAction);
        exitButton = createButton("Exit", this::exitAction);
    }

    /**
     * Crée un bouton avec un libellé et un écouteur d'action donnés.
     *
     * @param label          Libellé du bouton
     * @param actionListener Écouteur d'action du bouton
     * @return Bouton créé
     */
    private JButton createButton(String label, ActionListener actionListener) {
        JButton button = new JButton(label);
        button.addActionListener(actionListener);
        return button;
    }

    /**
     * Action du bouton "Start".
     *
     * @param e Événement de clic sur le bouton "Start"
     */
    private void startGameAction(ActionEvent e) {
        Thread gameThread = new Thread(() -> {
            startGame(stateGame);
        });
        if (reset == 0) {
            replace = false;
            gameThread.start();
            reset++;
        }
    }

    /**
     * Action du bouton "Restart".
     *
     * @param e Événement de clic sur le bouton "Restart"
     */
    private void resetGameAction(ActionEvent e) {
        int aliveCount = 0;
        for (Player player : stateGame.getPlayerPosition().keySet()) {
            if (player.getIsAlive()) {
                aliveCount++;
            }
        }
        if (aliveCount <= 1) {
            resetGame();
            reset = 0;
        }
    }

    /**
     * Action du bouton "Stop".
     *
     * @param e Événement de clic sur le bouton "Stop"
     */
    private void togglePlayAction(ActionEvent e) {
        stop = !stop;
        stopButton.setText(stop ? "Play" : "Stop");
    }

    /**
     * Action du bouton "Exit".
     *
     * @param e Événement de clic sur le bouton "Exit"
     */
    private void exitAction(ActionEvent e) {

        dispose();
        System.exit(0);
    }

    /**
     * Action du bouton "Replace".
     *
     * @param e Événement de clic sur le bouton "Replace"
     */
    private void replaceAction(ActionEvent e) {
        if (replace) {
            resetGame();
            reset = 0;
            this.stateGame = randomStateGame();
            boardPanel.repaint();
            updateBoardPanel(stateGame);
        }

    }

    /**
     * Réinitialise le jeu.
     */
    private void resetGame() {
        winnerLabel.setText(" ");

        for (Map.Entry<Player, Point> entry : stateGame.getPlayerPosition().entrySet()) {
            Player key = entry.getKey();
            key.setAlive(true);
        }

        boardPanel.repaint();
        updateBoardPanel(stateGame);
    }

    /**
     * Crée le panneau de contrôle de l'interface graphique.
     *
     * @return Le panneau de contrôle créé
     */
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setBackground(Color.black);

        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        controlPanel.add(resetButton);
        controlPanel.add(replaceButton);
        controlPanel.add(exitButton);

        return controlPanel;
    }

    /**
     * Crée le panneau de la grille de jeu.
     *
     * @param stateGame État actuel du jeu
     * @return Le panneau de la grille de jeu créé
     */
    private JPanel createBoardPanel(StateGame stateGame) {
        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dessine l'image de fond
                String filePath = new File("").getAbsolutePath();
                ImageIcon imageIcon = new ImageIcon(filePath + "/ressources/image/background.jpeg");
                Image image = imageIcon.getImage();
                int imageWidth = image.getWidth(this);
                int imageHeight = image.getHeight(this);
                int startXPosition = (getWidth() - imageWidth) / 2;
                int startYPosition = (getHeight() - imageHeight) / 2;
                g.drawImage(image, startXPosition, startYPosition, imageWidth, imageHeight, this);

                drawGrid(g);
                drawPlayers(g);
            }
        };
        boardPanel.setBackground(Color.BLACK);
        return boardPanel;
    }

    /**
     * Dessine les joueurs sur la grille.
     *
     * @param g Graphique sur lequel dessiner
     */
    private void drawPlayers(Graphics g) {
        // Dessine les joueurs en utilisant les données de position actuelles
        for (Map.Entry<Player, Point> entry : stateGame.getPlayerPosition().entrySet()) {
            Player player = entry.getKey();
            Point position = entry.getValue();

            int startXPosition = (getWidth() - gridSizelength * cellSize) / 4;
            int startYPosition = (getHeight() - gridSizeWidth * cellSize) / 4;

            int x = startXPosition + position.x * cellSize + cellSize / 4;
            int y = startYPosition + position.y * cellSize + cellSize / 4;

            if (player.getIsAlive()) {
                g.setColor(getPlayerColor(player.getId()));
                g.fillOval(x, y, cellSize / 2, cellSize / 2);
            } else {
                g.setColor(getPlayerColor(player.getId()));
                g.fillOval(x, y, cellSize / 2, cellSize / 2);
                g.setColor(Color.white);
                g.fillRect(x, y + 15, cellSize / 2, cellSize / 8);
                g.fillRect(x + 15, y, cellSize / 8, cellSize / 8);
                g.fillRect(x, y, cellSize / 8, cellSize / 8);
            }
        }
    }

    /**
     * Dessine les joueurs sur la grille en utilisant les données de position
     * fournies.
     *
     * @param g              Graphique sur lequel dessiner
     * @param playerPosition Map contenant les joueurs et leurs positions
     * @return Le graphique mis à jour avec les joueurs dessinés
     */
    private Graphics drawPlayers(Graphics g, Map<Player, Point> playerPosition) {
        for (Map.Entry<Player, Point> entry : playerPosition.entrySet()) {
            Player player = entry.getKey();
            Point position = entry.getValue();

            // Calcul des positions de départ pour centrer les joueurs
            int startXPosition = (getWidth() - gridSizelength * cellSize) / 4;
            int startYPosition = (getHeight() - gridSizeWidth * cellSize) / 4;

            int x = startXPosition + position.x * cellSize + cellSize / 4;
            int y = startYPosition + position.y * cellSize + cellSize / 4;

            if (player.getIsAlive()) {
                g.setColor(getPlayerColor(player.getId()));
                g.fillOval(x, y, cellSize / 2, cellSize / 2);
            } else {
                g.setColor(getPlayerColor(player.getId()));
                g.fillOval(x, y, cellSize / 2, cellSize / 2);
                g.setColor(Color.white);
                g.fillRect(x, y + 15, cellSize / 2, cellSize / 8);
                g.fillRect(x + 15, y, cellSize / 8, cellSize / 8);
                g.fillRect(x, y, cellSize / 8, cellSize / 8);
            }
        }
        return g;
    }

    /**
     * Dessine la grille de jeu.
     *
     * @param g Graphique sur lequel dessiner
     * @return Le graphique mis à jour avec la grille
     */
    private Graphics drawGrid(Graphics g) {
        g.setColor(Color.white);

        // Calculer les positions de départ pour centrer la grille
        int startXPosition = (getWidth() - gridSizelength * cellSize) / 4;
        int startYPosition = (getHeight() - gridSizeWidth * cellSize) / 4;

        // Dessiner les lignes verticales
        for (int x = 0; x <= gridSizelength; x++) {
            int xPosition = startXPosition + x * cellSize;
            g.drawLine(xPosition, startYPosition, xPosition, startYPosition + gridSizeWidth * cellSize);
        }

        // Dessiner les lignes horizontales
        for (int y = 0; y <= gridSizeWidth; y++) {
            int yPosition = startYPosition + y * cellSize;
            g.drawLine(startXPosition, yPosition, startXPosition + gridSizelength * cellSize, yPosition);
        }

        return g;
    }

    /**
     * Met à jour le panneau de la grille avec un nouvel état de jeu.
     *
     * @param newStateGame Nouvel état du jeu à afficher
     */
    public void updateBoardPanel(StateGame newStateGame) {
        Graphics g = boardPanel.getGraphics();
        drawPlayers(g);

    }

    /**
     * Obtient la couleur associée à un joueur en fonction de son ID.
     *
     * @param id ID du joueur
     * @return Couleur associée au joueur
     */
    public static Color getPlayerColor(int id) {
        final Color[] playerColors = { Color.BLUE, Color.ORANGE, Color.MAGENTA, Color.PINK, Color.GREEN };

        if (id >= 0 && id < playerColors.length) {
            return playerColors[id];
        } else {
            return Color.RED; // Couleur par défaut
        }
    }

    /**
     * Détermine le joueur gagnant.
     *
     * @return Joueur gagnant s'il y en a un, sinon null
     */
    private Player determineWinner() {
        int aliveCount = 0;
        Player winner = null;
        for (Player player : stateGame.getPlayerPosition().keySet()) {
            if (player.getIsAlive()) {
                aliveCount++;
                winner = player;
            }
        }
        if (aliveCount == 1) {
            return winner;
        } else {
            return null;
        }
    }

    /**
     * Génère un état de jeu aléatoire avec une grille et des joueurs positionnés
     * aléatoirement.
     *
     * @return État de jeu initial aléatoire
     */
    public StateGame randomStateGame() {
        int gridSize = 15; // Taille de la grille
        int numPlayers = 3; // Nombre de joueurs

        Player[][] grid = new Player[gridSize][gridSize];
        Map<Player, Point> playerPosition = new HashMap<>();

        Random random = new Random();

        for (int i = 0; i < numPlayers; i++) {
            String playerName = "Player " + (i + 1);
            char playerSymbol = (char) ('1' + i);
            Player player = new Player(playerName, i, playerSymbol);

            int x, y;
            do {
                // Génère des coordonnées aléatoires pour le joueur
                x = random.nextInt(gridSize);
                y = random.nextInt(gridSize);
            } while (grid[x][y] != null); // Vérifie si la case est déjà occupée

            // Positionne le joueur sur la grille
            grid[x][y] = player;
            playerPosition.put(player, new Point(x, y));
        }

        // Crée et retourne l'état de jeu initial
        Player currentPlayer = playerPosition.keySet().iterator().next(); // Prend le premier joueur comme joueur
                                                                          // courant
        return new StateGame(grid, playerPosition, currentPlayer);
    }

    /**
     * Démarre le jeu.
     *
     * @param stateGame État initial du jeu
     */
    public void startGame(StateGame stateGame) {
        int delayInMillis = 0;
        StateGame currentState = new StateGame(stateGame.getGrid(), stateGame.getPlayerPosition(),
                stateGame.getCurrentPlayer());
        Action action = new Action();
        Voronoi voronoi = new Voronoi();
        StateEvaluation voronoiEvaluation = new VoronoiStateEvaluation();
        MaxNAlgorithme maxn = new MaxNAlgorithme(voronoiEvaluation, action);
        ParanoidAlgorithm paranoidAlgorithm = new ParanoidAlgorithm(voronoiEvaluation, action);

        while (!maxn.isTerminal(currentState)) {
            if (stop) {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            // Profondeur de recherche des algorithmes
            int depth = 3;
            String ch = null;
            Map<Player, String> playerActions = new HashMap<>();

            // Sélection des actions pour chaque joueur
            for (Player player : currentState.getPlayerPosition().keySet()) {
                if (player.getIsAlive()) {
                    // Utilisation de l'algorithme MaxN pour le premier joueur et Paranoid pour les
                    // autres
                    if (player.getId() == 0) {
                        ch = maxn.getBestActionForPlayer(currentState, voronoi, player, depth);
                    } else {
                        ch = paranoidAlgorithm.getBestActionForPlayer(currentState, voronoi, player, depth);
                    }
                    playerActions.put(player, ch);

                }
            }

            // Application des actions et mise à jour de l'affichage
            currentState = action.applyActions(currentState, playerActions);
            if (boardPanel != null) { // Vérifier si boardPanel n'est pas nul
                Graphics g = boardPanel.getGraphics();
                drawPlayers(g, currentState.getPlayerPosition());
            }
            delayInMillis += 2;

            try {
                Thread.sleep(delayInMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Détermination du gagnant et mise à jour de l'étiquette du gagnant
        Player winner = determineWinner();
        if (winner != null) {
            winnerLabel.setText("                                                           le gagnant est :  *** "
                    + winner.getNom() + " ***");
            winnerLabel.setForeground(getPlayerColor(winner.getId()));

        } else {
            winnerLabel.setText("                                                       MATCH NULL");
        }
        replace = true;
    }

    /**
     * Crée le panneau légende affichant les joueurs et leurs algorithmes.
     *
     * @return Le panneau légende créé
     */
    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new GridLayout(10, 1));
        legendPanel.setBackground(Color.black);

        addTitleLabel(legendPanel);
        addPlayerLabels(legendPanel);

        return legendPanel;
    }

    /**
     * Ajoute une étiquette de titre au panneau légende.
     *
     * @param legendPanel Le panneau légende
     */
    private void addTitleLabel(JPanel legendPanel) {
        JLabel titleLabel = new JLabel("Liste des joueurs :");
        titleLabel.setForeground(Color.cyan);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        legendPanel.add(titleLabel);
    }

    /**
     * Ajoute les étiquettes des joueurs au panneau légende.
     *
     * @param legendPanel Le panneau légende
     */
    private void addPlayerLabels(JPanel legendPanel) {
        for (Map.Entry<Player, Point> entry : stateGame.getPlayerPosition().entrySet()) {
            Player player = entry.getKey();
            String algorithmName = (player.getId() == 0) ? "MaxN" : "Paranoid";
            JLabel legendLabel = new JLabel(player.getNom() + " :" + algorithmName);
            legendLabel.setForeground(getPlayerColor(player.getId()));
            legendLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            legendLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            legendPanel.add(legendLabel);
        }
    }

}

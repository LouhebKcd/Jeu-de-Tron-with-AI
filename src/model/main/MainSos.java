package model.main;

import java.awt.Point;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import model.algorithmes.*;
import model.evaluation.*;
import model.jeu.*;

public class MainSos {
    public static void main(String[] args) {
        // Vérification des arguments en ligne de commande
        if (args.length < 3) {
            System.out.println("Veuillez spécifier la taille de la grille, la profondeur de recherche et le nombre de joueurs par équipes.");
            return;
        }

        // Initialisation des paramètres
        int gridSize = Integer.parseInt(args[0]);
        int depth = Integer.parseInt(args[1]);
        int numTeams = 2;
        int numPlayersPerTeam = Integer.parseInt(args[2]);
        int numPlayers = numTeams * numPlayersPerTeam;

        // Création des joueurs
        Player[] players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            String name = "robot" + (i + 1);
            char symbol = (char) ('1' + i);
            players[i] = new Player(name, i, symbol);
        }

        // Tri des joueurs par ID
        for (int i = 0; i < players.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < players.length; j++) {
                if (players[j].getId() < players[minIndex].getId()) {
                    minIndex = j;
                }
            }
            // Échange des éléments
            Player temp = players[minIndex];
            players[minIndex] = players[i];
            players[i] = temp;
        }

        // Génération et tri des équipes
        Map<Player, Set<Player>> notSortedTeams = generateTeams(players, numPlayersPerTeam);

        Map<Player, Set<Player>> teamPlayers = new TreeMap<>(new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Integer.compare(p1.getId(), p2.getId());
            }
        });
        teamPlayers.putAll(notSortedTeams);



        // Initialisation de la grille
        Player[][] grid = new Player[gridSize][gridSize];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = null;
            }
        }

        // Attribution aléatoire des positions initiales des joueurs sur la grille
        Random random = new Random();

        Map<Player, Point> playerPosition = new HashMap<>();

        Map<String, Set<Player>> equipes = teamsSet(teamPlayers);

        System.out.println("Les équipes sont :");
        for (Map.Entry<String, Set<Player>> entry : equipes.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        for (Player player : players) {
            int x = random.nextInt(gridSize);
            int y = random.nextInt(gridSize);
            while (grid[x][y] != null) { // Vérifie si la case est déjà occupée
                x = random.nextInt(gridSize);
                y = random.nextInt(gridSize);
            }
            grid[x][y] = player;
            playerPosition.put(player, new Point(x, y));
        }

        // Initialisation de l'état initial du jeu
        Player currentPlayer = players[0];
        StateGame initialState = new StateGame(grid, playerPosition, currentPlayer);
        initialState.showGrid();
        initialState.setTeamPlayers(teamPlayers);

        // Initialisation des algorithmes de jeu
        Action action = new Action();
        Voronoi voronoi = new Voronoi();
        StateEvaluation voronoiEvaluation = new VoronoiStateEvaluation();
        MaxNAlgorithme maxn = new MaxNAlgorithme(voronoiEvaluation, action);
        //ParanoidAlgorithm paranoidAlgorithm = new ParanoidAlgorithm(voronoiEvaluation, action);
        SOSAlgorithm sos = new SOSAlgorithm(voronoiEvaluation, action);

        // Boucle principale du jeu
        while (!sos.isTerminalSos(initialState)) {
            String ch = null;
            Map<Player, String> playerActions = new HashMap<>();

            for (Player player : players) {
                // Choix de l'algorithme pour chaque joueur
                if (player.getIsAlive()) {
                    if (equipes.get("equipe1").contains(player)) {
                        initialState.setCurrentPlayer(player);
                        initialState.setTeamPlayers(teamPlayers);
                        ch = sos.getBestActionForPlayer(initialState, voronoi, currentPlayer, depth);
                    } else {
                        initialState.setCurrentPlayer(player);
                        initialState.setTeamPlayers(teamPlayers);
                        //ch = action.getRandomAction(initialState, currentPlayer);
                        //ch = paranoidAlgorithm.getBestActionForPlayer(initialState, voronoi, currentPlayer, depth);
                        ch = maxn.getBestActionForPlayer(initialState, voronoi, currentPlayer, depth);
                    }
                }
                // Stockage des actions des joueurs
                if (ch != null && action.isApplicableAction(initialState, ch, player)) {
                    playerActions.put(player, ch);
                    currentPlayer = sos.nextPlayer(initialState, player);
                } else {
                    // Si aucune action n'est disponible ou si l'action n'est pas applicable, le joueur est éliminé
                    player.setAlive(false);
                    currentPlayer = sos.nextPlayer(initialState, player);
                }
            }
            // Application des actions et affichage de la grille après chaque tour
            initialState = action.applyActions(initialState, playerActions);
            initialState.setTeamPlayers(teamPlayers);
            initialState.showGrid();
        }

        // Affichage de l'équipe gagnante
        if (currentPlayer != null) {
            for (Map.Entry<String, Set<Player>> setPlayers : equipes.entrySet()) {
                if (setPlayers.getValue().contains(currentPlayer)) {
                    System.out.println("GameOver\nLa team gagnante est : " + setPlayers.getKey());
                    break;
                }
            }
        } else {
            System.out.println("Partie null");
        }
    }

    // Méthode pour générer les équipes
    public static Map<Player, Set<Player>> generateTeams(Player[] players, int playersPerTeam) {

        Map<Player, Set<Player>> teams = new HashMap<>();

        // Création des équipes
        for (int i = 0; i < players.length; i += playersPerTeam) {
            for (int j = i; j < i + playersPerTeam && j < players.length; j++) {
                Player player = players[j];
                Set<Player> team = new HashSet<>();
                team.add(player);
                teams.put(player, team);
            }
        }

        // Attribution des coéquipiers
        for (int i = 0; i < players.length; i += playersPerTeam) {
            for (int j = i; j < i + playersPerTeam && j < players.length; j++) {
                Player player1 = players[j];
                for (int k = i; k < i + playersPerTeam && k < players.length; k++) {
                    if (j != k) {
                        Player player2 = players[k];
                        teams.get(player1).add(player2);
                    }
                }
            }
        }
        
        return teams;
    }
        
    // Méthode pour convertir les équipes en un format de Map plus convivial
    public static Map<String, Set<Player>> teamsSet(Map<Player, Set<Player>> playersMap) {
        Map<String, Set<Player>> teamsMap = new HashMap<>();

        for (Map.Entry<Player, Set<Player>> entry : playersMap.entrySet()) {
            Set<Player> team = entry.getValue();
            String teamName = "equipe" + (teamsMap.size() + 1);

            if (!teamsMap.containsValue(team)) {
                teamsMap.put(teamName, team);
            }
        }

        return teamsMap;
    }
}
        

       

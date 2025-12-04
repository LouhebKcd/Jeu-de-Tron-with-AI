package model.main;

import java.awt.Point;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import model.algorithmes.MaxNAlgorithme;
import model.algorithmes.ParanoidAlgorithm;
import model.algorithmes.SOSAlgorithm;
import model.evaluation.StateEvaluation;
import model.evaluation.Voronoi;
import model.evaluation.VoronoiStateEvaluation;
import model.jeu.Action;
import model.jeu.Player;
import model.jeu.StateGame;

public class DemoParametrable {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Initialisation des variables
        int numPlayers = 0;
        int numPlayersPerTeam = 0;

        // Sélection du mode de jeu
        int mode = 0;
        while (true) {
            System.out.println("Veuillez choisir le mode de jeu :");
            System.out.println("1. pour Solo");
            System.out.println("2. pour Equipe");
        
            if (sc.hasNextInt()) {
                mode = sc.nextInt();
                if (mode >= 1 && mode <= 2) {
                    break;
                } else {
                    System.out.println("Saisie invalide, veuillez saisir 1 pour Solo ou 2 pour Equipe.");
                }
            } else {
                System.out.println("Saisie invalide, veuillez saisir un nombre entier.");
                sc.next();
            }
        }

        // Demande de profondeur de recherche et taille de la grille
        int depth = 0;
        while (true) {
            System.out.println("Veuillez saisir la profondeur de recherche :");
            
            if (sc.hasNextInt()) {
                depth = sc.nextInt();
                if (depth > 0) {
                    break; 
                } else {
                    System.out.println("Saisie invalide, veuillez saisir un entier positif pour la profondeur de recherche.");
                }
            } else {
                System.out.println("Saisie invalide, veuillez saisir un nombre entier.");
                sc.next(); 
            }
        }


        int taille = 0;
        while (true) {
            System.out.println("Veuillez saisir la taille de la grille du jeu :");

            if (sc.hasNextInt()) {
                taille = sc.nextInt();
                if (taille > 0) {
                    break; 
                } else {
                    System.out.println("Saisie invalide, veuillez saisir un entier positif pour la taille de la grille du jeu.");
                }
            } else {
                System.out.println("Saisie invalide, veuillez saisir un nombre entier.");
                sc.next(); 
            }
        }


        // Initialisation du nombre de joueurs selon le mode de jeu
        if (mode == 1) {
            while (true) {
                System.out.println("Veuillez saisir le nombre de joueurs : ");
                if (sc.hasNextInt()) {
                    numPlayers = sc.nextInt();
                    if (numPlayers > 0) {
                        break; // Sortir de la boucle si l'entrée est valide
                    } else {
                        System.out.println("Saisie invalide, veuillez saisir un entier positif pour le nombre de joueurs.");
                    }
                } else {
                    System.out.println("Saisie invalide, veuillez saisir un nombre entier.");
                    sc.next(); // Vider le scanner après une saisie incorrecte
                }
            }
        } else if (mode == 2) {
            while (true) {
                System.out.println("Veuillez saisir le nombre de joueurs par équipe : ");
                if (sc.hasNextInt()) {
                    numPlayersPerTeam = sc.nextInt();
                    if (numPlayersPerTeam > 0) {
                        break; // Sortir de la boucle si l'entrée est valide
                    } else {
                        System.out.println("Saisie invalide, veuillez saisir un entier positif pour le nombre de joueurs par équipe.");
                    }
                } else {
                    System.out.println("Saisie invalide, veuillez saisir un nombre entier.");
                    sc.next(); // Vider le scanner après une saisie incorrecte
                }
            }
            numPlayers = numPlayersPerTeam * 2;
        }
        

        // Initialisation des structures de données pour stocker les positions et stratégies des joueurs
        Map<Player, String> playerStrategyMap = new HashMap<>();
        Map<Player, Point> playerPositionMap = new HashMap<>();
        Map<Player, Set<Player>> teamPlayers = new HashMap<>();
        Map<String, Set<Player>> equipes = new HashMap<>();

        // Saisie des positions des joueurs
        for (int i = 0; i < numPlayers; i++) {
            String playerName = "robot" + (i);
            Player player = new Player(playerName, i, Integer.toString(i).charAt(0));
        
            int posX = 0;
            int posY = 0;
        
            while (true) {
                System.out.println("Veuillez saisir la position x du joueur " + playerName + " : ");
                if (sc.hasNextInt()) {
                    posX = sc.nextInt();
                    if (posX >= 0 && posX < taille) {
                        break; 
                    } else {
                        System.out.println("Saisie invalide, veuillez saisir une position x entre 0 et " + (taille - 1) + ".");
                    }
                } else {
                    System.out.println("Saisie invalide, veuillez saisir un nombre entier.");
                    sc.next(); 
                }
            }
        
            while (true) {
                System.out.println("Veuillez saisir la position y du joueur " + playerName + " : ");
                if (sc.hasNextInt()) {
                    posY = sc.nextInt();
                    if (posY >= 0 && posY < taille) {
                        break; 
                    } else {
                        System.out.println("Saisie invalide, veuillez saisir une position y entre 0 et " + (taille - 1) + ".");
                    }
                } else {
                    System.out.println("Saisie invalide, veuillez saisir un nombre entier.");
                    sc.next();
                }
            }
        
            playerPositionMap.put(player, new Point(posX, posY));
        }
        

        // Création des équipes et choix des stratégies
        if(mode == 1){
            
            for (Player player : playerPositionMap.keySet()) {
                String playerName = player.getNom();
            
                int strategyChoice = 0;
                while (true) {
                    System.out.println("Veuillez choisir une stratégie pour le joueur " + playerName + " :");
                    System.out.println("1. MaxN \n2. Paranoid \n3. Aleatoire");
            
                    if (sc.hasNextInt()) {
                        strategyChoice = sc.nextInt();
                        if (strategyChoice >= 1 && strategyChoice <= 3) {
                            break;
                        } else {
                            System.out.println("Saisie invalide, veuillez saisir un nombre entre 1 et 3.");
                        }
                    } else {
                        System.out.println("Saisie invalide, veuillez saisir un nombre entier.");
                        sc.next(); 
                    }
                }
            
                String strategyName = (strategyChoice == 1) ? "MaxN" : (strategyChoice == 2) ? "Paranoid" : "Aleatoire";
                playerStrategyMap.put(player, strategyName);
            }
            
        }
        else if (mode == 2) {
            int nombreEquipes = 2; 

            for (int i = 0; i < nombreEquipes; i++) {
                System.out.println("Équipe " + (i + 1) + ":\n");
                Set<Player> joueursEquipe = new HashSet<>();
            
                for (int j = 0; j < numPlayersPerTeam; j++) {
                    System.out.println("Choisissez un joueur en saisissant son nom complet :");
                    for (Map.Entry<Player, Point> entry : playerPositionMap.entrySet()) {
                        System.out.println(entry.getKey().getNom());
                    }
                    System.out.println();
                    String choixJoueurNom = sc.next();
                    Player choixJoueur = null;
                    for (Map.Entry<Player, Point> entry : playerPositionMap.entrySet()) {
                        if (entry.getKey().getNom().equals(choixJoueurNom)) {
                            choixJoueur = entry.getKey();
                            break;
                        }
                    }
                    joueursEquipe.add(choixJoueur);
                }
            
                for (Player joueur : joueursEquipe) {
                    Set<Player> allies = new HashSet<>(joueursEquipe);
                    teamPlayers.put(joueur, allies);
                }
            }
            
            equipes = teamsSet(teamPlayers);
            
            for (int i = 1; i <= nombreEquipes; i++) {
                int strategyChoice = 0;
                while (true) {
                    System.out.println("Veuillez choisir une stratégie pour l'équipe " + i + " : ");
                    System.out.println("1. SOS \n2. MaxN \n3. Paranoid \n4. Aleatoire");
            
                    if (sc.hasNextInt()) {
                        strategyChoice = sc.nextInt();
                        if (strategyChoice >= 1 && strategyChoice <= 4) {
                            break;
                        } else {
                            System.out.println("Saisie invalide, veuillez saisir un nombre entre 1 et 4.");
                        }
                    } else {
                        System.out.println("Saisie invalide, veuillez saisir un nombre entier.");
                        sc.next(); 
                    }
                }
            
                String strategyName = (strategyChoice == 1) ? "SOS" : (strategyChoice == 2) ? "MaxN" : (strategyChoice == 3) ? "Paranoid" : "Aleatoire";
            
                for (Player player : equipes.get("equipe" + i)) {
                    playerStrategyMap.put(player, strategyName);
                }
            }
            
        }

        // Affichage des équipes et stratégies
        Player currentPlayer = playerPositionMap.keySet().iterator().next();

        if(mode == 2){

            System.out.println("\nÉquipes formées :");
            for (Map.Entry<String, Set<Player>> entry : equipes.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }

            System.out.println("\nStrategie de chaque equipe : ");
            for (Map.Entry<Player, String> entry : playerStrategyMap.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
        else if(mode == 1){
            System.out.println("Les strtégies pour chaque joueur sont : ");
            for (Map.Entry<Player,String> strategy : playerStrategyMap.entrySet()) {
                System.out.println(strategy.getKey().getNom() + " : " + strategy.getValue());
            }
        }

        TreeMap<Player, Point> sortedMapPlayers = new TreeMap<>(new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Integer.compare(p1.getId(), p2.getId());
            }
        });

        // Copier les éléments de teamPlayers vers sortedTeamPlayers
        sortedMapPlayers.putAll(playerPositionMap);

        // Initialisation de la grille de jeu
        Player[][] grid = new Player[taille][taille];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = null;
            }
        }

        // Ajout des joueurs sur la grille
        for (Map.Entry<Player, Point> entry : sortedMapPlayers.entrySet()) {
            Player player = entry.getKey();
            Point position = entry.getValue();
            grid[position.x][position.y] = player;
        }
        
        // Initialisation de l'état de jeu initial
        StateGame initialState = new StateGame(grid, sortedMapPlayers, currentPlayer);
        initialState.setTeamPlayers(teamPlayers);
        initialState.showGrid();

        // Initialisation des algorithmes
        Action action = new Action();
        Voronoi voronoi = new Voronoi();
        StateEvaluation voronoiEvaluation = new VoronoiStateEvaluation();
        MaxNAlgorithme maxn = new MaxNAlgorithme(voronoiEvaluation, action);
        ParanoidAlgorithm paranoid = new ParanoidAlgorithm(voronoiEvaluation, action);
        SOSAlgorithm sos = new SOSAlgorithm(voronoiEvaluation, action);

        // Boucle principale du jeu
        while (true) {
            if (mode == 1) {
                if (maxn.isTerminal(initialState)) {
                    break;
                }
            } else {
                if (sos.isTerminalSos(initialState)) {
                    break;
                }
            }
            String ch = null;
            Map<Player, String> playerActions = new HashMap<>();

            for (Map.Entry<Player, Point> playerKey : sortedMapPlayers.entrySet()) {
                Player player = playerKey.getKey();

                String strategy = playerStrategyMap.get(player);
                if (player.getIsAlive()) {
                    if (strategy.equals("SOS")) {
                        initialState.setCurrentPlayer(player);
                        initialState.setTeamPlayers(teamPlayers);
                        ch = sos.getBestActionForPlayer(initialState, voronoi, currentPlayer, depth);
                    } else if (strategy.equals("MaxN")) {
                        initialState.setCurrentPlayer(player);
                        ch = maxn.getBestActionForPlayer(initialState, voronoi, currentPlayer, depth);
                    } else if (strategy.equals("Paranoid")) {
                        initialState.setCurrentPlayer(player);
                        ch = paranoid.getBestActionForPlayer(initialState, voronoi, currentPlayer, depth);
                    } else if(strategy.equals("Aleatoire")){
                        initialState.setCurrentPlayer(player);
                        ch = action.getRandomAction(initialState, currentPlayer);
                    }
                }

                // Si une action est disponible et applicable, on la stocke dans notre map pour l'appliquer ensuite
                if (ch != null && action.isApplicableAction(initialState, ch, player)) {
                    playerActions.put(player, ch);
                    currentPlayer = maxn.nextPlayer(initialState, player);
                } else {
                    // Si aucune action n'est disponible ou si l'action n'est pas applicable,
                    // on passe au joueur suivant et marque le joueur courant comme éliminé
                    player.setAlive(false);
                    currentPlayer = maxn.nextPlayer(initialState, player);
                }
            }

            // Application des actions de chaque joueur et affichage de la grille de jeu
            initialState = action.applyActions(initialState, playerActions);
            initialState.setTeamPlayers(teamPlayers);
            initialState.showGrid();
        }

        // Affichage du joueur gagnant ou indication d'une partie nulle
        if (currentPlayer != null) {
            if(mode == 1){
                System.out.println("\nGameOver\nLe joueur gagnant est :" + currentPlayer);
            }else{
                for (Map.Entry<String, Set<Player>> setPlayers : equipes.entrySet()) {
                    if (setPlayers.getValue().contains(currentPlayer)) {
                        System.out.println("\nGameOver\nLa team gagnante est : " + setPlayers.getKey());
                        break;
                    }
                }
            }
        } else {
            System.out.println("Partie null");
        }

        sc.close();
    }

    // Méthode utilitaire pour obtenir un tableau de joueurs à partir des positions des joueurs
    public static Player[] players(Map<Player, Point> playersPosition) {
        return playersPosition.keySet().toArray(new Player[0]);
    }

    // Méthode utilitaire pour créer un ensemble d'équipes à partir des joueurs et des équipes
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
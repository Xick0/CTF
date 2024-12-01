package dev.marco.xicko.Game;

import dev.marco.xicko.Collections.Queue.ArrayQueue;
import dev.marco.xicko.Collections.Queue.LinkedQueue;
import dev.marco.xicko.Maps.CustomNetworkADT;
import dev.marco.xicko.Maps.CustomNetworkBidirecional;
import dev.marco.xicko.Maps.CustomNetworkUniDiretional;
import dev.marco.xicko.Player.Player;
import dev.marco.xicko.Bot.Bot;

import java.util.Iterator;
import java.util.Scanner;

public class Game {
    private Player Red, Blue;
    private CustomNetworkADT<Integer> network;
    private ArrayQueue<String> botPositions;

    public Game() {
        setupGame();
    }

    private void setupGame() {
        Red = new Player("Red");
        Blue = new Player("Blue");
        botPositions = new ArrayQueue<>();
        network = setupNetwork();
        setPlayerFlag();
        setupBots();
        startGame();

    }

    public void startGame() {
        while (!isGameOver()) {
            if (!botPositions.isEmpty()) {
                String botName = botPositions.dequeue();
                Bot currentBot = findBotByName(botName);
                if (currentBot != null) {
                    System.out.println(currentBot.getName() + "'s turn");
                    moveBotOneVertex(currentBot, findPlayerByBotName(botName));

                    // Check for game over condition after each move
                    if (isGameOver()) {
                        System.out.println("Game over!");
                        return;
                    }
                }
                botPositions.enqueue(botName); // Put the bot back at the end of the queue

                // Delay for 5 seconds
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Sleep interrupted: " + e.getMessage());
                }
            }
        }
        System.out.println("Game over!");
    }
    private Player findPlayerByBotName(String botName) {
        // Check if the bot belongs to Red or Blue team
        return botName.startsWith("Red") ? Red : Blue;
    }
    private Bot findBotByName(String botName) {
        // Iterate through both Red and Blue teams to find the bot
        for (Bot bot : Red.getBots()) {
            if (bot.getName().equals(botName)) {
                return bot;
            }
        }
        for (Bot bot : Blue.getBots()) {
            if (bot.getName().equals(botName)) {
                return bot;
            }
        }
        return null;
    }

    private void playTurn(Player player) {
        for (Bot bot : player.getBots()) {
            moveBotOneVertex(bot, player);
            // Delay for 5 seconds
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Handle the interruption in case the sleep is interrupted
                Thread.currentThread().interrupt();
                System.out.println("Sleep interrupted: " + e.getMessage());
            }
        }
    }

    private void moveBotOneVertex(Bot bot, Player player) {
        if (bot.getIterator().hasNext()) {
            int nextPosition = bot.getIterator().next();

            if (isPositionOccupied(nextPosition, player)) {
                // Try to recalculate the path
                boolean canMove = recalculatePath(bot, player);

                // If no path available, don't move and end the turn for this bot
                if (!canMove) {
                    System.out.println(bot.getName() + " cannot move.");
                    return;
                }

                nextPosition = bot.getIterator().next();
            }

            // Move the bot
            bot.setCurrentPosition(nextPosition);
            System.out.println(bot.getName() + " moved to position " + nextPosition);
        }
    }

    private boolean isPositionOccupied(int position, Player player) {
        // Check if the given position is occupied by the other player's bot
        Player otherPlayer = (player == Red) ? Blue : Red;
        for (Bot otherBot : otherPlayer.getBots()) {
            if (otherBot.getCurrentPosition() == position) {
                return true;
            }
        }
        return false;
    }
    private boolean recalculatePath(Bot bot, Player player) {
        int target = (player.getName().equals(Red.getName())) ? Blue.getFlag() : Red.getFlag();
        Iterator<Integer> newPath = network.iteratorShortestPath(bot.getCurrentPosition(), target);

        if (newPath.hasNext()) {
            int nextPosition = newPath.next();
            if (!isPositionOccupied(nextPosition, player)) {
                bot.setIterator(newPath);
                return true;
            }
        }

        return false; // No alternative path found
    }



    private boolean hasReachedTarget(Bot bot, Player player) {
        // Determine the opposing player
        Player opposingPlayer = (player.getName().equals(Red.getName())) ? Blue : Red;

        // Check if the bot's current position matches the opposing player's flag position
        return bot.getCurrentPosition() == opposingPlayer.getFlag();
    }

    private boolean isGameOver() {
        // Check if any of Red's bots have reached Blue's flag
        for (Bot bot : Red.getBots()) {
            if (bot.getCurrentPosition() == Blue.getFlag()) {
                System.out.println("Player Red wins! Bot " + bot.getName() + " has captured Blue's flag.");
                return true;
            }
        }

        // Check if any of Blue's bots have reached Red's flag
        for (Bot bot : Blue.getBots()) {
            if (bot.getCurrentPosition() == Red.getFlag()) {
                System.out.println("Player Blue wins! Bot " + bot.getName() + " has captured Red's flag.");
                return true;
            }
        }

        // If no bot has reached the opposing flag, the game is not over
        return false;
    }

    private CustomNetworkADT<Integer> setupNetwork() {
        int mapType = getUserInput("Choose the network type:\n1 - Network Bidirectional\n2 - Network Unidirectional");
        int mapSetup = getUserInput("Do you want to import an existing map or generate a new one?\n1 - Import existing map\n2 - Generate new map");
        CustomNetworkADT<Integer> network = (mapType == 1) ? new CustomNetworkBidirecional<>() : new CustomNetworkUniDiretional<>();

        boolean isMapGenerated = false;

        if (mapSetup == 1) {
            network.importFromJson();
        } else {
            network.generateRandomGraph();
            isMapGenerated = true;
        }

        System.out.println(network);

        // Prompt for export only if the map was generated
        if (isMapGenerated) {
            int exportChoice = getUserInput("Do you want to export the generated map?\n1 - Yes\n2 - No");
            if (exportChoice == 1) {
                String filename = getUserInputString("Enter the filename to export the map:");
                if (network instanceof CustomNetworkBidirecional) {
                    ((CustomNetworkBidirecional<Integer>) network).exportToJson(filename);
                } else if (network instanceof CustomNetworkUniDiretional) {
                    ((CustomNetworkUniDiretional<Integer>) network).exportToJson(filename);
                }
                System.out.println("Map exported successfully to " + filename);
            }
        }

        return network;
    }

    private void setPlayerFlag() {
        Red.setFlag(getUserInput(Red.getName() + ", choose your flag position:"));
        Blue.setFlag(getUserInput(Blue.getName() + ", choose your flag position:"));
    }

    private void setupBots() {
        int numberOfBots = getUserInput("How many Bots to be in the game: ");
        for (int i = 0; i < numberOfBots; i++) {
            setupBotForPlayer(Red, i);
            setupBotForPlayer(Blue, i);
        }
    }


    private void setupBotForPlayer(Player player, int botNumber) {
        Bot bot = new Bot((player.getName() + " " + (botNumber + 1)), getStrategy(player));
        bot.setCurrentPosition(player.getFlag());
        player.addBot(bot);
        botPositions.enqueue(bot.getName()); // Enqueue bots in alternating order
    }

    private Iterator<Integer> getStrategy(Player player) {
        int choice = getUserInput("Choose the strategy:\n1- BFS\n2- DFS\n3- ShortestPath");
        if (choice == 1) return network.iteratorBFS(player.getFlag());
        if (choice == 2) return network.iteratorDFS(player.getFlag());
        return network.iteratorShortestPath(player.getFlag(), (player.getName().equals("Red")) ? Blue.getFlag() : Red.getFlag());
    }

    private int getUserInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt);
        return scanner.nextInt();
    }
    private String getUserInputString(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt);
        return scanner.nextLine();
    }






}
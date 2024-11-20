import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
import java.util.*;


// need to add a check condition to check if there are >=2 && <=6 players

// Lack of the implementation of Go, Chance, Income tax, Free Parking, Go to Jail

// Implement those function by


class Property {
    String name;
    int price;
    int rent;
    boolean owned;
    Player owner;

    public Property(String name, int price, int rent) {
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.owned = false;
        this.owner = null;
    }
}

class Player {
    String name;
    int money;
    int position;
    boolean inJail;
    int state;
    int bonus;
    List<Property> ownedProperties;
    int jailDay;
    // changesjkljk
    public Player(String name) {
        this.name = name;
        this.money = 1500; // Starting money
        this.position = 0; // Starts on square 0
        this.inJail = false;
        this.state = 0; // 0: first turn, in 'GO' doesn't earn  1000
        // 1: other turn, in 'GO' get 1000
        this.bonus = 0;
        this.ownedProperties = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public int getMoney() {
        return money;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public List<Property> getOwnedProperties() {
        return ownedProperties;
    }

    public void addProperty(Property property) {
        ownedProperties.add(property);
    }

}


public class MonopolyGame {
    public static final int BOARD_SIZE = 20;
    public static Property[] properties;
    private static List<Player> players;
    public static int currentPlayerIndex;
    public int dice1, dice2;
    public int round = 0;
    public static final int MAXIMUM_ROUND = 100;

    public MonopolyGame() {
        properties = new Property[]{
                new Property("Go", 0, 0), // 0
                new Property("Central", 800, 90), //  1
                new Property("Wan Chai", 700, 65), // 2
                new Property("Income tax", 0, 0), // 3
                new Property("Stanley", 600, 60), // 4
                new Property("Just Visiting/In Jail", 0, 0), // 5
                new Property("Shek O", 400, 10), // 6
                new Property("Mong Kok", 500, 40), // 7
                new Property("Chance", 0, 0), // 8
                new Property("Tsing Yi", 400, 15), // 9
                new Property("Free Parking", 0, 0), // 10
                new Property("Shatin", 700, 75), // 11
                new Property("Chance", 0, 0), // 12
                new Property("Tuen Mun", 400, 20), // 13
                new Property("Tai Po", 500, 25), //14
                new Property("Go to jail", 0, 0), // 15
                new Property("Sai Kung", 400, 10), //16
                new Property("Yuen Long", 400, 25), //17
                new Property("Chance", 0, 0), //18
                new Property("Tai O", 600, 25) //19
        };

        players = new ArrayList<>();
        currentPlayerIndex = 0;
    }

    public void addPlayer(String name) {
        players.add(new Player(name));
    }

    public static void removePlayer(String name) {
        players.remove(name);
    }
    public List<Player> getPlayers() {
        return players;
    }
    public void playTurn() {
        Player currentPlayer = players.get(currentPlayerIndex);

        // Display the round number
        if (currentPlayerIndex == 0) {
            System.out.println("Round " + (round + 1));
        }

        System.out.println(currentPlayer.name + "'s turn.");
        System.out.println(currentPlayer.name + " has $" + currentPlayer.money);
        Scanner scanner = new Scanner(System.in);
        boolean turnEnd = false;

        while (!turnEnd) {
            // Display formatted player option
            System.out.printf("%-35s %-35s%n%-35s %-35s%n%-35s %-35s%n%-35s", "1. Roll dice", "2. Save game", "3. View players' status", "4. View all players' status", "5. View game status", "6. Query next player?", "7. Quit game\n");
            //System.out.println("1. Roll dice 2. Save game \n3. View player status \n4. View all players' status \n5. View game status \n6. Query next player?\n7. Quit game\n (1/2/3/4/5/6/7)");
            String input = scanner.nextLine();

            // 1. Roll dice
            if (input.equalsIgnoreCase("1")) {
                DiceResult result = rollDice();
                int diceRoll = result.getSum();
                currentPlayer.bonus = 0;

                if (!currentPlayer.inJail && currentPlayer.state == 0) {
                    System.out.println(currentPlayer.name + " rolled a " + diceRoll);
                    currentPlayer.position = (currentPlayer.position + diceRoll) % BOARD_SIZE + 1;
                    System.out.println(currentPlayer.name + " moved to square " + (currentPlayer.position == 0 ? BOARD_SIZE : currentPlayer.position));
                }

                if (!currentPlayer.inJail && currentPlayer.state == 1) {
                    System.out.println(currentPlayer.name + " rolled a " + diceRoll);
                    if ((currentPlayer.position + diceRoll) > BOARD_SIZE) {
                        currentPlayer.bonus = 1;
                    }
                    currentPlayer.position = (currentPlayer.position + diceRoll) % BOARD_SIZE;
                    System.out.println(currentPlayer.name + " moved to square " + (currentPlayer.position == 0 ? BOARD_SIZE : currentPlayer.position));
                }

                currentPlayer.state = 1;
                //Property property = properties[currentPlayer.position - 1];
                Property property = properties[(currentPlayer.position - 1 + BOARD_SIZE) % BOARD_SIZE];

                if (currentPlayer.bonus == 1) {
                    handleGo(currentPlayer);
                    currentPlayer.bonus = 0;
                }
                if (currentPlayer.position == 1 && currentPlayer.bonus == 1) {
                    handleGo(currentPlayer);
                } else if (currentPlayer.position == 4) {
                    handleIncomeTax(currentPlayer);
                } else if (currentPlayer.position == 6) {
                    if (!currentPlayer.inJail) {
                        System.out.println(currentPlayer.name + " landed on " + property.name);
                    }
                    handleJustVisiting(currentPlayer);
                } else if (currentPlayer.position == 9 || currentPlayer.position == 13 || currentPlayer.position == 19) {
                    handleChance(currentPlayer);
                } else if (currentPlayer.position == 11) {
                    handleFreeParking();
                } else if (currentPlayer.position == 16) {
                    handleGoToJail(currentPlayer);
                } else {
                    handleProperty(currentPlayer);
                }
                turnEnd = true;
            }

            // save game

            if (input.equalsIgnoreCase("2")) {
                saveGame();
                System.exit(0); // Exit the game after saving
            }

            if (input.equalsIgnoreCase("3")) {
                System.out.println("Enter player name:");
                String playerName = scanner.nextLine();
                viewPlayerStatus(playerName);
            }

            if (input.equalsIgnoreCase("4")) {
                viewAllPlayersStatus();
            }

            if (input.equalsIgnoreCase("5")) {
                viewGameStatus();
            }

            if (input.equalsIgnoreCase("6")) {
                queryNextPlayer();
            }

            if (input.equalsIgnoreCase("7")) {
                System.out.println("You quit the game.");
                System.exit(0);
            }

            if (!input.equalsIgnoreCase("1") && !input.equalsIgnoreCase("2") && !input.equalsIgnoreCase("3") && !input.equalsIgnoreCase("4") && !input.equalsIgnoreCase("5") && !input.equalsIgnoreCase("6")) {
                System.out.println("Invalid option. Please try again.");
            }
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        if (currentPlayerIndex == 0) {
            round++; // Increment the round number after all players have taken their turn
        }

        System.out.println("<-- Enter to continue -->");
        scanner.nextLine();

    }


    public void queryNextPlayer() {
        int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
        Player nextPlayer = players.get(nextPlayerIndex);
        System.out.println("Next Player: " + nextPlayer.getName());
        System.out.println("Money: $" + nextPlayer.getMoney());
        System.out.println("Position: " + nextPlayer.getPosition());
        System.out.println("In Jail: " + (nextPlayer.inJail ? "Yes" : "No"));
        System.out.println("Owned Properties:");
        for (Property property : nextPlayer.getOwnedProperties()) {
            System.out.println(property.name + " (Price: $" + property.price + ", Rent: $" + property.rent + ")");
        }
    }

    public void viewGameStatus() {
        System.out.println("Game Status:");
        System.out.println("Board:");
        for (int i = 0; i < properties.length; i++) {
            Property property = properties[i];
            System.out.print("Square " + (i + 1) + ": " + property.name);
            if (property.price > 0) {
                System.out.print(" (Price: " + property.price + ", Rent: " + property.rent + ")");
                if (property.owned) {
                    System.out.print(" (Owned by " + property.owner.getName() + ")");
                }
            }
            System.out.println();
        }
        System.out.println("\nPlayers' Positions:");
        for (Player player : players) {
            System.out.println(player.getName() + " is on square " + player.getPosition());
        }
    }

    public void viewAllPlayersStatus() {
        for (Player player : players) {
            System.out.println("Player Name: " + player.getName());
            System.out.println("Money: $" + player.getMoney());
            System.out.println("Position: " + player.getPosition());
            System.out.println("In Jail: " + (player.inJail ? "Yes" : "No"));
            System.out.println("Properties owned:");
            for (Property property : player.getOwnedProperties()) {
                System.out.println(property.name + " (Price: $" + property.price + ", Rent: $" + property.rent + ")");
            }
            System.out.println(); // Add a blank line
        }
    }

    private void viewPlayerStatus(String playerName) {
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(playerName)) {
                System.out.println("Player Name: " + player.getName());
                System.out.println("Money: $" + player.getMoney());
                System.out.println("Position: " + player.getPosition());
                System.out.println("In Jail: " + (player.inJail ? "Yes" : "No"));
                System.out.println("Properties owned: ");
                for (Property property : player.getOwnedProperties()) {
                    System.out.println(property.name + " (Price: $" + property.price + ", Rent: $" + property.rent + ")");
                }
                return;
            }
        }
        System.out.println("Player not found.");
    }

    /*    private int rollDice() {
            Random rand = new Random();
            boolean sameDice = false;
            int dice1 = rand.nextInt(6) + 1;
            int dice2 = rand.nextInt(6) + 1;
            if(dice1 == dice2){
                sameDice = true;
            }
            return dice1+dice2;
            //return rand.nextInt(6) + 1 + rand.nextInt(6) + 1; // Two dice
        }*/
    class DiceResult {
        // private int sum;
        private int dice1, dice2, sum;

        private boolean sameDice;

        public DiceResult(int dice1, int dice2, boolean sameDice) {
            this.dice1 = dice1;
            this.dice2 = dice2;
            this.sameDice = sameDice;
            this.sum = dice1 + dice2;
        }

        public int getDice1() {
            return dice1;
        }

        public int getDice2() {
            return dice2;
        }

        public int getSum() {
            return sum;
        }

        public boolean isSameDice() {
            return sameDice;
        }

    }

    public DiceResult rollDice() {
        Random rand = new Random();
        int dice1 = rand.nextInt(4) + 1;
        int dice2 = rand.nextInt(4) + 1;
        boolean sameDice = (dice1 == dice2);
        return new DiceResult(dice1, dice2, sameDice); // Return a new DiceResult object
    }

    public void handleIncomeTax(Player player) {
        Property property = properties[player.position];
        player.money -= 10 * (player.money / 100);
        System.out.println("You need to pay 10% income tax!");
        System.out.println("Your money becomes " + player.money);
    }

    public void handleProperty(Player player) {
        int Position = (player.position - 1 + BOARD_SIZE) % BOARD_SIZE; //solve out of bound problem
        Property property = properties[Position];

        if (Position == 0) {
            return; // Exit the method so Go will not be treated as property
        }

        if (!property.owned) {
            System.out.println("You landed on " + property.name + ". Price: " + property.price);
            if (player.money >= property.price) {
                System.out.println("Do you want to buy it? (yes/no)");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("yes")) {
                    player.money -= property.price;
                    property.owned = true;
                    property.owner = player;
                    player.addProperty(property);
                    System.out.println("You bought " + property.name + ". Remaining money: " + player.money);
                }
            } else {
                System.out.println("Not enough money to buy this property.");
            }
        } else if (property.owned == true && property.owner == player) {
            System.out.println("You landed on your own place " + property.name);
        } else {
            System.out.println("Property " + property.name + " is owned. Pay rent: " + property.rent);
            player.money -= property.rent;
            System.out.println("Remaining money: " + player.money);
        }
    }


    public void handleGo(Player player) {
        if (player.state == 1) {
            player.money += 1500;
            // System.out.println("Now you are in the " + player.position);
            System.out.println("You have earned 1500 HKD. Now your asset becomes " + player.money);
        }
    }

    public void handleChance(Player player) {
        System.out.println("Now you land on chance!");
        Random num = new Random();
        int amount = (num.nextInt(51) - 30) * 10;
        if (amount > 0) {
            System.out.println("You gained HKD: " + amount);
        } else {
            System.out.println("You lost HKD: " + amount);
        }
        player.addMoney(amount);
        System.out.println("Total money: " + player.getMoney());
    }

    public void handleFreeParking() {
        // System.out.println("Now you are in the " + player.position);
        System.out.println("You landed on Free Parking.");
    }

    private void handleJustVisiting(Player player) { // Also the state of in jail
//        int diceCount = 0;
        player.jailDay++; // jailDay = 1 means Day1 in jail
        while (player.inJail) { // need to get the value of two dice
            DiceResult result = rollDice(); // Call the rollDice method
            int dice1 = result.getDice1();
            int dice2 = result.getDice2();
            int sum = dice1 + dice2; // Get the sum of the dice
            boolean sameDice = result.isSameDice(); // Get the boolean result
            System.out.println("First dice " + player.name + " rolled is " + dice1);
            System.out.println("Second dice " + player.name + " rolled is " + dice2);
            if (sameDice) { // player rolled same dice, get out of Jail
                System.out.println(player.name + " successfully roll a doubles.\n" + player.name + " released from jail now.");
                int position = (player.position + sum) % BOARD_SIZE;
                //player.setPosition(position); // Move player to the position of adding the dice they roll
                player.setPosition(15);
                player.inJail = false;
                break;
            }
//            diceCount++;
//            if (diceCount == 1) {
//            System.out.println("First dice " + player.name + " rolled is " + dice1);
//            System.out.println("Second dice " + player.name + " rolled is " + dice2);
            System.out.println("Do you want to pay fine? (yes/no)");
            System.out.println("Remaining money of " + player.name + " is " + player.money);
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("yes") && (player.money > 150)) {
                player.money -= 150;
                player.inJail = false;
                player.setPosition(player.position + dice1 + dice2);
                player.jailDay = 0; //release
            }
//            else if (diceCount == 2) {
//                System.out.println("First dice " + player.name + " rolled is " + dice1);
//                System.out.println("Second dice " + player.name + " rolled is " + dice2);

//        }
//             else if (diceCount < 3) { // not pay fine, go to next round to roll dice
////            if (dice1 == dice2) {
////                player.inJail = false;
////                player.setPosition(player.position + dice1 + dice2);
////            }
//            } else if (jailCount == 3) {
            else if(input.equalsIgnoreCase("no") && player.jailDay != 3){ //round 1 and round 2 in jail
                System.out.println("Day " + player.jailDay + " in jail...");
                break;
            }
            else {
                if (player.money > 150) {
                    player.money -= 150;
                    System.out.println("You are required to pay a $150 fine to be released from jail now.");
                    player.inJail = false;
                    player.jailDay = 0;
                    player.setPosition(player.position + dice1 + dice2);
                } else {
                    player.money -= 150;
                    System.out.println("You don't have enough money to pay fine, you are broke!");
                    removePlayer(player.name);
                }
            }


        }
    }

    // find if player position currently on go to jail(sq 15)
    public void handleGoToJail(Player player) {
        System.out.println(player.name + " currently landed on [Go to jail]");
        System.out.println(player.name + " is sent to jail");

        player.setPosition(6); // move player to in jail square
        player.inJail = true; // the handle just visiting
        // System.out.println("Player currently landed on: ");
    }
    // data required to store?
    // round, player info, property info

    public void saveGame() {
        String fileName = "monopoly_game_state.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write header
            writer.write("Player Name,Money,Position,In Jail,Owned Properties");
            writer.newLine();

            // Write player data
            for (Player player : players) {
                StringBuilder sb = new StringBuilder();
                sb.append(player.getName()).append(",")
                        .append(player.getMoney()).append(",")
                        .append(player.getPosition()).append(",")
                        .append(player.inJail).append(",");

                // Append owned properties
                List<Property> ownedProperties = player.getOwnedProperties();
                if (ownedProperties.isEmpty()) {
                    sb.append("None");
                } else {
                    for (Property property : ownedProperties) {
                        sb.append(property.name).append(" Price: $").append(property.price)
                                .append("*Rent: $").append(property.rent).append(") (");
                    }
                    // Remove the trailing comma and space
                    sb.setLength(sb.length() - 2);
                }

                writer.write(sb.toString());
                writer.newLine();
            }

            System.out.println("Game saved successfully to " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the game: " + e.getMessage());
        }
    }


    public void loadGame() {
        String fileName = "monopoly_game_state.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String header = reader.readLine(); // Read and ignore the header line

            String line;
            while ((line = reader.readLine()) != null) {
                String[] playerData = line.split(",");

                if (playerData.length < 5) {
                    System.out.println("Invalid data format for player: " + line);
                    continue; // Skip invalid lines
                }

                String playerName = playerData[0].trim();
                int playerMoney = Integer.parseInt(playerData[1].trim());
                int playerPosition = Integer.parseInt(playerData[2].trim());
                boolean inJail = Boolean.parseBoolean(playerData[3].trim());

                // Add player to the game using the addPlayer method
                addPlayer(playerName); // Add the player using the method

                // Initialize the player properties
                Player player = players.get(players.size() - 1); // Get the last added player
                player.addMoney(playerMoney - player.getMoney()); // Adjust the player's money
                player.setPosition(playerPosition);
                player.inJail = inJail;

                // Process owned properties as before...
                // (Keep the owned properties loading logic here)
            }

            System.out.println("Game loaded successfully from " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while loading the game: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number in game state: " + e.getMessage());
        }
    }

    public void modifyGameBoard(int SquareNumber, String name, int price, int rent) {
        properties[SquareNumber].name = name;
        properties[SquareNumber].price = price;
        properties[SquareNumber].rent = rent;
    }


    private void designNewGameboard() {
        Scanner scanner = new Scanner(System.in);

        // Display the current gameboard
        System.out.println("Existing Gameboard:");
        for (int i = 0; i < properties.length; i++) {
            Property property = properties[i];
            System.out.print("Square " + (i + 1) + ": " + property.name);
            if (property.price > 0) {
                System.out.print(" (Price: " + property.price + ", Rent: " + property.rent + ")");
            }
            System.out.println();
        }

        while (true) {
            System.out.println("Enter the square number of the property to modify (1-20):");
            int n;
            try {
                n = Integer.parseInt(scanner.nextLine()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid property index. Please try again.");
                continue;
            }

            if (n >= 0 && n < properties.length && properties[n].price > 0) {
                while (true) {
                    System.out.println("Enter the new name of the property:");
                    String name = scanner.nextLine();

                    // Check if the property name already exists
                    boolean propertyExists = false;
                    for (Property property : properties) {
                        if (property.name.equalsIgnoreCase(name)) {
                            propertyExists = true;
                            break;
                        }
                    }

                    if (propertyExists) {
                        System.out.println("This property already exists in the gameboard. Please enter another name.");
                    } else {
                        System.out.println("Enter the new price of the property:");
                        int price = Integer.parseInt(scanner.nextLine());

                        System.out.println("Enter the new rent of the property:");
                        int rent = Integer.parseInt(scanner.nextLine());

                        modifyGameBoard(n, name, price, rent);

                        System.out.println("Property modified successfully.");
                        break;
                    }
                }
            } else {
                System.out.println("This is not a property square. Please enter again.");
                continue;
            }

            System.out.println("1. modify another property square\n2. save the gameboard\n3. start playing on your designed gameboard\n(Enter 1/2/3)");
            String nextAction = scanner.nextLine();

            if (nextAction.equalsIgnoreCase("2")) {
                saveGameBoard();

                System.out.println("1. Exit\n2. start playing on your designed gameboard\n(Enter 1/2)");
                String Choice = scanner.nextLine();

                if (Choice.equalsIgnoreCase("1")) {
                    System.exit(0); // Exit the program

                } else if (Choice.equalsIgnoreCase("2") || nextAction.equalsIgnoreCase("3")) {
                    System.out.println("Starting the game with the modified gameboard...");
                    System.out.println();
                    break; // Exit the loop to start the game
                }
            }
        }
    }


    public void saveGameBoard() {
        String fileName = "gameboard.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Square name, Price, Rent");
            writer.newLine();
            for (Property property : properties) {
                writer.write(property.name + "," + property.price + "," + property.rent + "\n");
            }
            System.out.println("Gameboard saved successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the gameboard.");
        }
    }

    public void loadGameBoard() {
        String fileName = "gameboard.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int index = 0;

            // Skip the header line
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    int price = Integer.parseInt(parts[1].trim());
                    int rent = Integer.parseInt(parts[2].trim());
                    properties[index] = new Property(name, price, rent);
                    index++;
                }
            }
            System.out.println("Gameboard loaded successfully.");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Do you want to modify the loaded gameboard or start playing on it?");
                System.out.println("1. Modify the gameboard\n2. Start playing");
                String choice = scanner.nextLine();

                if (choice.equals("1")) {
                    designNewGameboard();
                    break;
                } else if (choice.equals("2")) {
                    System.out.println("Starting the game with the loaded gameboard...");
                    break;
                } else {
                    System.out.println("Invalid choice. Please enter 1 or 2.");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading the gameboard.");
        }
    }

    public static void main(String[] args) throws IOException {
        MonopolyGame game = new MonopolyGame();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hi, Welcome to Monopoly Game!");
        while (true) {
            System.out.println("1. Start a new game\n2. Continue progress from last play");
            String choice = scanner.nextLine();

            if (choice.equals("1") || choice.equals("2")) {
                if (choice.equals("2")) {
                    game.loadGame();
                    break;
                }
                break;
            } else {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }

        while (true) {
            System.out.println("Do you want to play on an existing gameboard or a custom gameboard? (existing/custom)");
            System.out.println("Note: If you want to continue from your last game, please enter the existing gameboard and select the previous gameboard you played.");
            String boardChoice = scanner.nextLine();

            if (boardChoice.equalsIgnoreCase("existing")) {
                break;
            } else if (boardChoice.equalsIgnoreCase("custom")) {
                while (true) {
                    System.out.println("1. Load the gameboard you created before\n2. Create new gameboard");
                    String action = scanner.nextLine();

                    if (action.equals("1")) {
                        game.loadGameBoard();
                        break;
                    } else if (action.equals("2")) {
                        game.designNewGameboard();
                        break;
                    } else {
                        System.out.println("Invalid choice. Please enter 1 or 2.");
                    }
                }
                break;
            } else {
                System.out.println("Invalid choice. Please enter 'existing' or 'custom'.");
            }
        }

        List<Player> players = new ArrayList<>();
        String qinput = "1";
        int count = 0;
        String fileName = "monopoly_game_state.csv";
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        while (reader.readLine() != null) count++;
        reader.close();
        count-=1;

        while (qinput.equals("1")) {
            System.out.println("1. Enter your name\n2. Generate a random name\n3. Start the game");
            String input = scanner.nextLine();
            String name = null;


            if (input.equals("1")) {
                System.out.println("Please enter your name: ");
                name = scanner.nextLine();
                if (!name.trim().isEmpty()) {
                    game.addPlayer(name);
                    count++;
                } else {
                    System.out.println("Invalid name. Please enter your name again.");
                    continue;
                }
            } else if (input.equals("2")) {
                String randomName = RandomNameGenerator.generateRandomName();
                game.addPlayer(randomName);
                System.out.println("Your name is: " + randomName);
                count++;
            } else if(input.equals("3")){
                break;
            } else {
                System.out.println("Error input! Please enter 1 or 2.");
                continue;
            }

            System.out.println("1. Add more players\n2. Finish entering names, start the game");
            qinput = scanner.nextLine();
        }

        if (count >= 2 && count <= 6) {
            System.out.println("Game start");
            for (int round = 0; round < 100; round++) {
                for (int i = 0; i < game.players.size(); i++) {
                    Player currentPlayer = game.players.get(game.currentPlayerIndex);
                    if (currentPlayer.money <= 0) {
                        // System.out.println("Sorry, You go bankrupt.");
                        game.currentPlayerIndex = (game.currentPlayerIndex + 1) % game.players.size();
                        game.removePlayer(currentPlayer.name);
                    }
                    if (game.players.size() <= 1) {
                        Player cPlayer = game.players.get(game.currentPlayerIndex);
                        System.out.println(cPlayer.name + " wins!");
                        System.exit(0);
                    }
                    game.playTurn();
                }
            }
            Player winner = game.players.stream().max(Comparator.comparingInt(player -> player.money)).orElse(null);
            if (winner != null) {
                System.out.println("Game over! " + winner.name + " wins! He has the most money!");
            }
        } else {
            System.out.println("Not enough players to start the game");
        }
    }
}

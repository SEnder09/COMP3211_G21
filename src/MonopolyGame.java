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
    private static final int BOARD_SIZE = 20;
    private static Property[] properties;
    private static List<Player> players;
    private static int currentPlayerIndex;
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
            System.out.printf("%-35s %-35s%n%-35s %-35s%n%-35s %-35s%n%-35s", "1. Roll dice" , "2. Save game", "3. View players' status" , "4. View all players' status", "5. View game status" , "6. Query next player?", "7. Quit game\n");
            //System.out.println("1. Roll dice 2. Save game \n3. View player status \n4. View all players' status \n5. View game status \n6. Query next player?\n7. Quit game\n (1/2/3/4/5/6/7)");
            String input = scanner.nextLine();

            // 1. Roll dice
            if (input.equalsIgnoreCase("1")) {
                DiceResult result = rollDice();
                int diceRoll = result.getSum();
                currentPlayer.bonus = 0;
                // test
                if (!currentPlayer.inJail && currentPlayer.state == 0) {
                    System.out.println(currentPlayer.name + " rolled a " + diceRoll);
                    currentPlayer.position = (currentPlayer.position + diceRoll) % BOARD_SIZE + 1;
                    System.out.println(currentPlayer.name + " moved to square " + (currentPlayer.position == 0 ? BOARD_SIZE : currentPlayer.position));
                }

                if (!currentPlayer.inJail && currentPlayer.state == 1) {
                    System.out.println(currentPlayer.name + " rolled a " + diceRoll);
                    if ((currentPlayer.position + diceRoll) >= BOARD_SIZE) {
                        currentPlayer.bonus = 1;
                    }
                    currentPlayer.position = (currentPlayer.position + diceRoll) % BOARD_SIZE;
                    System.out.println(currentPlayer.name + " moved to square " + (currentPlayer.position == 0 ? BOARD_SIZE : currentPlayer.position));
                }

                currentPlayer.state = 1;
                Property property = properties[currentPlayer.position];

                if (currentPlayer.position == 1 && currentPlayer.state == 1 && currentPlayer.bonus == 1) {
                    handleGo(currentPlayer);
                }
                if (currentPlayer.position == 4) {
                    handleIncomeTax(currentPlayer);
                } else if (currentPlayer.position == 6) {
                    if(!currentPlayer.inJail){
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

            if(input.equalsIgnoreCase("7")) {
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


    private void queryNextPlayer() {
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

    private void viewGameStatus() {
        System.out.println("Game Status:");
        System.out.println("Board:");
        for (int i = 0; i < properties.length; i++) {
            Property property = properties[i];
            System.out.print("Square " + i + ": " + property.name);
            if (property.owned) {
                System.out.print(" (Owned by " + property.owner.getName() + ")");
            }
            System.out.println();
        }
        System.out.println("\nPlayers' Positions:");
        for (Player player : players) {
            System.out.println(player.getName() + " is on square " + player.getPosition());
        }
    }

    private void viewAllPlayersStatus() {
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

    private DiceResult rollDice() {
        Random rand = new Random();
        int dice1 = rand.nextInt(4) + 1;
        int dice2 = rand.nextInt(4) + 1;
        boolean sameDice = (dice1 == dice2);
        return new DiceResult(dice1, dice2, sameDice); // Return a new DiceResult object
    }

    private void handleIncomeTax(Player player) {
        Property property = properties[player.position];
        player.money -= 10 * (player.money / 100);
        System.out.println("You need to pay 10% income tax!");
        System.out.println("Your money becomes " + player.money);
    }

    private void handleProperty(Player player) {
        int Position = (player.position - 1 + BOARD_SIZE) % BOARD_SIZE; //solve out of bound problem 
        Property property = properties[Position];
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
        } else {
            System.out.println("Property " + property.name + " is owned. Pay rent: " + property.rent);
            player.money -= property.rent;
            System.out.println("Remaining money: " + player.money);
        }
    }


    private void handleGo(Player player) {
        if (player.state == 1) {
            player.money += 1500;
            // System.out.println("Now you are in the " + player.position);
            System.out.println("You have earned 1500 HKD. Now your asset becomes " + player.money);
        }
    }

    private void handleChance(Player player) {
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

    private void handleFreeParking() {
        // System.out.println("Now you are in the " + player.position);
        System.out.println("You landed on Free Parking.");
    }

    private void handleJustVisiting(Player player) { // Also the state of in jail
        int diceCount = 0;
        while (player.inJail && diceCount < 3) { // need to get the value of two dice
            DiceResult result = rollDice(); // Call the rollDice method
            int dice1 = result.getDice1();
            int dice2 = result.getDice2();
            int sum = dice1 + dice2; // Get the sum of the dice
            boolean sameDice = result.isSameDice(); // Get the boolean result

            if (sameDice) { // player rolled same dice, get out of Jail
                System.out.println(player.name + " successfully roll a doubles.\n" + player.name + " released from jail now.");
                int position = (player.position + sum) % BOARD_SIZE;
                player.setPosition(position); // Move player to the position of adding the dice they roll
                break;
            }
            diceCount++;
            if (diceCount == 1) {
                System.out.println("First dice " + player.name + " rolled is " + dice1);
                System.out.println("Second dice " + player.name + " rolled is " + dice2);
                System.out.println("Do you want to pay fine? (yes/no)");
                System.out.println("Remaining money of " + player.name + " is " + player.money);
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("yes") && (player.money > 150)) {
                    player.money -= 150;
                    player.inJail = false;
                    player.setPosition(player.position + dice1 + dice2);
                }
            } else if(diceCount == 2 ){
                System.out.println("First dice " + player.name + " rolled is " + dice1);
                System.out.println("Second dice " + player.name + " rolled is " + dice2);
            } else if (diceCount < 3) { // not pay fine, go to next round to roll dice
                if (dice1 == dice2) {
                    player.inJail = false;
                    player.setPosition(player.position + dice1 + dice2);
                }
            } else if (diceCount == 3) {
                if (player.money > 150) {
                    System.out.println("After rolling the dice three times without getting a double, you are now required to pay a $150 fine.");
                    player.money -= 150;
                    System.out.println("You are required to pay a $150 fine to be released from jail now.");
                    player.inJail = false;
                    player.setPosition(player.position + dice1 + dice2);
                } else {
                    player.money -= 150;
                    System.out.println("You don't have enough money to pay fine, you are broke!");
                    removePlayer(player.name);
                }
            }

/*            System.out.println("First dice " + player.name + " rolled is " + dice1);
            System.out.println("Second dice " + player.name + " rolled is " + dice2);*/
            diceCount++;
/*            if(dice1 == dice2){
                player.inJail = false;
                player.setPosition(player.position+dice1+dice2);
            } else if(diceCount == 3){
                // know if player have roll thrid time dice?
                player.money -= 150;
                //compulsory pay hkd150
                player.inJail = false;
                player.setPosition(player.position+dice1+dice2);
            }*/
        }
    }

    // find if player position currently on go to jail(sq 15)
    private void handleGoToJail(Player player) {
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

    public static void main(String[] args) {
        MonopolyGame game = new MonopolyGame();
        Scanner scanner = new Scanner(System.in);


        System.out.println("1. Start a new game\n2. Continue progress from last play\n");
        String choice = scanner.nextLine();

        if (choice.equals("2")) {
            game.loadGame(); // Load the game state
            System.out.println("Continuing from the last saved game...");
        } else {
            System.out.println("Do you want to play on the existing gameboard or design a new gameboard? (existing/new)");
            String boardChoice = scanner.nextLine();

            if (boardChoice.equalsIgnoreCase("new")) {
                // Initialize a new game board here if needed
            }
        }

        // Continue with player setup
        String qinput = "1";
        int count = 0;

        while (qinput.equals("1")) {
            System.out.println("1. Enter your name\n2. Generate a random name\n3. Start the game");
            String input = scanner.nextLine();
            String name = null;

            if (input.equals("1")) {
                System.out.println("Please enter your name: ");
                name = scanner.nextLine();
                game.addPlayer(name);
                count++;
            } else if (input.equals("2")) {
                String randomName = RandomNameGenerator.generateRandomName();
                game.addPlayer(randomName);
                System.out.println("Your name is: " + randomName);
                count++;
            } else if (input.equals("3")) {
                // Use loaded player names
                for (Player player : game.players) {
                    System.out.println("Loaded player: " + player.getName());
                }
                count = game.players.size();
                break;
            } else {
                System.out.println("Error input!");
            }

            System.out.println("1. Add more players\n2. Finish entering names, start the game");
            qinput = scanner.nextLine();
        }

        if (count >= 2 && count <= 6) {
            System.out.println("Game start");
            for (int round = 0; round < 99; round++) {
                for (int i = 0; i < game.players.size(); i++) {
                    Player currentPlayer = game.players.get(i);
                    if (currentPlayer.money <= 0) {
                        removePlayer(currentPlayer.name);
                    }
                    if (game.players.size() <= 1) {
                        Player cPlayer = game.players.get(i);
                        System.out.println(cPlayer.name + " wins!");
                        System.exit(0);
                    }
                    game.playTurn(); // This is where the game logic starts executing
                }
            }
        } else {
            System.out.println("Not enough players to start the game");
        }
    }
}

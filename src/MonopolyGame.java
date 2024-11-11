import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// need to add a check condition to check if there are >=2 && <=6 players

// Lack of the implementation of Go, Chance, Income tax, Free Parking, Go to Jail

// Implement those function by


class Property {
    String name;
    int price;
    int rent;
    boolean owned;

    public Property(String name, int price, int rent) {
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.owned = false;
    }
}

class Player {
    String name;
    int money;
    int position;
    boolean inJail;
    int state;

    public Player(String name) {
        this.name = name;
        this.money = 1500; // Starting money
        this.position = 0; // Starts on square 0
        this.inJail = false;
        this.state = 0; // 0: first turn, in 'GO' doesn't earn  1000
                        // 1: other turn, in 'GO' get 1000
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
                new Property("Income tax", 600, 60), // 3
                new Property("Stanley", 0, 0), // 4
                new Property("Just Visiting/In Jail", 400, 10), // 5
                new Property("Shek O", 0, 0), //6
                new Property("Mong Kok", 500, 40), // 7
                new Property("Chance", 400, 15), // 8
                new Property("Tsing Yi", 0, 0), // 9
                new Property("Free Parking", 700, 75), // 10
                new Property("Shatin", 0, 0), // 11
                new Property("Chance", 400, 20), // 12
                new Property("Tuen Mun", 0, 0), // 13
                new Property("Tai Po", 500, 25), //14
                new Property("Go to jail",0,0), // 15
                new Property("Sai Kung", 400, 10), //16
                new Property("Yuen Long", 400, 25), //17
                new Property("Chance",0,0), //18
                new Property("Tai O", 600, 25) //19
        };
        players = new ArrayList<>();
        currentPlayerIndex = 0;
    }

    public void addPlayer(String name) {
        players.add(new Player(name));
    }
    public static void removePlayer(String name){players.remove(name);};

    public void playTurn() {
        Player currentPlayer = players.get(currentPlayerIndex);
        System.out.println(currentPlayer.name + "'s turn.");
        System.out.println(currentPlayer.name + " have $" + currentPlayer.money);

        // Roll the dice
        //int diceRoll = rollDice();
        DiceResult result = rollDice();
        int diceRoll = result.getSum(); // Get the sum of the dice
        // boolean sameDice = result.isSameDice(); // Get the boolean result
        // System.out.println(currentPlayer.name + " rolled a " + diceRoll);
        currentPlayer.state = 1;


        // Move the player
        if(!currentPlayer.inJail) { // if player not in jail, move
            System.out.println(currentPlayer.name + " rolled a " + diceRoll);
            currentPlayer.position = (currentPlayer.position + diceRoll) % BOARD_SIZE;
            System.out.println(currentPlayer.name + " moved to square " + currentPlayer.position);
        }
        // start implement method
        //currentPlayer.position = 1/4/6/9/11/13/16/19
        //1:go
        //4:income tax
        //6:just visiting
        //9: and 13 and 19:chance
        //11: free parking
        //16:go to jail
        // currentPlayer.setPosition(0);
        Property property = properties[currentPlayer.position];
        if(currentPlayer.position == 0){ // Go
            handleGo(currentPlayer);
        }
        if(currentPlayer.position == 4) // income tax
        {
            handleIncomeTax(currentPlayer);
        }
        if(currentPlayer.position == 6) // just visiting/In jail
        {
            handleJustVisiting(currentPlayer);
        }
        if(currentPlayer.position == 9 || currentPlayer.position == 13 || currentPlayer.position == 19) // Chance
        {
            handleChance(currentPlayer);
        }
        if(currentPlayer.position == 11) // Free parking
        {
            handleFreeParking(currentPlayer);

        }
        if(currentPlayer.position == 15) // Go to jail
        {
            handleGoToJail(currentPlayer);
        }
        // Check for property
        //else if(currentPlayer.position == 1 || currentPlayer.position == 2 || currentPlayer.position == 3 || currentPlayer.position == 5 || currentPlayer.position == 7 || currentPlayer.position == 8 || currentPlayer.position == 10 || currentPlayer.position == 12 || currentPlayer.position == 14 || currentPlayer.position == 16 || currentPlayer.position == 17 || currentPlayer.position == 19) {
        else
        {
            System.out.println("Get into handleProperty condition");
            handleProperty(currentPlayer);
        }

        // Move to next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
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
        this.sum = dice1+dice2;
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
        int dice1 = rand.nextInt(6) + 1;
        int dice2 = rand.nextInt(6) + 1;
        boolean sameDice = (dice1 == dice2);
        return new DiceResult(dice1, dice2, sameDice); // Return a new DiceResult object
    }
    private void handleIncomeTax(Player player){
        Property property = properties[player.position];
        player.money -= 10 * (player.money / 10);
        // System.out.println("Now you are in the" + player.position);
        System.out.println("Your money becomes " + player.money);
    }
    private void handleProperty(Player player) {
        if (player.position < properties.length) {
            Property property = properties[player.position];
            if (!property.owned) {
                System.out.println("You landed on " + property.name + ". Price: " + property.price);
                if (player.money >= property.price) {
                    System.out.println("Do you want to buy it? (yes/no)");
                    Scanner scanner = new Scanner(System.in);
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("yes")) {
                        player.money -= property.price;
                        property.owned = true;
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
    }
    private void handleGo(Player player){
        if(player.state == 1){
            player.money += 1500;
            // System.out.println("Now you are in the " + player.position);
            System.out.println("You have earned 1500 HKD. Now your assets become " + player.money);
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

    private void handleFreeParking(Player player) {
        // System.out.println("Now you are in the " + player.position);
        System.out.println("You landed on Free Parking.");
    }

    private void handleJustVisiting(Player player){ // Also the state of in jail
        int diceCount = 0;
        while(player.inJail && !(diceCount >= 3)){ // need to get the value of two dice
            DiceResult result = rollDice(); // Call the rollDice method
            int dice1 = result.getDice1();
            int dice2 = result.getDice2();
            int sum = dice1+dice2; // Get the sum of the dice
            boolean sameDice = result.isSameDice(); // Get the boolean result
            System.out.println("First dice " + player.name+ " rolled is " + dice1);
            System.out.println("Second dice " + player.name+ " rolled is " + dice2);
            if(sameDice){ // player rolled same dice, get out of Jail
                System.out.println(player.name + " successfully roll a doubles.\n" + player.name + " gets out of jail now.");
                int position = (player.position + sum) % BOARD_SIZE;
                player.setPosition(position); // Move player to the position of adding the dice they roll
                break;
            }
            diceCount++;
            if(diceCount == 1){
                System.out.println("Do you want to pay fine? (yes/no)");
                System.out.println("Remaining money of " + player.name + " is " + player.money);
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                if(input.equalsIgnoreCase("yes") && (player.money > 150)){
                    player.money -= 150;
                    player.inJail = false;
                    player.setPosition(player.position + dice1 + dice2);
                }
            }
            else if(diceCount < 3) { // not pay fine, go to next round to roll dice
                if (dice1 == dice2) {
                    player.inJail = false;
                    player.setPosition(player.position + dice1 + dice2);
                }
            }
            else if(diceCount == 3){
                    if(player.money > 150) {
                        player.money -= 150;
                    }
                    else {
                        System.out.println("You don't have enough money to pay fine, you are broke!");
                        removePlayer(player.name);
                }
            }

            System.out.println("First dice " + player.name+ " rolled is " + dice1);
            System.out.println("Second dice " + player.name+ " rolled is " + dice2);
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
    private void handleGoToJail(Player player){
        System.out.println(player.name + " currently landed on [Go to jail]");
        System.out.println(player.name + " is sent to jail");

        player.setPosition(6); // move player to in jail square
        player.inJail = true; // the handle just visiting
        // System.out.println("Player currently landed on: ");
    }

    public static void main(String[] args) {
        MonopolyGame game = new MonopolyGame();
        String qinput = "1";
        int count = 0;
        while (qinput.equals("1")) {
            System.out.println("1.Enter your name\n2.Generate a random name ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String name = null;

            if (input.equals("1")) {
                System.out.println("Please enter your name: ");
                Scanner nameScanner = new Scanner(System.in);
                name = nameScanner.nextLine();
                game.addPlayer(name);
            }
            if (input.equals("2")) {
                String randomName = RandomNameGenerator.generateRandomName();
                game.addPlayer(randomName);
                System.out.println("Your name is: "+randomName);
            }
            count++;
            // need to add a check condition to check if there are >=2 && <=6 players
            // Lack of the implementation of Go, Chance, Income tax, Free Parking, Go to Jail
            System.out.println("1.Add more player's name\n2.Finish entering name, start the game");
            Scanner qscanner = new Scanner(System.in);
            qinput = qscanner.nextLine();
        }

        if(count >= 2 && count <= 6) {
            //check the member number
            System.out.println("Game start");
            // Game loop
            for (int round = 0; round < 5; round++) { // 5 rounds for demonstration
                for (int i = 0; i < game.players.size(); i++) {
                    Player currentPlayer = players.get(currentPlayerIndex);
                    if(currentPlayer.money < 0 ){
                        System.out.println("Sorry, You go bankrupt.");
                        // Move to next player
                        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                        removePlayer(currentPlayer.name);
                    }
                    game.playTurn();
                }
            }
        }
        else{
            System.out.println("No enough members to start the game");
        }
    }
}
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.io.*;
import java.util.List;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.*;


class MonopolyGameTest {
    private MonopolyGame game; // Assuming the class containing addPlayer is called Game
    private List<Player> players;
    private Player player;


    @BeforeEach
    void setUp() throws IOException {
        game = new MonopolyGame();
        players = game.getPlayers();
        game.addPlayer("Player1");
        game.addPlayer("Player2");

        // Create a temporary file for testing
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }
    @Test
    void testAddPlayer() {
        String name = "TestPlayer";
        game.addPlayer(name);
        boolean test = players.stream().anyMatch(player -> player.getName().equals(name));
        assertTrue(test, "Failed to add the player!");
    }

    @Test
    void testRemovePlayer() {
        String name = "TestPlayer";
        game.removePlayer(name);
        boolean test = players.stream().anyMatch(player -> player.getName().equals(name));
        assertTrue(!test, "Failed to add the player!");
    }


    @Test
    void testInitialMoney() {
        game.addPlayer("Player1");
        assertEquals(1500, game.getPlayers().get(0).getMoney());
    }

    @Test
    void testRollDice() {
        for (int i = 0; i < 100; i++) {
            MonopolyGame.DiceResult result = game.rollDice();
            assertTrue(result.getSum() >= 2);
            assertTrue(result.getSum() <= 8);
        }
    }


    @Test
    void testAddMoney() {
        Player player = new Player("Player1");
        player.addMoney(500);
        assertEquals(2000, player.getMoney());
        player.addMoney(-200);
        assertEquals(1800, player.getMoney());
    }


    @Test
    public void testQueryNextPlayer() {
        MonopolyGame game = new MonopolyGame();
        game.addPlayer("player1");
        game.addPlayer("player2");
        game.addPlayer("player3");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        game.queryNextPlayer();

        String expectedOutput = "Next Player: player2\n" +
                "Money: $1500\n" +
                "Position: 0\n" +
                "In Jail: No\n" +
                "Owned Properties:\n";
        assertEquals(expectedOutput, outContent.toString());

        // Reset the standard output
        System.setOut(System.out);
    }


    @Test
    public void testViewGameStatus() {
        MonopolyGame game = new MonopolyGame();
        game.addPlayer("Player1");
        game.addPlayer("Player2");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        game.viewGameStatus();

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("Game Status:\n");
        expectedOutput.append("Board:\n");
        for (int i = 0; i < MonopolyGame.properties.length; i++) {
            Property property = MonopolyGame.properties[i];
            expectedOutput.append("Square ").append(i + 1).append(": ").append(property.name);
            if (property.price > 0) {
                expectedOutput.append(" (Price: ").append(property.price).append(", Rent: ").append(property.rent).append(")");
            }
            expectedOutput.append("\n");
        }
        expectedOutput.append("\nPlayers' Positions:\n");
        for (Player player : game.getPlayers()) {
            expectedOutput.append(player.getName()).append(" is on square ").append(player.getPosition()).append("\n");
        }

        assertEquals(expectedOutput.toString(), outContent.toString());

        System.setOut(System.out);
    }

    @Test
    void viewAllPlayersStatus() {
        MonopolyGame game = new MonopolyGame();
        game.addPlayer("Player1");
        game.addPlayer("Player2");

        // Capture the output of viewAllPlayersStatus
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        game.viewAllPlayersStatus();

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("Player Name: Player1\n");
        expectedOutput.append("Money: $1500\n");
        expectedOutput.append("Position: 0\n");
        expectedOutput.append("In Jail: No\n");
        expectedOutput.append("Properties owned:\n\n");

        expectedOutput.append("Player Name: Player2\n");
        expectedOutput.append("Money: $1500\n");
        expectedOutput.append("Position: 0\n");
        expectedOutput.append("In Jail: No\n");
        expectedOutput.append("Properties owned:\n\n");

        assertEquals(expectedOutput.toString(), outContent.toString());

        System.setOut(System.out);
    }

    @Test
    public void testViewPlayerStatus() {
        MonopolyGame game = new MonopolyGame();
        game.addPlayer("Player1");
        game.addPlayer("Player2");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        game.viewAllPlayersStatus();

        String expectedOutput = "Player Name: Player1\n" +
                "Money: $1500\n" +
                "Position: 0\n" +
                "In Jail: No\n" +
                "Properties owned:\n\n" +
                "Player Name: Player2\n" +
                "Money: $1500\n" +
                "Position: 0\n" +
                "In Jail: No\n" +
                "Properties owned:\n\n";

        assertEquals(expectedOutput, outContent.toString());

        // Reset the standard output
        System.setOut(System.out);
    }

    @Test
    void testHandleIncomeTax() {
        Player player = new Player("Player1");
        player.addMoney(5000);

        game.addPlayer(player.getName());
        player.setPosition(4);
        game.handleIncomeTax(player);

        assertEquals(5850, player.getMoney());
    }

    @Test
    void testHandleProperty() {
        Player player = new Player("Player1");
        Property property = new Property("Central", 800, 90);

        assertFalse(property.owned);
        assertNull(property.owner);

        player.addProperty(property);
        property.owned = true;
        property.owner = player;

        assertTrue(property.owned);
        assertEquals(player, property.owner);
        assertEquals(1, player.getOwnedProperties().size());
        assertEquals("Central", player.getOwnedProperties().get(0).name);

    }



    @Test
    void handleGo(){
        Player player = new Player("Player");
        game.addPlayer(player.getName());

        player.position = 0;
        player.state = 1;
        game.handleGo(player);

        assertEquals(3000, player.getMoney());
    }


    @Test
    void handleChance() {
        Player player = new Player("Player");
        game.addPlayer(player.getName());

        game.handleChance(player);
        int money = player.getMoney();
        assertTrue(money >= 1200 && money <= 1700, "Chance resulted in unexpected money amount: " + money);
    }

    @Test
    void handleFreeParking() {
        Player player = new Player("Player");
        game.addPlayer(player.getName());

        game.handleFreeParking();
        assertEquals(1500, player.getMoney());
        assertEquals(0, player.getPosition());
    }

    @Test
    void handleJustVisiting() {

    }

    @Test void handleGoToJail(){
        Player player = new Player("Player1");
        player.setPosition(16); // Go to jail position
        game.handleGoToJail(player);
        assertTrue(player.inJail);
        assertEquals(6, player.getPosition());
    }

    @Test
    public void test_pays_to_exit_jail() {
        Player player = new Player("TestPlayer");
        player.setPosition(15);
        MonopolyGame game = new MonopolyGame();
        game.handleGoToJail(player);

        int bailAmount = 150;
        player.addMoney(-bailAmount);
        player.inJail = false;

        assertEquals(1350, player.getMoney());
        assertFalse(player.inJail);
    }


    @Test
    void testSaveGame() {
        game.saveGame();

        File file = new File("monopoly_game_state.csv");
        assertTrue(file.exists(), "Game state file should be created after saving.");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            assertEquals("Player Name,Money,Position,In Jail,Owned Properties", header);

            String line = reader.readLine();
            System.out.println("line: "+line);
            assertNotNull(line, "There should be player data after saving.");

            // Check Player1's data
            String[] data1 = line.split(",");
            assertEquals("Player1", data1[0]); // Check Player1's name
            assertEquals("1500", data1[1]); // Check initial money
            assertEquals("0", data1[2]); // Check initial position
            assertEquals("false", data1[3]); // Check if in jail

            // Check Player2's data
            line = reader.readLine();
            assertNotNull(line, "There should be player data after saving.");
            String[] data2 = line.split(",");
            assertEquals("Player2", data2[0]); // Check Player2's name
            assertEquals("1500", data2[1]); // Check initial money
            assertEquals("0", data2[2]); // Check initial position
            assertEquals("false", data2[3]); // Check if in jail
        } catch (IOException e) {
            fail("IOException occurred while reading the save file: " + e.getMessage());
        }
    }


    @Test
    void testLoadGame() {
        // First, save the game state to create a file
        game.saveGame();

        // Create a new game instance and load the game state
        MonopolyGame newGame = new MonopolyGame();
        newGame.loadGame();

        // Validate the loaded state
        List<Player> loadedPlayers = newGame.getPlayers();
        System.out.println("loadPlayers: "+loadedPlayers);
        assertEquals(2, loadedPlayers.size(), "Should load the same number of players.");

        Player player1 = loadedPlayers.get(0);
        assertEquals("Player1", player1.getName());
        assertEquals(1500, player1.getMoney()); // Check initial money
        assertEquals(0, player1.getPosition()); // Check initial position
        assertFalse(player1.inJail); // Check if in jail

        Player player2 = loadedPlayers.get(1);
        assertEquals("Player2", player2.getName());
        assertEquals(1500, player2.getMoney()); // Check initial money
        assertEquals(0, player2.getPosition()); // Check initial position
        assertFalse(player2.inJail); // Check if in jail
    }



    @Test
    void testModifyGameBoard() {
        game.modifyGameBoard(1, "New Central", 900, 100);
        Property modifiedProperty = game.properties[1];
        assertEquals("New Central", modifiedProperty.name);
        assertEquals(900, modifiedProperty.price);
        assertEquals(100, modifiedProperty.rent);
    }


    // Players can design a custom game board and start a game with it
    @Test
    public void testDesignNewGameboard() {
        MonopolyGame game = new MonopolyGame();
        game.modifyGameBoard(1, "New Central", 1000, 100);
        game.modifyGameBoard(2, "New Wan Chai", 900, 80);

        assertEquals("New Central", MonopolyGame.properties[1].name);
        assertEquals(1000, MonopolyGame.properties[1].price);
        assertEquals(100, MonopolyGame.properties[1].rent);

        assertEquals("New Wan Chai", MonopolyGame.properties[2].name);
        assertEquals(900, MonopolyGame.properties[2].price);
        assertEquals(80, MonopolyGame.properties[2].rent);

    }

    @Test
    void saveGameBoard(){

    }

    @Test
    void loadGameBoard(){

    }

    @Test
    public void testStartNewGame() throws IOException {
        MonopolyGame game = new MonopolyGame();
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("1\nexisting\n3\n".getBytes());
        System.setIn(in);
        game.main(new String[]{});
        assertEquals(0, game.getPlayers().size());
        System.setIn(sysInBackup);
    }


}

class RandomNameGeneratorTest{
    private static final int MAX_LENGTH = 10;
    @Test
    void testRandomNameGenerator() {
        for (int i = 0; i < 100; i++) {
            String name = RandomNameGenerator.generateRandomName();
            assertTrue(name.length() >= 1 && name.length() <= MAX_LENGTH);
        }
    }
}

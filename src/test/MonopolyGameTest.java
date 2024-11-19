import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void playTurn() {

    }

//    @org.junit.jupiter.api.Test
//    void saveGame() {
//    }

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
    void modifyGameBoard() {
    }

    @Test
    void main() {
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
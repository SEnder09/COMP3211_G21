import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MonopolyGameTest {
    private MonopolyGame game; // Assuming the class containing addPlayer is called Game
    private List<Player> players;
    private Player player;


    @BeforeEach
    void setUp() throws IOException {
        game = new MonopolyGame();
        players = game.getPlayers();


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
    void saveGame(){
    }


    @Test
    void loadGame() {
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
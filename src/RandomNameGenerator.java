import java.util.Random;

public class RandomNameGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int MAX_LENGTH = 10;

    public static void main(String[] args) {
        String randomName = generateRandomName();
        System.out.println("Generated Random Name: " + randomName);
    }

    public static String generateRandomName() {
        Random random = new Random();
        int nameLength = random.nextInt(MAX_LENGTH) + 1; // Generate length between 1 and MAX_LENGTH
        StringBuilder name = new StringBuilder(nameLength);

        for (int i = 0; i < nameLength; i++) {
            int index = random.nextInt(CHARACTERS.length());
            name.append(CHARACTERS.charAt(index));
        }

        return name.toString();
    }
}
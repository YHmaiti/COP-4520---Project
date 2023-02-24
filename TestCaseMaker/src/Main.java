import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.SplittableRandom;

public class Main {
    public static void main(String[] args) throws IOException {
        final int QUANTITY = (int)Math.pow(10, 6);
        final int MIN = -1 * QUANTITY;
        final int MAX = QUANTITY;

        File file = new File("input.txt");
        FileWriter fileWriter = new FileWriter(file);
        SplittableRandom rand = new SplittableRandom();
        for (int i = 0; i < QUANTITY; i++) {
            fileWriter.write(Integer.toString(rand.nextInt(MAX-MIN)+MIN) + "\n");
        }
        fileWriter.close();
    }
}
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static final String inputFileName = "input.txt";
    static final String outputFileName = "output.txt";

    public static void main(String[] args) {
        File inputFile = new File(inputFileName);
        Scanner input = null;
        try {
            input = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found");
            System.exit(1);
        }
        ArrayList<Integer> inputList = new ArrayList<>();
        while (input.hasNextInt()) {
            inputList.add(input.nextInt());
        }
        input.close();

        int[] arr = inputList.stream().mapToInt(i -> i).toArray();

        long startTime = System.currentTimeMillis();
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        Quicksort quicksort = new Quicksort(arr.length / Quicksort.NUMWORKERS, arr, 0, arr.length - 1);
        quicksort.run();
        Quicksort.executorService.shutdown();

        long endTime = System.currentTimeMillis();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long runtime = endTime - startTime;
        long memUsed = afterUsedMem - beforeUsedMem;

        try {
            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFileName));

            for (int i : arr) {
                outputWriter.write(Integer.toString(i));
                outputWriter.newLine();
            }
            outputWriter.flush();
            outputWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Runtime: " + runtime + " ms");
        System.out.println("Memory used: " + memUsed + " bytes");
    }
}
/*
 * 
 * This program represents a parallel implementation of merge sort algorithm 
 * using multithreading through the ExecutorService interface and the 
 * Future interface. We test the performance of this design with different array sizes and 
 * we report the time taken to sort the array and the memory usage.
 */

// preprocessor directives
import java.util.stream.IntStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Arrays;


// class definition for the parallel merge sort implementation
public class MergeSortWithParallelizationV2 {
    
    /*
     * @param startTime: the time at which the program starts
     * @param endTime: the time at which the program ends
     */
    private static long startTime, endTime;

    /*
     * @brief: takes the input from a file and fills the array 
     * @param fileName: the name of the file that has the input
     * @return: the array loaded with the data 
     */
    public static int[] getInputFromFile(String fileName) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String current = br.readLine();
        String[] input = current.split(" ");
        int[] arr = new int[input.length];

        for (int i = 0; i < input.length; i++) {
            arr[i] = Integer.parseInt(input[i]);
        }

        br.close();
        return arr;

    }

    /*
     * @brief: writes the sorted array to a file
     * @param array: the sorted array to be printed to the file
     * @return: void
     * @throws: IOException
     */
    private static void printOutput(int[] array) throws IOException {

        FileWriter fw = new FileWriter("output.txt");
        BufferedWriter bw = new BufferedWriter(fw);

        for (int x : array) {
            bw.write(x + " ");
        }

        bw.close();
        fw.close();

    }

    /*
     * @brief: merge two arrays that are sorted to a single sorted array
     * @param arr1 & arr2: two arrays that are sorted and require merging as part of the merge sort
     *                     process.
     * @return: a fully sorted array resulting from the merge of arr1 and arr2
     */
    public static int[] merge(int[] arr1, int[] arr2) {

        int[] result = new int[arr1.length + arr2.length];
        int i = 0, j = 0, k = 0;

        while (i < arr1.length && j < arr2.length) {
            if (arr1[i] <= arr2[j]) {
                result[k++] = arr1[i++];
            } else {
                result[k++] = arr2[j++];
            }
        }

        while (i < arr1.length) {
            result[k++] = arr1[i++];
        }

        while (j < arr2.length) {
            result[k++] = arr2[j++];
        }

        return result;

    }

    // we set a method for the regular merge sort algorithm
    // this will be used for the control case where no multithreading or parallelization is used
    public static int[] SimpleMergeSortWithoutParallelization(int[] arr) {

        if (arr.length < 2) {
            return arr;
        }

        int mid = arr.length / 2;
        int[] leftArray = Arrays.copyOfRange(arr, 0, mid);
        int[] rightArray = Arrays.copyOfRange(arr, mid, arr.length);

        int[] resultLeft = SimpleMergeSortWithoutParallelization(leftArray);
        int[] resultRight = SimpleMergeSortWithoutParallelization(rightArray);

        return merge(resultLeft, resultRight);

    }

    /*
     * @brief: use a parallel and thread based approach to sort an array following
     * the merge sort algorithm
     * 
     * @param arr: the array to be sorted
     * 
     * @param numThreads: the number of threads to be used for the parallelization
     * 
     * @return: the sorted array
     * 
     * @throws: InterruptedException, ExecutionException
     * 
     * @interfaces: ExecutorService, Future
     * 
     * @IMPORTANT: The number of threads inputted, if beyond the number of threads
     * available for use in the machine
     * will be ignored and the max number of threads available will be used instead.
     * 
     * @Documentation: https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html
     *                 https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Future.html
     * 
     */
    public static int[] MergeSortParallel(int[] arr, int numThreads) throws InterruptedException, ExecutionException {
            if (arr.length < 2) {
                return arr;
            }     
    
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
    
            Future<int[]>[] futures = new Future[numThreads];
    
            for (int i = 0; i < numThreads; i++) {
                int start = i * arr.length / numThreads;
                int end = (i + 1) * arr.length / numThreads;
                int[] subArray = Arrays.copyOfRange(arr, start, end);
                futures[i] = executor.submit(() -> SimpleMergeSortWithoutParallelization(subArray));
            }
    
            int[][] results = new int[numThreads][];

            results = IntStream.range(0, numThreads).mapToObj(i -> {
                try {
                    return futures[i].get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            }).toArray(int[][]::new);
    
            while (numThreads > 1) {
                int getMidThreadCount = (numThreads + 1) / 2;
                final int[][] finalResults = results;
                results = IntStream.range(0, numThreads / 2).mapToObj(i -> merge(finalResults[i], finalResults[i + getMidThreadCount])).toArray(int[][]::new);
                numThreads = getMidThreadCount;
            }
    
            executor.shutdown();
    
            return results[0];
    
    }

    public static void main(String[] args) throws Exception {
        

        // for now we generate an array with a random size and content for testing purposes
        // a more thorough testing will be done later with a proper evaluation
        // int [] array = new int[(int)Math.pow(10, 6)];

        // we take from the command line the name for each of the test files in total 6 
        // and we store the names in string arrays
        String[] testFiles = new String[6];
        try{
            testFiles = Arrays.stream(new File("../TestCases").listFiles()).filter(f -> f.getName().endsWith(".txt")).map(f -> f.getName()).toArray(String[]::new);
        }catch(Exception e){
            System.out.println("Error: " + e);
        }
        // we declare a 2D array to store the results for the time and memory 
        // for each of the test cases
        double[][] results = new double[6][2];


        // test that we got all the files correctly
        System.out.println(Arrays.toString(testFiles));
        int totaltests = 6, testCase = 0;

        while (testCase < totaltests) {
            // loop through the current test file for the test case and read the array
            // from the file
            int[] array = new int[0];
            try (BufferedReader br = new BufferedReader(new FileReader("../TestCases/" + testFiles[testCase]))) {
                System.out.println("The current test file is -> " + testFiles[testCase]);
                // loop through the file and read the array
                File file = new File("../TestCases/" + testFiles[testCase]);
                System.out.println("The size of the file is -> " + file.length());
                array = new int[(int)file.length()];
                int i = 0;
                String line;
                while ((line = br.readLine()) != null) {
                    array[i++] = Integer.parseInt(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("The size of the array is -> " + array.length);
        
            // generate an array with random values 
            // array = Arrays.stream(array).map(i -> (int) (Math.random() * Math.pow(10, 6))).toArray();

            // inform the user about the number maximum of threads that can be used on the machine they have now 
            System.out.println("The maximum number of threads that can be used on this machine is -> " + Runtime.getRuntime().availableProcessors());
            // we prompt the user for the number of threads that will be used and tested 
            System.out.print("The number of threads we use is -> " + 12);
            int  numThreads = 4;

            // we check that the number of threads inputted by the user is not less than 1
            // and also that it is not more than the number of threads available in the machine
            if (numThreads < 1 || numThreads > Runtime.getRuntime().availableProcessors()) {
                System.out.println("Invalid number of threads. We will automatically use 4.");
                numThreads = Runtime.getRuntime().availableProcessors() - 2;
            }

            // log the time 
            startTime = System.currentTimeMillis();

            // log the consumtion of memory 
            Long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            /*
            * @param array: the array to be sorted
            * @param numThreads: the number of threads to be used for the parallelization
            * @return: the sorted array
            * @throws: InterruptedException, ExecutionException
            * @interfaces: ExecutorService, Future
            * @classes: Executors
            * @methods: submit(), get(), shutdown()
            */
            array = MergeSortParallel(array, numThreads);
            //array = SimpleMergeSortWithoutParallelization(array);
            // log the end time 
            endTime = System.currentTimeMillis();

            // log the end memory consumption
            Long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            // print the time taken
            System.out.println("\nTime taken: " + (endTime - startTime) + " ms");

            // print the memory consumption
            System.out.println("Memory consumption: " + (endMemory - startMemory) + " bytes");

            // store the results in the 2D array
            results[testCase][0] = (endTime - startTime);
            results[testCase][1] = (endMemory - startMemory);

            /*
            * @param array: the array to be checked for sorting
            * @return: true if the array is sorted, false otherwise
            * @interpretation: if the result is true, the array is sorted, so then we print the output to 
            *                  a file and print a confirmation message to the console. Otherwise, we print
            *                  a message to the console and exit the program.
            */

            // another option: boolean result  = (Arrays.equals(array, Arrays.stream(array).sorted().toArray()));

            final int[] finalArray = Arrays.copyOf(array, array.length);
            boolean result = array.length == 0 || IntStream.range(0, array.length - 1).allMatch(i -> finalArray[i] <= finalArray[i + 1]);

            if (result) {
                success(array);
            } else {
                failed();
            }
        
            testCase++;

        }


        // generalize finalized outut in the text format expected 
        generateOutputWithSpecificFormat(results, testFiles);

    }

    // output file
    private static void generateOutputWithSpecificFormat(double[][] results, String[] testFiles) throws IOException {

        // format:
        /*
         * FolderName: {SortName_Results}
         * filename: {SortName}_{testCaseSize}.txt
         * Content:
         * 
         * test case size1:
         * Execution time (ms):
         * Memory usage (bytes):
         * 
         * test case size2:
         * Execution time (ms):
         * Memory usage (bytes):
         */

        // create the folder if it does not exist
        File folder = new File("./MergeSortParallel_Results");
        if (!folder.exists()) {
            folder.mkdir();
        }

        // create the file if it does not exist
        File file = new File("./MergeSortParallel_Results/MergeSortParallel_Results.txt");

        if (!file.exists()) {
            file.createNewFile();
        }

        // write to the file
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        // declare an array of sizes of the test cases
        String[] sizes = {"10^8", "5 * 10^8", "10^7", "5 * 10^7", "10^6", "5 * 10^6"};
        int sizeIndex = 0;
        // write the content only, if the file has content overwrite it
        bw.write("Content:\n");
        for (int i = 0; i < testFiles.length; i++) {
            bw.write("test case size: " + sizes[sizeIndex] + "\n");
            bw.write("\tExecution time (ms): " + results[i][0] + "\n");

            // we write the memory usage in scientific expression to avoid large numbers in teh form num * 10^x
            bw.write("\tMemory usage (bytes): " + String.format("%.4f", results[i][1]) + "\n");

            // we write to terminal also for debugging 
            System.out.println("test case size: " + sizes[sizeIndex++]);
            System.out.println("\tExecution time (ms): " + results[i][0]);
            System.out.println("\tMemory usage (bytes): " + String.format("%.4f", results[i][1]));

        }

        bw.close();

    }

    // method to write scientifique expression for large integers
    private static String formatNumber(long number) {
        return String.format("%,d", number);
    }

    // methods that handle resulting output after the processing of the array ends
    private static void success(int[] array) throws IOException {
        System.out.println("The array is sorted");
        //printOutput(array);
    }

    private static void failed() {
        System.out.println("The array is not sorted");
    }
}
import java.util.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.Random;

public class InsertionSort {

    // the maximum size of the array to be sorted
    public static final int MAXSIZE = 100000000;

    // iterative sequential sort
    public void sequential(int arr[]) {

        // loop through the whole array
        for (int i = 1; i < arr.length; ++i) {

            // the current item
            int target = arr[i];
            int j = i - 1;

            // this sort runs in quadratic time, o(n^2)
            // for every item, we go for elements starting
            // from the current item
            // and moving backwards to the start of the array
            while (j >= 0 && arr[j] > target) {

                // if the elemtnt is smaller than target it
                // does not need to move
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            // all items to the left of target
            // should be smaller than it
            arr[j + 1] = target;
        }

    }

    // parallel insertion sort
    public static Runnable parallel(int arr[], int min, int max) {

        // have multiple parts of the array
        // sorted on different threads

        // max is the number of the maximum index this thread
        // will sort from the array
        // min is the minimum index

        // loop through the array to the max index
        for (int i = min; i < max; ++i) {

            // the current item
            int target = arr[i];
            int j = i - 1;

            // this sort runs in quadratic time, o(n^2)
            // for every item, we go for elements starting
            // from the current item
            // and moving backwards to the start of the array
            while (j >= 0 && arr[j] > target) {

                // if the element is smaller than target it
                // does not need to move
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            // all items to the left of target
            // should be smaller than it
            arr[j + 1] = target;
        }

        // this method implements Runnable which allows it
        // to be run on multiple threads
        return null;

    }

    // driver function
    public static void main(String[] args) {

        System.out.println("Max processors: " + Runtime.getRuntime().availableProcessors());

        long pStart = 0; // the times for the parallel version
        long pEnd = 0;
        long pDuration = 0;
        long sStart = 0; // the times for the sequential version
        long sEnd = 0;
        long sDuration = 0;
        int max = 0; // used for the maximum index number of the array each thread will compute
        int min = 0; // the minimum index number of the array a thread will compute
        int threads = 8; // the number of threads to be ran
        int n = 10; // the factor to increase each thread's indexes by

        // create an array of random ints for testing
        Random rand = new Random();
        int[] arr = new int[rand.nextInt(1000)]; // the size of the array is random

        // each index of the array contains a random number
        for (int i = 0; i < arr.length; i++) {

            arr[i] = rand.nextInt(MAXSIZE);

        }

        System.out.println("----------Array before sorting----------");

        for (int i = 0; i < arr.length; i++) {

            System.out.println(arr[i]);

        }

        // duplicate the array to test with iterative version
        int[] itArray = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {

            itArray[i] = arr[i];

        }

        // prompt the user for the number of threads
        // System.out.println("Enter the number of threads to be ran (2, 4, 8, 12):");
        // Scanner sc = new Scanner(System.in);
        // threads = sc.nextInt();

        // create an array containing multiple threads
        Thread[] thread = new Thread[threads];
        // create subarrays based on the number of threads
        int numIndexes = arr.length / threads;
        // to start, min is 0 and max is the numIndexes - 1
        max = numIndexes - 1;

        // start the timer for the simulation
        pStart = System.currentTimeMillis();

        // for each thread, sort a subarray
        for (int i = 0; i < threads; i++) {

            thread[i] = new Thread(parallel(arr, min, max));
            thread[i].start();

            // change the max number that each thread will compute
            min = (max + 1);
            max = min + (numIndexes - 1);

        }

        for (int i = 0; i < threads; i++) {

            try {

                // stop each thread
                thread[i].join();

            } catch (InterruptedException e) {

                e.printStackTrace();
            }

        }

        pEnd = System.currentTimeMillis();

        pDuration = pEnd - pStart;

        System.out.println("----------Array after parallel sort----------");

        for (int i = 0; i < arr.length; i++) {

            System.out.println(arr[i]);

        }

        // close the scanner
        // sc.close();

        // run sequential version to compare
        sStart = System.currentTimeMillis();
        InsertionSort ob = new InsertionSort();
        ob.sequential(itArray);
        sEnd = System.currentTimeMillis();
        sDuration = sEnd - sStart;

        System.out.println("----------Array after sequential sort----------");

        for (int i = 0; i < itArray.length; i++) {

            System.out.println(itArray[i]);

        }

        System.out.println("Duration of parallel insertion sort with " + arr.length + " elements: " + pDuration);
        System.out.println("Duration of sequential insertion sort with " + arr.length + " elements: " + sDuration);

    }

}
import java.io.*;
import java.util.*;

public class ParallelBS implements Runnable{
    private static int[] testData;
    private static Map<Thread, Integer> map = new HashMap<>();

    private static int smallSize = (int) Math.pow(10, 4);

    private static String[] testFiles = new String[]{"SmallTC1", "SmallTC2", "MediumTC1", "MediumTC2", "LargeTC1", "LargeTC2"};
    private static int[] sizes = new int[]{smallSize, smallSize * 5, smallSize * 10, smallSize * 50, smallSize * 100, smallSize * 500};

    public static void main(String[] args) throws Exception{
        for(int i = 0; i < testFiles.length; i++){
            testData = readInput(new Scanner(new File("../TestCases/" + testFiles[i] + ".txt")), sizes[i]);

            System.out.println("Test Case " + (i + 1) + ":");

            // The amount of memory prior to execution of the threads
            long preSortingMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            long start = System.currentTimeMillis();

            ParallelBS p = new ParallelBS();

            Thread t1 = new Thread(p);
            Thread t2 = new Thread(p);

            map.put(t1, 0);
            map.put(t2, 1);

            t1.start();
            t2.start();

            t1.join(0);
            t2.join(0);

            combine();

            long end = System.currentTimeMillis();

            System.out.println("Execution Time: " + (end - start) + "ms");

            long postSortingMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            long memoryUsed = postSortingMemory - preSortingMemory;

            System.out.println("Total Memory Usage: " + memoryUsed + " bytes\n");
        }
    }

    // Runs the concurrent bubble sort algorithm
    @Override
    public void run()
    {   
        int start = map.get(Thread.currentThread());
        int len = testData.length;

        int stopOuter;
        int stopInner;

        if(len % 2 == 0)
            stopOuter = len / 2;
        else
            stopOuter = (len / 2) + (1 - start);

        // So the program can save time by not having to 
        // constantly calculate len - 2
        stopInner = len - 2;

        for(int i = 1; i < stopOuter; i++)
            for(int j = start; j < stopInner; j += 2)
                if(testData[j] > testData[j + 2])
                    swap(testData, j, j + 2);
    }

    // Merge in O(n) time the even and odd portions of the array to 
    // ensure everything is sorted
    private static void combine(){
        int[] newArray = new int[testData.length];

        int i = 0;

        int even = 0;
        int odd = 1;
        

        while(odd < testData.length && even < testData.length){
            if(testData[even] < testData[odd]){
                newArray[i] = testData[even];
                even += 2;
            }else{
                newArray[i] = testData[odd];
                odd += 2;
            }

            i++;
        }

        if(odd >= testData.length){
            for(int j = i; j < testData.length; j++){
                newArray[j] = testData[even];
                even += 2;
            }
        }else{
            for(int j = i; j < testData.length; j++){
                newArray[j] = testData[odd];
                odd += 2;
            }
        }

        testData = newArray;
    }

    // The standard iterative bubble sort
    private static void bubbleSort(){
        for(int i = 0; i < testData.length - 1; i++)
            for(int j = 0; j < testData.length - i - 1; j++)
                if(testData[j] > testData[j + 1])
                    swap(testData, j, j + 1);
    }

    private static void swap(int[] nums, int i, int j){
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    // Will generate a random array size from [1, 100000]
    private static int getRandomNum(){
        return (int) (Math.random() * 100000) + 1;
    }

    private static int[] readInput(Scanner s, int size){
        int[] nums = new int[size];

        for(int i = 0; i < size; i++)
            nums[i] = Integer.parseInt(s.nextLine());

        return nums;
    }
}

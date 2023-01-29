import java.util.*;

public class ParallelBS implements Runnable{

    static int[] testData;
    static Map<Thread, Integer> map = new HashMap<>();

    public static void main(String[] args) throws Exception{
        int size = getRandomNum();
        testData = new int[size];

        for(int j = 0; j < size; j++)
            testData[j] = getRandomNum();

        int[] og = testData.clone();

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

        System.out.println(end - start);

        testData = og;

        start = System.currentTimeMillis();

        bubbleSort();

        end = System.currentTimeMillis();

        System.out.println(end - start);
    }

    @Override
    public void run()
    {   
        int start = map.get(Thread.currentThread());
        int len = testData.length;

        int stopOuter;

        if(len % 2 == 0)
            stopOuter = len / 2;
        else
            stopOuter = (len / 2) + (1 - start);

        for(int i = 1; i < stopOuter; i++)
            for(int j = start; j < len - 2; j += 2)
                if(testData[j] > testData[j + 2])
                    swap(testData, j, j + 2);
    }

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

    private static int getRandomNum(){
        return (int) (Math.random() * 100000);
    }
}
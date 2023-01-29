import java.util.*;

public class ParallelBS{

    private static int[] testData;
    private static int size;

    public static void main(String[] args){
        size = getRandomNum();
        testData = new int[size];

        for(int i = 0; i < size; i++)
            testData[i] = getRandomNum();

        
    }

    private static int getRandomNum(){
        return (int) (Math.random() * 100);
    }
}
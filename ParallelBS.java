import java.util.*;

public class ParallelBS{
    public static void main(String[] args){
        for(int i = 0; i < 1000; i++){
            int size = getRandomNum();
            int[] testData = new int[size];
    
            for(int j = 0; j < size; j++)
                testData[j] = getRandomNum();
    
            int[] sortedArray = testData.clone();

            Arrays.sort(sortedArray);
    
            bubbleSort(testData);
    
            if(!Arrays.equals(sortedArray, testData))
                System.out.println(Arrays.toString(testData));
        }
    }

    private static void bubbleSort(int[] nums){
        int len = nums.length;

        int even = (nums.length % 2 == 0) ? 1 : 0;

        for(int j = 0; j < len - 1; j++){
            int i;

            for(i = 0; i < (len / 2) - even; i++)
                if(nums[2*i+1] > nums[2*i+2])
                    swap(nums, 2*i+1, 2*i+2);

            for(i = 0; i < (len / 2); i++)
                if(nums[2*i] > nums[2*i+1])
                    swap(nums, 2*i, 2*i+1);
        }
    }

    private static void swap(int[] nums, int i, int j){
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    private static int getRandomNum(){
        return (int) (Math.random() * 100);
    }
}
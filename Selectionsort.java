import java.util.Random;

public class Selectionsort {   
    
    public static void MMBPSS(int arr[]) // MMBPSS is for Min-Max Bidirectional Parallel Selection Sort 
    {
        int size = arr.length;
        int mid = size / 2;
        int start = 0;
        int end = size - 1;

        while (start < end)
        {

            Thread[] threads = new Thread[2];


            int min1Index = 0;
            int max1Index = 0;
            int min2Index = 0;
            int max2Index = 0;

            int min1 = Integer.MAX_VALUE;
            int max1 = Integer.MIN_VALUE;
            int min2 = Integer.MAX_VALUE;
            int max2 = Integer.MIN_VALUE;
            
            for (int i = start; i < mid; i++)
            {

                if (arr[i] < min1)
                {
                    min1 = arr[i];
                    min1Index = i;
                }

                if (arr[i] > max1)
                {
                    max1 = arr[i];
                    max1Index = i;
                }
                    

            }

            for (int j = mid; j <= end; j++)
            {

                if (arr[j] < min2)
                {
                    min2 = arr[j];
                    min2Index = j;
                }

                if (arr[j] > max2)
                {
                    max2 = arr[j];
                    max2Index = j;
                }
                    

            }

            System.out.println("min1 is " + min1 +  " min2 is " + min2 + " max1 is " + max1 + " max2 is " + max2);

            if (min1 < min2)
            {
                if (max1Index == start)
                    max1Index = min1Index;
                swap(arr, min1Index, start);
                
            }
            else
            {
                if (max1Index == start)
                    max1Index = min2Index;
                swap(arr, min2Index, start);
            }

            if (end - start > 1)
            {
                if (max1 > max2)
                    swap(arr, max1Index, end);
                else
                    swap(arr, max2Index, end);
            }

            for(int i:arr){  
                System.out.print(" -" + i + "- ");        }  
            System.out.println();

            start++;
            end --;

        }

    }

    public static void swap(int[] arr, int indexA, int indexB)
    {

        int temp = arr[indexA];
        arr[indexA] = arr[indexB];
        arr[indexB] = temp;

    }

    // =================================================
    public static void selectionSort(int[] arr){  
        for (int i = 0; i < arr.length - 1; i++)  
        {  
            int index = i;  
            for (int j = i + 1; j < arr.length; j++){  
                if (arr[j] < arr[index]){  
                    index = j; //searching for lowest index  
                }  
            }  
            int smallerNumber = arr[index];   
            arr[index] = arr[i];  
            arr[i] = smallerNumber;  
        }  
    }  

    public static boolean isSorted(int[] a) {

            for (int i = 0; i < a.length - 1; i++) {
                if (a[i] > a[i + 1]) {
                    return false; // It is proven that the array is not sorted.
                }
            }
        
            return true; // If this part has been reached, the array must be sorted.
        }
       
    public static void main(String a[]){  
        Random random = new Random();
        int[] arr1 = random.ints(10, 10,1000).toArray();

        System.out.println("before Selection Sort");  
        for(int i:arr1){  
            System.out.print(" -" + i + "- ");        }  
        System.out.println();

        long startTime = System.nanoTime();
        // selectionSort(arr1);//sorting array using selection sort  
        MMBPSS(arr1);
        System.out.println("\n" + (isSorted(arr1) ? "Array sorted successfully" : "unsuccessful sorting"));
        long endTime = System.nanoTime();

        System.out.println("After Selection Sort");  
        for(int i:arr1){  
            System.out.print(" -" + i + "- ");  
        }  


        System.out.println("\nTime it took to execute parallel selection sort: " + String.format("%.4f",((double)(endTime - startTime) / 1000000)/1000) + " seconds" );
    }  
}  
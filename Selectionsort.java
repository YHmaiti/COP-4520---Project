import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

class Utility
{

    public int min1Index;
    public int max1Index;
    public int min2Index;
    public int max2Index;

    public int min1;
    public int max1;
    public int min2;
    public int max2;

    public Utility()
    {
        min1 = Integer.MAX_VALUE;
        max1 = Integer.MIN_VALUE;
        min2 = Integer.MAX_VALUE;
        max2 = Integer.MIN_VALUE;

    }

}

class Boundries
{

    public int start;
    public int end;

    public Boundries(int start, int end)
    {
        this.start = start;
        this.end = end;

    }

}


public class Selectionsort {
    
    public static void MMBPSS(int arr[]) // MMBPSS is for Min-Max Bidirectional Parallel Selection Sort
    {
        int size = arr.length;
        int mid = size / 2;

        Boundries bound = new Boundries(0, size -1);

        while (bound.start < bound.end)
        {
            
            Thread[] threads = new Thread[2];

            Utility util = new Utility();
          
            threads[0] = new Thread(() -> {

                int startT = bound.start;
                for (int i = startT; i < mid; i++)
                {

    
                    if (arr[i] < util.min1)
                    {
                        util.min1 = arr[i];
                        util.min1Index = i;
                    }
    
                    if (arr[i] > util.max1)
                    {
                        util.max1 = arr[i];
                        util.max1Index = i;
                    }
                        
    
                }               
            
            });

            threads[1] = new Thread(() -> {

                for (int j = mid; j <= bound.end; j++)
                {
    
                    if (arr[j] < util.min2)
                    {
                        util.min2 = arr[j];
                        util.min2Index = j;
                    }
    
                    if (arr[j] > util.max2)
                    {
                        util.max2 = arr[j];
                        util.max2Index = j;
                    }
                        
    
                }             
            
            });

            threads[0].start();
            threads[1].start();

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            // System.out.println("min1 is " + util.min1 +  " min2 is " + util.min2 + " max1 is " + util.max1 + " max2 is " + util.max2);

            if (util.min1 < util.min2)
            {
                if (util.max1Index == bound.start)
                    util.max1Index = util.min1Index;
                swap(arr, util.min1Index, bound.start);
                
            }
            else
            {
                if (util.max1Index == bound.start)
                    util.max1Index = util.min2Index;
                swap(arr, util.min2Index, bound.start);
            }

            if (bound.end - bound.start > 1)
            {
                if (util.max1 > util.max2)
                    swap(arr, util.max1Index, bound.end);
                else
                    swap(arr, util.max2Index, bound.end);
            }

            // for(int i:arr){  
            //     System.out.print(" -" + i + "- ");        }  
            // System.out.println();

            bound.start++;
            bound.end --;

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
            swap(arr, index, i);
            // int smallerNumber = arr[index];   
            // arr[index] = arr[i];  
            // arr[i] = smallerNumber;  
        }  
    } 
    
    public static void dynamicSelectionSort(int[] arr){  

        Stack<Integer> minStack = new Stack<>();
        HashMap<Integer, Integer> indexMap = new HashMap<>();
        minStack.push(arr[0]);
        indexMap.put(arr[0], 0);
        
        int startOfSearch = 1;

        for (int i = 0; i < arr.length - 1; i++)  
        {  
            for (int j = startOfSearch; j < arr.length; j++){  
                if (minStack.isEmpty() || arr[j] < minStack.peek()){  
                    // index = j; //searching for lowest index
                    // System.out.println();
                    minStack.push(arr[j]);
                    indexMap.put(arr[j], j);

                }  
            }  

            int min = minStack.pop();
            int swapedValue = arr[i];
            
            swap(arr, indexMap.get(min), i);

            indexMap.put(swapedValue, indexMap.get(min));
            indexMap.put(min, i);
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


        // System.out.println("before Selection Sort");  
        // for(int i:arr1){  
        //     System.out.print(" -" + i + "- ");        }  
        System.out.println();

        int[] arr1 = random.ints(600000, 10,10000).toArray();
        long startTime = System.nanoTime();
        System.out.println("Running regular selectionSort on " + arr1.length + " elements");
        selectionSort(arr1);//sorting array using selection sort
        long endTime = System.nanoTime();
        System.out.println("\n" + (isSorted(arr1) ? "Array sorted successfully" : "unsuccessful sorting"));
        System.out.println("\nTime it took to execute sort: " + String.format("%.4f",((double)(endTime - startTime) / 1000000)/1000) + " seconds" );


        int[] arr2 = random.ints(600000, 10,10000).toArray();
        startTime = System.nanoTime();
        System.out.println("Running MMBPSS selectionSort on " + arr2.length + " elements");
        MMBPSS(arr2);//sorting array using selection sort
        endTime = System.nanoTime();
        System.out.println("\n" + (isSorted(arr2) ? "Array sorted successfully" : "unsuccessful sorting"));
        System.out.println("\nTime it took to execute sort: " + String.format("%.4f",((double)(endTime - startTime) / 1000000)/1000) + " seconds" );

        // System.out.println("Running MMBPSS on " + arr1.length + " elements");
        // MMBPSS(arr1);
        // dynamicSelectionSort(arr1);
   

        // System.out.println("After Selection Sort");  
        // for(int i:arr2){  
        //     System.out.print(" -" + i + "- ");  
        // }  


    }  
}  
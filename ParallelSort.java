import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ParallelSort {

    // Split and concurrent selection algorithm
    public static void splitAndParallelSelection(int[] D, int[] d, int n) {
        int S = D.length; // size of data sequence D
        int s = S / n; // size of each subsequence D
        int[][] subSequences = new int[n][s]; // to store the subsequences
        
        // Split data sequence D into n subsequences
        for (int i = 0; i < n; i++) {
            System.arraycopy(D, i * s, subSequences[i], 0, s);
        }
        
        // Apply parallel radix sort on each subsequence
        for (int i = 0; i < n; i++) {
            radixSort(subSequences[i]);
        }
        
        // Apply parallel selection sort to find correct position of each element
        for (int i = 0; i < n; i++) {
            int FST = i * s; // first element of the subsequence
            int LAT = FST + s - 1; // last element of the subsequence
            for (int k = 0; k < s; k++) {
                int index = binarySearch(d, D[i * s + k]);
                if (index == -1) {
                    d[FST + k] = D[i * s + k];
                } else {
                    // FCFS technique if multiple elements have the same value
                    while (index < S && d[index] == D[i * s + k]) {
                        index++;
                    }
                    for (int j = S - 1; j >= index + 1; j--) {
                        d[j] = d[j - 1];
                    }
                    d[index] = D[i * s + k];
                }
            }
        }
    }

    // Radix sort algorithm
    public static void radixSort(int[] arr) {
        int radix = 10;
        int maxDigits = getMaxDigits(arr);
        int divisor = 1;
        for (int i = 0; i < maxDigits; i++) {
            ArrayList<Integer>[] bucketList = new ArrayList[radix];
            for (int j = 0; j < arr.length; j++) {
                int number = (arr[j] / divisor) % radix;
                if (bucketList[number] == null) {
                    bucketList[number] = new ArrayList<>();
                }
                bucketList[number].add(arr[j]);
            }
            int index = 0;
            for (int j = 0; j < radix; j++) {
                if (bucketList[j] != null) {
                    for (int k = 0; k < bucketList[j].size(); k++) {
                        arr[index++] = bucketList[j].get(k);
                    }
                }
            }
            divisor *= radix;
        }
    }
    
    public static int getMaxDigits(int[] arr) {
        int maxDigits = 0;
        for (int i = 0; i < arr.length; i++) {
            int digits = String.valueOf(arr[i]).length();
            if (digits > maxDigits) {
                maxDigits = digits;
            }
        }
        return maxDigits;
    }


    // Binary search algorithm
public static int binarySearch(int[] arr, int target) {
    int left = 0;
    int right = arr.length - 1;
    while (left <= right) {
        int mid = (left + right) / 2;
        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return -1;
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
    int[] arr1 = random.ints(100000, 10,100000).toArray();
    int [] ans = Arrays.copyOf(arr1, arr1.length);

    System.out.println((isSorted(ans) ? "ans is sorted" : "ans is not sorted"));

    splitAndParallelSelection(arr1, ans, 4);

    System.out.println((isSorted(ans) ? "ans is sorted" : "ans is not sorted"));
}  
}
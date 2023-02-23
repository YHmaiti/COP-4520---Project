import java.util.Random;
import java.util.concurrent.*;

public class Quicksort implements Runnable {
    public static final int NUMWORKERS = Runtime.getRuntime().availableProcessors();
    static final ExecutorService executorService = Executors.newFixedThreadPool(NUMWORKERS);

    final int minSize;
    final int[] arr;
    final int low;
    final int high;

    public Quicksort(int minSize, int[] arr, int low, int high) {
        this.minSize = minSize;
        this.arr = arr;
        this.low = low;
        this.high = high;
    }

    @Override
    public void run() {
        sort(arr, low, high);
    }

    public void sort(int[] arr, int low, int high) {
        int length = high - low + 1;
        if (length <= 1) {
            return;
        }

        int pivotIndex = new Random().nextInt(high - low) + low;
        int pivotValue = arr[pivotIndex];

        swap(arr, pivotIndex, high);

        int curIndex = low;
        for (int i = low; i < high; i++) {
            if (arr[i] <= pivotValue) {
                swap(arr, i, curIndex);
                curIndex++;
            }
        }

        swap(arr, curIndex, high);

        if (length > minSize) {
            Quicksort quick = new Quicksort(minSize, arr, low, curIndex - 1);
            Future<?> future = executorService.submit(quick);
            sort(arr, curIndex + 1, high);

            try {
                future.get(1000, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            sort(arr, low, curIndex - 1);
            sort(arr, curIndex + 1, high);
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }
}

import java.util.Objects;

/**
 * PRE: each sort method receives a non-null array.
 * POST: selectionSort, insertionSort, bubbleSort, mergeSort, and quickSort
 *       sort that array in ascending order.
 */
public class Sorts {
    // Source: https://github.com/CS201Java/Lecture13

    public static void selectionSort(int[] arr) {
        selectionSort(arr, new SortStats());
    }

    public static void selectionSort(int[] arr, SortStats stats) {
        Objects.requireNonNull(arr, "arr");
        resetCounts(stats);

        for (int i = 0; i < arr.length; i++) {
            stats.loops++;
            int indexSmallest = i;
            for (int j = i + 1; j < arr.length; j++) {
                stats.loops++;
                stats.comparisons++;
                if (arr[j] < arr[indexSmallest]) {
                    indexSmallest = j;
                }
            }

            if (indexSmallest != i) {
                swap(arr, i, indexSmallest);
                stats.swaps++;
            }
        }
    }

    public static void insertionSort(int[] arr) {
        insertionSort(arr, new SortStats());
    }

    public static void insertionSort(int[] arr, SortStats stats) {
        Objects.requireNonNull(arr, "arr");
        resetCounts(stats);

        for (int i = 1; i < arr.length; i++) {
            stats.loops++;
            int j = i;
            while (j > 0) {
                stats.loops++;
                stats.comparisons++;
                if (arr[j] >= arr[j - 1]) {
                    break;
                }
                swap(arr, j, j - 1);
                stats.swaps++;
                j--;
            }
        }
    }

    public static void bubbleSort(int[] arr) {
        bubbleSort(arr, new SortStats());
    }

    public static void bubbleSort(int[] arr, SortStats stats) {
        Objects.requireNonNull(arr, "arr");
        resetCounts(stats);

        for (int i = arr.length - 1; i > 0; i--) {
            stats.loops++;
            for (int j = 0; j < i; j++) {
                stats.loops++;
                stats.comparisons++;
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    stats.swaps++;
                }
            }
        }
    }

    public static void mergeSort(int[] arr) {
        mergeSort(arr, new SortStats());
    }

    public static void mergeSort(int[] arr, SortStats stats) {
        Objects.requireNonNull(arr, "arr");
        resetCounts(stats);
        mergeSort(arr, new int[arr.length], 0, arr.length - 1, stats);
    }

    private static void mergeSort(
            int[] arr, int[] helper, int low, int high, SortStats stats) {
        if (low >= high) {
            return;
        }

        int mid = low + (high - low) / 2;
        mergeSort(arr, helper, low, mid, stats);
        mergeSort(arr, helper, mid + 1, high, stats);
        merge(arr, helper, low, mid, high, stats);
    }

    private static void merge(
            int[] arr, int[] helper, int low, int mid, int high,
            SortStats stats) {
        System.arraycopy(arr, low, helper, low, high - low + 1);
        int left = low;
        int right = mid + 1;
        int index = low;

        while (left <= mid && right <= high) {
            stats.loops++;
            stats.comparisons++;
            if (helper[left] <= helper[right]) {
                arr[index++] = helper[left++];
            } else {
                arr[index++] = helper[right++];
            }
        }

        while (left <= mid) {
            stats.loops++;
            arr[index++] = helper[left++];
        }

        while (right <= high) {
            stats.loops++;
            arr[index++] = helper[right++];
        }
    }

    public static void quickSort(int[] arr) {
        quickSort(arr, new SortStats());
    }

    public static void quickSort(int[] arr, SortStats stats) {
        Objects.requireNonNull(arr, "arr");
        resetCounts(stats);
        quickSort(arr, 0, arr.length - 1, stats);
    }

    private static void quickSort(
            int[] arr, int low, int high, SortStats stats) {
        while (low < high) {
            int pivotIndex = partition(arr, low, high, stats);
            if (pivotIndex - low < high - pivotIndex) {
                quickSort(arr, low, pivotIndex - 1, stats);
                low = pivotIndex + 1;
            } else {
                quickSort(arr, pivotIndex + 1, high, stats);
                high = pivotIndex - 1;
            }
        }
    }

    private static int partition(
            int[] arr, int low, int high, SortStats stats) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            stats.loops++;
            stats.comparisons++;
            if (arr[j] <= pivot) {
                i++;
                if (i != j) {
                    swap(arr, i, j);
                    stats.swaps++;
                }
            }
        }

        if (i + 1 != high) {
            swap(arr, i + 1, high);
            stats.swaps++;
        }
        return i + 1;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static void resetCounts(SortStats stats) {
        Objects.requireNonNull(stats, "stats");
        stats.comparisons = 0;
        stats.swaps = 0;
        stats.loops = 0;
        stats.timeNano = 0;
    }

    public static class SortStats {
        public String sortName;
        public String fileName;
        public long comparisons;
        public long swaps;
        public long loops;
        public long timeNano;
    }
}

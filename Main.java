import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
// Jaden Vernon, Comp_Sci-201L-010L, Lab 12


class Main {
    public static void main(String[] args) throws IOException {
        String[] inputFiles = args.length == 0
                ? new String[] {"data1.txt", "data2.txt", "data3.txt"}
                : args;
        String outputFile = "sort-results.txt";
        String[] sortNames = {
            "Bubble", "Insertion", "MergeSort", "QuickSort", "Selection"
        };
        SortOperation[] operations = {
            Sorts::bubbleSort,
            Sorts::insertionSort,
            Sorts::mergeSort,
            Sorts::quickSort,
            Sorts::selectionSort
        };
        int[][] inputData = new int[inputFiles.length][];
        String[] fileLabels = new String[inputFiles.length];

        for (int i = 0; i < inputFiles.length; i++) {
            inputData[i] = readNumbers(inputFiles[i]);
            fileLabels[i] = getFileLabel(inputFiles[i]);
        }

        try (PrintWriter output = new PrintWriter(new FileWriter(outputFile))) {
            output.printf("%-14s %-20s %14s %16s %14s %18s%n",
                    "Sort Name", "File", "Swaps", "Comparisons",
                    "Loops", "NanoSeconds");

            for (int sortIndex = 0; sortIndex < operations.length; sortIndex++) {
                for (int fileIndex = 0; fileIndex < inputFiles.length; fileIndex++) {
                    runSort(output, sortNames[sortIndex], fileLabels[fileIndex],
                            inputData[fileIndex], operations[sortIndex]);
                }
            }
            output.flush();
            if (output.checkError()) {
                throw new IOException("Failed to write " + outputFile);
            }
        }

        System.out.println("Results written to " + outputFile);
    }

    private static String getFileLabel(String fileName) {
        String baseName = new File(fileName).getName();
        for (int i = 1; i <= 6; i++) {
            if (baseName.equalsIgnoreCase("data" + i + ".txt")) {
                int type = (i - 1) % 3;
                String description = type == 0 ? "random"
                        : type == 1 ? "ascending" : "descending";
                return type + 1 + " (" + description + ")";
            }
        }

        return new File(fileName).getName();
    }

    private static int[] readNumbers(String fileName)
            throws IOException {
        ArrayList<Integer> numbers = new ArrayList<>();

        try (BufferedReader input = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 0;

            while ((line = input.readLine()) != null) {
                lineNumber++;
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }

                String[] values = trimmed.split("[,\\s]+");
                for (String value : values) {
                    try {
                        numbers.add(Integer.parseInt(value));
                    } catch (NumberFormatException exception) {
                        throw new IOException("Invalid integer in " + fileName
                                + " on line " + lineNumber + ": " + value,
                                exception);
                    }
                }
            }
        }

        int[] values = new int[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) {
            values[i] = numbers.get(i);
        }
        return values;
    }

    private static void runSort(
            PrintWriter output,
            String sortName,
            String fileName,
            int[] original,
            SortOperation operation) throws IOException {
        int[] numbers = original.clone();
        Sorts.SortStats stats = new Sorts.SortStats();
        stats.sortName = sortName;
        stats.fileName = fileName;

        long startTime = System.nanoTime();
        operation.sort(numbers, stats);
        stats.timeNano = System.nanoTime() - startTime;

        if (!isSorted(numbers)) {
            throw new IOException(sortName + " did not sort " + fileName
                    + " correctly.");
        }
        output.printf("%-14s %-20s %14d %16d %14d %15d ns%n",
                sortName, fileName, stats.swaps, stats.comparisons,
                stats.loops, stats.timeNano);
    }

    private static boolean isSorted(int[] numbers) {
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i - 1] > numbers[i]) {
                return false;
            }
        }
        return true;
    }

    private interface SortOperation {
        void sort(int[] numbers, Sorts.SortStats stats);
    }
}

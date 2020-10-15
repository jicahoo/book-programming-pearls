package chapter_1;

import javax.xml.ws.soap.MTOMFeature;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ProblemSortPhoneNumber {

    /**
     * In file filePath, it contains phone number whose count is less than 10^7 and values is less than 10^7.
     * If there is duplicate record, it's an error.
     * @param filePath
     * @return The sorted phone number.
     */
    public List<Integer> sort(final String filePath) throws FileNotFoundException {

        //你觉得奇怪的问题：为什么没有整数的幂函数？而只有double的。 这种基础性编程库的作者肯定要比我更早意识到这个问题。
        //为什么还是如此设计？肯定有其一定的原因。此处的原因是，计算机里的int的取值范围很小，所以，很容易越界。所以要用double.
        //https://stackoverflow.com/questions/8071363/calculating-powers-of-integers
        // 我的总结：反直觉的奇怪设计必有其客观原因。
        BitArray bitArray = new BitArray((int) Math.pow(10, 7));
        Scanner scanner = new Scanner(new File(filePath));

        while (scanner.hasNextInt()) {
            int phoneNumber = scanner.nextInt();
            bitArray.add(phoneNumber);
        }
        return bitArray.getSortedElements();
    }

    public List<Integer> sort(final int[] a) {
        BitArray bitArray = new BitArray((int) Math.pow(10, 7));
        for (int i : a) {
            bitArray.add(i);
        }
        return bitArray.getSortedElements();
    }

    /**
     * Book chapter 1, exercise 1 (1.6 section).
     * @param a
     * @return
     */
    public Iterator<Integer> sortUseJdk(final int[] a) {
        TreeSet<Integer> treeSet = new TreeSet<Integer>();


        for (int i : a) {
            if (treeSet.contains(i)) {
                throw new IllegalStateException("Duplicate record.");
            } else {
                treeSet.add(i);
            }
        }
        return treeSet.iterator();
    }

    public void generateTestFileWithDupValue(final String toFilePath) {
    }

    /**
     * https://stackoverflow.com/questions/16000196/java-generating-non-repeating-random-numbers/16000210
     *  Collections.shuffle, Programming Pearls p. 127.,  Fisher–Yates shuffle
     * https://stackoverflow.com/questions/4040001/creating-random-numbers-with-no-duplicates
     * @param toFilePath
     */
    public  int[] generateTestFileWithoutDupValue(final String toFilePath) {
        int n = (int) Math.pow(10, 7);
        int[] a = new int[n];
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }

        Shuffle shuffle = new Shuffle();
        shuffle.knuthDurstenfeldShuffle(a);
        for (int i = 0; i < 100; i++) {
            System.out.print(a[i] + " ");
        }
        System.out.println();
        return a;
    }

    public void testCaseHaveDupValue() throws FileNotFoundException {
        final String testFiePath = "TODO";
        generateTestFileWithDupValue(testFiePath);
        sort(testFiePath);
        //TODO: should exception
    }

    public void testCaseWithoutDupValue() throws FileNotFoundException {
        final String testFilePath = "TODO";
        generateTestFileWithoutDupValue(testFilePath);
        List<Integer> sortedPhoneNumber = sort(testFilePath);
        assert isSorted(sortedPhoneNumber);
    }

    public boolean isSorted(List<Integer> integerList) {
        if (integerList == null || integerList.isEmpty() || integerList.size() == 1) {
            return true;
        }
        for (int i = 0; i < integerList.size() - 1; i++) {
            if (integerList.get(i) > integerList.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public static void solutionUseBitVevtor() {

        // Only need 0.7 seconds, no more than 1 second.
        ProblemSortPhoneNumber problemSortPhoneNumber = new ProblemSortPhoneNumber();
        int[] inputArray = problemSortPhoneNumber.generateTestFileWithoutDupValue("");
        List<Integer>  outputList = problemSortPhoneNumber.sort(inputArray);
        for (int i = 0; i < 100; i++) {
            System.out.print(outputList.get(i) + " ");
        }
        System.out.println();
    }

    public static void solutionUseTreeSet() {
        // What about List? More slower? If use List, it will last a long time. Use tree set, about 13 seconds
        // on my machine.
        ProblemSortPhoneNumber problemSortPhoneNumber = new ProblemSortPhoneNumber();
        int[] inputArray = problemSortPhoneNumber.generateTestFileWithoutDupValue("");
        Iterator<Integer> intIter = problemSortPhoneNumber.sortUseJdk(inputArray);
        int i = 0;
        System.out.println("First 100 items after sorting.");
        while (i < 100 && intIter.hasNext()) {
            System.out.print(intIter.next() + " ");
            i++;
        }
        System.out.println();
    }

    public void timingAnalyze() {
        long start = System.currentTimeMillis();
        solutionUseBitVevtor();
        long end = System.currentTimeMillis();
        long duration = end - start;
        System.out.println("BitVector duration: " + duration/1000.0 + " secs.");

        start = System.currentTimeMillis();
        solutionUseTreeSet();
        end = System.currentTimeMillis();
        duration = end - start;

        System.out.println("TreeSet duration: " + duration/1000.0 + " secs.");
    }

    public void memoryAnalyze() {
       //TODO?
    }

    public static void main(String[] args) {
        solutionUseTreeSet();
    }
}

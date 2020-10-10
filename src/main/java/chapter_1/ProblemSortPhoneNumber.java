package chapter_1;

import javax.xml.ws.soap.MTOMFeature;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

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

    public void generateTestFileWithDupValue(final String toFilePath) {
    }

    public void generateTestFileWithoutDupValue(final String toFilePath) {
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



    public static void main(String[] args) {
        byte[] x = new byte[10];
        for (byte b : x) {
            System.out.println(b);
        }
    }
}

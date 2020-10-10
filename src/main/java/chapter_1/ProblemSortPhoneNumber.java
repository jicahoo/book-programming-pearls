package chapter_1;

import javax.xml.ws.soap.MTOMFeature;
import java.util.List;

public class ProblemSortPhoneNumber {

    /**
     * In file filePath, it contains phone number whose count is less than 10^7 and values is less than 10^7.
     * If there is duplicate record, it's an error.
     * @param filePath
     * @return The sorted phone number.
     */
    public List<Integer> sort(final String filePath) {
        return null;
    }

    public void generateTestFileWithDupValue(final String toFilePath) {
    }

    public void generateTestFileWithoutDupValue(final String toFilePath) {
    }

    public void testCaseHaveDupValue() {
        final String testFiePath = "TODO";
        generateTestFileWithDupValue(testFiePath);
        sort(testFiePath);
        //TODO: should exception
    }

    public void testCaseWithoutDupValue() {
        final String testFilePath = "TODO";
        generateTestFileWithoutDupValue(testFilePath);
        List<Integer> sortedPhoneNumber = sort(testFilePath);
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
        System.out.println(Integer.MAX_VALUE);
    }
}

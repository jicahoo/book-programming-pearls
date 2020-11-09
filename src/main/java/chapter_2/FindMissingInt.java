package chapter_2;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class FindMissingInt {
    private static String BASE_DIR = "src/main/java/chapter_2/";

    public int findMissingInt(String filePath, int first, int last) throws IOException {

        Iterator<String> ints = Files.lines(Paths.get(filePath)).iterator();
        return findMissingInt(ints, first, last);
    }

    public int findMissingInt(Iterator<String> ints, int first, int last) throws IOException {
        assert first <= last; //Why can first can == last.

        double halfCount = ((long) last - (long) first)/2.0;
        double mid = first + halfCount;
        boolean midIsInt = false;
        if ((mid == Math.floor(mid)) && !Double.isInfinite(mid)) {
            midIsInt = true;
        }

        boolean gotMid = false;
        String lessFilePath = BASE_DIR + "less" + mid + ".txt";
        BufferedWriter lessFile = new BufferedWriter(new FileWriter(lessFilePath));
        int lessCounter = 0;
        String greaterFilePath = BASE_DIR + "greater" + mid + ".txt";
        BufferedWriter greaterFile = new BufferedWriter(new FileWriter(greaterFilePath));
        int greaterCounter = 0;

        while (ints.hasNext()) {
            String intStr = ints.next();
            int i = Integer.parseInt(intStr);
            if (midIsInt && i == (int)mid) {
                gotMid = true;
            } else if (i < mid) {
                lessFile.write(intStr);
                lessFile.newLine();
                lessCounter++;
            } else {
                greaterFile.write(intStr);
                greaterFile.newLine();
                greaterCounter++;
            }
        }
        lessFile.close();
        greaterFile.close();

        if (midIsInt && !gotMid) {
            return (int)mid;
        }

        if (lessCounter < halfCount) {
            if (midIsInt) {
                return findMissingInt(lessFilePath, first, (int)mid - 1);
            } else {
                return findMissingInt(lessFilePath, first, (int) Math.floor(mid));
            }
        }
        if (greaterCounter < halfCount) {
            if (midIsInt) {
                return findMissingInt(greaterFilePath, (int)mid + 1 , last);
            } else {
                return findMissingInt(greaterFilePath, (int) Math.ceil(mid), last);
            }
        }
        throw new IllegalStateException("Impossible here. Why?");
    }

    public static void searchIntRange(int first, int last) {
        assert first <= last;
        if (first == last) {
            System.out.println("Recursive end: " + first);
        } else {
            double mid = first + (last - first) / 2.0;
            boolean midIsInt = (mid%1) == 0;
            if (midIsInt) {
                System.out.println("Check mid: " + (int) mid);
            }
            int[] bound = getSubProblemBoundary(mid);
            int leftLast = bound[0];
            int rightFirst = bound[1];
            searchIntRange(first, leftLast);
            searchIntRange(rightFirst, last);
        }
    }

    public static void searchIntRangeV2(int first, int last) {
        //assert first <= last;
        if (first <= last) {
            double mid = first + (last - first) / 2.0;
            boolean midIsInt = (mid%1) == 0;
            if (midIsInt) {
                System.out.println("Check mid: " + (int) mid);
            }
            int[] bound = getSubProblemBoundary(mid);
            int leftLast = bound[0];
            int rightFirst = bound[1];
            searchIntRangeV2(first, leftLast);
            searchIntRangeV2(rightFirst, last);
        }
    }

    private static int[] getSubProblemBoundary(double mid) {
        int[] bound = new int[2];
        boolean midIsInt = (mid%1) == 0;
        if (midIsInt) {
            bound[0] = (int) mid - 1;
            bound[1] = (int) mid + 1;
        } else {
            bound[0] = (int)Math.floor(mid);
            bound[1] = (int) Math.ceil(mid);
        }
        return bound;
    }

    public static int binarySearch(int t, int[] a, int startIdx, int endIdx) {
        assert startIdx <= endIdx;
        int idx = -1;
        if (startIdx == endIdx) {
            if (a[startIdx] == t) {
                idx= startIdx;
            }
        } else {
            //int nrCnt = endIdx - startIdx + 1;
            int mid = -1;
            mid = startIdx +  (endIdx - startIdx) / 2;
            if (a[mid] == t) {
                idx = mid;
            } else if (a[mid] < t) {
                idx = binarySearch(t, a, mid + 1, endIdx);
            } else {
                if (mid == startIdx) {
                    idx = binarySearch(t, a, startIdx, mid);
                } else {
                    idx = binarySearch(t, a, startIdx, mid - 1);
                }
            }
        }
        return idx;
    }
    public static int binarySearchV2(int t, int[] a, int startIdx, int endIdx) {
        assert startIdx <= endIdx;
        int idx = -1;
        if (startIdx == endIdx) {
            if (a[startIdx] == t) {
                idx= startIdx;
            }
        } else {
            //int nrCnt = endIdx - startIdx + 1;
            double mid = startIdx + (endIdx - startIdx) / 2.0;
            boolean midIsInt = (mid % 1) == 0;

            if (midIsInt) {
                if (a[(int) mid] == t) {
                    idx = (int)mid;
                }
            }
            int[] bound = getSubProblemBoundary(mid);
            int leftEndIdx = bound[0];
            int rightStartIdx = bound[1];

        }
        return idx;
    }

    public static int binarySearchV3(int t, int[] a, int startIdx, int endIdx) {
        int idx = -1;
        if (startIdx == endIdx) {
            if (a[startIdx] == t) {
                idx= startIdx;
            }
        } else if (startIdx < endIdx) {
            //int nrCnt = endIdx - startIdx + 1;
            int mid = -1;
            mid = startIdx +  (endIdx - startIdx) / 2;
            if (a[mid] == t) {
                idx = mid;
            } else if (a[mid] < t) {
                idx = binarySearchV3(t, a, mid + 1, endIdx);
            } else {
                idx = binarySearchV3(t, a, startIdx, mid - 1);
            }
        }
        return idx;
    }

    public static int binarySearchV4(int t, int[] a, int startIdx, int endIdx, int stackDepth) {
        String ident = multiple("-", stackDepth);
        int idx = -1;
        System.out.println(ident + ": f(" + t + ", a, " + startIdx + ", " + endIdx + ", " + stackDepth + ")");
        if (startIdx == endIdx) {
            if (a[startIdx] == t) {
                idx= startIdx;
                System.out.println(ident + ": Found at a[" + idx + "] = t = " + t);
            } else {
                System.out.println(ident + ": Not found. End at (t=" + t + ") != (a[" + startIdx+ "]=" + a[startIdx]+")");
            }
        } else if (startIdx < endIdx) {
            //int nrCnt = endIdx - startIdx + 1;
            int mid = -1;
            mid = startIdx +  (endIdx - startIdx) / 2;
            System.out.println(ident + ": Pick mid = " + mid + " from [" + startIdx + "," + endIdx + "]");
            if (a[mid] == t) {
                System.out.println(ident + ": Found at mid " + mid);
                idx = mid;
            } else if (a[mid] < t) {
                System.out.println(ident + ": Go to right [" + (mid + 1) + ", " + endIdx + "]");
                idx = binarySearchV4(t, a, mid + 1, endIdx, ++stackDepth);
            } else {
                System.out.println(ident + ": Go to left [" + startIdx + ", " + (mid - 1) + "], since t = " + t + " < a["+mid+"] = " + a[mid]);
                idx = binarySearchV4(t, a, startIdx, mid - 1, ++stackDepth);
            }
        } else {
            System.out.println( ident + ": " + startIdx + " > " + endIdx);
        }
        return idx;
    }

    public static int binarySearchV5(int t, int[] a, int startIdx, int endIdx, int stackDepth) {
        String ident = multiple("-", stackDepth);
        int idx = -1;
        System.out.println(ident + ": f(" + t + ", a, " + startIdx + ", " + endIdx + ", " + stackDepth + ")");
        if (startIdx <= endIdx) {
            //int nrCnt = endIdx - startIdx + 1;
            int mid = -1;
            mid = startIdx +  (endIdx - startIdx) / 2;
            System.out.println(ident + ": Pick mid = " + mid + " from [" + startIdx + "," + endIdx + "]");
            if (a[mid] == t) {
                System.out.println(ident + ": Found at mid " + mid);
                idx = mid;
            } else if (a[mid] < t) {
                System.out.println(ident + ": Go to right [" + (mid + 1) + ", " + endIdx + "]");
                idx = binarySearchV5(t, a, mid + 1, endIdx, ++stackDepth);
            } else {
                System.out.println(ident + ": Go to left [" + startIdx + ", " + (mid - 1) + "], since t = " + t + " < a["+mid+"] = " + a[mid]);
                idx = binarySearchV5(t, a, startIdx, mid - 1, ++stackDepth);
            }
        } else {
            System.out.println( ident + ": startIdx = " + startIdx + " > endIdx = " + endIdx);
        }
        return idx;
    }

    public static String multiple(String base, int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            sb.append(base);
        }
        return sb.toString();
    }

    public static void testBinarySearch() {
        int[] a = new int[]{100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
        System.out.println(binarySearchV5(10, a, 0, a.length - 1, 0));
        System.out.println(binarySearchV5(500, a, 0, a.length - 1, 0));
        System.out.println(binarySearchV5(100, a, 0, a.length - 1, 0));
        System.out.println(binarySearchV5(1000, a, 0, a.length - 1, 0));
    }

    public void generateInputFile() throws IOException {
        String filePath = BASE_DIR + "input-small.txt";
        BufferedWriter inputFile = new BufferedWriter(new FileWriter(filePath));
        long cap =  4 * (long)Math.pow(10, 3);
        //long cap =  4 * (long)Math.pow(10, 9);  will generate a big file with size 40 GB!
        System.out.println(cap);
        for (long i = 0; i < cap; i++) {
            Random r = new Random();
            inputFile.write(""+r.nextInt());
            inputFile.newLine();
        }
        inputFile.close();
    }

    public static void testSmallSet() throws IOException {
        //Test assert first < last to first <= last.
        FindMissingInt findMissingInt = new FindMissingInt();
        List<String> intStrs = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");
        int x = findMissingInt.findMissingInt(intStrs.iterator(), 0, 9);
        System.out.println(x);
        assert x == 0;

        intStrs = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8");
        x = findMissingInt.findMissingInt(intStrs.iterator(), 0, 9);
        System.out.println(x);
        assert x == 9;
    }
    public static void testBigSet() throws IOException {
        String inputFilePath = BASE_DIR + "input-small.txt";

        FindMissingInt findMissingInt = new FindMissingInt();
        //TODO: ? All number in file is positive?
        int missingInt = findMissingInt.findMissingInt(inputFilePath, Integer.MIN_VALUE, Integer.MAX_VALUE);
        System.out.println(missingInt);
    }

    public static void main(String[] args) throws IOException {
        //testBinarySearch();
        searchIntRangeV2(-9, 11);
    }
}

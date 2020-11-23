package chapter_2;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class FindMissingInt {
    private static String BASE_DIR = "src/main/java/chapter_2/";

    public long findMissingIntV2Cnt = 0;
    public long tailCnt = 0;
    public long avgDur = 0;
    public long avgDurSec = 0;
    public long avgDurThird = 0;
    public FindMissingInt(){
        this.findMissingIntV2Cnt = 0;
    }

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
    public int findMissingIntV2(final String filePath, int first, int last) throws IOException {
        long start = System.nanoTime();
        findMissingIntV2Cnt++;
        assert first <= last; //Why can first can == last.

        Path inputFilePath = Paths.get(filePath);
        Iterator<String> ints = Files.lines(inputFilePath).iterator();
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

        long two = System.nanoTime();
        long dur = two - start;
        if (avgDur != 0) {
            avgDur = (avgDur+dur)/2;
        } else {
            avgDur = dur;
        }

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
        Files.deleteIfExists(inputFilePath);

        long three = System.nanoTime();
        long durSec = three - two;
        if (avgDurSec == 0) {
            avgDurSec = durSec;
        } else {
            avgDurSec = (avgDurSec  + durSec)/2;
        }

        int result = -1;
        if (midIsInt && !gotMid) {
            result = (int)mid;
        } else {
            if (lessCounter < halfCount) {
                Files.deleteIfExists(Paths.get(greaterFilePath));
                if (midIsInt) {
                    result = findMissingIntV2(lessFilePath, first, (int)mid - 1);
                } else {
                    result = findMissingIntV2(lessFilePath, first, (int) Math.floor(mid));
                }
            } else {
                Files.deleteIfExists(Paths.get(lessFilePath));
                if (midIsInt) {
                    result = findMissingIntV2(greaterFilePath, (int)mid + 1 , last);
                } else {
                    result = findMissingIntV2(greaterFilePath, (int) Math.ceil(mid), last);
                }
            }
        }
        if (result < 0) {
            String msg = "mid: " + mid + ", lessCounter: " + lessCounter + ", greaterCounter: " + greaterCounter;
            throw new IllegalStateException("Impossible here. Why? hint: " + msg);
        }
        long four = System.nanoTime();
        long durThird = four - three;
        if (avgDurThird == 0) {
            avgDurThird = durThird;
        } else {
            avgDurThird = (avgDurThird  + durThird)/2;
        }
        tailCnt++;
        return result;
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

    public static int binarySearchV6(int t, int[] a, int startIdx, int endIdx) {
        int idx = -1;
        if (startIdx <= endIdx) {
            int mid = startIdx +  (endIdx - startIdx) / 2;
            if (a[mid] == t) {
                idx = mid;
            } else if (a[mid] < t) {
                idx = binarySearchV6(t, a, mid + 1, endIdx);
            } else {
                idx = binarySearchV6(t, a, startIdx, mid - 1);
            }
        }
        return idx;
    }

    public static int binarySearchV7(int t, int[] a, int startIdx, int endIdx) {
        int idx = -1;
        if (startIdx <= endIdx) {
            int mid = (startIdx + endIdx) / 2;
            if (a[mid] == t) {
                idx = mid;
            } else if (a[mid] < t) {
                idx = binarySearchV7(t, a, mid + 1, endIdx);
            } else {
                idx = binarySearchV7(t, a, startIdx, mid - 1);
            }
        }
        return idx;
    }

    public static int binarySearchV8(int t, int[] a, int startIdx, int endIdx) {
        int idx = -1;
        while (startIdx <= endIdx) {
            int m = (startIdx + endIdx) /2;
            if (t == a[m]) {
                idx = m;
                break;
            } else if (t < a[m]) {
                endIdx = m - 1;
            } else {
                startIdx = m + 1;
            }
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
        System.out.println(binarySearchV8(10, a, 0, a.length - 1));
        System.out.println(binarySearchV8(500, a, 0, a.length - 1));
        System.out.println(binarySearchV8(100, a, 0, a.length - 1));
        System.out.println(binarySearchV8(1000, a, 0, a.length - 1));
    }

    public void generateInputFile() throws IOException {
        String filePath = BASE_DIR + "input-100mb.txt";
        BufferedWriter inputFile = new BufferedWriter(new FileWriter(filePath));
        long cap =  2 * (long)Math.pow(10, 9)/200;
        //long cap =  4 * (long)Math.pow(10, 9);  will generate a big file with size 40 GB!
        System.out.println(cap);
        Random r = new Random();
        for (long i = 0; i < cap; i++) {
            inputFile.write(""+r.nextInt(Integer.MAX_VALUE));
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

    public int fib(int n) {
        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else {
            return fib(n-1) + fib(n - 2);
        }

    }

    public static void testFindMissing() throws IOException {

        FindMissingInt s = new FindMissingInt();
        int missInt = s.findMissingIntV2(BASE_DIR + "input.txt", 0, Integer.MAX_VALUE);
        /**
         Missing 16

         real	15m15.698s
         user	9m33.865s
         sys	5m16.316s

         real	0m2.988s
         user	0m2.786s
         sys	0m0.923s

         */
        System.out.println("Missing " + missInt);
        System.out.println("Call cnt: " + s.findMissingIntV2Cnt);
        System.out.println("Tail cnt: " + s.tailCnt);
        System.out.println("Dur: " + s.avgDur);
        System.out.println("Second Dur: " + s.avgDurSec);
        System.out.println("Thrid Dur: " + s.avgDurThird);

        //s.generateInputFile();

    }

    public static void main(String[] args) throws IOException {
        FindMissingInt s = new FindMissingInt();
        int fb = s.fib(45);
        System.out.println(fb);


    }
}

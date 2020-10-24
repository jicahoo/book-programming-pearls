package chapter_1.problem_sort_phonenumber;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SolutionMergeSort {
    private static final String MID_FILE_PATH = "src\\main\\java\\chapter_1\\problem_sort_phonenumber\\temp";
    private static final String MID_FILE_NAME_PREFIX = "mid_";
    //private static final int PAGE_SIZE = 100;
    private static final int PAGE_SIZE = (1024*1024)/4; //1MB can store how many 4 byte ints?

    public void mergeSort(final String inputFilePath, final String outputFilePath) throws IOException {
        List<String> midFilePaths = sortToMidFiles(inputFilePath);
        mergeSortAndWriteToFile(midFilePaths, outputFilePath);
    }


    /**
     * Sort integers in inputFilePath in batch style.
     * @param inputFilePath
     * @return Middle file paths.
     * @throws IOException
     */
    private List<String> sortToMidFiles(String inputFilePath) throws IOException {
        List<String> midFilePaths = new ArrayList<>();
        Iterator<String> iter = getLineIterator(inputFilePath);
        int midFileNum = 0;
        int dataCount = 0;
        List<Integer> pageBuffer = new ArrayList<>();
        while (iter.hasNext()) {
            pageBuffer.add(Integer.parseInt(iter.next()));
            dataCount++;
            if (dataCount == PAGE_SIZE|| !iter.hasNext()) {
                Collections.sort(pageBuffer);
                assert pageBuffer.size() <= PAGE_SIZE;
                writeToMidFile(pageBuffer, midFileNum++, midFilePaths);
                dataCount = 0;
                pageBuffer.clear();//error
            }
        }
        return midFilePaths;
    }

    private void writeToMidFile(List<Integer> pageBuffer, int i, List<String> midFilePaths) throws IOException {
        if (!Files.exists(Paths.get(MID_FILE_PATH))) {
            Files.createDirectory(Paths.get(MID_FILE_PATH));
        }
        String inputFilePath = MID_FILE_PATH + "\\" + MID_FILE_NAME_PREFIX+ i + ".txt";
        midFilePaths.add(inputFilePath);
        List<String> lines = pageBuffer.stream().map(Object::toString).collect(Collectors.toList());
        Files.write(Paths.get(inputFilePath), lines);
    }

    static class CacheIterator<T> implements Iterator<T> {
        private T cachedOneValue;
        private final Iterator<T> originalIterator;

        CacheIterator(Iterator<T> iter) {
            originalIterator = iter;
        }

        @Override
        public boolean hasNext() {
            if (cachedOneValue != null) {
                return true;
            }
            return originalIterator.hasNext();
        }

        @Override
        public T next() {
            if (cachedOneValue != null) {
                T temp = cachedOneValue;
                cachedOneValue = null;
                return temp;
            }
            return originalIterator.next();
        }

        public void setCachedOneValue(T val) {
            cachedOneValue = val;
        }

    }


    private void mergeSortAndWriteToFile(List<String> midFilePaths, String outputFilePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
        List<CacheIterator<String>> iters = midFilePaths.stream().map(this::getCachedIterator).collect(Collectors.toList());
        while (!iters.isEmpty()) {
            MinResult minResult = getMinUseCachedIter(iters);
            writer.write(minResult.min.toString());
            writer.newLine();
            //writer.flush();
            if (!minResult.minIter.hasNext()) {
                iters.remove(minResult.minIter);
            }
        }
        writer.close();
    }
    static class MinResult {
        public Integer min;
        public CacheIterator<String> minIter;
    }

    MinResult getMinUseCachedIter(List<CacheIterator<String>> iters) {
        assert iters.stream().allMatch(CacheIterator::hasNext);

        MinResult minResult = new MinResult();
        minResult.min = Integer.MAX_VALUE;
        minResult.minIter = null;
        Map<CacheIterator<String>, String> cache = new HashMap<>();
        for (CacheIterator<String> iter : iters) {
            if (iter.hasNext()) {
                String intStr = iter.next();
                cache.put(iter, intStr);
                int curInt = Integer.parseInt(intStr);
                if (curInt < minResult.min) {
                    minResult.min = curInt;
                    minResult.minIter = iter;
                }
            }
        }
        for (CacheIterator<String> iter : iters) {
            if (iter != minResult.minIter) {
                iter.setCachedOneValue(cache.get(iter));
            } else {
                iter.setCachedOneValue(null);
            }
        }

        assert null != minResult.minIter;
        return minResult;
    }


    public Iterator<String> getLineIterator(final String filePath) {
        try {
            return Files.lines(Paths.get(filePath)).iterator();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO:
            return null;
        }
    }

    public CacheIterator<String> getCachedIterator(final String filePath) {
        try {
            return new CacheIterator<>(Files.lines(Paths.get(filePath)).iterator());
        } catch (IOException e) {
            e.printStackTrace();
            //TODO:
            return null;
        }
    }

    public List<String> getLinesAsList(final String filePath) {
        try {
            Iterator<String> iters = Files.lines(Paths.get(filePath)).iterator();
            List<String> result = new ArrayList<>();

            while (iters.hasNext()) {
                result.add(iters.next());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            //TODO:
            return null;
        }
    }

    private static void iterateFileLines() throws IOException {
        String inputFilePath = "src\\main\\java\\chapter_1\\problem_sort_phonenumber\\mid_9.txt";
        Stream<String> lines = Files.lines(Paths.get(inputFilePath));
        Iterator<String> lineIter = lines.iterator();
        int i = 0;
        while (lineIter.hasNext()) {
            System.out.println(""+(i++)+" " + lineIter.next());
        }

    }

    public static void main(String[] args) throws IOException {
        long time = System.currentTimeMillis();
        int numCnt = (int) Math.pow(10, 7);
        String inputFilePath = "src\\main\\java\\chapter_1\\problem_sort_phonenumber\\RandomInt_" + numCnt + ".txt";
        String outputFilePath = "src\\main\\java\\chapter_1\\problem_sort_phonenumber\\Sorted_RandomInt_" + numCnt + ".txt";
        SolutionMergeSort s = new SolutionMergeSort();
        s.mergeSort(inputFilePath, outputFilePath);
        time = System.currentTimeMillis() - time;
        System.out.println(time/1000 + " seconds.");
    }
}

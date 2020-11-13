package chapter_2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * What is big file? How big?
 * https://www.java-success.com/processing-large-files-efficiently-java-part-1/ :
 * Q3. What are the different data sizes, and what technologies can be used to process them?
 * A3. In general, data sizes can be classified as shown below.
 *
 * 1) Small size data is < 10 GB in multiple files. It fits in a single machine’s memory when you process them by streaming to conserve memory. Java’s file processing APIs, Apache commons File APIs, Spring batch framework or Java EE 7 batch processing framework can be used.
 *
 * 2) Medium size data is 10 GB to 1 TB in multiple files. Fits in a single machine’s disk space. Process them by splitting or streaming as you won’t be able read all the contents into memory. Spring batch framework or Java EE 7 batch processing framework can be used.
 *
 * 3) Big data is > 1 TB in multiple files. Stored across multiple machines and processed in distributed fashion. E.g. run a map reduce or a Spark job.
 *
 * Processing medium size data? Look at Spring-batch. Spring batch industrial strength tutorial – part1
 *
 * Processing big data? Look at Apache Spark – parallel processing
 */
public class ReadBigFile {

    public static void readUseNio(final String filePath) throws IOException {
        //5 mins (if use wc -l, use about 1min)
        System.out.println("readUseNio");
        Stream<String> lineStream = Files.lines(Paths.get(filePath));
        System.out.println("Total line number: " + lineStream.count());
    }

    public String generateBigLines(final int lineNumber) {
        int cap = lineNumber * 11;
        System.out.println(" cap : " + cap);
        StringBuilder sb = new StringBuilder(cap);
        String oneLine = "0123456789\n";
        for (long i = 0; i < lineNumber; i++) {
            sb.append(oneLine);
        }
        return sb.toString();
    }

    public long computeLineNumber(final String lines) {
        long lineNumber = 0L;
        for (char c : lines.toCharArray()) {
            if (c == '\n') {
                lineNumber++;
            }
        }
        return lineNumber;
    }

    public static void testReadFileUseNio() throws IOException {
        long start = System.currentTimeMillis();
        final String filePath = "src/main/java/chapter_2/input.txt";
        readUseNio(filePath);
        long duration = System.currentTimeMillis() - start;
        System.out.println("Time: " + duration/1000 + " secs");
    }

    public static void main(String[] args) throws IOException {
        ReadBigFile r = new ReadBigFile();
        String lines = r.generateBigLines((int) Math.pow(10, 8));
        //String lines = r.generateBigLines((long) Math.pow(10, 2));
        System.out.println(r.computeLineNumber(lines));
    }


}

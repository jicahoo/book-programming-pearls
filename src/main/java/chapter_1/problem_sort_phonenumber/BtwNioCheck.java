package chapter_1.problem_sort_phonenumber;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Just by the way. Check some behaviors of NIO related class.
 * 1. DirectByteBuffer is using malloc mmap. Check by strace
 * 2. FielChannel.transferTo. zero copy. Calling system call sendfile? Use dtrace.
 */
public class BtwNioCheck {

    public static void checkDirectByteBUffer() {
        System.out.println("checkDirectByteBuffer enter.");
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024 * 1024);
        System.out.println("checkDirectByteBUffer exit");
    }

    public static void zeroCopy() throws IOException {
        String from = "src/main/java/chapter_1/problem_sort_phonenumber/RandomInt_1000.txt";
        String to = "src/main/java/chapter_1/problem_sort_phonenumber/RandomInt_1000.copy";
        try (FileChannel source = new FileInputStream(from).getChannel();
             FileChannel destination = new FileOutputStream(to).getChannel()) {
            source.transferTo(0, source.size(), destination);
        }
    }

    public static void main(String[] args) throws IOException {
        checkDirectByteBUffer();
    }
}

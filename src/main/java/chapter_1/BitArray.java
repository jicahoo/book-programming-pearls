package chapter_1;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * https://en.wikipedia.org/wiki/Bit_array
 * BitArray can express element by index from 0 to n.
 */
class BitArray {
    public BitArray(int n) {
        int byteSize = (int) Math.ceil(n / 8.0);
        bytes = new byte[byteSize];
    }

    public void add(int i) {
        int byteIndex = i / 8;
        int offset = i % 8;
        int mask = 0x80 >> offset;
        bytes[byteIndex] |= mask;
    }

    public void remove(int i) {
        int byteIndex = i / 8;
        int offset = i % 8;
        int mask = ~ (0x80 >> offset);
        bytes[byteIndex] &= mask;
    }

    public boolean contains(int i) {
        int byteIndex = i / 8;
        int offset = i % 8;
        int mask =  (0x80 >> offset);
        return (bytes[byteIndex] & mask) != 0;
    }

    public int count() {
        return 0;
    }

    public BitArray intersection(BitArray right) {
        return null;
    }

    public BitArray difference(BitArray right) {
        return null;
    }

    public BitArray union(BitArray right) {
        return null;
    }

    public BitArray complement(BitArray righ) {
        return null;
    }

    public List<Integer> getSortedElements() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < bytes.length; i++) {
            byte byteValue = bytes[i];
            for (int j = 0; j < 8; j++) {
                int mask = 0x80 >> j;
                if (0 != (byteValue & mask)) {
                    result.add(i * 8 + j);
                }
            }
        }
        return result;
    }

    private byte[] bytes;

    public static void main(String[] args) {
        BitArray bitArray = new BitArray(100);
        bitArray.add(10);
        bitArray.add(0);
        bitArray.add(1);
        bitArray.add(2);
        bitArray.add(100);
        bitArray.getSortedElements().forEach(System.out::println);
        bitArray.remove(2);
        bitArray.getSortedElements().forEach(System.out::println);
    }
}

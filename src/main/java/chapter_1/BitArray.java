package chapter_1;

import java.util.Set;

/**
 * https://en.wikipedia.org/wiki/Bit_array
 */
abstract class BitArray {
    public BitArray(long n) {
    }

    public boolean add(long i) {
        return false;
    }

    public boolean contains(long i) {
        return false;
    }

    public long count() {
        return 0;
    }

    public BitArray intersection(BitArray righ) {
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

    public Set<Long> getElements() {
        return null;
    }
}

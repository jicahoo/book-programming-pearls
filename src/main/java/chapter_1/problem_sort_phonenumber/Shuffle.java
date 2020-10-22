package chapter_1.problem_sort_phonenumber;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Shuffle {

    public void knuthDurstenfeldShuffle(int[] a) {
        int k = a.length - 1;
        Random random = new Random();
        for (int i = a.length; i > 0; i--) {
            int j = random.nextInt(i);
            swap(a, j, k);
            k--;
        }
    }

    private void swap(int[] a, int j, int k) {
        int temp = a[j];
        a[j] = a[k];
        a[k] = temp;
    }

    public void shuffleExample() {
        int[] a = {-1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Shuffle shuffle = new Shuffle();
        shuffle.knuthDurstenfeldShuffle(a);
        for (int i : a) {
            System.out.print(" " + i);
        }
        System.out.println();
    }
    public void writeToFile() throws IOException {
        int n = 1000;
        String outputFilePath = "src\\main\\java\\chapter_1\\problem_sort_phonenumber\\RandomInt_" + n + ".txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
        knuthDurstenfeldShuffle(arr);
        for (int i : arr) {
            writer.write("" + i);
            writer.newLine();
        }
        writer.close();
    }
    public static void main(String[] args) throws IOException {
        Shuffle s = new Shuffle();
        s.writeToFile();
    }
}
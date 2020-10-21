package chapter_1.problem_sort_phonenumber;

import java.util.Random;

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

    public static void main(String[] args) {
        int[] a = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Shuffle shuffle = new Shuffle();
        shuffle.knuthDurstenfeldShuffle(a);
        for (int i : a) {
            System.out.print(" " + i);
        }
        System.out.println();
    }
}

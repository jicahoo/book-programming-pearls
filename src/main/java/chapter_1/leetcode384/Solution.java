package chapter_1.leetcode384;


import java.util.Random;

/**
 * Accepted.After 2 times tries. First time, I try to cache the shuffle result.
 */
class Solution {
    private int[] backup;
    private int[] a;
    private boolean isShuffed;

    public Solution(int[] nums) {

        a = nums;
        backup = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            backup[i] = a[i];
        }
        isShuffed = false;

    }

    /** Resets the array to its original configuration and return it. */
    public int[] reset() {
        for (int i = 0; i < backup.length; i++) {
            a[i] = backup[i];
        }
        return a;
    }

    /** Returns a random shuffling of the array. */
    public int[] shuffle() {
        shuffleInternal(a);
        return a;
    }

    private void shuffleInternal(int[] a) {
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
        Solution obj = new Solution(a);
        int[] param_1 = obj.reset();
        for (int i : param_1) {
            System.out.print(i);
        }
        System.out.println();
        int[] param_2 = obj.shuffle();
        for (int i : param_2) {
            System.out.print(i);
        }
        System.out.println();
    }
}

/**
 * Your Solution object will be instantiated and called as such:
 * Solution obj = new Solution(nums);
 * int[] param_1 = obj.reset();
 * int[] param_2 = obj.shuffle();
 */


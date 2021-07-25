package search;

import java.util.*;

public class BinarySearch {
    // PRE: i < j => a[i] >= a[j]
    // POST: R = i  ∧  a[i-1] > x >= a[i]
    static private int iterativeBinarySearch(List<Integer> a, int x, int left, int right) {
        // INV: a[0]...a[l] > x >= a[r]..a[a.size()]
        while (left < right) {
            // INV: ^ l < r
            int M = (left + (right - left) / 2);
            // l <= m < r

            // PRE: ∧  a[m] > x
            // a[0]..a[m+1] > x >= a[m+2]..a[a.size()]
            // m+1 < R <= r
            // POST: l -> m+1
            if (a.get(M) > x) {
                left = M + 1;
            } else {
                // PRE: ∧  a[m] <= x
                // a[0]..a[m] > x >= a[m+1]..a[a.size()]
                // l < R <= m
                //POST: r -> m
                right = M;
            }
        }
        return right;
    }

    // PRE: i < j => a[i] >= a[j]
    // POST: R = i  ∧  a[i-1] > x >= a[i]
    // INV: a[0]..a[l] > x >= a[r]..a[a.size()]
    static private int recursiveBinarySearch(List<Integer> a, int x, int left, int right) {
        // PRE: ^ l >= r
        // POST: a[l] > x >= a[r] => R = r
        if (left >= right) {
            return right;
        }
        // INV: ^ l < r
        int M = (left + (right - left) / 2);
        // l <= m < r

        // PRE: ^ a[m] > x
        // m+1 < R <= r
        // POST: l -> m+1
        if (a.get(M) > x) {
            return recursiveBinarySearch(a, x, M + 1, right);
        }
        // PRE: ^ a[m] <= x
        // l < R <= m
        // POST: r -> m
        return recursiveBinarySearch(a, x, left, M);
    }

    // PRE: args.length > 0
    // POST: R = iterativeResult = recursiveResult
    public static void main(String[] args) throws Exception {
        // PRE: ^ args.length <= 1
        // POST: R = 0
        if (args.length <= 1) {
            System.out.println("0");
            return;
        }

        int x = 0;
        List<Integer> a = new ArrayList<>();
        try {
            // INV: 1 <= i <= args.length
            x = Integer.parseInt(args[0]);
            for (int i = 1; i < args.length; ++i) {
                a.add(Integer.parseInt(args[i]));
            }
        } catch (NumberFormatException e) {
            System.out.println("Incorrect input!");
        }

        int iterativeResult = iterativeBinarySearch(a, x, 0, a.size());
        int recursiveResult = recursiveBinarySearch(a, x, 0, a.size());

        System.out.println((iterativeResult == recursiveResult) ? iterativeResult : "Error!");
    }
}
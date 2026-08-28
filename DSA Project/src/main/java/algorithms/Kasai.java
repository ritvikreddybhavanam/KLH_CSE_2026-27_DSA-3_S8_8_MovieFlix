package algorithms;

import java.util.Arrays;

public class Kasai {

    public static int[] buildSuffixArray(String text) {
        int n = text.length();
        Integer[] suffixes = new Integer[n];

        for (int i = 0; i < n; i++) {
            suffixes[i] = i;
        }

        Arrays.sort(suffixes, (a, b) ->
                text.substring(a).compareTo(text.substring(b)));

        int[] suffixArray = new int[n];

        for (int i = 0; i < n; i++) {
            suffixArray[i] = suffixes[i];
        }

        return suffixArray;
    }

    public static int[] buildLCP(String text, int[] suffixArray) {
        int n = text.length();
        int[] lcp = new int[n];
        int[] rank = new int[n];

        for (int i = 0; i < n; i++) {
            rank[suffixArray[i]] = i;
        }

        int k = 0;

        for (int i = 0; i < n; i++) {
            if (rank[i] == n - 1) {
                k = 0;
                continue;
            }

            int j = suffixArray[rank[i] + 1];

            while (i + k < n && j + k < n &&
                    text.charAt(i + k) == text.charAt(j + k)) {
                k++;
            }

            lcp[rank[i]] = k;

            if (k > 0) {
                k--;
            }
        }

        return lcp;
    }

    public static boolean search(String pattern, String text) {
        if (pattern == null || text == null) {
            return false;
        }

        pattern = pattern.toLowerCase();
        text = text.toLowerCase();

        if (pattern.isEmpty()) {
            return true;
        }

        if (pattern.length() > text.length()) {
            return false;
        }

        int[] suffixArray = buildSuffixArray(text);
        int left = 0;
        int right = suffixArray.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            int suffixIndex = suffixArray[mid];
            String suffix = text.substring(suffixIndex);

            if (suffix.startsWith(pattern)) {
                return true;
            }

            if (suffix.compareTo(pattern) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return false;
    }
}
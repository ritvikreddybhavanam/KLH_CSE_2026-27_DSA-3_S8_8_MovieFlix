package algorithms;

public class KMP {

    private KMP() {
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

        int[] lps = buildLPS(pattern);
        int i = 0;
        int j = 0;

        while (i < text.length()) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;

                if (j == pattern.length()) {
                    return true;
                }
            } else {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        return false;
    }

    private static int[] buildLPS(String pattern) {
        int[] lps = new int[pattern.length()];
        int length = 0;
        int i = 1;

        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }
}
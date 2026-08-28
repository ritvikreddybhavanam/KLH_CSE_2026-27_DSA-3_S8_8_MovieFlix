package algorithms;

public class ZFunction {

    private ZFunction() {
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

        String combined = pattern + "$" + text;
        int[] z = calculateZ(combined);

        for (int value : z) {
            if (value == pattern.length()) {
                return true;
            }
        }

        return false;
    }

    public static int[] calculateZ(String text) {
        int n = text.length();
        int[] z = new int[n];

        int left = 0;
        int right = 0;

        for (int i = 1; i < n; i++) {
            if (i <= right) {
                z[i] = Math.min(right - i + 1, z[i - left]);
            }

            while (i + z[i] < n &&
                    text.charAt(z[i]) == text.charAt(i + z[i])) {
                z[i]++;
            }

            if (i + z[i] - 1 > right) {
                left = i;
                right = i + z[i] - 1;
            }
        }

        return z;
    }
}
package algorithms;

public class RabinKarp {

    private static final int PRIME = 101;

    private RabinKarp() {
    }

    public static boolean search(String pattern, String text) {
        if (pattern == null || text == null) {
            return false;
        }

        pattern = pattern.toLowerCase();
        text = text.toLowerCase();

        int m = pattern.length();
        int n = text.length();

        if (m == 0) {
            return true;
        }

        if (m > n) {
            return false;
        }

        int patternHash = 0;
        int textHash = 0;
        int h = 1;

        for (int i = 0; i < m - 1; i++) {
            h = (h * 256) % PRIME;
        }

        for (int i = 0; i < m; i++) {
            patternHash = (256 * patternHash + pattern.charAt(i)) % PRIME;
            textHash = (256 * textHash + text.charAt(i)) % PRIME;
        }

        for (int i = 0; i <= n - m; i++) {
            if (patternHash == textHash) {
                boolean match = true;

                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    return true;
                }
            }

            if (i < n - m) {
                textHash = (256 * (textHash - text.charAt(i) * h) + text.charAt(i + m)) % PRIME;

                if (textHash < 0) {
                    textHash += PRIME;
                }
            }
        }

        return false;
    }
}
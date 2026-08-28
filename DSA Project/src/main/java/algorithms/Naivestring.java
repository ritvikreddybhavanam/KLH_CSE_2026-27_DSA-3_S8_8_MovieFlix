package algorithms;

public class Naivestring {

    private Naivestring() {
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

        for (int i = 0; i <= text.length() - pattern.length(); i++) {
            int j = 0;

            while (j < pattern.length() && text.charAt(i + j) == pattern.charAt(j)) {
                j++;
            }

            if (j == pattern.length()) {
                return true;
            }
        }

        return false;
    }
}
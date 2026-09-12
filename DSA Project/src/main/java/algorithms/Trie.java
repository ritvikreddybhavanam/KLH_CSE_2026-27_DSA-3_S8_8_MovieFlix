package algorithms;

import model.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {

    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        List<Movie> movies = new ArrayList<>();
        boolean isEndOfWord;
    }

    private final TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public Trie(List<Movie> movies) {
        root = new TrieNode();

        if (movies != null) {
            for (Movie movie : movies) {
                if (movie != null && movie.getTitle() != null && !movie.getTitle().isBlank()) {
                    insert(movie.getTitle(), movie);
                }
            }
        }
    }

    public void insert(String word, Movie movie) {
        if (word == null || word.isBlank() || movie == null) {
            return;
        }

        String normalized = word.toLowerCase().trim();
        TrieNode current = root;

        for (char c : normalized.toCharArray()) {
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c);
        }

        current.isEndOfWord = true;

        if (!current.movies.contains(movie)) {
            current.movies.add(movie);
        }
    }

    public List<Movie> searchPrefix(String prefix) {
        List<Movie> results = new ArrayList<>();

        if (prefix == null || prefix.isBlank()) {
            return results;
        }

        String normalized = prefix.toLowerCase().trim();
        TrieNode current = root;

        for (char c : normalized.toCharArray()) {
            current = current.children.get(c);

            if (current == null) {
                return results;
            }
        }

        collectMovies(current, results);

        return results;
    }

    private void collectMovies(TrieNode node, List<Movie> results) {
        if (node == null) {
            return;
        }

        for (Movie movie : node.movies) {
            if (!results.contains(movie)) {
                results.add(movie);
            }
        }

        for (TrieNode child : node.children.values()) {
            collectMovies(child, results);
        }
    }

    public List<String> getSuggestions(String prefix, int limit) {
        List<String> suggestions = new ArrayList<>();

        if (prefix == null || prefix.isBlank() || limit <= 0) {
            return suggestions;
        }

        String normalized = prefix.toLowerCase().trim();
        TrieNode current = root;

        for (char c : normalized.toCharArray()) {
            current = current.children.get(c);

            if (current == null) {
                return suggestions;
            }
        }

        collectTitles(current, suggestions, limit);

        return suggestions;
    }

    private void collectTitles(
            TrieNode node,
            List<String> suggestions,
            int limit
    ) {
        if (node == null || suggestions.size() >= limit) {
            return;
        }

        for (Movie movie : node.movies) {
            if (movie != null &&
                    movie.getTitle() != null &&
                    !suggestions.contains(movie.getTitle())) {

                suggestions.add(movie.getTitle());

                if (suggestions.size() >= limit) {
                    return;
                }
            }
        }

        for (TrieNode child : node.children.values()) {
            collectTitles(child, suggestions, limit);

            if (suggestions.size() >= limit) {
                return;
            }
        }
    }

    public boolean contains(String word) {
        if (word == null || word.isBlank()) {
            return false;
        }

        String normalized = word.toLowerCase().trim();
        TrieNode current = root;

        for (char c : normalized.toCharArray()) {
            current = current.children.get(c);

            if (current == null) {
                return false;
            }
        }

        return current.isEndOfWord;
    }

    public boolean startsWith(String prefix) {
        if (prefix == null || prefix.isBlank()) {
            return false;
        }

        String normalized = prefix.toLowerCase().trim();
        TrieNode current = root;

        for (char c : normalized.toCharArray()) {
            current = current.children.get(c);

            if (current == null) {
                return false;
            }
        }

        return true;
    }
}
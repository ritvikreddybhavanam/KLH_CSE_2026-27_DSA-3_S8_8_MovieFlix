package algorithms;

import model.Genre;
import model.Keyword;
import model.Movie;

import java.util.*;

public class DocumentSimilarity {

    private DocumentSimilarity() {
    }

    public static List<Movie> getRecommendations(Movie selectedMovie, List<Movie> movies, int limit) {
        List<Movie> recommendations = new ArrayList<>();

        if (selectedMovie == null || movies == null || movies.isEmpty()) {
            return recommendations;
        }

        List<MovieScore> scores = new ArrayList<>();

        for (Movie movie : movies) {
            if (movie == null || movie == selectedMovie) {
                continue;
            }

            double similarity = calculateSimilarity(selectedMovie, movie);
            scores.add(new MovieScore(movie, similarity));
        }

        scores.sort((a, b) -> Double.compare(b.score, a.score));

        int count = Math.min(limit, scores.size());

        for (int i = 0; i < count; i++) {
            recommendations.add(scores.get(i).movie);
        }

        return recommendations;
    }

    public static double calculateSimilarity(Movie movie1, Movie movie2) {
        Set<String> document1 = createDocument(movie1);
        Set<String> document2 = createDocument(movie2);

        if (document1.isEmpty() && document2.isEmpty()) {
            return 0.0;
        }

        Set<String> intersection = new HashSet<>(document1);
        intersection.retainAll(document2);

        Set<String> union = new HashSet<>(document1);
        union.addAll(document2);

        if (union.isEmpty()) {
            return 0.0;
        }

        return (double) intersection.size() / union.size();
    }

    private static Set<String> createDocument(Movie movie) {
        Set<String> document = new HashSet<>();

        if (movie == null) {
            return document;
        }

        addText(document, movie.getOverview());
        addText(document, movie.getTagline());

        if (movie.getGenres() != null) {
            for (Genre genre : movie.getGenres()) {
                if (genre != null && genre.getName() != null) {
                    addText(document, genre.getName());
                }
            }
        }

        if (movie.getKeywords() != null) {
            for (Keyword keyword : movie.getKeywords()) {
                if (keyword != null && keyword.getName() != null) {
                    addText(document, keyword.getName());
                }
            }
        }

        return document;
    }

    private static void addText(Set<String> document, String text) {
        if (text == null || text.isBlank()) {
            return;
        }

        text = text.toLowerCase().replaceAll("[^a-z0-9 ]", " ");
        String[] words = text.split("\\s+");

        for (String word : words) {
            if (!word.isBlank() && !isStopWord(word)) {
                document.add(word);
            }
        }
    }

    private static boolean isStopWord(String word) {
        return word.equals("the")
                || word.equals("a")
                || word.equals("an")
                || word.equals("and")
                || word.equals("or")
                || word.equals("of")
                || word.equals("to")
                || word.equals("in")
                || word.equals("on")
                || word.equals("is")
                || word.equals("are")
                || word.equals("was")
                || word.equals("were")
                || word.equals("for")
                || word.equals("with")
                || word.equals("from")
                || word.equals("by")
                || word.equals("at")
                || word.equals("as")
                || word.equals("this")
                || word.equals("that")
                || word.equals("into")
                || word.equals("their")
                || word.equals("his")
                || word.equals("her");
    }

    private static class MovieScore {
        Movie movie;
        double score;

        MovieScore(Movie movie, double score) {
            this.movie = movie;
            this.score = score;
        }
    }
}
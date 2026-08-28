package algorithms;

import model.Keyword;
import model.Movie;
import model.SpokenLanguage;

import java.util.ArrayList;
import java.util.List;

public class MovieSearchAlgorithms {

    private MovieSearchAlgorithms() {
    }

    public static List<Movie> searchByTitle(List<Movie> movies, String query) {
        List<Movie> result = new ArrayList<>();

        if (movies == null || query == null) {
            return result;
        }

        query = query.trim().toLowerCase();

        for (Movie movie : movies) {
            if (movie == null || movie.getTitle() == null) {
                continue;
            }

            if (KMP.search(query, movie.getTitle())) {
                result.add(movie);
            }
        }

        return result;
    }

    public static List<Movie> searchByKeyword(List<Movie> movies, String query) {
        List<Movie> result = new ArrayList<>();

        if (movies == null || query == null) {
            return result;
        }

        query = query.trim().toLowerCase();

        for (Movie movie : movies) {
            if (movie == null || movie.getKeywords() == null) {
                continue;
            }

            boolean found = false;

            for (Keyword keyword : movie.getKeywords()) {
                if (keyword == null || keyword.getName() == null) {
                    continue;
                }

                if (Naivestring.search(query, keyword.getName())) {
                    found = true;
                    break;
                }
            }

            if (found) {
                result.add(movie);
            }
        }

        return result;
    }

    public static List<Movie> searchByTagline(List<Movie> movies, String query) {
        List<Movie> result = new ArrayList<>();

        if (movies == null || query == null) {
            return result;
        }

        query = query.trim().toLowerCase();

        for (Movie movie : movies) {
            if (movie == null || movie.getTagline() == null) {
                continue;
            }

            if (RabinKarp.search(query, movie.getTagline())) {
                result.add(movie);
            }
        }

        return result;
    }

    public static List<Movie> searchByLanguage(List<Movie> movies, String query) {
        List<Movie> result = new ArrayList<>();

        if (movies == null || query == null) {
            return result;
        }

        query = query.trim().toLowerCase();

        for (Movie movie : movies) {
            if (movie == null || movie.getSpokenLanguages() == null) {
                continue;
            }

            boolean found = false;

            for (SpokenLanguage language : movie.getSpokenLanguages()) {
                if (language == null || language.getName() == null) {
                    continue;
                }

                if (ZFunction.search(query, language.getName())) {
                    found = true;
                    break;
                }
            }

            if (found) {
                result.add(movie);
            }
        }

        return result;
    }

    public static Movie findClosestMovie(List<Movie> movies, String query) {
        if (movies == null || movies.isEmpty() || query == null || query.trim().isEmpty()) {
            return null;
        }

        query = query.trim().toLowerCase();

        Movie bestMovie = null;
        int bestDistance = Integer.MAX_VALUE;

        for (Movie movie : movies) {
            if (movie == null || movie.getTitle() == null) {
                continue;
            }

            int distance = EditDistance.calculate(query, movie.getTitle());

            if (distance < bestDistance) {
                bestDistance = distance;
                bestMovie = movie;
            }
        }

        return bestMovie;
    }

    public static int getDistance(String query, Movie movie) {
        if (movie == null || movie.getTitle() == null) {
            return Integer.MAX_VALUE;
        }

        return EditDistance.calculate(query, movie.getTitle());
    }
}
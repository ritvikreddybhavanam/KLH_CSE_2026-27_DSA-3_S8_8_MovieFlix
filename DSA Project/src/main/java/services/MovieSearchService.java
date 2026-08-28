package services;

import algorithms.KMP;
import model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieSearchService {

    public static List<Movie> searchByTitle(List<Movie> movies, String query) {
        List<Movie> results = new ArrayList<>();

        if (movies == null || query == null || query.trim().isEmpty()) {
            return results;
        }

        query = query.trim().toLowerCase();

        for (Movie movie : movies) {
            if (movie == null || movie.getTitle() == null) {
                continue;
            }

            String title = movie.getTitle().toLowerCase();

            if (KMP.search(query, title)) {
                results.add(movie);
            }
        }

        return results;
    }
}
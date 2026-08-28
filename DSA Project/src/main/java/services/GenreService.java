package services;

import data.MovieDataLoader;
import model.Genre;
import model.Movie;
import variables.Variables;

import java.util.ArrayList;
import java.util.List;

public class GenreService {

    public static List<String> getGenres() {
        List<Movie> movies = MovieDataLoader.loadMovies(Variables.filePath);
        List<String> genres = new ArrayList<>();

        if (movies == null) {
            return genres;
        }

        for (Movie movie : movies) {
            if (movie == null || movie.getGenres() == null) {
                continue;
            }

            for (Genre genre : movie.getGenres()) {
                if (genre != null && genre.getName() != null && !genres.contains(genre.getName())) {
                    genres.add(genre.getName());
                }
            }
        }

        return genres;
    }
}
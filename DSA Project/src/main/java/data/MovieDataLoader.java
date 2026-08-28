package data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Movie;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MovieDataLoader {

    public static List<Movie> loadMovies(String filePath) {
        List<Movie> movies = new ArrayList<>();
        File file = filePath != null
                ? new File(filePath)
                : new File("data/movie_with_posters.json");

        if (!file.exists()) {
            File[] fallbacks = {
                    new File("data/movie_with_posters.json"),
                    new File("data/movies_dsa.json"),
                    new File("data/movie.json"),
                    new File("../data/movie_with_posters.json"),
                    new File("../data/movies_dsa.json")
            };

            for (File fallback : fallbacks) {
                if (fallback.exists()) {
                    file = fallback;
                    break;
                }
            }
        }

        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type movieListType = new TypeToken<List<Movie>>() {}.getType();

            movies = gson.fromJson(reader, movieListType);

            if (movies == null) {
                movies = new ArrayList<>();
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
        }

        return movies;
    }
}
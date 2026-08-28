package ui;

import data.MovieDataLoader;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Movie;
import variables.Variables;
import java.util.List;

public class MovieFlixApp extends Application {
    private static List<Movie> movies;

    public static void setMovies(List<Movie> movieList) {
        movies = movieList;
    }

    public static List<Movie> getMovies() {
        if (movies == null || movies.isEmpty()) {
            movies = MovieDataLoader.loadMovies(Variables.filePath);
        }
        return movies;
    }

    @Override
    public void start(Stage stage) {
        if (movies == null || movies.isEmpty()) {
            movies = MovieDataLoader.loadMovies(Variables.filePath);
        }

        LoginPage loginPage = new LoginPage(movies);
        loginPage.show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
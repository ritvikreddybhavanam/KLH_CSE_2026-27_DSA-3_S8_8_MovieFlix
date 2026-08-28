import data.MovieDataLoader;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Movie;
import ui.LoginPage;
import variables.Variables;
import java.util.List;

public class Main extends Application {
    private List<Movie> movies;

    @Override
    public void start(Stage stage) {
        System.out.println("========================================");
        System.out.println("          MOVIEFLIX");
        System.out.println("========================================");
        System.out.println("Loading movie data...");

        try {
            movies = MovieDataLoader.loadMovies(Variables.filePath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load movie data.");
            movies = new java.util.ArrayList<>();
        }

        System.out.println("Movies loaded: " + movies.size());

        if (!movies.isEmpty()) {
            Movie firstMovie = movies.get(0);

            if (firstMovie != null) {
                System.out.println("First movie: " + firstMovie.getTitle());
                System.out.println("Rating: " + firstMovie.getVoteAverage());
            }
        }

        System.out.println("========================================");

        LoginPage loginPage = new LoginPage(movies);
        loginPage.show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
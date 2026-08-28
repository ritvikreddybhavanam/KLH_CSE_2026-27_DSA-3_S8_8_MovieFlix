package display;

import algorithms.KMP;
import model.Genre;
import model.Movie;
import services.GenreService;

import java.util.List;
import java.util.Scanner;

public class MenuDisplay {

    public static void showMainMenu() {
        System.out.println();
        System.out.println("--------------- MOVIEFLIX ---------------");
        System.out.println("1. Search Movie by Title");
        System.out.println("2. Browse Movies by Genre");
        System.out.println("3. View Available Genres");
        System.out.println("4. Exit");
        System.out.println("------------------------------------------");
        System.out.print("Enter your choice: ");
    }

    public static void searchByTitle(Scanner sc, List<Movie> movies) {
        System.out.println();
        System.out.println("========== SEARCH BY TITLE ==========");
        System.out.print("Enter movie title: ");
        String title = sc.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("Title cannot be empty.");
            return;
        }

        boolean found = false;

        for (Movie movie : movies) {
            if (movie == null || movie.getTitle() == null) {
                continue;
            }

            String pattern = title.toLowerCase();
            String text = movie.getTitle().toLowerCase();

            if (KMP.search(pattern, text)) {
                MovieDisplay.displayMovie(movie);
                found = true;
            }
        }

        if (!found) {
            System.out.println();
            System.out.println("No movies found for: " + title);
        }
    }

    public static void searchByGenre(Scanner sc, List<Movie> movies) {
        System.out.println();
        System.out.println("========== SEARCH BY GENRE ==========");
        System.out.print("Enter genre: ");
        String genre = sc.nextLine().trim();

        if (genre.isEmpty()) {
            System.out.println("Genre cannot be empty.");
            return;
        }

        boolean found = false;

        for (Movie movie : movies) {
            if (movie == null || movie.getGenres() == null) {
                continue;
            }

            for (Genre movieGenre : movie.getGenres()) {
                if (movieGenre == null || movieGenre.getName() == null) {
                    continue;
                }

                if (movieGenre.getName().equalsIgnoreCase(genre)) {
                    MovieDisplay.displayMovie(movie);
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            System.out.println();
            System.out.println("No movies found for genre: " + genre);
        }
    }

    public static void showGenres() {
        System.out.println();
        System.out.println("========== AVAILABLE GENRES ==========");

        List<String> genres = GenreService.getGenres();

        if (genres == null || genres.isEmpty()) {
            System.out.println("No genres available.");
            return;
        }

        for (int i = 0; i < genres.size(); i++) {
            System.out.println((i + 1) + ". " + genres.get(i));
        }

        System.out.println("--------------------------------------");
        System.out.println("Total Genres: " + genres.size());
    }
}
package display;

import model.Movie;

public class MovieDisplay {

    public static void displayMovie(Movie movie) {
        if (movie == null) {
            System.out.println("Movie not found.");
            return;
        }

        System.out.println();
        System.out.println("================================");
        System.out.println("          MOVIE DETAILS");
        System.out.println("================================");

        System.out.println("ID: " + movie.getId());
        System.out.println("Title: " + movie.getTitle());
        System.out.println("Original Title: " + movie.getOriginalTitle());
        System.out.println("Overview: " + movie.getOverview());
        System.out.println("Genres: " + movie.getGenres());
        System.out.println("Keywords: " + movie.getKeywords());
        System.out.println("Language: " + movie.getOriginalLanguage());
        System.out.println("Release Date: " + movie.getReleaseDate());
        System.out.println("Runtime: " + (movie.getRuntime() != null ? movie.getRuntime() + " minutes" : "N/A"));
        System.out.println("Popularity: " + movie.getPopularity());
        System.out.println("Rating: " + movie.getVoteAverage());
        System.out.println("Vote Count: " + movie.getVoteCount());
        System.out.println("Budget: $" + movie.getBudget());
        System.out.println("Revenue: $" + movie.getRevenue());
        System.out.println("Status: " + movie.getStatus());
        System.out.println("Tagline: " + movie.getTagline());
        System.out.println("Production Companies: " + movie.getProductionCompanies());
        System.out.println("Production Countries: " + movie.getProductionCountries());
        System.out.println("Spoken Languages: " + movie.getSpokenLanguages());

        System.out.println("================================");
    }
}
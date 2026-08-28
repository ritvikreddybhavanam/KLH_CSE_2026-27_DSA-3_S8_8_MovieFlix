package ui;

import algorithms.DocumentSimilarity;
import algorithms.MovieSearchAlgorithms;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import model.Genre;
import model.Movie;

import java.util.List;

public class SearchPage {

    private final List<Movie> movies;

    private static final String BACKGROUND = "#F9F9F7";
    private static final String WHITE = "#FFFFFF";

    private static final String PRIMARY = "#FF6B35";
    private static final String PRIMARY_DARK = "#AB3500";

    private static final String TEXT = "#1A1C1B";
    private static final String SECONDARY = "#594139";
    private static final String BORDER = "#E1BFB5";

    private static final String LIGHT_GRAY = "#F4F4F2";
    private static final String MEDIUM_GRAY = "#E8E8E6";

    private FlowPane movieGrid;
    private FlowPane recommendationGrid;

    private Label resultLabel;
    private Label recommendationLabel;

    private TextField searchField;
    private ComboBox<String> searchType;

    private int displayedMovies = 20;

    private static final int RECOMMENDATION_LIMIT = 5;

    public SearchPage(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            this.movies = data.MovieDataLoader.loadMovies(
                    variables.Variables.filePath
            );
        } else {
            this.movies = movies;
        }
    }

    public SearchPage() {
        this(null);
    }

    public void show(Stage stage) {

        BorderPane root = createPage(stage);

        Scene scene = new Scene(root, 1280, 720);

        stage.setTitle("MovieFlix - Search");
        stage.setScene(scene);
        stage.show();
    }

    private BorderPane createPage(Stage stage) {

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: " + BACKGROUND + ";"
        );

        root.setTop(createNavbar(stage));

        VBox content = new VBox(24);

        content.setPadding(
                new Insets(48, 64, 60, 64)
        );

        Label title = new Label("Find Your Next Movie");

        title.setStyle(
                "-fx-font-family: 'Inter';" +
                        "-fx-font-size: 46px;" +
                        "-fx-font-weight: 700;" +
                        "-fx-text-fill: " + TEXT + ";"
        );

        Label subtitle = new Label(
                "Search through the MovieFlix collection."
        );

        subtitle.setStyle(
                "-fx-font-family: 'Inter';" +
                        "-fx-font-size: 18px;" +
                        "-fx-text-fill: " + SECONDARY + ";"
        );

        HBox searchBox = new HBox(10);

        searchBox.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();

        searchField.setPromptText("Search movies...");
        searchField.setPrefHeight(48);

        searchField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 0 16;" +
                        "-fx-font-size: 15px;"
        );

        HBox.setHgrow(searchField, Priority.ALWAYS);

        searchType = new ComboBox<>();

        searchType.getItems().addAll(
                "Movie Title",
                "Keyword",
                "Tagline",
                "Spoken Languages"
        );

        searchType.setValue("Movie Title");
        searchType.setPrefHeight(48);
        searchType.setPrefWidth(190);

        searchType.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-size: 14px;"
        );

        Button searchButton = new Button("Search");

        searchButton.setPrefHeight(48);
        searchButton.setPrefWidth(120);

        searchButton.setStyle(
                "-fx-background-color: " + PRIMARY + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );

        searchButton.setOnAction(e -> performSearch());

        searchField.setOnAction(e -> performSearch());

        searchBox.getChildren().addAll(
                searchField,
                searchType,
                searchButton
        );

        resultLabel = new Label();

        resultLabel.setStyle(
                "-fx-font-family: 'Inter';" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-text-fill: " + TEXT + ";"
        );

        movieGrid = new FlowPane();

        movieGrid.setHgap(24);
        movieGrid.setVgap(24);

        movieGrid.setPadding(
                new Insets(10, 0, 20, 0)
        );

        recommendationLabel = new Label(
                "Recommended Movies"
        );

        recommendationLabel.setStyle(
                "-fx-font-family: 'Inter';" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-text-fill: " + TEXT + ";"
        );

        recommendationLabel.setVisible(false);
        recommendationLabel.setManaged(false);

        recommendationGrid = new FlowPane();

        recommendationGrid.setHgap(24);
        recommendationGrid.setVgap(24);

        recommendationGrid.setPadding(
                new Insets(10, 0, 30, 0)
        );

        recommendationGrid.setVisible(false);
        recommendationGrid.setManaged(false);

        content.getChildren().addAll(
                title,
                subtitle,
                searchBox,
                resultLabel,
                movieGrid,
                recommendationLabel,
                recommendationGrid
        );

        ScrollPane scrollPane = new ScrollPane(content);

        scrollPane.setFitToWidth(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;"
        );

        root.setCenter(scrollPane);

        displayMovies(movies);

        return root;
    }

    private void performSearch() {

        String query = searchField.getText().trim();

        if (query.isEmpty()) {
            displayMovies(movies);
            hideRecommendations();
            return;
        }

        displayedMovies = 20;

        String type = searchType.getValue();

        if (type == null) {
            type = "Movie Title";
        }

        switch (type) {

            case "Movie Title":
                searchByTitle(query);
                break;

            case "Keyword":
                searchByKeyword(query);
                break;

            case "Tagline":
                searchByTagline(query);
                break;

            case "Spoken Languages":
                searchByLanguage(query);
                break;

            default:
                displayMovies(movies);
        }
    }

    private void searchByTitle(String query) {

        List<Movie> result =
                MovieSearchAlgorithms.searchByTitle(
                        movies,
                        query
                );

        if (!result.isEmpty()) {

            resultLabel.setText(
                    "Movie Title Search (" +
                            result.size() +
                            ")"
            );

            displayMovieCards(result);

            return;
        }

        Movie suggestion =
                MovieSearchAlgorithms.findClosestMovie(
                        movies,
                        query
                );

        movieGrid.getChildren().clear();

        if (suggestion == null) {

            resultLabel.setText(
                    "No movie found"
            );

            hideRecommendations();

            return;
        }

        resultLabel.setText(
                "Did You Mean? " +
                        suggestion.getTitle()
        );

        movieGrid.getChildren().add(
                createMovieCard(suggestion)
        );

        showRecommendations(suggestion);
    }

    private void searchByKeyword(String query) {

        List<Movie> result =
                MovieSearchAlgorithms.searchByKeyword(
                        movies,
                        query
                );

        resultLabel.setText(
                "Keyword Search (" +
                        result.size() +
                        ")"
        );

        displaySearchResults(result);
    }

    private void searchByTagline(String query) {

        List<Movie> result =
                MovieSearchAlgorithms.searchByTagline(
                        movies,
                        query
                );

        resultLabel.setText(
                "Tagline Search (" +
                        result.size() +
                        ")"
        );

        displaySearchResults(result);
    }

    private void searchByLanguage(String query) {

        List<Movie> result =
                MovieSearchAlgorithms.searchByLanguage(
                        movies,
                        query
                );

        resultLabel.setText(
                "Spoken Language Search (" +
                        result.size() +
                        ")"
        );

        displaySearchResults(result);
    }

    private void displaySearchResults(List<Movie> result) {

        hideRecommendations();

        movieGrid.getChildren().clear();

        if (result == null || result.isEmpty()) {

            resultLabel.setText(
                    resultLabel.getText() +
                            " — No movies found"
            );

            return;
        }

        displayMovieCards(result);
    }

    private void displayMovies(List<Movie> movieList) {

        hideRecommendations();

        movieGrid.getChildren().clear();

        if (movieList == null || movieList.isEmpty()) {

            resultLabel.setText(
                    "No movies found"
            );

            return;
        }

        resultLabel.setText(
                "Movies (" +
                        movieList.size() +
                        ")"
        );

        displayMovieCards(movieList);
    }

    private void displayMovieCards(List<Movie> movieList) {

        movieGrid.getChildren().clear();

        if (movieList == null || movieList.isEmpty()) {
            return;
        }

        int end = Math.min(
                displayedMovies,
                movieList.size()
        );

        for (int i = 0; i < end; i++) {

            Movie movie = movieList.get(i);

            if (movie != null) {

                movieGrid.getChildren().add(
                        createMovieCard(movie)
                );
            }
        }
    }

    private void showRecommendations(Movie selectedMovie) {

        if (selectedMovie == null) {
            hideRecommendations();
            return;
        }

        List<Movie> recommendations =
                DocumentSimilarity.getRecommendations(
                        selectedMovie,
                        movies,
                        RECOMMENDATION_LIMIT
                );

        recommendationGrid.getChildren().clear();

        if (recommendations == null ||
                recommendations.isEmpty()) {

            hideRecommendations();
            return;
        }

        recommendationLabel.setText(
                "Recommended for " +
                        selectedMovie.getTitle()
        );

        recommendationLabel.setVisible(true);
        recommendationLabel.setManaged(true);

        recommendationGrid.setVisible(true);
        recommendationGrid.setManaged(true);

        for (Movie movie : recommendations) {

            if (movie != null) {

                recommendationGrid
                        .getChildren()
                        .add(
                                createMovieCard(movie)
                        );
            }
        }
    }

    private void hideRecommendations() {

        if (recommendationGrid != null) {

            recommendationGrid.getChildren().clear();

            recommendationGrid.setVisible(false);
            recommendationGrid.setManaged(false);
        }

        if (recommendationLabel != null) {

            recommendationLabel.setVisible(false);
            recommendationLabel.setManaged(false);
        }
    }

    private VBox createMovieCard(Movie movie) {

        VBox card = new VBox();

        card.setPrefWidth(220);
        card.setMaxWidth(220);

        card.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: rgba(225,191,181,0.25);" +
                        "-fx-border-radius: 12;" +
                        "-fx-effect: dropshadow(" +
                        "gaussian," +
                        "rgba(0,0,0,0.04)," +
                        "20,0,0,4);" +
                        "-fx-cursor: hand;"
        );

        StackPane posterBox = new StackPane();

        posterBox.setPrefSize(220, 330);

        String posterUrl = getPosterUrl(movie);

        if (posterUrl != null && !posterUrl.isBlank()) {

            try {

                Image image = new Image(
                        posterUrl,
                        220,
                        330,
                        false,
                        true,
                        true
                );

                ImageView imageView = new ImageView(image);

                imageView.setFitWidth(220);
                imageView.setFitHeight(330);
                imageView.setPreserveRatio(false);

                posterBox.getChildren().add(imageView);

            } catch (Exception e) {

                posterBox.getChildren().add(
                        createPlaceholder(movie)
                );
            }

        } else {

            posterBox.getChildren().add(
                    createPlaceholder(movie)
            );
        }

        VBox info = new VBox(6);

        info.setPadding(new Insets(14));

        String titleText = movie.getTitle();

        if (titleText == null || titleText.isBlank()) {
            titleText = "Unknown Movie";
        }

        Label title = new Label(titleText);

        title.setMaxWidth(190);
        title.setEllipsisString("...");

        title.setStyle(
                "-fx-font-family: 'Inter';" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-text-fill: " + TEXT + ";"
        );

        HBox metadata = new HBox();

        Label year = new Label(
                getYear(movie)
        );

        year.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: " + SECONDARY + ";"
        );

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Label genre = new Label(
                getGenreText(movie)
        );

        genre.setPadding(
                new Insets(3, 8, 3, 8)
        );

        genre.setStyle(
                "-fx-background-color: " + MEDIUM_GRAY + ";" +
                        "-fx-background-radius: 4;" +
                        "-fx-font-size: 12px;" +
                        "-fx-text-fill: " + SECONDARY + ";"
        );

        metadata.getChildren().addAll(
                year,
                spacer,
                genre
        );

        Label rating = new Label(
                "★ " +
                        String.format(
                                "%.1f",
                                movie.getVoteAverage()
                        )
        );

        rating.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + PRIMARY_DARK + ";"
        );

        info.getChildren().addAll(
                title,
                metadata,
                rating
        );

        card.getChildren().addAll(
                posterBox,
                info
        );

        card.setOnMouseClicked(
                e -> openMovieDetails(movie)
        );

        card.setOnMouseEntered(
                e -> {
                    card.setScaleX(1.02);
                    card.setScaleY(1.02);
                }
        );

        card.setOnMouseExited(
                e -> {
                    card.setScaleX(1);
                    card.setScaleY(1);
                }
        );

        return card;
    }

    private StackPane createPlaceholder(Movie movie) {

        StackPane pane = new StackPane();

        pane.setPrefSize(220, 330);

        pane.setStyle(
                "-fx-background-color: " +
                        LIGHT_GRAY + ";"
        );

        String title = movie.getTitle();

        if (title == null || title.isBlank()) {
            title = "Unknown Movie";
        }

        Label label = new Label(title);

        label.setWrapText(true);
        label.setMaxWidth(180);
        label.setAlignment(Pos.CENTER);

        label.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + SECONDARY + ";"
        );

        pane.getChildren().add(label);

        return pane;
    }

    private void openMovieDetails(Movie movie) {

        if (movie == null) {
            return;
        }

        showRecommendations(movie);

        Stage stage =
                (Stage) movieGrid
                        .getScene()
                        .getWindow();

        MovieDetailsPage details =
                new MovieDetailsPage(
                        movie,
                        movies,
                        () -> showHome(stage),
                        () -> new SearchPage(movies).show(stage),
                        () -> showGenres(stage),
                        selectedMovie ->
                                new MovieDetailsPage(
                                        selectedMovie,
                                        movies,
                                        () -> showHome(stage),
                                        () -> new SearchPage(movies).show(stage),
                                        () -> showGenres(stage),
                                        null
                                ).show(stage)
                );

        stage.setScene(
                new Scene(
                        details.getRoot(),
                        1280,
                        720
                )
        );

        stage.show();
    }

    private void showHome(Stage stage) {

        new SearchPage(movies)
                .show(stage);
    }

    private void showGenres(Stage stage) {

        new GenrePage(
                movies,

                movie -> {

                    MovieDetailsPage details =
                            new MovieDetailsPage(
                                    movie,
                                    movies,
                                    () -> showHome(stage),
                                    () -> new SearchPage(movies).show(stage),
                                    () -> showGenres(stage),

                                    selected ->
                                            new MovieDetailsPage(
                                                    selected,
                                                    movies,
                                                    () -> showHome(stage),
                                                    () -> new SearchPage(movies).show(stage),
                                                    () -> showGenres(stage),
                                                    null
                                            ).show(stage)
                            );

                    stage.setScene(
                            new Scene(
                                    details.getRoot(),
                                    1280,
                                    720
                            )
                    );

                    stage.show();
                },

                () -> showHome(stage),

                () -> showGenres(stage)

        ).show(stage);
    }

    private String getPosterUrl(Movie movie) {

        try {

            String url = movie.getPosterUrl();

            if (url == null || url.isBlank()) {
                return null;
            }

            if (url.startsWith("http://") ||
                    url.startsWith("https://")) {

                return url;
            }

            if (url.startsWith("/")) {

                return "https://image.tmdb.org/t/p/w500" + url;
            }

            return url;

        } catch (Exception e) {

            return null;
        }
    }

    private String getYear(Movie movie) {

        try {

            String date = movie.getReleaseDate();

            if (date != null && date.length() >= 4) {

                return date.substring(0, 4);
            }

        } catch (Exception ignored) {
        }

        return "N/A";
    }

    private String getGenreText(Movie movie) {

        try {

            if (movie.getGenres() != null &&
                    !movie.getGenres().isEmpty()) {

                Genre genre =
                        movie.getGenres().get(0);

                if (genre != null &&
                        genre.getName() != null) {

                    return genre.getName();
                }
            }

        } catch (Exception ignored) {
        }

        return "Movie";
    }

    private HBox createNavbar(Stage stage) {

        HBox navbar = new HBox();

        navbar.setAlignment(Pos.CENTER_LEFT);

        navbar.setPadding(
                new Insets(0, 64, 0, 64)
        );

        navbar.setPrefHeight(80);

        navbar.setStyle(
                "-fx-background-color: rgba(249,249,247,0.97);" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-width: 0 0 1 0;"
        );

        Label logo = new Label("MovieFlix");

        logo.setStyle(
                "-fx-font-family: 'Inter';" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: 900;" +
                        "-fx-text-fill: " + PRIMARY_DARK + ";"
        );

        HBox navigation = new HBox(24);

        navigation.setAlignment(Pos.CENTER_LEFT);

        navigation.setPadding(
                new Insets(0, 0, 0, 48)
        );

        Button home = createNavButton(
                "Home",
                false
        );

        Button search = createNavButton(
                "Search",
                true
        );

        Button genres = createNavButton(
                "Genres",
                false
        );

        home.setOnAction(
                e -> showHome(stage)
        );

        genres.setOnAction(
                e -> showGenres(stage)
        );

        navigation.getChildren().addAll(
                home,
                search,
                genres
        );

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Button favorite = createIconButton("♥");

        Button watchlist = createIconButton("🔖");

        Circle circle = new Circle(
                20,
                Color.web(MEDIUM_GRAY)
        );

        Label profileText = new Label("U");

        profileText.setStyle(
                "-fx-font-family: 'Inter';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + TEXT + ";"
        );

        StackPane profile = new StackPane(
                circle,
                profileText
        );

        navbar.getChildren().addAll(
                logo,
                navigation,
                spacer,
                favorite,
                watchlist,
                profile
        );

        HBox.setMargin(
                favorite,
                new Insets(0, 16, 0, 0)
        );

        HBox.setMargin(
                watchlist,
                new Insets(0, 24, 0, 0)
        );

        return navbar;
    }

    private Button createNavButton(
            String text,
            boolean active
    ) {

        Button button = new Button(text);

        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-width: 0;" +
                        "-fx-padding: 6 0 8 0;" +
                        "-fx-font-family: 'Inter';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: " +
                        (active ? "700" : "500") + ";" +
                        "-fx-text-fill: " +
                        (active ? PRIMARY_DARK : SECONDARY) + ";" +
                        "-fx-cursor: hand;"
        );

        return button;
    }

    private Button createIconButton(String text) {

        Button button = new Button(text);

        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-font-size: 20px;" +
                        "-fx-text-fill: " + TEXT + ";" +
                        "-fx-cursor: hand;"
        );

        return button;
    }
}
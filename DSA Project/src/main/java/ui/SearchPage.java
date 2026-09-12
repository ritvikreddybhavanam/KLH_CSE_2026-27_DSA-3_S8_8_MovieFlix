package ui;

import algorithms.DocumentSimilarity;
import algorithms.MovieSearchAlgorithms;
import algorithms.Trie;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Genre;
import model.Movie;
import theme.ThemeManager;
import ui.components.Navbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchPage {

    private final List<Movie> movies;

    private Trie titleTrie;

    private final Map<String, Movie> titleMovieMap = new HashMap<>();

    private FlowPane movieGrid;
    private FlowPane recommendationGrid;

    private Label resultLabel;
    private Label recommendationLabel;

    private TextField searchField;
    private ComboBox<String> searchType;

    private ListView<String> suggestionList;

    private VBox searchContainer;

    private int displayedMovies = 20;

    private static final int RECOMMENDATION_LIMIT = 5;
    private static final int SUGGESTION_LIMIT = 6;

    public SearchPage(List<Movie> movies) {

        if (movies == null || movies.isEmpty()) {
            this.movies = data.MovieDataLoader.loadMovies(
                    variables.Variables.filePath
            );
        } else {
            this.movies = movies;
        }

        buildTitleMap();

        if (this.movies != null && !this.movies.isEmpty()) {
            titleTrie = new Trie(this.movies);
        }
    }

    public SearchPage() {
        this(null);
    }

    private void buildTitleMap() {

        if (movies == null) {
            return;
        }

        for (Movie movie : movies) {

            if (movie == null ||
                    movie.getTitle() == null ||
                    movie.getTitle().isBlank()) {
                continue;
            }

            String title = movie.getTitle()
                    .trim()
                    .toLowerCase();

            titleMovieMap.put(title, movie);
        }
    }

    public void show(Stage stage) {

        if (stage == null) {
            return;
        }

        BorderPane root = createPage(stage);

        Scene scene = new Scene(root, 1280, 720);

        ThemeManager.applyTheme(scene);

        stage.setTitle("MovieFlix - Home");
        stage.setScene(scene);

        // Keep the application maximized
        stage.setMaximized(true);

        stage.show();
    }

    private BorderPane createPage(Stage stage) {

        BorderPane root = new BorderPane();

        ThemeManager.stylePage(root);

        /*
         * Use the shared Navbar component.
         *
         * "Home" is the active page because SearchPage
         * currently represents the Home/Search screen.
         */
        root.setTop(
                Navbar.create(movies, "Home")
        );

        VBox content = new VBox(24);

        content.setPadding(
                new Insets(48, 64, 60, 64)
        );

        Label title = new Label(
                "Find Your Next Movie"
        );

        title.setStyle(
                ThemeManager.primaryTextStyle() +
                        "-fx-font-size: 46px;" +
                        "-fx-font-weight: 700;"
        );

        Label subtitle = new Label(
                "Search through the MovieFlix collection."
        );

        subtitle.setStyle(
                ThemeManager.secondaryTextStyle() +
                        "-fx-font-size: 18px;"
        );

        // ---------------------------------------------------------
        // SEARCH CONTAINER
        // ---------------------------------------------------------

        searchContainer = new VBox(4);

        searchContainer.setMaxWidth(
                Double.MAX_VALUE
        );

        searchContainer.setFillWidth(true);

        // ---------------------------------------------------------
        // SEARCH FIELD
        // ---------------------------------------------------------

        searchField = new TextField();

        searchField.setPromptText(
                "Search movies..."
        );

        searchField.setPrefHeight(48);

        searchField.setMaxWidth(
                Double.MAX_VALUE
        );

        ThemeManager.styleInput(searchField);

        // ---------------------------------------------------------
        // AUTOCOMPLETE SUGGESTIONS
        // ---------------------------------------------------------

        suggestionList = new ListView<>();

        suggestionList.setPrefHeight(180);

        suggestionList.setMaxHeight(180);

        suggestionList.setMaxWidth(
                Double.MAX_VALUE
        );

        suggestionList.setVisible(false);

        suggestionList.setManaged(false);

        suggestionList.setFocusTraversable(false);

        suggestionList.setOnMousePressed(e -> {

            String selected =
                    suggestionList
                            .getSelectionModel()
                            .getSelectedItem();

            if (selected == null ||
                    selected.isBlank()) {
                return;
            }

            Movie selectedMovie =
                    findMovieByTitle(selected);

            searchField.setText(selected);

            hideSuggestions();

            searchField.requestFocus();

            if (selectedMovie != null) {
                openMovieDetails(selectedMovie);
            }
        });

        // ---------------------------------------------------------
        // SEARCH FIELD KEYBOARD CONTROLS
        // ---------------------------------------------------------

        searchField.setOnKeyPressed(e -> {

            switch (e.getCode()) {

                case DOWN:

                    if (suggestionList.isVisible() &&
                            !suggestionList.getItems().isEmpty()) {

                        suggestionList.requestFocus();

                        suggestionList
                                .getSelectionModel()
                                .selectFirst();
                    }

                    break;

                case ENTER:

                    if (suggestionList.isVisible() &&
                            suggestionList
                                    .getSelectionModel()
                                    .getSelectedItem() != null) {

                        String selected =
                                suggestionList
                                        .getSelectionModel()
                                        .getSelectedItem();

                        Movie selectedMovie =
                                findMovieByTitle(selected);

                        searchField.setText(selected);

                        hideSuggestions();

                        if (selectedMovie != null) {
                            openMovieDetails(selectedMovie);
                        }

                    } else {

                        hideSuggestions();

                        performSearch();
                    }

                    break;

                case ESCAPE:

                    hideSuggestions();

                    break;

                default:
                    break;
            }
        });

        // ---------------------------------------------------------
        // SUGGESTION LIST KEYBOARD CONTROLS
        // ---------------------------------------------------------

        suggestionList.setOnKeyPressed(e -> {

            switch (e.getCode()) {

                case ENTER:

                    String selected =
                            suggestionList
                                    .getSelectionModel()
                                    .getSelectedItem();

                    if (selected != null &&
                            !selected.isBlank()) {

                        Movie selectedMovie =
                                findMovieByTitle(selected);

                        searchField.setText(selected);

                        hideSuggestions();

                        if (selectedMovie != null) {
                            openMovieDetails(selectedMovie);
                        }
                    }

                    break;

                case ESCAPE:

                    hideSuggestions();

                    searchField.requestFocus();

                    break;

                case UP:

                    if (suggestionList
                            .getSelectionModel()
                            .getSelectedIndex() == 0) {

                        searchField.requestFocus();
                    }

                    break;

                default:
                    break;
            }
        });

        // ---------------------------------------------------------
        // SEARCH FIELD TEXT LISTENER
        // ---------------------------------------------------------

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    if (searchType == null ||
                            "Movie Title".equals(
                                    searchType.getValue())) {

                        showSuggestions(newValue);

                    } else {

                        hideSuggestions();
                    }
                }
        );

        // ---------------------------------------------------------
        // SEARCH FIELD FOCUS LISTENER
        // ---------------------------------------------------------

        searchField.focusedProperty().addListener(
                (observable, oldValue, focused) -> {

                    if (focused) {

                        String text =
                                searchField.getText();

                        if (searchType == null ||
                                "Movie Title".equals(
                                        searchType.getValue())) {

                            showSuggestions(text);
                        }
                    }
                }
        );

        // ---------------------------------------------------------
        // SEARCH TYPE
        // ---------------------------------------------------------

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
                "-fx-background-color: #FFFFFF;" +
                        "-fx-border-color: #E5D6CF;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-text-fill: #2B211E;"
        );

        searchType.setOnAction(e -> {

            if ("Movie Title".equals(
                    searchType.getValue())) {

                showSuggestions(
                        searchField.getText()
                );

            } else {

                hideSuggestions();
            }
        });

        // ---------------------------------------------------------
        // SEARCH BUTTON
        // ---------------------------------------------------------

        Button searchButton =
                new Button("Search");

        searchButton.setPrefHeight(48);

        searchButton.setPrefWidth(120);

        ThemeManager.styleButton(searchButton);

        searchButton.setOnAction(e -> {

            hideSuggestions();

            performSearch();
        });

        // ---------------------------------------------------------
        // SEARCH BOX
        // ---------------------------------------------------------

        HBox searchBox = new HBox(10);

        searchBox.setAlignment(
                Pos.TOP_LEFT
        );

        HBox.setHgrow(
                searchContainer,
                Priority.ALWAYS
        );

        searchBox.getChildren().addAll(
                searchContainer,
                searchType,
                searchButton
        );

        searchContainer.getChildren().addAll(
                searchField,
                suggestionList
        );

        // ---------------------------------------------------------
        // RESULT LABEL
        // ---------------------------------------------------------

        resultLabel = new Label();

        resultLabel.setStyle(
                ThemeManager.primaryTextStyle() +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: 600;"
        );

        // ---------------------------------------------------------
        // MOVIE GRID
        // ---------------------------------------------------------

        movieGrid = new FlowPane();

        movieGrid.setHgap(24);

        movieGrid.setVgap(24);

        movieGrid.setPadding(
                new Insets(10, 0, 20, 0)
        );

        // ---------------------------------------------------------
        // RECOMMENDATION LABEL
        // ---------------------------------------------------------

        recommendationLabel =
                new Label("Recommended Movies");

        recommendationLabel.setStyle(
                ThemeManager.primaryTextStyle() +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: 600;"
        );

        recommendationLabel.setVisible(false);

        recommendationLabel.setManaged(false);

        // ---------------------------------------------------------
        // RECOMMENDATION GRID
        // ---------------------------------------------------------

        recommendationGrid = new FlowPane();

        recommendationGrid.setHgap(24);

        recommendationGrid.setVgap(24);

        recommendationGrid.setPadding(
                new Insets(10, 0, 30, 0)
        );

        recommendationGrid.setVisible(false);

        recommendationGrid.setManaged(false);

        // ---------------------------------------------------------
        // ADD CONTENT
        // ---------------------------------------------------------

        content.getChildren().addAll(
                title,
                subtitle,
                searchBox,
                resultLabel,
                movieGrid,
                recommendationLabel,
                recommendationGrid
        );

        // ---------------------------------------------------------
        // SCROLL PANE
        // ---------------------------------------------------------

        ScrollPane scrollPane =
                new ScrollPane(content);

        scrollPane.setFitToWidth(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;"
        );

        root.setCenter(scrollPane);

        // Display all movies initially
        displayMovies(movies);

        return root;
    }

    // =============================================================
    // AUTOCOMPLETE
    // =============================================================

    private void showSuggestions(String text) {

        if (suggestionList == null ||
                titleTrie == null) {
            return;
        }

        String query =
                text == null
                        ? ""
                        : text.trim();

        if (query.isEmpty()) {

            hideSuggestions();

            return;
        }

        List<String> suggestions;

        try {

            suggestions =
                    titleTrie.getSuggestions(
                            query,
                            SUGGESTION_LIMIT
                    );

        } catch (Exception e) {

            hideSuggestions();

            return;
        }

        suggestionList.getItems().clear();

        if (suggestions == null ||
                suggestions.isEmpty()) {

            hideSuggestions();

            return;
        }

        suggestionList
                .getItems()
                .addAll(suggestions);

        suggestionList
                .getSelectionModel()
                .clearSelection();

        suggestionList.setManaged(true);

        suggestionList.setVisible(true);
    }

    private void hideSuggestions() {

        if (suggestionList == null) {
            return;
        }

        suggestionList
                .getItems()
                .clear();

        suggestionList
                .getSelectionModel()
                .clearSelection();

        suggestionList.setVisible(false);

        suggestionList.setManaged(false);
    }

    private Movie findMovieByTitle(String title) {

        if (title == null ||
                title.isBlank()) {

            return null;
        }

        return titleMovieMap.get(
                title.trim().toLowerCase()
        );
    }

    // =============================================================
    // SEARCH
    // =============================================================

    private void performSearch() {

        hideSuggestions();

        if (searchField == null) {
            return;
        }

        String text =
                searchField.getText();

        String query =
                text == null
                        ? ""
                        : text.trim();

        if (query.isEmpty()) {

            displayMovies(movies);

            hideRecommendations();

            return;
        }

        displayedMovies = 20;

        String type =
                searchType.getValue();

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

                break;
        }
    }

    private void searchByTitle(String query) {

        List<Movie> result =
                MovieSearchAlgorithms
                        .searchByTitle(
                                movies,
                                query
                        );

        if (result != null &&
                !result.isEmpty()) {

            resultLabel.setText(
                    "Movie Title Search (" +
                            result.size() +
                            ")"
            );

            displayMovieCards(result);

            hideRecommendations();

            return;
        }

        Movie suggestion =
                MovieSearchAlgorithms
                        .findClosestMovie(
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
                MovieSearchAlgorithms
                        .searchByKeyword(
                                movies,
                                query
                        );

        resultLabel.setText(
                "Keyword Search (" +
                        (result == null
                                ? 0
                                : result.size()) +
                        ")"
        );

        displaySearchResults(result);
    }

    private void searchByTagline(String query) {

        List<Movie> result =
                MovieSearchAlgorithms
                        .searchByTagline(
                                movies,
                                query
                        );

        resultLabel.setText(
                "Tagline Search (" +
                        (result == null
                                ? 0
                                : result.size()) +
                        ")"
        );

        displaySearchResults(result);
    }

    private void searchByLanguage(String query) {

        List<Movie> result =
                MovieSearchAlgorithms
                        .searchByLanguage(
                                movies,
                                query
                        );

        resultLabel.setText(
                "Spoken Language Search (" +
                        (result == null
                                ? 0
                                : result.size()) +
                        ")"
        );

        displaySearchResults(result);
    }

    private void displaySearchResults(
            List<Movie> result) {

        hideRecommendations();

        movieGrid.getChildren().clear();

        if (result == null ||
                result.isEmpty()) {

            resultLabel.setText(
                    resultLabel.getText() +
                            " — No movies found"
            );

            return;
        }

        displayMovieCards(result);
    }

    // =============================================================
    // MOVIE DISPLAY
    // =============================================================

    private void displayMovies(
            List<Movie> movieList) {

        hideRecommendations();

        movieGrid.getChildren().clear();

        if (movieList == null ||
                movieList.isEmpty()) {

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

    private void displayMovieCards(
            List<Movie> movieList) {

        movieGrid.getChildren().clear();

        if (movieList == null ||
                movieList.isEmpty()) {

            return;
        }

        int end =
                Math.min(
                        displayedMovies,
                        movieList.size()
                );

        for (int i = 0; i < end; i++) {

            Movie movie =
                    movieList.get(i);

            if (movie != null) {

                movieGrid.getChildren().add(
                        createMovieCard(movie)
                );
            }
        }
    }

    // =============================================================
    // RECOMMENDATIONS
    // =============================================================

    private void showRecommendations(
            Movie selectedMovie) {

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

        recommendationGrid
                .getChildren()
                .clear();

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

                recommendationGrid.getChildren().add(
                        createMovieCard(movie)
                );
            }
        }
    }

    private void hideRecommendations() {

        if (recommendationGrid != null) {

            recommendationGrid
                    .getChildren()
                    .clear();

            recommendationGrid.setVisible(false);

            recommendationGrid.setManaged(false);
        }

        if (recommendationLabel != null) {

            recommendationLabel.setVisible(false);

            recommendationLabel.setManaged(false);
        }
    }

    // =============================================================
    // MOVIE CARD
    // =============================================================

    private VBox createMovieCard(Movie movie) {

        VBox card = new VBox();

        card.setPrefWidth(220);

        card.setMaxWidth(220);

        ThemeManager.styleCard(card);

        StackPane posterBox =
                new StackPane();

        posterBox.setPrefSize(
                220,
                330
        );

        String posterUrl =
                getPosterUrl(movie);

        if (posterUrl != null &&
                !posterUrl.isBlank()) {

            try {

                Image image =
                        new Image(
                                posterUrl,
                                220,
                                330,
                                false,
                                true,
                                true
                        );

                ImageView imageView =
                        new ImageView(image);

                imageView.setFitWidth(220);

                imageView.setFitHeight(330);

                imageView.setPreserveRatio(false);

                posterBox.getChildren().add(
                        imageView
                );

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

        info.setPadding(
                new Insets(14)
        );

        String titleText =
                movie.getTitle();

        if (titleText == null ||
                titleText.isBlank()) {

            titleText = "Unknown Movie";
        }

        Label title =
                new Label(titleText);

        title.setMaxWidth(190);

        title.setEllipsisString("...");

        title.setStyle(
                ThemeManager.primaryTextStyle() +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: 600;"
        );

        HBox metadata =
                new HBox();

        Label year =
                new Label(
                        getYear(movie)
                );

        year.setStyle(
                ThemeManager.secondaryTextStyle() +
                        "-fx-font-size: 13px;"
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Label genre =
                new Label(
                        getGenreText(movie)
                );

        genre.setPadding(
                new Insets(3, 8, 3, 8)
        );

        genre.setStyle(
                ThemeManager.secondaryTextStyle() +
                        "-fx-font-size: 12px;" +
                        "-fx-background-radius: 4;"
        );

        metadata.getChildren().addAll(
                year,
                spacer,
                genre
        );

        Label rating =
                new Label(
                        "★ " +
                                String.format(
                                        "%.1f",
                                        movie.getVoteAverage()
                                )
                );

        rating.setStyle(
                ThemeManager.primaryTextStyle() +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;"
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

        // ---------------------------------------------------------
        // CARD CLICK
        // ---------------------------------------------------------

        card.setOnMouseClicked(
                e -> openMovieDetails(movie)
        );

        // ---------------------------------------------------------
        // CARD HOVER
        // ---------------------------------------------------------

        card.setOnMouseEntered(e -> {

            card.setScaleX(1.02);

            card.setScaleY(1.02);
        });

        card.setOnMouseExited(e -> {

            card.setScaleX(1);

            card.setScaleY(1);
        });

        return card;
    }

    // =============================================================
    // PLACEHOLDER
    // =============================================================

    private StackPane createPlaceholder(
            Movie movie) {

        StackPane pane =
                new StackPane();

        pane.setPrefSize(
                220,
                330
        );

        Label label =
                new Label(
                        movie.getTitle() == null ||
                                movie.getTitle().isBlank()
                                ? "Unknown Movie"
                                : movie.getTitle()
                );

        label.setWrapText(true);

        label.setMaxWidth(180);

        label.setAlignment(
                Pos.CENTER
        );

        label.setStyle(
                ThemeManager.secondaryTextStyle() +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );

        pane.getChildren().add(label);

        return pane;
    }

    // =============================================================
    // MOVIE DETAILS
    // =============================================================

    private void openMovieDetails(
            Movie movie) {

        if (movie == null) {
            return;
        }

        Stage stage =
                (Stage) movieGrid
                        .getScene()
                        .getWindow();

        MovieDetailsPage details =
                new MovieDetailsPage(
                        movie,
                        movies,

                        // Home
                        () -> showHome(stage),

                        // Search
                        () -> new SearchPage(
                                movies
                        ).show(stage),

                        // Genres
                        () -> showGenres(stage),

                        // Recommendation
                        selectedMovie ->
                                new MovieDetailsPage(
                                        selectedMovie,
                                        movies,

                                        () -> showHome(stage),

                                        () -> new SearchPage(
                                                movies
                                        ).show(stage),

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

        ThemeManager.applyTheme(
                stage.getScene()
        );

        stage.setMaximized(true);
        stage.show();
    }

    // =============================================================
    // HOME
    // =============================================================

    private void showHome(Stage stage) {

        new SearchPage(
                movies
        ).show(stage);
    }

    // =============================================================
    // GENRES
    // =============================================================

    private void showGenres(Stage stage) {

        new GenrePage(
                movies,

                movie -> {

                    MovieDetailsPage details =
                            new MovieDetailsPage(
                                    movie,
                                    movies,

                                    // Home
                                    () -> showHome(stage),

                                    // Search
                                    () -> new SearchPage(
                                            movies
                                    ).show(stage),

                                    // Genres
                                    () -> showGenres(stage),

                                    // Recommendation
                                    selected ->
                                            new MovieDetailsPage(
                                                    selected,
                                                    movies,

                                                    // Home
                                                    () -> showHome(stage),

                                                    // Search
                                                    () -> new SearchPage(
                                                            movies
                                                    ).show(stage),

                                                    // Genres
                                                    () -> showGenres(stage),

                                                    // Recommendation
                                                    null
                                            ).show(stage)
                            );

                    // Create Movie Details scene
                    stage.setScene(
                            new Scene(
                                    details.getRoot(),
                                    1280,
                                    720
                            )
                    );

                    // Apply centralized theme
                    ThemeManager.applyTheme(
                            stage.getScene()
                    );

                    // Keep the window maximized
                    stage.setMaximized(true);

                    // Show the updated scene
                    stage.show();
                },

                // Home
                () -> showHome(stage),

                // Genres
                () -> showGenres(stage)

        ).show(stage);
    }

    // =============================================================
    // POSTER URL
    // =============================================================

    private String getPosterUrl(Movie movie) {

        try {

            String url =
                    movie.getPosterUrl();

            if (url == null ||
                    url.isBlank()) {

                return null;
            }

            if (url.startsWith("http://") ||
                    url.startsWith("https://")) {

                return url;
            }

            if (url.startsWith("/")) {

                return "https://image.tmdb.org/t/p/w500"
                        + url;
            }

            return url;

        } catch (Exception e) {

            return null;
        }
    }

    // =============================================================
    // YEAR
    // =============================================================

    private String getYear(Movie movie) {

        try {

            String date =
                    movie.getReleaseDate();

            if (date != null &&
                    date.length() >= 4) {

                return date.substring(0, 4);
            }

        } catch (Exception ignored) {
        }

        return "N/A";
    }

    // =============================================================
    // GENRE
    // =============================================================

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
}

package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Genre;
import model.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GenrePage {
    private final List<Movie> movies;
    private final Consumer<Movie> onMovieSelected;
    private final Runnable onHome;
    private final Runnable onGenres;

    private static final String BACKGROUND = "#F9F9F7";
    private static final String WHITE = "#FFFFFF";
    private static final String PRIMARY = "#FF6B35";
    private static final String PRIMARY_DARK = "#AB3500";
    private static final String TEXT = "#1A1C1B";
    private static final String SECONDARY_TEXT = "#594139";
    private static final String BORDER = "#E1BFB5";
    private static final String LIGHT_GRAY = "#F4F4F2";
    private static final String MEDIUM_GRAY = "#E8E8E6";

    private FlowPane movieGrid;
    private Label resultsTitle;
    private Button loadMoreButton;
    private String selectedGenre = "Drama";
    private int displayedMovies = 20;

    private final String[] genres = {"Action", "Adventure", "Animation", "Comedy", "Crime", "Drama", "Fantasy", "Horror", "Romance", "Science Fiction", "Thriller", "War"};
    private final List<Button> genreButtons = new ArrayList<>();

    public GenrePage(List<Movie> movies, Consumer<Movie> onMovieSelected, Runnable onHome, Runnable onGenres) {
        if (movies == null || movies.isEmpty()) {
            this.movies = data.MovieDataLoader.loadMovies(variables.Variables.filePath);
        } else {
            this.movies = movies;
        }
        this.onMovieSelected = onMovieSelected;
        this.onHome = onHome;
        this.onGenres = onGenres;
    }

    public GenrePage(List<Movie> movies) {
        this(movies, null, null, null);
    }

    public GenrePage() {
        this(null, null, null, null);
    }

    public void show(javafx.stage.Stage stage) {
        BorderPane root = createPage();
        javafx.scene.Scene scene = new javafx.scene.Scene(root, 1280, 720);
        stage.setTitle("MovieFlix - Browse by Genre");
        stage.setScene(scene);
        stage.show();
    }

    public BorderPane createPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BACKGROUND + ";");
        root.setTop(createNavbar());

        VBox content = new VBox(24);
        content.setPadding(new Insets(48, 64, 80, 64));

        VBox header = new VBox(5);
        Label title = new Label("Browse by Genre");
        title.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 48px; -fx-font-weight: 700; -fx-text-fill: " + TEXT + ";");

        Label subtitle = new Label("Explore movies by your favorite genres.");
        subtitle.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 18px; -fx-text-fill: " + SECONDARY_TEXT + ";");
        header.getChildren().addAll(title, subtitle);

        FlowPane genrePane = new FlowPane();
        genrePane.setHgap(12);
        genrePane.setVgap(12);
        genreButtons.clear();

        for (String genre : genres) {
            Button button = createGenreButton(genre);
            genreButtons.add(button);
            genrePane.getChildren().add(button);
        }

        HBox resultHeader = new HBox();
        resultHeader.setAlignment(Pos.CENTER_LEFT);
        resultHeader.setPadding(new Insets(20, 0, 16, 0));
        resultHeader.setBorder(new Border(new BorderStroke(Color.web(BORDER), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 1, 0))));

        resultsTitle = new Label("Drama Movies");
        resultsTitle.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 32px; -fx-font-weight: 600; -fx-text-fill: " + TEXT + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button gridButton = createViewButton("▦");
        Button listButton = createViewButton("☷");
        HBox viewButtons = new HBox(8);
        viewButtons.getChildren().addAll(gridButton, listButton);
        resultHeader.getChildren().addAll(resultsTitle, spacer, viewButtons);

        movieGrid = new FlowPane();
        movieGrid.setHgap(24);
        movieGrid.setVgap(24);
        movieGrid.setPadding(new Insets(24, 0, 0, 0));

        HBox loadMoreBox = new HBox();
        loadMoreBox.setAlignment(Pos.CENTER);
        loadMoreBox.setPadding(new Insets(24, 0, 0, 0));

        loadMoreButton = new Button();
        loadMoreButton.setStyle("-fx-background-color: " + WHITE + "; -fx-border-color: " + PRIMARY_DARK + "; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-text-fill: " + PRIMARY_DARK + "; -fx-font-family: 'Inter'; -fx-font-size: 14px; -fx-font-weight: 600; -fx-padding: 12 32 12 32; -fx-cursor: hand;");
        loadMoreButton.setOnAction(e -> {
            displayedMovies += 20;
            updateMovies();
        });
        loadMoreBox.getChildren().add(loadMoreButton);

        content.getChildren().addAll(header, genrePane, resultHeader, movieGrid, loadMoreBox);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        root.setCenter(scrollPane);

        showMoviesByGenre("Drama");
        return root;
    }

    private HBox createNavbar() {
        HBox navbar = new HBox();
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setPadding(new Insets(0, 64, 0, 64));
        navbar.setPrefHeight(80);
        navbar.setStyle("-fx-background-color: rgba(249,249,247,0.97); -fx-border-color: " + BORDER + "; -fx-border-width: 0 0 1 0;");

        Label logo = new Label("MovieFlix");
        logo.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 24px; -fx-font-weight: 900; -fx-text-fill: " + PRIMARY_DARK + ";");

        HBox navigation = new HBox(24);
        navigation.setAlignment(Pos.CENTER_LEFT);
        navigation.setPadding(new Insets(0, 0, 0, 48));

        Button home = createNavButton("Home", false);
        Button search = createNavButton("Search", false);
        Button genresButton = createNavButton("Genres", true);

        home.setOnAction(e -> {
            if (onHome != null) {
                onHome.run();
            } else {
                javafx.stage.Stage stage = (javafx.stage.Stage) ((Button) e.getSource()).getScene().getWindow();
                new SearchPage(movies).show(stage);
            }
        });

        search.setOnAction(e -> {
            if (onHome != null) {
                onHome.run();
            } else {
                javafx.stage.Stage stage = (javafx.stage.Stage) ((Button) e.getSource()).getScene().getWindow();
                new SearchPage(movies).show(stage);
            }
        });

        genresButton.setOnAction(e -> {
            if (onGenres != null) {
                onGenres.run();
            }
        });

        navigation.getChildren().addAll(home, search, genresButton);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button favorite = createIconButton("♥");
        Button watchlist = createIconButton("🔖");

        Circle circle = new Circle(20, Color.web(MEDIUM_GRAY));
        Label profileText = new Label("U");
        profileText.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + TEXT + ";");

        StackPane profile = new StackPane(circle, profileText);

        navbar.getChildren().addAll(logo, navigation, spacer, favorite, watchlist, profile);
        HBox.setMargin(favorite, new Insets(0, 16, 0, 0));
        HBox.setMargin(watchlist, new Insets(0, 24, 0, 0));

        return navbar;
    }

    private Button createNavButton(String text, boolean active) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 6 0 8 0; -fx-font-family: 'Inter'; -fx-font-size: 14px; -fx-font-weight: " + (active ? "700" : "500") + "; -fx-text-fill: " + (active ? PRIMARY_DARK : SECONDARY_TEXT) + "; -fx-cursor: hand;");
        return button;
    }

    private Button createIconButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: transparent; -fx-font-size: 20px; -fx-text-fill: " + TEXT + "; -fx-cursor: hand;");

        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: transparent; -fx-font-size: 20px; -fx-text-fill: " + PRIMARY_DARK + "; -fx-cursor: hand;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-font-size: 20px; -fx-text-fill: " + TEXT + "; -fx-cursor: hand;"));

        return button;
    }

    private Button createGenreButton(String genre) {
        Button button = new Button(genre);
        button.setPadding(new Insets(9, 24, 9, 24));
        button.setStyle(getGenreStyle(genre.equals(selectedGenre)));
        button.setOnAction(e -> showMoviesByGenre(genre));
        return button;
    }

    private String getGenreStyle(boolean active) {
        if (active) {
            return "-fx-background-color: " + PRIMARY + "; -fx-text-fill: white; -fx-background-radius: 999px; -fx-font-family: 'Inter'; -fx-font-size: 14px; -fx-font-weight: 500; -fx-cursor: hand;";
        }

        return "-fx-background-color: " + WHITE + "; -fx-border-color: " + BORDER + "; -fx-border-radius: 999px; -fx-background-radius: 999px; -fx-text-fill: " + TEXT + "; -fx-font-family: 'Inter'; -fx-font-size: 14px; -fx-font-weight: 500; -fx-cursor: hand;";
    }

    private Button createViewButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(42, 42);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: " + BORDER + "; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-font-size: 20px; -fx-text-fill: " + SECONDARY_TEXT + "; -fx-cursor: hand;");
        return button;
    }

    private void showMoviesByGenre(String genre) {
        selectedGenre = genre;
        displayedMovies = 20;

        for (int i = 0; i < genreButtons.size(); i++) {
            Button button = genreButtons.get(i);
            String genreName = genres[i];
            button.setStyle(getGenreStyle(genreName.equals(selectedGenre)));
        }

        if (resultsTitle != null) {
            resultsTitle.setText(genre + " Movies");
        }

        updateMovies();
    }

    private List<Movie> getFilteredMovies() {
        List<Movie> result = new ArrayList<>();

        for (Movie movie : movies) {
            if (movie == null) {
                continue;
            }

            if (hasGenre(movie, selectedGenre)) {
                result.add(movie);
            }
        }

        return result;
    }

    private boolean hasGenre(Movie movie, String genreName) {
        try {
            if (movie.getGenres() == null) {
                return false;
            }

            for (Genre genre : movie.getGenres()) {
                if (genre == null || genre.getName() == null) {
                    continue;
                }

                if (genre.getName().equalsIgnoreCase(genreName)) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }

        return false;
    }

    private void updateMovies() {
        if (movieGrid == null) {
            return;
        }

        movieGrid.getChildren().clear();

        List<Movie> filtered = getFilteredMovies();
        int end = Math.min(displayedMovies, filtered.size());

        for (int i = 0; i < end; i++) {
            movieGrid.getChildren().add(createMovieCard(filtered.get(i)));
        }

        if (filtered.size() > displayedMovies) {
            loadMoreButton.setText("Load More " + selectedGenre + "s");
            loadMoreButton.setVisible(true);
            loadMoreButton.setManaged(true);
        } else {
            loadMoreButton.setVisible(false);
            loadMoreButton.setManaged(false);
        }

        if (filtered.isEmpty()) {
            Label noMovies = new Label("No " + selectedGenre + " movies found.");
            noMovies.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 18px; -fx-text-fill: " + SECONDARY_TEXT + ";");
            movieGrid.getChildren().add(noMovies);
        }
    }

    private VBox createMovieCard(Movie movie) {
        VBox card = new VBox();
        card.setPrefWidth(220);
        card.setMaxWidth(220);
        card.setStyle("-fx-background-color: " + WHITE + "; -fx-background-radius: 12px; -fx-border-color: rgba(225,191,181,0.25); -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian,rgba(0,0,0,0.04),20,0,0,4); -fx-cursor: hand;");

        StackPane posterBox = new StackPane();
        posterBox.setPrefSize(220, 330);
        posterBox.setMinSize(220, 330);
        posterBox.setMaxSize(220, 330);

        String posterUrl = getPosterUrl(movie);

        if (posterUrl != null && !posterUrl.isBlank()) {
            try {
                Image image = new Image(posterUrl, 220, 330, false, true, true);
                ImageView poster = new ImageView(image);
                poster.setFitWidth(220);
                poster.setFitHeight(330);
                poster.setPreserveRatio(false);
                posterBox.getChildren().add(poster);
            } catch (Exception e) {
                posterBox.getChildren().add(createPlaceholder(movie));
            }
        } else {
            posterBox.getChildren().add(createPlaceholder(movie));
        }

        Button bookmark = new Button("🔖");
        bookmark.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 50%; -fx-font-size: 16px; -fx-cursor: hand;");
        StackPane.setAlignment(bookmark, Pos.TOP_RIGHT);
        StackPane.setMargin(bookmark, new Insets(10));
        posterBox.getChildren().add(bookmark);

        VBox info = new VBox(6);
        info.setPadding(new Insets(14));

        String titleText = movie.getTitle();
        if (titleText == null || titleText.isBlank()) {
            titleText = "Unknown Movie";
        }

        Label title = new Label(titleText);
        title.setMaxWidth(190);
        title.setEllipsisString("...");
        title.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 18px; -fx-font-weight: 600; -fx-text-fill: " + TEXT + ";");

        HBox metadata = new HBox();
        metadata.setAlignment(Pos.CENTER_LEFT);

        Label year = new Label(getYear(movie));
        year.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 14px; -fx-text-fill: " + SECONDARY_TEXT + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label genre = new Label(getGenreText(movie));
        genre.setPadding(new Insets(3, 8, 3, 8));
        genre.setStyle("-fx-background-color: " + MEDIUM_GRAY + "; -fx-background-radius: 4px; -fx-font-family: 'Inter'; -fx-font-size: 12px; -fx-text-fill: " + SECONDARY_TEXT + ";");

        metadata.getChildren().addAll(year, spacer, genre);

        Label rating = new Label("★ " + getRating(movie));
        rating.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: " + PRIMARY_DARK + ";");

        info.getChildren().addAll(title, metadata, rating);
        card.getChildren().addAll(posterBox, info);

        card.setOnMouseClicked(e -> {
            if (e.getTarget() == bookmark) {
                return;
            }

            if (onMovieSelected != null) {
                onMovieSelected.accept(movie);
            } else {
                javafx.stage.Stage stage = (javafx.stage.Stage) card.getScene().getWindow();
                new MovieDetailsPage(movie, movies).show(stage);
            }
        });

        card.setOnMouseEntered(e -> {
            card.setScaleX(1.02);
            card.setScaleY(1.02);
            card.setStyle("-fx-background-color: " + WHITE + "; -fx-background-radius: 12px; -fx-border-color: " + BORDER + "; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian,rgba(0,0,0,0.10),30,0,0,10); -fx-cursor: hand;");
        });

        card.setOnMouseExited(e -> {
            card.setScaleX(1);
            card.setScaleY(1);
            card.setStyle("-fx-background-color: " + WHITE + "; -fx-background-radius: 12px; -fx-border-color: rgba(225,191,181,0.25); -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian,rgba(0,0,0,0.04),20,0,0,4); -fx-cursor: hand;");
        });

        return card;
    }

    private StackPane createPlaceholder(Movie movie) {
        StackPane pane = new StackPane();
        pane.setStyle("-fx-background-color: " + LIGHT_GRAY + ";");

        String title = movie.getTitle();
        Label label = new Label(title == null || title.isBlank() ? "Unknown Movie" : title);
        label.setWrapText(true);
        label.setMaxWidth(180);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 18px; -fx-font-weight: 600; -fx-text-fill: " + SECONDARY_TEXT + ";");

        pane.getChildren().add(label);
        return pane;
    }

    private String getPosterUrl(Movie movie) {
        try {
            String url = movie.getPosterUrl();

            if (url == null || url.isBlank()) {
                return null;
            }

            if (url.startsWith("http://") || url.startsWith("https://")) {
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

    private String getRating(Movie movie) {
        try {
            return String.format("%.1f", movie.getVoteAverage());
        } catch (Exception e) {
            return "N/A";
        }
    }

    private String getGenreText(Movie movie) {
        try {
            if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
                Genre genre = movie.getGenres().get(0);

                if (genre != null && genre.getName() != null) {
                    return genre.getName();
                }
            }
        } catch (Exception ignored) {
        }

        return selectedGenre;
    }
}
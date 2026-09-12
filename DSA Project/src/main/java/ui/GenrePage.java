package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import model.Genre;
import model.Movie;
import theme.ThemeManager;
import ui.components.Navbar;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GenrePage {

    private final List<Movie> movies;
    private final Consumer<Movie> onMovieSelected;
    private final Runnable onHome;
    private final Runnable onGenres;

    private FlowPane movieGrid;
    private Label resultsTitle;
    private Button loadMoreButton;

    private String selectedGenre = "Drama";
    private int displayedMovies = 20;

    private final String[] genres = {
            "Action", "Adventure", "Animation", "Comedy",
            "Crime", "Drama", "Fantasy", "Horror",
            "Romance", "Science Fiction", "Thriller", "War"
    };

    private final List<Button> genreButtons = new ArrayList<>();

    public GenrePage(
            List<Movie> movies,
            Consumer<Movie> onMovieSelected,
            Runnable onHome,
            Runnable onGenres
    ) {
        if (movies == null || movies.isEmpty()) {
            this.movies = data.MovieDataLoader.loadMovies(
                    variables.Variables.filePath
            );
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

        ThemeManager.applyTheme(scene);

        stage.setTitle("MovieFlix - Browse by Genre");
        stage.setScene(scene);
        stage.show();
    }

    public BorderPane createPage() {
        BorderPane root = new BorderPane();
        ThemeManager.stylePage(root);

        root.setTop(Navbar.create(movies, "Genres"));

        VBox content = new VBox(24);
        content.setPadding(new Insets(42, 64, 80, 64));

        VBox header = new VBox(6);

        Label title = new Label("Browse by Genre");
        title.setStyle(
                ThemeManager.primaryTextStyle() +
                        "-fx-font-size: 48px;" +
                        "-fx-font-weight: 700;"
        );

        Label subtitle = new Label(
                "Explore movies by your favorite genres."
        );
        subtitle.setStyle(
                ThemeManager.secondaryTextStyle() +
                        "-fx-font-size: 18px;"
        );

        header.getChildren().addAll(title, subtitle);

        FlowPane genrePane = new FlowPane();
        genrePane.setHgap(14);
        genrePane.setVgap(14);

        genreButtons.clear();

        for (String genre : genres) {
            Button button = createGenreButton(genre);
            genreButtons.add(button);
            genrePane.getChildren().add(button);
        }

        HBox resultHeader = new HBox();
        resultHeader.setAlignment(Pos.CENTER_LEFT);
        resultHeader.setPadding(new Insets(20, 0, 16, 0));
        resultHeader.setStyle(ThemeManager.borderStyle());

        resultsTitle = new Label("Drama Movies");
        resultsTitle.setStyle(
                ThemeManager.primaryTextStyle() +
                        "-fx-font-size: 32px;" +
                        "-fx-font-weight: 700;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button gridButton = createViewButton("▦");
        Button listButton = createViewButton("☷");

        HBox viewButtons = new HBox(8);
        viewButtons.getChildren().addAll(gridButton, listButton);

        resultHeader.getChildren().addAll(
                resultsTitle,
                spacer,
                viewButtons
        );

        movieGrid = new FlowPane();
        movieGrid.setHgap(24);
        movieGrid.setVgap(24);
        movieGrid.setPadding(new Insets(24, 0, 0, 0));

        HBox loadMoreBox = new HBox();
        loadMoreBox.setAlignment(Pos.CENTER);
        loadMoreBox.setPadding(new Insets(24, 0, 0, 0));

        loadMoreButton = new Button("Load More");
        ThemeManager.styleButton(loadMoreButton);
        loadMoreButton.setPadding(new Insets(12, 32, 12, 32));

        loadMoreButton.setOnMousePressed(e ->
                loadMoreButton.setTranslateY(2)
        );

        loadMoreButton.setOnMouseReleased(e ->
                loadMoreButton.setTranslateY(0)
        );

        loadMoreButton.setOnAction(e -> {
            displayedMovies += 20;
            updateMovies();
        });

        loadMoreBox.getChildren().add(loadMoreButton);

        content.getChildren().addAll(
                header,
                genrePane,
                resultHeader,
                movieGrid,
                loadMoreBox
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;" +
                        "-fx-border-color: transparent;"
        );

        root.setCenter(scrollPane);

        showMoviesByGenre("Drama");

        return root;
    }

    private Button createIconButton(String text) {
        Button button = new Button(text);
        ThemeManager.styleButton(button);
        button.setPrefSize(44, 44);
        return button;
    }

    private Button createGenreButton(String genre) {
        Button button = new Button(genre);
        ThemeManager.styleButton(button);
        button.setPadding(new Insets(10, 24, 10, 24));

        button.setOnAction(e ->
                showMoviesByGenre(genre)
        );

        button.setOnMousePressed(e -> {
            if (!genre.equals(selectedGenre)) {
                button.setTranslateY(2);
            }
        });

        button.setOnMouseReleased(e ->
                button.setTranslateY(0)
        );

        return button;
    }

    private Button createViewButton(String text) {
        Button button = new Button(text);
        ThemeManager.styleButton(button);
        button.setPrefSize(44, 44);
        button.setStyle(
                ThemeManager.primaryTextStyle() +
                        "-fx-font-size: 20px;" +
                        "-fx-background-radius: 14px;"
        );
        return button;
    }

    private void showMoviesByGenre(String genre) {
        selectedGenre = genre;
        displayedMovies = 20;

        for (int i = 0; i < genreButtons.size(); i++) {
            Button button = genreButtons.get(i);
            String genreName = genres[i];

            if (genreName.equals(selectedGenre)) {
                button.setScaleX(1.03);
                button.setScaleY(1.03);
            } else {
                button.setScaleX(1);
                button.setScaleY(1);
            }
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
            movieGrid.getChildren().add(
                    createMovieCard(filtered.get(i))
            );
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
            Label noMovies = new Label(
                    "No " + selectedGenre + " movies found."
            );

            noMovies.setStyle(
                    ThemeManager.secondaryTextStyle() +
                            "-fx-font-size: 18px;" +
                            "-fx-font-weight: 500;"
            );

            movieGrid.getChildren().add(noMovies);
        }
    }

    private VBox createMovieCard(Movie movie) {
        VBox card = new VBox();
        card.setPrefWidth(220);
        card.setMaxWidth(220);
        ThemeManager.styleCard(card);

        StackPane posterBox = new StackPane();
        posterBox.setPrefSize(220, 330);
        posterBox.setMinSize(220, 330);
        posterBox.setMaxSize(220, 330);

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
        ThemeManager.styleButton(bookmark);
        bookmark.setPrefSize(40, 40);

        bookmark.setOnMouseEntered(e -> {
            bookmark.setScaleX(1.08);
            bookmark.setScaleY(1.08);
        });

        bookmark.setOnMouseExited(e -> {
            bookmark.setScaleX(1);
            bookmark.setScaleY(1);
        });

        StackPane.setAlignment(bookmark, Pos.TOP_RIGHT);
        StackPane.setMargin(bookmark, new Insets(10));
        posterBox.getChildren().add(bookmark);

        VBox info = new VBox(7);
        info.setPadding(new Insets(14));

        String titleText = movie.getTitle();

        if (titleText == null || titleText.isBlank()) {
            titleText = "Unknown Movie";
        }

        Label title = new Label(titleText);
        title.setMaxWidth(190);
        title.setEllipsisString("...");
        title.setStyle(
                ThemeManager.primaryTextStyle() +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: 700;"
        );

        HBox metadata = new HBox();
        metadata.setAlignment(Pos.CENTER_LEFT);

        Label year = new Label(getYear(movie));
        year.setStyle(
                ThemeManager.secondaryTextStyle() +
                        "-fx-font-size: 14px;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label genre = new Label(getGenreText(movie));
        genre.setPadding(new Insets(4, 9, 4, 9));
        genre.setStyle(
                ThemeManager.secondaryTextStyle() +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-background-radius: 8px;"
        );

        metadata.getChildren().addAll(year, spacer, genre);

        Label rating = new Label("★ " + getRating(movie));
        rating.setStyle(
                ThemeManager.primaryTextStyle() +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: 700;"
        );

        info.getChildren().addAll(title, metadata, rating);
        card.getChildren().addAll(posterBox, info);

        card.setOnMouseClicked(e -> {
            if (e.getTarget() == bookmark) {
                return;
            }

            if (onMovieSelected != null) {
                onMovieSelected.accept(movie);
            } else {
                javafx.stage.Stage stage =
                        (javafx.stage.Stage) card.getScene().getWindow();

                new MovieDetailsPage(movie, movies).show(stage);
            }
        });

        card.setOnMouseEntered(e -> {
            card.setScaleX(1.025);
            card.setScaleY(1.025);
            card.setTranslateY(-3);
        });

        card.setOnMouseExited(e -> {
            card.setScaleX(1);
            card.setScaleY(1);
            card.setTranslateY(0);
        });

        return card;
    }

    private StackPane createPlaceholder(Movie movie) {
        StackPane pane = new StackPane();
        pane.setStyle("-fx-background-radius: 18px 18px 0 0;");

        String title = movie.getTitle();

        Label label = new Label(
                title == null || title.isBlank()
                        ? "Unknown Movie"
                        : title
        );

        label.setWrapText(true);
        label.setMaxWidth(180);
        label.setAlignment(Pos.CENTER);
        label.setStyle(
                ThemeManager.secondaryTextStyle() +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: 700;"
        );

        pane.getChildren().add(label);
        return pane;
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

    private String getRating(Movie movie) {
        try {
            return String.format("%.1f", movie.getVoteAverage());
        } catch (Exception e) {
            return "N/A";
        }
    }

    private String getGenreText(Movie movie) {
        try {
            if (movie.getGenres() != null &&
                    !movie.getGenres().isEmpty()) {

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
package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Movie;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MovieDetailsPage {

    private static final String PRIMARY = "#AB3500";
    private static final String PRIMARY_CONTAINER = "#FF6B35";
    private static final String BACKGROUND = "#F9F9F7";
    private static final String SURFACE = "#FFFFFF";
    private static final String SURFACE_CONTAINER = "#EEEEEC";
    private static final String TEXT = "#1A1C1B";
    private static final String SECONDARY_TEXT = "#594139";
    private static final String BORDER = "#E1BFB5";

    private final Movie movie;
    private final List<Movie> allMovies;
    private final Runnable onHome;
    private final Runnable onSearch;
    private final Runnable onGenres;
    private final Consumer<Movie> onMovieSelected;
    private BorderPane root;

    public MovieDetailsPage(Movie movie, List<Movie> allMovies, Runnable onHome, Runnable onSearch, Runnable onGenres, Consumer<Movie> onMovieSelected) {
        this.movie = movie;
        this.allMovies = allMovies != null ? allMovies : new ArrayList<>();
        this.onHome = onHome;
        this.onSearch = onSearch;
        this.onGenres = onGenres;
        this.onMovieSelected = onMovieSelected;
        createPage();
    }

    public MovieDetailsPage(Movie movie, List<Movie> allMovies) {
        this(movie, allMovies, null, null, null, null);
    }

    private void createPage() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: " + BACKGROUND + ";");
        root.setTop(createNavigationBar());

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent;-fx-background: transparent;");

        VBox content = new VBox(50);
        content.setPadding(new Insets(40, 64, 80, 64));
        content.getChildren().add(createHeroSection());
        content.getChildren().add(createDetailsSection());
        content.getChildren().add(createRecommendationsSection());

        scrollPane.setContent(content);
        root.setCenter(scrollPane);
    }

    public void show(Stage stage) {
        if (stage == null) {
            return;
        }

        if (root == null) {
            createPage();
        }

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("MovieFlix - " + getMovieTitle());
        stage.setScene(scene);
        stage.show();
    }

    private HBox createNavigationBar() {
        HBox navBar = new HBox();
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(18, 64, 18, 64));
        navBar.setPrefHeight(80);
        navBar.setStyle("-fx-background-color: rgba(249,249,247,0.96);-fx-border-color: " + BORDER + ";-fx-border-width: 0 0 1 0;");

        Label logo = new Label("MovieFlix");
        logo.setStyle("-fx-font-family: 'Inter';-fx-font-size: 24px;-fx-font-weight: 900;-fx-text-fill: " + PRIMARY + ";");

        Button homeButton = createNavButton("Home");
        Button searchButton = createNavButton("Search");
        Button genresButton = createNavButton("Genres");

        homeButton.setOnAction(e -> {
            if (onHome != null) {
                onHome.run();
            } else {
                Stage stage = (Stage) homeButton.getScene().getWindow();
                new SearchPage(allMovies).show(stage);
            }
        });

        searchButton.setOnAction(e -> {
            if (onSearch != null) {
                onSearch.run();
            } else {
                Stage stage = (Stage) searchButton.getScene().getWindow();
                new SearchPage(allMovies).show(stage);
            }
        });

        genresButton.setOnAction(e -> {
            if (onGenres != null) {
                onGenres.run();
            } else {
                Stage stage = (Stage) genresButton.getScene().getWindow();
                new GenrePage(allMovies).show(stage);
            }
        });

        HBox navigation = new HBox(28);
        navigation.setAlignment(Pos.CENTER_LEFT);
        navigation.getChildren().addAll(homeButton, searchButton, genresButton);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button favoriteButton = createIconButton("♥");
        favoriteButton.setOnAction(e -> favoriteButton.setText("♥"));

        Button watchlistButton = createIconButton("🔖");
        watchlistButton.setOnAction(e -> watchlistButton.setText("✓"));

        Label profile = new Label("●");
        profile.setStyle("-fx-font-size: 25px;-fx-text-fill: " + PRIMARY + ";");

        navBar.getChildren().addAll(logo, navigation, spacer, favoriteButton, watchlistButton, profile);
        return navBar;
    }

    private Button createNavButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: transparent;-fx-text-fill: " + SECONDARY_TEXT + ";-fx-font-family: 'Inter';-fx-font-size: 14px;-fx-font-weight: 500;-fx-cursor: hand;");
        return button;
    }

    private Button createIconButton(String icon) {
        Button button = new Button(icon);
        button.setStyle("-fx-background-color: transparent;-fx-text-fill: " + SECONDARY_TEXT + ";-fx-font-size: 20px;-fx-cursor: hand;");
        return button;
    }

    private VBox createHeroSection() {
        VBox hero = new VBox();
        HBox heroContent = new HBox(40);

        VBox posterBox = new VBox();
        posterBox.setPrefWidth(280);
        posterBox.setMinWidth(280);
        posterBox.setStyle("-fx-background-color: white;-fx-background-radius: 12px;-fx-border-color: " + BORDER + ";-fx-border-radius: 12px;");

        ImageView poster = createPoster();
        poster.setFitWidth(280);
        poster.setFitHeight(420);
        poster.setPreserveRatio(false);

        Rectangle clip = new Rectangle(280, 420);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        poster.setClip(clip);
        posterBox.getChildren().add(poster);

        VBox information = new VBox(18);
        HBox.setHgrow(information, Priority.ALWAYS);

        Label title = new Label(getMovieTitle());
        title.setWrapText(true);
        title.setStyle("-fx-font-family: 'Inter';-fx-font-size: 48px;-fx-font-weight: 700;-fx-text-fill: " + TEXT + ";");

        HBox meta = new HBox(20);
        meta.setAlignment(Pos.CENTER_LEFT);

        Label rating = new Label("★  " + getRating());
        rating.setStyle("-fx-text-fill: " + PRIMARY_CONTAINER + ";-fx-font-size: 15px;-fx-font-weight: bold;");

        Label year = new Label(getYear());
        year.setStyle(metaStyle());

        Label runtime = new Label(getRuntime());
        runtime.setStyle(metaStyle());

        meta.getChildren().addAll(rating, year, runtime);

        FlowPane genrePane = new FlowPane();
        genrePane.setHgap(10);
        genrePane.setVgap(10);

        for (String genre : getGenres()) {
            Label genreLabel = new Label(genre);
            genreLabel.setPadding(new Insets(7, 12, 7, 12));
            genreLabel.setStyle("-fx-background-color: " + SURFACE_CONTAINER + ";-fx-background-radius: 8px;-fx-text-fill: #5E5E5E;-fx-font-size: 14px;");
            genrePane.getChildren().add(genreLabel);
        }

        Label overviewTitle = createSectionTitle("Overview");
        Label overview = new Label(getOverview());
        overview.setWrapText(true);
        overview.setMaxWidth(850);
        overview.setStyle("-fx-font-family: 'Inter';-fx-font-size: 17px;-fx-text-fill: " + SECONDARY_TEXT + ";-fx-line-spacing: 5px;");

        Label keywordsTitle = createSectionTitle("Keywords");

        FlowPane keywordPane = new FlowPane();
        keywordPane.setHgap(14);
        keywordPane.setVgap(8);

        for (String keyword : getKeywords()) {
            Label keywordLabel = new Label("#" + keyword);
            keywordLabel.setStyle("-fx-text-fill: " + PRIMARY + ";-fx-font-size: 14px;-fx-font-weight: 500;");
            keywordPane.getChildren().add(keywordLabel);
        }

        HBox actions = new HBox(15);
        actions.setPadding(new Insets(10, 0, 0, 0));

        Button trailerButton = createPrimaryButton("Watch Trailer");
        trailerButton.setOnAction(e -> showTrailerMessage());

        Button watchlistButton = createSecondaryButton("Add to Watchlist");
        watchlistButton.setOnAction(e -> watchlistButton.setText("✓ Added to Watchlist"));

        actions.getChildren().addAll(trailerButton, watchlistButton);

        information.getChildren().addAll(title, meta, genrePane, overviewTitle, overview, keywordsTitle, keywordPane, actions);
        heroContent.getChildren().addAll(posterBox, information);
        hero.getChildren().add(heroContent);

        return hero;
    }

    private VBox createDetailsSection() {
        VBox section = new VBox(18);
        Label title = createSectionTitle("Details");

        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(18);

        VBox languageCard = createDetailCard("Original Language", getLanguage());
        VBox releaseCard = createDetailCard("Release Date", getReleaseDate());
        VBox taglineCard = createDetailCard("Tagline", getTagline());

        GridPane.setColumnIndex(languageCard, 0);
        GridPane.setColumnIndex(releaseCard, 1);
        GridPane.setColumnIndex(taglineCard, 2);

        ColumnConstraints c1 = new ColumnConstraints();
        ColumnConstraints c2 = new ColumnConstraints();
        ColumnConstraints c3 = new ColumnConstraints();

        c1.setPercentWidth(25);
        c2.setPercentWidth(25);
        c3.setPercentWidth(50);

        grid.getColumnConstraints().addAll(c1, c2, c3);
        grid.getChildren().addAll(languageCard, releaseCard, taglineCard);
        section.getChildren().addAll(title, grid);

        return section;
    }

    private VBox createDetailCard(String heading, String value) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(22));
        card.setMinHeight(95);
        card.setStyle("-fx-background-color: " + SURFACE + ";-fx-background-radius: 12px;-fx-border-color: #E2E3E1;-fx-border-radius: 12px;");

        Label headingLabel = new Label(heading);
        headingLabel.setStyle("-fx-text-fill: #5E5E5E;-fx-font-size: 12px;-fx-font-weight: bold;");

        Label valueLabel = new Label(value);
        valueLabel.setWrapText(true);
        valueLabel.setStyle("-fx-text-fill: " + TEXT + ";-fx-font-size: 16px;");

        card.getChildren().addAll(headingLabel, valueLabel);
        return card;
    }

    private VBox createRecommendationsSection() {
        VBox section = new VBox(20);
        Label title = createSectionTitle("You May Also Like");

        HBox movieContainer = new HBox(20);
        movieContainer.setAlignment(Pos.TOP_LEFT);

        List<Movie> recommendations = getRecommendations();

        for (Movie recommended : recommendations) {
            VBox card = createMovieCard(recommended);
            movieContainer.getChildren().add(card);
        }

        section.getChildren().addAll(title, movieContainer);
        return section;
    }

    private VBox createMovieCard(Movie recommendedMovie) {
        VBox card = new VBox();
        card.setPrefWidth(220);
        card.setStyle("-fx-background-color: " + SURFACE + ";-fx-background-radius: 12px;-fx-border-color: transparent;-fx-border-radius: 12px;-fx-cursor: hand;");

        ImageView imageView = createPosterForMovie(recommendedMovie);
        imageView.setFitWidth(220);
        imageView.setFitHeight(330);
        imageView.setPreserveRatio(false);

        Rectangle clip = new Rectangle(220, 330);
        clip.setArcWidth(18);
        clip.setArcHeight(18);
        imageView.setClip(clip);

        Label title = new Label(getMovieTitle(recommendedMovie));
        title.setMaxWidth(210);
        title.setWrapText(false);
        title.setStyle("-fx-font-size: 17px;-fx-font-weight: 600;-fx-text-fill: " + TEXT + ";");

        Label meta = new Label(getYear(recommendedMovie) + " • " + getGenresString(recommendedMovie));
        meta.setStyle("-fx-font-size: 13px;-fx-text-fill: #5E5E5E;");

        VBox text = new VBox(6);
        text.setPadding(new Insets(12, 8, 12, 8));
        text.getChildren().addAll(title, meta);

        card.getChildren().addAll(imageView, text);

        card.setOnMouseClicked(e -> {
            if (onMovieSelected != null) {
                onMovieSelected.accept(recommendedMovie);
            } else {
                Stage stage = (Stage) card.getScene().getWindow();
                new MovieDetailsPage(recommendedMovie, allMovies).show(stage);
            }
        });

        return card;
    }

    private ImageView createPoster() {
        return createPosterForMovie(movie);
    }

    private ImageView createPosterForMovie(Movie m) {
        ImageView imageView = new ImageView();
        String posterUrl = getPosterUrl(m);

        if (posterUrl != null && !posterUrl.isEmpty()) {
            try {
                Image image = new Image(posterUrl, true);
                imageView.setImage(image);
            } catch (Exception ignored) {
            }
        }

        return imageView;
    }

    private List<Movie> getRecommendations() {
        List<Movie> result = new ArrayList<>();

        if (allMovies == null || movie == null) {
            return result;
        }

        List<String> currentGenres = getGenres(movie);

        for (Movie candidate : allMovies) {
            if (candidate == null) {
                continue;
            }

            if (candidate == movie) {
                continue;
            }

            boolean sameGenre = false;

            for (String genre : getGenres(candidate)) {
                if (currentGenres.contains(genre)) {
                    sameGenre = true;
                    break;
                }
            }

            if (sameGenre) {
                result.add(candidate);

                if (result.size() >= 4) {
                    break;
                }
            }
        }

        return result;
    }

    private Label createSectionTitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-family: 'Inter';-fx-font-size: 24px;-fx-font-weight: 600;-fx-text-fill: " + TEXT + ";");
        return label;
    }

    private Button createPrimaryButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + PRIMARY_CONTAINER + ";-fx-text-fill: white;-fx-font-size: 14px;-fx-font-weight: bold;-fx-padding: 12 24;-fx-background-radius: 8px;-fx-cursor: hand;");
        return button;
    }

    private Button createSecondaryButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: white;-fx-border-color: " + PRIMARY + ";-fx-border-width: 1px;-fx-text-fill: " + TEXT + ";-fx-font-size: 14px;-fx-font-weight: 500;-fx-padding: 12 24;-fx-background-radius: 8px;-fx-border-radius: 8px;-fx-cursor: hand;");
        return button;
    }

    private String metaStyle() {
        return "-fx-text-fill: " + SECONDARY_TEXT + ";-fx-font-size: 14px;";
    }

    private void showTrailerMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("MovieFlix");
        alert.setHeaderText("Trailer");
        alert.setContentText("Trailer functionality can be connected to the movie's YouTube/trailer URL.");
        alert.showAndWait();
    }

    public BorderPane getRoot() {
        return root;
    }

    private String getMovieTitle() {
        return getMovieTitle(movie);
    }

    private String getMovieTitle(Movie m) {
        if (m == null || m.getTitle() == null || m.getTitle().isEmpty()) {
            return "Unknown Movie";
        }

        return m.getTitle();
    }

    private String getRating() {
        if (movie == null) {
            return "N/A";
        }

        return String.format("%.1f", movie.getVoteAverage());
    }

    private String getYear() {
        return getYear(movie);
    }

    private String getYear(Movie m) {
        if (m == null || m.getReleaseDate() == null || m.getReleaseDate().isEmpty()) {
            return "N/A";
        }

        String date = m.getReleaseDate();

        if (date.length() >= 4) {
            return date.substring(0, 4);
        }

        return date;
    }

    private String getRuntime() {
        if (movie == null || movie.getRuntime() == null) {
            return "N/A";
        }

        return movie.getRuntime() + " min";
    }

    private String getOverview() {
        if (movie == null || movie.getOverview() == null || movie.getOverview().isEmpty()) {
            return "No overview available.";
        }

        return movie.getOverview();
    }

    private List<String> getGenres() {
        return getGenres(movie);
    }

    private List<String> getGenres(Movie m) {
        List<String> genres = new ArrayList<>();

        if (m == null || m.getGenres() == null) {
            return genres;
        }

        m.getGenres().forEach(genre -> {
            if (genre != null && genre.getName() != null) {
                genres.add(genre.getName());
            }
        });

        return genres;
    }

    private String getGenresString(Movie m) {
        List<String> genres = getGenres(m);

        if (genres.isEmpty()) {
            return "Movie";
        }

        return String.join(", ", genres);
    }

    private List<String> getKeywords() {
        List<String> keywords = new ArrayList<>();

        if (movie == null || movie.getKeywords() == null) {
            return keywords;
        }

        movie.getKeywords().forEach(keyword -> {
            if (keyword != null && keyword.getName() != null) {
                keywords.add(keyword.getName());
            }
        });

        if (keywords.size() > 8) {
            return new ArrayList<>(keywords.subList(0, 8));
        }

        return keywords;
    }

    private String getLanguage() {
        if (movie == null || movie.getOriginalLanguage() == null || movie.getOriginalLanguage().isEmpty()) {
            return "Unknown";
        }

        return movie.getOriginalLanguage();
    }

    private String getReleaseDate() {
        if (movie == null || movie.getReleaseDate() == null || movie.getReleaseDate().isEmpty()) {
            return "Unknown";
        }

        return movie.getReleaseDate();
    }

    private String getTagline() {
        if (movie == null || movie.getTagline() == null || movie.getTagline().isEmpty()) {
            return "No tagline available.";
        }

        return movie.getTagline();
    }

    private String getPosterUrl(Movie m) {
        if (m == null || m.getPosterUrl() == null) {
            return "";
        }

        String url = m.getPosterUrl();

        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }

        if (url.startsWith("/")) {
            return "https://image.tmdb.org/t/p/w500" + url;
        }

        return url;
    }
}

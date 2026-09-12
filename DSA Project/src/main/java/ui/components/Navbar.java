package ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Movie;
import theme.Theme;
import ui.GenrePage;
import ui.SearchPage;

import java.util.List;

public class Navbar {

    public static HBox create(List<Movie> movies, String activePage) {

        HBox navbar = new HBox();
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setPadding(new Insets(0, 64, 0, 64));
        navbar.setPrefHeight(Theme.NAVBAR_HEIGHT);

        navbar.setStyle(
                "-fx-background-color: " + toHex(Theme.BACKGROUND) + ";" +
                        "-fx-border-color: " + toHex(Theme.BORDER) + ";" +
                        "-fx-border-width: 0 0 1 0;"
        );

        // Logo
        Label logo = new Label("MovieFlix");
        logo.setFont(Theme.HEADING);
        logo.setTextFill(Theme.PRIMARY);

        // Navigation
        HBox navigation = new HBox(24);
        navigation.setAlignment(Pos.CENTER_LEFT);
        navigation.setPadding(new Insets(0, 0, 0, 48));

        // HOME
        Button homeButton = createNavButton(
                "Home",
                activePage != null && activePage.equalsIgnoreCase("Home")
        );

        // GENRES
        Button genresButton = createNavButton(
                "Genres",
                activePage != null && activePage.equalsIgnoreCase("Genres")
        );

        // Home navigation
        homeButton.setOnAction(e -> {
            Stage stage = (Stage) homeButton.getScene().getWindow();

            /*
             * Your Home/Search page.
             * If SearchPage is currently your Home page,
             * keep this as SearchPage.
             */
            new SearchPage(movies).show(stage);
        });

        // Genre navigation
        genresButton.setOnAction(e -> {
            Stage stage = (Stage) genresButton.getScene().getWindow();
            new GenrePage(movies).show(stage);
        });

        navigation.getChildren().addAll(
                homeButton,
                genresButton
        );

        // Flexible space
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Right-side buttons
        Button favorite = createIconButton("♥");
        Button watchlist = createIconButton("🔖");

        Circle circle = new Circle(20, Theme.SURFACE);
        circle.setStroke(Theme.BORDER);

        Label profileText = new Label("U");
        profileText.setFont(Theme.BODY);
        profileText.setTextFill(Theme.TEXT_PRIMARY);

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

    private static Button createNavButton(
            String text,
            boolean active
    ) {

        Button button = new Button(text);

        button.setStyle(
                navButtonStyle(active)
        );

        button.setOnMouseEntered(e ->
                button.setStyle(
                        navButtonHoverStyle(active)
                )
        );

        button.setOnMouseExited(e ->
                button.setStyle(
                        navButtonStyle(active)
                )
        );

        return button;
    }

    private static String navButtonStyle(
            boolean active
    ) {

        Color textColor =
                active
                        ? Theme.PRIMARY
                        : Theme.TEXT_SECONDARY;

        return
                "-fx-background-color: transparent;" +
                        "-fx-border-width: 0;" +
                        "-fx-padding: 6 0 8 0;" +
                        "-fx-font-family: '" +
                        Theme.FONT_FAMILY +
                        "';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: " +
                        (active ? "700" : "500") +
                        ";" +
                        "-fx-text-fill: " +
                        toHex(textColor) +
                        ";" +
                        "-fx-cursor: hand;";
    }

    private static String navButtonHoverStyle(
            boolean active
    ) {

        return
                "-fx-background-color: transparent;" +
                        "-fx-border-width: 0;" +
                        "-fx-padding: 6 0 8 0;" +
                        "-fx-font-family: '" +
                        Theme.FONT_FAMILY +
                        "';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: 700;" +
                        "-fx-text-fill: " +
                        toHex(Theme.PRIMARY) +
                        ";" +
                        "-fx-cursor: hand;";
    }

    private static Button createIconButton(
            String text
    ) {

        Button button = new Button(text);

        button.setStyle(
                iconButtonStyle(false)
        );

        button.setOnMouseEntered(e ->
                button.setStyle(
                        iconButtonStyle(true)
                )
        );

        button.setOnMouseExited(e ->
                button.setStyle(
                        iconButtonStyle(false)
                )
        );

        return button;
    }

    private static String iconButtonStyle(
            boolean hover
    ) {

        Color textColor =
                hover
                        ? Theme.PRIMARY
                        : Theme.TEXT_PRIMARY;

        return
                "-fx-background-color: transparent;" +
                        "-fx-font-family: '" +
                        Theme.FONT_FAMILY +
                        "';" +
                        "-fx-font-size: 20px;" +
                        "-fx-text-fill: " +
                        toHex(textColor) +
                        ";" +
                        "-fx-cursor: hand;";
    }

    private static String toHex(
            Color color
    ) {

        return String.format(
                "#%02X%02X%02X",
                (int) Math.round(color.getRed() * 255),
                (int) Math.round(color.getGreen() * 255),
                (int) Math.round(color.getBlue() * 255)
        );
    }
}
package ui;

import data.UserDataLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Movie;
import theme.Theme;
import theme.ThemeManager;

import java.util.List;

public class CreateAccountPage {

    private final List<Movie> movies;

    public CreateAccountPage(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            this.movies = data.MovieDataLoader.loadMovies(
                    variables.Variables.filePath
            );
        } else {
            this.movies = movies;
        }
    }

    public CreateAccountPage() {
        this(null);
    }

    public void show(Stage stage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: " + toHex(Theme.BACKGROUND) + ";");
        root.setPadding(new Insets(30));

        VBox card = new VBox();
        card.setMaxWidth(448);
        card.setPrefWidth(448);
        card.setPadding(new Insets(36, 42, 36, 42));
        card.setSpacing(18);
        ThemeManager.styleCard(card);

        VBox header = new VBox(6);
        header.setAlignment(Pos.CENTER);

        Label logo = new Label("MovieFlix");
        logo.setFont(Font.font(
                Theme.FONT_FAMILY,
                javafx.scene.text.FontWeight.BOLD,
                36
        ));
        logo.setTextFill(Theme.PRIMARY);

        Label subtitle = new Label("Join the premium cinematic experience.");
        subtitle.setWrapText(true);
        subtitle.setAlignment(Pos.CENTER);
        subtitle.setFont(Theme.BODY);
        subtitle.setTextFill(Theme.TEXT_SECONDARY);

        header.getChildren().addAll(logo, subtitle);

        VBox nameBox = new VBox(8);
        Label nameLabel = createLabel("Full Name");
        TextField nameField = new TextField();
        nameField.setPromptText("John Doe");
        ThemeManager.styleInput(nameField);
        nameBox.getChildren().addAll(nameLabel, nameField);

        VBox emailBox = new VBox(8);
        Label emailLabel = createLabel("Email Address");
        TextField emailField = new TextField();
        emailField.setPromptText("john@example.com");
        ThemeManager.styleInput(emailField);
        emailBox.getChildren().addAll(emailLabel, emailField);

        VBox passwordBox = new VBox(8);
        Label passwordLabel = createLabel("Password");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("••••••••");
        ThemeManager.stylePasswordField(passwordField);
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        VBox confirmPasswordBox = new VBox(8);
        Label confirmPasswordLabel = createLabel("Confirm Password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("••••••••");
        ThemeManager.stylePasswordField(confirmPasswordField);
        confirmPasswordBox.getChildren().addAll(
                confirmPasswordLabel,
                confirmPasswordField
        );

        Button createButton = new Button("Create Account");
        createButton.setMaxWidth(Double.MAX_VALUE);
        createButton.setPrefHeight(Theme.BUTTON_HEIGHT);
        createButton.setFont(Theme.BODY);
        createButton.setTextFill(Color.WHITE);
        ThemeManager.styleButton(createButton);

        createButton.setOnAction(event -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (name.isEmpty() ||
                    email.isEmpty() ||
                    password.isEmpty() ||
                    confirmPassword.isEmpty()) {

                showError(
                        "Missing Information",
                        "Please fill in all fields."
                );
                return;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                showError(
                        "Invalid Email",
                        "Please enter a valid email address."
                );
                return;
            }

            if (password.length() < 6) {
                showError(
                        "Weak Password",
                        "Password must contain at least 6 characters."
                );
                return;
            }

            if (!password.equals(confirmPassword)) {
                showError(
                        "Password Mismatch",
                        "Passwords do not match."
                );
                return;
            }

            if (UserDataLoader.userExists(email)) {
                showError(
                        "Account Already Exists",
                        "An account with this email already exists."
                );
                return;
            }

            boolean created = UserDataLoader.addUser(email, password);

            if (!created) {
                showError(
                        "Registration Failed",
                        "Unable to create your account."
                );
                return;
            }

            showSuccess(
                    "Account Created",
                    "Welcome to MovieFlix, " + name +
                            "!\n\nYour account has been created successfully."
            );

            LoginPage loginPage = new LoginPage(movies);
            loginPage.show(stage);
        });

        HBox signInBox = new HBox(5);
        signInBox.setAlignment(Pos.CENTER);

        Label alreadyAccount = new Label("Already have an account?");
        alreadyAccount.setFont(Theme.SMALL);
        alreadyAccount.setTextFill(Theme.TEXT_SECONDARY);

        Hyperlink signIn = new Hyperlink("Sign in instead");
        signIn.setFont(Theme.BODY);
        signIn.setTextFill(Theme.PRIMARY);
        signIn.setBorder(Border.EMPTY);
        signIn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-padding: 4 6;" +
                        "-fx-cursor: hand;"
        );

        signIn.setOnMouseEntered(event ->
                signIn.setTextFill(Theme.PRIMARY_HOVER)
        );

        signIn.setOnMouseExited(event ->
                signIn.setTextFill(Theme.PRIMARY)
        );

        signIn.setOnAction(event -> {
            LoginPage loginPage = new LoginPage(movies);
            loginPage.show(stage);
        });

        signInBox.getChildren().addAll(alreadyAccount, signIn);

        Label terms = new Label(
                "By creating an account, you agree to our " +
                        "Terms of Service and Privacy Policy."
        );

        terms.setWrapText(true);
        terms.setMaxWidth(350);
        terms.setAlignment(Pos.CENTER);
        terms.setTextAlignment(
                javafx.scene.text.TextAlignment.CENTER
        );
        terms.setFont(Theme.SMALL);
        terms.setTextFill(Theme.TEXT_SECONDARY);

        card.getChildren().addAll(
                header,
                nameBox,
                emailBox,
                passwordBox,
                confirmPasswordBox,
                createButton,
                signInBox,
                terms
        );

        root.getChildren().add(card);

        Scene scene = new Scene(root, 1280, 720);
        ThemeManager.applyTheme(scene);

        stage.setTitle("MovieFlix - Create Account");
        stage.setScene(scene);
        stage.show();
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Theme.BODY);
        label.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-text-fill: " +
                        toHex(Theme.TEXT_PRIMARY) +
                        ";"
        );
        return label;
    }

    private static String toHex(Color color) {
        return String.format(
                "#%02X%02X%02X",
                (int) Math.round(color.getRed() * 255),
                (int) Math.round(color.getGreen() * 255),
                (int) Math.round(color.getBlue() * 255)
        );
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
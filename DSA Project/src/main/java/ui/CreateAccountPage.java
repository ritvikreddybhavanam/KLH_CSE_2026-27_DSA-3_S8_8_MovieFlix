package ui;

import data.UserDataLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CreateAccountPage {
    private static final String PRIMARY = "#AB3500";
    private static final String PRIMARY_CONTAINER = "#FF6B35";
    private static final String SURFACE = "#F9F9F7";
    private static final String CARD = "#FFFFFF";
    private static final String TEXT = "#1A1C1B";
    private static final String SECONDARY = "#5F5E5E";
    private static final String BORDER = "#E2E3E1";

    private final java.util.List<model.Movie> movies;

    public CreateAccountPage(java.util.List<model.Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            this.movies = data.MovieDataLoader.loadMovies(variables.Variables.filePath);
        } else {
            this.movies = movies;
        }
    }

    public CreateAccountPage() {
        this(null);
    }

    public void show(Stage stage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: " + SURFACE + ";");
        root.setPadding(new Insets(24));

        VBox card = new VBox();
        card.setMaxWidth(448);
        card.setPrefWidth(448);
        card.setPadding(new Insets(48));
        card.setSpacing(20);
        card.setStyle(
                "-fx-background-color: " + CARD + ";" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 20, 0.2, 0, 4);"
        );

        VBox header = new VBox(4);
        header.setAlignment(Pos.CENTER);

        Label logo = new Label("MovieFlix");
        logo.setFont(Font.font("Inter", FontWeight.BOLD, 40));
        logo.setTextFill(Color.web(PRIMARY));

        Label subtitle = new Label("Join the premium cinematic experience.");
        subtitle.setFont(Font.font("Inter", FontWeight.NORMAL, 16));
        subtitle.setTextFill(Color.web(SECONDARY));

        header.getChildren().addAll(logo, subtitle);

        VBox nameBox = new VBox(8);
        Label nameLabel = createLabel("Full Name");
        TextField nameField = new TextField();
        nameField.setPromptText("John Doe");
        nameField.setPrefHeight(48);
        styleTextField(nameField);
        nameBox.getChildren().addAll(nameLabel, nameField);

        VBox emailBox = new VBox(8);
        Label emailLabel = createLabel("Email Address");
        TextField emailField = new TextField();
        emailField.setPromptText("john@example.com");
        emailField.setPrefHeight(48);
        styleTextField(emailField);
        emailBox.getChildren().addAll(emailLabel, emailField);

        VBox passwordBox = new VBox(8);
        Label passwordLabel = createLabel("Password");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("••••••••");
        passwordField.setPrefHeight(48);
        styleTextField(passwordField);
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        VBox confirmPasswordBox = new VBox(8);
        Label confirmPasswordLabel = createLabel("Confirm Password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("••••••••");
        confirmPasswordField.setPrefHeight(48);
        styleTextField(confirmPasswordField);
        confirmPasswordBox.getChildren().addAll(confirmPasswordLabel, confirmPasswordField);

        Button createButton = new Button("Create Account");
        createButton.setMaxWidth(Double.MAX_VALUE);
        createButton.setPrefHeight(48);
        createButton.setFont(Font.font("Inter", FontWeight.SEMI_BOLD, 14));
        createButton.setTextFill(Color.WHITE);
        setButtonStyle(createButton, PRIMARY_CONTAINER);

        createButton.setOnAction(event -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showError("Missing Information", "Please fill in all fields.");
                return;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                showError("Invalid Email", "Please enter a valid email address.");
                return;
            }

            if (password.length() < 6) {
                showError("Weak Password", "Password must contain at least 6 characters.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showError("Password Mismatch", "Passwords do not match.");
                return;
            }

            if (UserDataLoader.userExists(email)) {
                showError("Account Already Exists", "An account with this email already exists.");
                return;
            }

            boolean created = UserDataLoader.addUser(email, password);

            if (!created) {
                showError("Registration Failed", "Unable to create your account.");
                return;
            }

            showSuccess(
                    "Account Created",
                    "Welcome to MovieFlix, " + name + "!\n\nYour account has been created successfully."
            );

            LoginPage loginPage = new LoginPage(movies);
            loginPage.show(stage);
        });

        HBox signInBox = new HBox(5);
        signInBox.setAlignment(Pos.CENTER);

        Label alreadyAccount = new Label("Already have an account?");
        alreadyAccount.setFont(Font.font("Inter", FontWeight.NORMAL, 14));
        alreadyAccount.setTextFill(Color.web(SECONDARY));

        Hyperlink signIn = new Hyperlink("Sign in instead");
        signIn.setFont(Font.font("Inter", FontWeight.SEMI_BOLD, 14));
        signIn.setTextFill(Color.web(PRIMARY_CONTAINER));
        signIn.setBorder(Border.EMPTY);

        signIn.setOnAction(event -> {
            LoginPage loginPage = new LoginPage(movies);
            loginPage.show(stage);
        });

        signInBox.getChildren().addAll(alreadyAccount, signIn);

        Label terms = new Label(
                "By creating an account, you agree to our Terms of Service and Privacy Policy."
        );
        terms.setWrapText(true);
        terms.setMaxWidth(350);
        terms.setAlignment(Pos.CENTER);
        terms.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        terms.setFont(Font.font("Inter", FontWeight.NORMAL, 11));
        terms.setTextFill(Color.web("#5E5E5E"));

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
        stage.setTitle("MovieFlix - Create Account");
        stage.setScene(scene);
        stage.show();
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Inter", FontWeight.MEDIUM, 14));
        label.setTextFill(Color.web("#594139"));
        return label;
    }

    private void styleTextField(TextField field) {
        field.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 0 12;" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: " + TEXT + ";"
        );
    }

    private void setButtonStyle(Button button, String color) {
        button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(event -> button.setStyle(
                "-fx-background-color: " + PRIMARY + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-cursor: hand;"
        ));

        button.setOnMouseExited(event -> button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-cursor: hand;"
        ));
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
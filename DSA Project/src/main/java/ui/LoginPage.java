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

public class LoginPage {
    private static final String PRIMARY = "#AB3500";
    private static final String PRIMARY_CONTAINER = "#FF6B35";
    private static final String SURFACE = "#F9F9F7";
    private static final String CARD = "#FFFFFF";
    private static final String TEXT = "#1A1C1B";
    private static final String SECONDARY = "#5F5E5E";
    private static final String BORDER = "#E2E3E1";

    private final java.util.List<model.Movie> movies;

    public LoginPage() {
        this(null);
    }

    public LoginPage(java.util.List<model.Movie> movies) {
        this.movies = movies;
    }

    public void show(Stage stage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: " + SURFACE + ";");
        root.setPadding(new Insets(24));

        VBox card = new VBox(20);
        card.setMaxWidth(448);
        card.setPrefWidth(448);
        card.setPadding(new Insets(48));
        card.setStyle("-fx-background-color: " + CARD + ";" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 16;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 20, 0.2, 0, 4);");

        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);

        Label logo = new Label("MovieFlix");
        logo.setFont(Font.font("Inter", FontWeight.BOLD, 40));
        logo.setTextFill(Color.web(PRIMARY));

        Label subtitle = new Label("Welcome back to your cinematic world.");
        subtitle.setWrapText(true);
        subtitle.setAlignment(Pos.CENTER);
        subtitle.setFont(Font.font("Inter", FontWeight.NORMAL, 16));
        subtitle.setTextFill(Color.web(SECONDARY));
        header.getChildren().addAll(logo, subtitle);

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

        Button loginButton = new Button("Sign In");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setPrefHeight(48);
        loginButton.setFont(Font.font("Inter", FontWeight.SEMI_BOLD, 14));
        loginButton.setTextFill(Color.WHITE);
        setButtonStyle(loginButton, PRIMARY_CONTAINER);

        loginButton.setOnAction(event -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                showError("Missing Information", "Please enter your email and password.");
                return;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                showError("Invalid Email", "Please enter a valid email address.");
                return;
            }

            boolean valid = UserDataLoader.validateUser(email, password);

            if (!valid) {
                showError("Login Failed", "Incorrect email or password.");
                return;
            }

            openSearchPage(stage);
        });

        HBox createAccountBox = new HBox(5);
        createAccountBox.setAlignment(Pos.CENTER);

        Label newUser = new Label("Don't have an account?");
        newUser.setFont(Font.font("Inter", FontWeight.NORMAL, 14));
        newUser.setTextFill(Color.web(SECONDARY));

        Hyperlink createAccount = new Hyperlink("Create account");
        createAccount.setFont(Font.font("Inter", FontWeight.SEMI_BOLD, 14));
        createAccount.setTextFill(Color.web(PRIMARY_CONTAINER));
        createAccount.setBorder(Border.EMPTY);

        createAccount.setOnAction(event -> {
            CreateAccountPage page = new CreateAccountPage();
            page.show(stage);
        });

        createAccountBox.getChildren().addAll(newUser, createAccount);
        card.getChildren().addAll(header, emailBox, passwordBox, loginButton, createAccountBox);
        root.getChildren().add(card);

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("MovieFlix - Login");
        stage.setScene(scene);
        stage.show();
    }

    private void openSearchPage(Stage stage) {
        if (movies == null || movies.isEmpty()) {
            new SearchPage().show(stage);
        } else {
            new SearchPage(movies).show(stage);
        }
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Inter", FontWeight.MEDIUM, 14));
        label.setTextFill(Color.web("#594139"));
        return label;
    }

    private void styleTextField(TextField field) {
        field.setStyle("-fx-background-color: #FFFFFF;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 0 12;" +
                "-fx-font-size: 14px;" +
                "-fx-text-fill: " + TEXT + ";");
    }

    private void setButtonStyle(Button button, String color) {
        button.setStyle("-fx-background-color: " + color + ";" +
                "-fx-background-radius: 12;" +
                "-fx-cursor: hand;");

        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: " + PRIMARY + ";" +
                    "-fx-background-radius: 12;" +
                    "-fx-cursor: hand;");
        });

        button.setOnMouseExited(event -> {
            button.setStyle("-fx-background-color: " + color + ";" +
                    "-fx-background-radius: 12;" +
                    "-fx-cursor: hand;");
        });
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
package ui;

import data.UserDataLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import theme.Theme;
import theme.ThemeManager;

public class LoginPage {

    private final java.util.List<model.Movie> movies;

    public LoginPage() {
        this(null);
    }

    public LoginPage(java.util.List<model.Movie> movies) {
        this.movies = movies;
    }

    public void show(Stage stage) {
        StackPane root = new StackPane();
        root.setPadding(new Insets(Theme.PAGE_PADDING));
        ThemeManager.stylePage(root);

        VBox card = new VBox(20);
        card.setMaxWidth(448);
        card.setPrefWidth(448);
        card.setPadding(new Insets(48));
        ThemeManager.styleCard(card);

        VBox header = new VBox(6);
        header.setAlignment(Pos.CENTER);

        Label logo = new Label("MovieFlix");
        logo.setFont(Theme.TITLE);
        logo.setTextFill(Theme.PRIMARY);

        Label subtitle = new Label(
                "Welcome back to your cinematic world."
        );
        subtitle.setWrapText(true);
        subtitle.setAlignment(Pos.CENTER);
        subtitle.setFont(Theme.BODY);
        subtitle.setStyle(ThemeManager.secondaryTextStyle());

        header.getChildren().addAll(logo, subtitle);

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

        Button loginButton = new Button("Sign In");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        ThemeManager.styleButton(loginButton);

        loginButton.setOnMousePressed(event ->
                loginButton.setTranslateY(2)
        );

        loginButton.setOnMouseReleased(event ->
                loginButton.setTranslateY(0)
        );

        loginButton.setOnAction(event -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                showError(
                        "Missing Information",
                        "Please enter your email and password."
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

            boolean valid = UserDataLoader.validateUser(
                    email,
                    password
            );

            if (!valid) {
                showError(
                        "Login Failed",
                        "Incorrect email or password."
                );
                return;
            }

            openSearchPage(stage);
        });

        HBox createAccountBox = new HBox(5);
        createAccountBox.setAlignment(Pos.CENTER);

        Label newUser = new Label("Don't have an account?");
        newUser.setFont(Theme.BODY);
        newUser.setStyle(ThemeManager.secondaryTextStyle());

        Hyperlink createAccount = new Hyperlink("Create account");
        createAccount.setFont(Theme.BODY);
        createAccount.setTextFill(Theme.PRIMARY);
        createAccount.setBorder(Border.EMPTY);
        createAccount.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-padding: 4 6;" +
                        "-fx-cursor: hand;"
        );

        createAccount.setOnMouseEntered(event ->
                createAccount.setTextFill(Theme.PRIMARY_HOVER)
        );

        createAccount.setOnMouseExited(event ->
                createAccount.setTextFill(Theme.PRIMARY)
        );

        createAccount.setOnAction(event -> {
            CreateAccountPage page = new CreateAccountPage();
            page.show(stage);
        });

        createAccountBox.getChildren().addAll(
                newUser,
                createAccount
        );

        card.getChildren().addAll(
                header,
                emailBox,
                passwordBox,
                loginButton,
                createAccountBox
        );

        root.getChildren().add(card);

        Scene scene = new Scene(root, 1280, 720);
        ThemeManager.applyTheme(scene);

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
        label.setFont(Theme.BODY);
        label.setStyle(ThemeManager.primaryTextStyle());
        return label;
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
package theme;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Region;

public class ThemeManager {

    private ThemeManager() {}

    public static void applyTheme(Scene scene) {
        if (scene == null) return;
        scene.setFill(Theme.BACKGROUND);
        scene.getRoot().setStyle("-fx-background-color: " + toHex(Theme.BACKGROUND) + ";");
    }

    public static void styleButton(Button button) {
        if (button == null) return;
        button.setPrefHeight(Theme.BUTTON_HEIGHT);
        button.setStyle(Theme.buttonStyle());
        button.setOnMouseEntered(event -> button.setStyle(Theme.buttonHoverStyle()));
        button.setOnMouseExited(event -> button.setStyle(Theme.buttonStyle()));
    }

    public static void styleInput(TextField input) {
        if (input == null) return;
        input.setPrefHeight(Theme.INPUT_HEIGHT);
        input.setStyle(Theme.inputStyle());
    }

    public static void stylePasswordField(PasswordField input) {
        if (input == null) return;
        input.setPrefHeight(Theme.INPUT_HEIGHT);
        input.setStyle(Theme.inputStyle());
    }

    public static void styleCard(Region card) {
        if (card == null) return;
        card.setStyle(Theme.cardStyle());
    }

    public static void apply(Node node, String style) {
        if (node == null) return;
        node.setStyle(style);
    }

    public static void stylePage(Region page) {
        if (page == null) return;
        page.setStyle("-fx-background-color: " + toHex(Theme.BACKGROUND) + ";");
    }

    public static void styleSurface(Region node) {
        if (node == null) return;
        node.setStyle("-fx-background-color: " + toHex(Theme.SURFACE) + ";");
    }

    public static String primaryTextStyle() {
        return "-fx-text-fill: " + toHex(Theme.TEXT_PRIMARY) + ";";
    }

    public static String secondaryTextStyle() {
        return "-fx-text-fill: " + toHex(Theme.TEXT_SECONDARY) + ";";
    }

    public static String borderStyle() {
        return "-fx-border-color: " + toHex(Theme.BORDER) + ";";
    }

    private static String toHex(javafx.scene.paint.Color color) {
        return String.format("#%02X%02X%02X", (int) Math.round(color.getRed() * 255), (int) Math.round(color.getGreen() * 255), (int) Math.round(color.getBlue() * 255));
    }
}

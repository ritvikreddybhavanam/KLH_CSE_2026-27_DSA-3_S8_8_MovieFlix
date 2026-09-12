package theme;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Theme {

    // ============================================================
    // GLASSMORPHISM COLOR PALETTE
    // ============================================================

    // Main accent
    public static final Color PRIMARY = Color.web("#7C3AED");
    public static final Color PRIMARY_HOVER = Color.web("#6D28D9");

    // Page background
    public static final Color BACKGROUND = Color.web("#EEF2FF");

    // Glass surfaces
    public static final Color SURFACE = Color.web("#FFFFFF");

    // Text
    public static final Color TEXT_PRIMARY = Color.web("#1E1B4B");
    public static final Color TEXT_SECONDARY = Color.web("#64748B");

    // Glass borders
    public static final Color BORDER = Color.web("#FFFFFF");

    // Status colors
    public static final Color SUCCESS = Color.web("#16A34A");
    public static final Color ERROR = Color.web("#DC2626");

    // ============================================================
    // FONTS
    // ============================================================

    public static final String FONT_FAMILY = "Arial";

    public static final Font TITLE =
            Font.font(FONT_FAMILY, FontWeight.BOLD, 28);

    public static final Font HEADING =
            Font.font(FONT_FAMILY, FontWeight.BOLD, 22);

    public static final Font SUBHEADING =
            Font.font(FONT_FAMILY, FontWeight.BOLD, 18);

    public static final Font BODY =
            Font.font(FONT_FAMILY, FontWeight.NORMAL, 14);

    public static final Font SMALL =
            Font.font(FONT_FAMILY, FontWeight.NORMAL, 12);

    // ============================================================
    // DIMENSIONS
    // ============================================================

    public static final double NAVBAR_HEIGHT = 70;
    public static final double BUTTON_HEIGHT = 42;
    public static final double INPUT_HEIGHT = 42;

    public static final double RADIUS = 14;
    public static final double CARD_RADIUS = 20;

    public static final double PAGE_PADDING = 30;

    // ============================================================
    // GLASS EFFECT COLORS
    // ============================================================

    /*
     * JavaFX does not provide real CSS backdrop-filter blur like
     * modern web browsers.
     *
     * Therefore, glassmorphism is simulated using:
     *
     *  - Semi-transparent white backgrounds
     *  - Light white borders
     *  - Drop shadows
     *  - Rounded corners
     */

    public static final String GLASS_BACKGROUND =
            "rgba(255,255,255,0.55)";

    public static final String GLASS_BACKGROUND_LIGHT =
            "rgba(255,255,255,0.40)";

    public static final String GLASS_BACKGROUND_STRONG =
            "rgba(255,255,255,0.75)";

    public static final String GLASS_BORDER =
            "rgba(255,255,255,0.70)";

    public static final String GLASS_SHADOW =
            "rgba(80,70,120,0.18)";

    // ============================================================
    // BUTTON STYLE
    // ============================================================

    public static String buttonStyle() {

        return String.format(
                "-fx-background-color: %s;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: '%s';" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: %.0f;" +
                        "-fx-border-radius: %.0f;" +
                        "-fx-border-color: rgba(255,255,255,0.35);" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 10 20 10 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(80,70,120,0.20), 12, 0.2, 0, 4);",

                toHex(PRIMARY),
                FONT_FAMILY,
                RADIUS,
                RADIUS
        );
    }

    // ============================================================
    // BUTTON HOVER STYLE
    // ============================================================

    public static String buttonHoverStyle() {

        return String.format(
                "-fx-background-color: %s;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: '%s';" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: %.0f;" +
                        "-fx-border-radius: %.0f;" +
                        "-fx-border-color: rgba(255,255,255,0.55);" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 10 20 10 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(124,58,237,0.35), 18, 0.3, 0, 5);",

                toHex(PRIMARY_HOVER),
                FONT_FAMILY,
                RADIUS,
                RADIUS
        );
    }

    // ============================================================
    // GLASS BUTTON
    // ============================================================

    public static String glassButtonStyle() {

        return
                "-fx-background-color: rgba(255,255,255,0.45);" +
                        "-fx-text-fill: #1E1B4B;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-radius: 14;" +
                        "-fx-border-color: rgba(255,255,255,0.75);" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 10 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(80,70,120,0.15), 12, 0.2, 0, 4);";
    }

    // ============================================================
    // INPUT / TEXT FIELD
    // ============================================================

    public static String inputStyle() {

        return
                "-fx-background-color: rgba(255,255,255,0.60);" +
                        "-fx-text-fill: #1E1B4B;" +
                        "-fx-prompt-text-fill: #64748B;" +
                        "-fx-border-color: rgba(255,255,255,0.80);" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;" +
                        "-fx-padding: 10 14;" +
                        "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 14px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(80,70,120,0.10), 10, 0.15, 0, 3);";
    }

    // ============================================================
    // GLASS CARD
    // ============================================================

    public static String cardStyle() {

        return
                "-fx-background-color: rgba(255,255,255,0.55);" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: rgba(255,255,255,0.75);" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(80,70,120,0.15), 20, 0.20, 0, 6);";
    }

    // ============================================================
    // STRONG GLASS CARD
    // ============================================================

    public static String strongGlassStyle() {

        return
                "-fx-background-color: rgba(255,255,255,0.75);" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: rgba(255,255,255,0.85);" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(80,70,120,0.18), 18, 0.2, 0, 5);";
    }

    // ============================================================
    // NAVBAR GLASS STYLE
    // ============================================================

    public static String navbarStyle() {

        return
                "-fx-background-color: rgba(255,255,255,0.65);" +
                        "-fx-border-color: rgba(255,255,255,0.80);" +
                        "-fx-border-width: 0 0 1 0;" +
                        "-fx-effect: dropshadow(gaussian, rgba(80,70,120,0.12), 15, 0.15, 0, 3);";
    }

    // ============================================================
    // PAGE BACKGROUND
    // ============================================================

    public static String backgroundStyle() {

        return
                "-fx-background-color: #EEF2FF;";
    }

    // ============================================================
    // SECONDARY TEXT
    // ============================================================

    public static String secondaryTextStyle() {

        return
                "-fx-text-fill: #64748B;" +
                        "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 14px;";
    }

    // ============================================================
    // PRIMARY TEXT
    // ============================================================

    public static String primaryTextStyle() {

        return
                "-fx-text-fill: #1E1B4B;" +
                        "-fx-font-family: 'Arial';";
    }

    // ============================================================
    // SEARCH FIELD
    // ============================================================

    public static String searchStyle() {

        return
                "-fx-background-color: rgba(255,255,255,0.65);" +
                        "-fx-text-fill: #1E1B4B;" +
                        "-fx-prompt-text-fill: #64748B;" +
                        "-fx-background-radius: 18;" +
                        "-fx-border-radius: 18;" +
                        "-fx-border-color: rgba(255,255,255,0.80);" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 10 16;" +
                        "-fx-font-size: 14px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(80,70,120,0.12), 12, 0.2, 0, 3);";
    }

    // ============================================================
    // COMBO BOX
    // ============================================================

    public static String comboBoxStyle() {

        return
                "-fx-background-color: rgba(255,255,255,0.60);" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: rgba(255,255,255,0.75);" +
                        "-fx-border-radius: 14;" +
                        "-fx-border-width: 1;" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: #1E1B4B;";
    }

    // ============================================================
    // GLASS PILL
    // ============================================================

    public static String pillStyle() {

        return
                "-fx-background-color: rgba(255,255,255,0.50);" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: rgba(255,255,255,0.75);" +
                        "-fx-border-radius: 30;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 6 14;" +
                        "-fx-text-fill: #1E1B4B;";
    }

    // ============================================================
    // SUCCESS
    // ============================================================

    public static String successStyle() {

        return
                "-fx-background-color: rgba(22,163,74,0.12);" +
                        "-fx-text-fill: #15803D;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: rgba(22,163,74,0.25);" +
                        "-fx-border-radius: 12;" +
                        "-fx-padding: 8 12;";
    }

    // ============================================================
    // ERROR
    // ============================================================

    public static String errorStyle() {

        return
                "-fx-background-color: rgba(220,38,38,0.12);" +
                        "-fx-text-fill: #B91C1C;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: rgba(220,38,38,0.25);" +
                        "-fx-border-radius: 12;" +
                        "-fx-padding: 8 12;";
    }

    // ============================================================
    // COLOR → HEX
    // ============================================================

    private static String toHex(Color color) {

        return String.format(
                "#%02X%02X%02X",
                (int) Math.round(color.getRed() * 255),
                (int) Math.round(color.getGreen() * 255),
                (int) Math.round(color.getBlue() * 255)
        );
    }
}
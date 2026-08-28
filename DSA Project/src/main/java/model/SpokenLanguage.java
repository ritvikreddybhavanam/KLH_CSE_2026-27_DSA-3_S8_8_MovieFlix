package model;

public class SpokenLanguage {

    private String iso_639_1;
    private String name;

    public SpokenLanguage() {
    }

    public String getLanguageCode() {
        return iso_639_1;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
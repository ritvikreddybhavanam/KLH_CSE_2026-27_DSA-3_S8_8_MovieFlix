package model;

public class ProductionCountry {
    public String iso_3166_1;
    public String name;

    public ProductionCountry() {
    }

    public String getIsoCode() {
        return iso_3166_1;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
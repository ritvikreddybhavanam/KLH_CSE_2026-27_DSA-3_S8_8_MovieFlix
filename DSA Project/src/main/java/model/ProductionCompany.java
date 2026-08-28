package model;

public class ProductionCompany {
    public int id;
    public String name;

    public ProductionCompany() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
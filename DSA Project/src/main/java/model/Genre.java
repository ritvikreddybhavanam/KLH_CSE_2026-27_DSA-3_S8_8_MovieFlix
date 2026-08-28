package model;

public class Genre {
    public int id;
    public String name;

    public Genre() {
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
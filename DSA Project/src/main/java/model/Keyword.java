package model;

public class Keyword {
    public int id;
    public String name;

    public Keyword() {
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
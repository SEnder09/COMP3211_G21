package model;

public class Property {
    public String name;
    public int price;
    public int rent;
    public boolean owned;
    public Player owner;

    public Property(String name, int price, int rent) {
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.owned = false;
        this.owner = null;
    }
}

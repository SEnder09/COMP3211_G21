package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public String name;
    public int money;
    public int position;
    public boolean inJail;
    public int state;
    public int bonus;
    public List<Property> ownedProperties;
    public int jailDay;
    // changesjkljk
    public Player(String name) {
        this.name = name;
        this.money = 1500; // Starting money
        this.position = 0; // Starts on square 0
        this.inJail = false;
        this.state = 0; // 0: first turn, in 'GO' doesn't earn  1500
        // 1: other turn, in 'GO' get 1500
        this.bonus = 0;
        this.ownedProperties = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public int getMoney() {
        return money;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public List<Property> getOwnedProperties() {
        return ownedProperties;
    }

    public void addProperty(Property property) {
        ownedProperties.add(property);
    }

}

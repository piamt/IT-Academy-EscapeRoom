package model.item.implementations;

import model.item.Item;
import model.enums.Theme;

public class Clue extends Item {

    Theme theme;

    Clue(String name, Double price, Theme theme) {
        super(name, price);
        this.theme = theme;
    }

    Clue() { super(); }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    @Override
    public String toString() {
        return "Clue{" +
                "itemId=" + this.getItemId() +
                ", name='" + this.getName() +
                "', price= " + String.format("%.2f", this.getPrice()) + "€" +
                ", theme=" + this.getTheme().name() +
                '}';
    }
}

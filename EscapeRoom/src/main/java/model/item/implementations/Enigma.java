package model.item.implementations;

import model.item.Item;

public class Enigma extends Item {
    Enigma(String name, Double price) {
        super(name, price);
    }

    Enigma() { super(); }

    @Override
    public String toString() {
        return "Enigma{" +
                "itemId= " + this.getItemId() +
                ", name= " + this.getName() +
                ", price= " + String.format("%.2f", this.getPrice()) + "€" +
                '}';
    }
}

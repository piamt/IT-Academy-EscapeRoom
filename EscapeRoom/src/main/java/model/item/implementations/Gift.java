package model.item.implementations;

import model.item.Item;

public class Gift extends Item {
    Gift(String name, Double price) {
		super(name, price);
    }

    Gift() { super(); }

    public String toString() {
        return "Gift{" +
                "itemId: " + this.getItemId() +
                ", name: " + this.getName() +
                '}';
    }
}

package model.item;

import model.enums.Material;
import model.enums.Theme;
import model.item.implementations.Clue;
import model.item.implementations.Decoration;
import model.item.implementations.Enigma;
import model.item.implementations.Gift;

public interface ItemFactory {
    Decoration createDecoration(String name, Double price, Material material, Integer quantity);
    Clue createClue(String name, Double price, Theme theme);
    Enigma createEnigma(String name, Double price);
    Gift createGift(String name, Double price);

    Decoration createDecoration();
    Clue createClue();
    Enigma createEnigma();
    Gift createGift();
}

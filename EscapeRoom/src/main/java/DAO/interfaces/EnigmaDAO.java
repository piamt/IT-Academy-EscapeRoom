package DAO.interfaces;

import exception.CallFailedException;
import model.User;
import model.item.implementations.Enigma;

import java.util.List;

public interface EnigmaDAO extends DAO<Enigma> {
    void addEnigma(Enigma enigma, Integer roomId)  throws CallFailedException;
    void addEnigmaToUser(Integer userId, Integer enigmaId) throws CallFailedException;
    List<Enigma> getAllEnigmasByRoom(Integer roomId);
    Enigma getEnigmaById(Integer itemId);
    List<Enigma> getAllEnigmasByUser(User user);
}

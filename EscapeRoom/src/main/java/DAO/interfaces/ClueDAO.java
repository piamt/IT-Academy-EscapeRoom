package DAO.interfaces;

import exception.CallFailedException;
import model.item.implementations.Clue;

import java.util.List;

public interface ClueDAO extends DAO<Clue> {
    void addClue(Clue clue, Integer enigmaId)  throws CallFailedException;
    List<Clue> getAllCluesByEnigma(Integer enigmaId);
    Clue getClueById(Integer itemId);
}

package DAO.interfaces;

import exception.CallFailedException;
import model.item.implementations.Decoration;

import java.util.List;

public interface DecorationDAO extends DAO<Decoration> {
    void addDecoration(Decoration decoration, Integer roomId) throws CallFailedException;
    Decoration getDecorationById(Integer itemId);
    List<Decoration> getAllDecorationsByRoom(Integer roomId);
}

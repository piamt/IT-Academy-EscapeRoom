package DAO.interfaces;

import exception.CallFailedException;
import model.item.implementations.Gift;

import java.util.List;

public interface GiftDAO extends DAO<Gift> {
    void addGift(Gift gift) throws CallFailedException;
    List<Gift> getAllGiftsByUser(Integer userId);
    Gift getGiftById(Integer itemId);
    void assignGiftToUser(Integer giftId, Integer userId) throws CallFailedException;
}

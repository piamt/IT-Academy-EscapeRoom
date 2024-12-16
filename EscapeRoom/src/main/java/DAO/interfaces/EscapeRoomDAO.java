package DAO.interfaces;

import exception.CallFailedException;
import model.EscapeRoom;

public interface EscapeRoomDAO extends DAO<EscapeRoom> {
    void add(EscapeRoom escapeRoom) throws CallFailedException;
}

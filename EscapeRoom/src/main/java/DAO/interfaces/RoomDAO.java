package DAO.interfaces;

import exception.CallFailedException;
import model.Room;

import java.util.List;

public interface RoomDAO extends DAO<Room> {
    void addRoom(Room room, Integer escapeRoomId) throws CallFailedException;
    List<Room> getAllRoomsByEscapeRoom(Integer escapeRoomId);
    Room getRoomById(Integer roomId);
}

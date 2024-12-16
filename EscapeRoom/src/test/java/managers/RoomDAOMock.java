package managers;

import DAO.interfaces.RoomDAO;
import exception.CallFailedException;
import model.Room;

import java.util.List;

public class RoomDAOMock implements RoomDAO {

    Boolean success = false;
    Room room;
    Integer escapeRoomId;

    @Override
    public void addRoom(Room room, Integer escapeRoomId) throws CallFailedException {
        this.room = room;
        this.escapeRoomId = escapeRoomId;
        if (!success) throw new CallFailedException("Failed room creation");
    }

    @Override
    public List<Room> getAllRoomsByEscapeRoom(Integer escapeRoomId) {
        return List.of();
    }

    @Override
    public Room getRoomById(Integer roomId) {
        return null;
    }

    @Override
    public List<Room> getData() {
        return List.of();
    }

    @Override
    public void delete(Integer id) {

    }
}

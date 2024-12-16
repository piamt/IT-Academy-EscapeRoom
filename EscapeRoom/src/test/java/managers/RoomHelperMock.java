package managers;

import model.Room;
import utils.RoomHelper;

public class RoomHelperMock implements RoomHelper {

    Room room;

    public RoomHelperMock(Room room) {
        this.room = room;
    }

    @Override
    public Room createRoom() {
        return room;
    }
}

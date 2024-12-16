package managers;

import exception.CallFailedException;
import model.Room;
import model.enums.Level;
import manager.InventoryManager;
import org.junit.jupiter.api.*;

import static org.junit.Assert.assertEquals;


class InventoryManagerTest {

    InventoryManager inventoryManager;
    ObservableMock observable;
    RoomDAOMock roomDAO;
    Room room;

    @BeforeEach
    void init() {
        room = new Room("Mistery room", 45.0, Level.LOW);
        observable = new ObservableMock();
        roomDAO = new RoomDAOMock();
        inventoryManager = InventoryManager.getInstance();
        inventoryManager.setRoomHelper(new RoomHelperMock(room));
        inventoryManager.setRoomDAO(roomDAO);
        inventoryManager.setObservable(observable);
    }

    @Test
    void givenAddRoomCalled_wheDAOCalled_ThenExpectedRoomSentToDDBB() {
        roomDAO.success = true;
        try {
            inventoryManager.addRoomToEscapeRoom(3);
            Assertions.assertEquals(roomDAO.room, this.room);
        } catch (CallFailedException e) {
            Assertions.fail();
        }
    }

    @Test
    void givenAddRoomCalled_whenDAOResponseFailed_ThenSubscribersNotNotified() {
        roomDAO.success = false;
        try {
            inventoryManager.addRoomToEscapeRoom(3);
            Assertions.fail();
        } catch (CallFailedException e) {
            Assertions.assertEquals(e.getMessage(), "Room could not be created in DDBB. Failed room creation");
        }
    }

    @Test
    void givenAddRoomCalled_whenDAOResponseSucceeds_ThenSubscribersGetNotified() {
        roomDAO.success = true;
        try {
            inventoryManager.addRoomToEscapeRoom(3);
            Assertions.assertTrue(observable.notified);
        } catch (CallFailedException e) {
            Assertions.fail();
        }
    }
}
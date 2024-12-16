package manager;

import DAO.implementation.EscapeRoomDAOImpl;
import DAO.interfaces.EscapeRoomDAO;
import exception.CallFailedException;
import model.EscapeRoom;
import connection.DbConnectionImpl;
import utils.Entry;

import java.util.List;

public class EscapeRoomManager {

    private static EscapeRoomManager instance;
    EscapeRoomDAO escapeRoomDAO;
    EscapeRoom escapeRoom;

    private EscapeRoomManager(){
        this.escapeRoomDAO = new EscapeRoomDAOImpl(DbConnectionImpl.getInstance());
    }

    public static EscapeRoomManager getInstance(){
        if (instance == null) instance = new EscapeRoomManager();
        return instance;
    }

    public void createEscapeRoomIfNotPresent() throws CallFailedException {
        List<EscapeRoom> escapeRoomList = escapeRoomDAO.getData();

        if (escapeRoomList.isEmpty()) {
            try {
                System.out.println("To start, let's create a Escape Room. ");
                String name = Entry.readString("Give a name to the escape room");
                String CIF = Entry.readString("Give a CIF to the escape room");
                escapeRoomDAO.add(new EscapeRoom(name, CIF));
                System.out.println("Escape room created");
            } catch (CallFailedException e) {
                throw new CallFailedException("Escape room could not be added to DDBB. " + e.getMessage());
            }
        } else {
            escapeRoom = escapeRoomList.getFirst();
            System.out.println("\nWelcome to the Escape Room '" + escapeRoom.getName() + "'!!!!");
            System.out.println(escapeRoom);
        }
    }

    public EscapeRoom getEscapeRoom() {
        if (this.escapeRoom != null) return escapeRoom;
        List<EscapeRoom> escapeRoomList = escapeRoomDAO.getData();
        escapeRoom = escapeRoomList.getFirst();
        return escapeRoom;
    }
}

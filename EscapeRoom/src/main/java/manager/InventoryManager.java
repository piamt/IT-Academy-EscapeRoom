package manager;

import DAO.implementation.*;
import DAO.interfaces.*;
import exception.*;
import model.Room;
import model.enums.Material;
import model.enums.Theme;
import model.item.Item;
import model.item.ItemFactory;
import model.item.implementations.*;
import connection.DbConnectionImpl;
import org.apache.commons.compress.harmony.pack200.NewAttributeBands;
import org.testcontainers.shaded.com.google.common.base.Throwables;
import subscription.Observable;
import utils.Entry;
import utils.MenuDeleteInventoryOptions;
import utils.RoomHelperImpl;
import utils.RoomHelper;

import java.util.List;
import java.util.stream.IntStream;

public class InventoryManager {

    private static InventoryManager instance;

    private RoomHelper roomHelper;

    private RoomDAO roomDAO;
    private EnigmaDAO enigmaDAO;
    private ClueDAO clueDAO;
    private DecorationDAO decorationDAO;
    private GiftDAO giftDAO;
    private  UserDAO userDAO;

    private ItemFactory itemFactory;
    private Observable observable;

    private InventoryManager(){
        roomHelper = new RoomHelperImpl();

        roomDAO = new RoomDAOImpl(DbConnectionImpl.getInstance());
        enigmaDAO = new EnigmaDAOImpl();
        clueDAO = new ClueDAOImpl();
        decorationDAO = new DecorationDAOImpl(DbConnectionImpl.getInstance());
        giftDAO = new GiftDAOImpl();
        userDAO = new UserDAOImpl();

        itemFactory = new ItemFactoryImpl();
    }

    public static InventoryManager getInstance(){
        if (instance == null) instance = new InventoryManager();
        return instance;
    }

    public void setObservable(Observable observable) {
        this.observable = observable;
    }

    public void setRoomHelper(RoomHelper roomHelper) {
        this.roomHelper = roomHelper;
    }

    public void setRoomDAO(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    public void addRoomToEscapeRoom(Integer escapeRoomId) throws CallFailedException {
        Room room = roomHelper.createRoom();
        try {
            roomDAO.addRoom(room, escapeRoomId);
            observable.notifySubscribers("New room created " + room.getName());
        } catch (CallFailedException e) {
            throw new CallFailedException("Room could not be created in DDBB. " + e.getMessage());
        }
    }

    public void addNewEnigma() throws NoRoomsException, CallFailedException {
        List<Room> rooms = getAllRooms();
        if (rooms.isEmpty()) throw new NoRoomsException("You need to add a room first");

        System.out.println("--- ROOM LIST ---");
        rooms.forEach(System.out::println);
        addEnigmaToRoom(rooms.stream().map(Room::getIdRoom).toList());
    }

    public void addNewDecoration() throws NoRoomsException, CallFailedException {
        List<Room> rooms = getAllRooms();
        if (rooms.isEmpty()) throw new NoRoomsException("You need to add a room first");

        System.out.println("--- ROOM LIST ---");
        rooms.forEach(System.out::println);
        try {
            addDecorationToRoom(rooms.stream().map(Room::getIdRoom).toList());
        } catch (CallFailedException e) {
            throw new CallFailedException("Decoration could not be created in the DDBB. " + e.getMessage());
        }
    }

    public void createGift() throws CallFailedException {
        String name = Entry.readString("Give a name for the gift");
        giftDAO.addGift(itemFactory.createGift(name, 0.0));
    }

    public void addNewClue() throws CallFailedException, NoRoomsException, NoEnigmasException {
        List<Room> rooms = getAllRooms();
        if (rooms.isEmpty()) throw new NoRoomsException("You need to add a room first");

        System.out.println("--- ROOM LIST ---");
        rooms.forEach(System.out::println);
        List<Enigma> enigmas = getEnigmasForRoom(rooms.stream().map(Room::getIdRoom).toList());
        if (enigmas.isEmpty()) throw new NoEnigmasException("You need to add an enigma first");

        System.out.println("--- ENIGMA LIST ---");
        enigmas.forEach(System.out::println);
        addClueForEnigma(enigmas.stream().map(Enigma::getItemId).toList());
    }

    public void showInventory(Integer escapeRoomId) {
        List<Room> rooms = roomDAO.getAllRoomsByEscapeRoom(escapeRoomId);
        for (Room room: rooms) {
            System.out.println(room);
            decorationDAO.getAllDecorationsByRoom(room.getIdRoom()).forEach(deco -> System.out.println("  " + deco));

            List<Enigma> enigmas = enigmaDAO.getAllEnigmasByRoom(room.getIdRoom());
            for (Enigma enigma: enigmas) {
                System.out.println("  " + enigma);
                clueDAO.getAllCluesByEnigma(enigma.getItemId()).forEach(clue -> System.out.println("    " + clue));
            }

            System.out.println();
        }
    }

    public String showTotalInventoryValue(Integer escapeRoomId) {
        Double totalValue = 0.0;
        List<Room> rooms = roomDAO.getAllRoomsByEscapeRoom(escapeRoomId);
        for (Room room: rooms) {
            totalValue += room.getPrice();
            Double totalDeco = decorationDAO.getAllDecorationsByRoom(room.getIdRoom())
                    .stream()
                    .map(deco -> deco.getPrice()*deco.getQuantity())
                    .reduce(0.0, Double::sum);
            totalValue += totalDeco;

            List<Enigma> enigmas = enigmaDAO.getAllEnigmasByRoom(room.getIdRoom());
            for (Enigma enigma: enigmas) {
                totalValue += enigma.getPrice();
                Double totalClue = clueDAO.getAllCluesByEnigma(enigma.getItemId())
                        .stream()
                        .map(Clue::getPrice)
                        .reduce(0.0, Double::sum);
                totalValue += totalClue;
            }
        }
        return ("Total inventory value:" + totalValue);
    }

    public void deleteMenuStart() {
        boolean close = false;
        int selectedMenuOption = -1;

        do {
            try {
                selectedMenuOption = menu();

                switch (selectedMenuOption) {
                    case 1:
                        List<Room> rooms = getAllRooms();
                        if(!rooms.isEmpty()){
                            rooms.forEach(System.out::println);
                            deleteRoom(rooms);
                        }else {
                            System.out.println("No rooms available!");
                        }
                        break;
                    case 2:
                        List<Enigma> enigmas = getAllEnigmas();
                        if(!enigmas.isEmpty()){
                            enigmas.forEach(System.out::println);
                            deleteEnigma(enigmas);
                        }else {
                            System.out.println("No enigmas available!");
                        }
                        break;
                    case 3:
                        List<Clue> clues = getAllClues();
                        if(!clues.isEmpty()){
                            clues.forEach(System.out::println);
                            deleteClue(clues);
                        }else {
                            System.out.println("No clues available!");
                        }
                        break;
                    case 4:
                        List<Decoration> decorations = getAllDecoration();
                        if (!decorations.isEmpty()){
                            decorations.forEach(System.out::println);
                            deleteDecoration(decorations);
                        }else{
                            System.out.println("No decoration available!");
                        }
                        break;
                    case 0:
                        close = true;
                        break;
                    default: break;
                }
            } catch (IncorrectMenuOptionException | CallFailedException e) {
                System.out.println(e.getMessage());
            }
        } while (!close);
    }

    public int menu() throws IncorrectMenuOptionException {
        System.out.println("\nWhat do you want to delete?");
        for (int i = 1; i <= MenuDeleteInventoryOptions.options.length; i++) {
            System.out.println( i + ". " + MenuDeleteInventoryOptions.options[i-1]);
        }
        System.out.println("0. " + MenuDeleteInventoryOptions.close);

        List<Integer> validOptions = IntStream.rangeClosed(0, MenuDeleteInventoryOptions.options.length).boxed().toList();
        int menuOption = Entry.readInt("Select a menu option between 0 and " + MenuDeleteInventoryOptions.options.length + ".", validOptions);
        if (menuOption < 0 || menuOption > MenuDeleteInventoryOptions.options.length) throw new IncorrectMenuOptionException("Menu option should be between 0 and " + MenuDeleteInventoryOptions.options.length + ".");
        else return menuOption;
    }

    private void deleteRoom(List<Room> rooms) throws CallFailedException {
        Integer roomId = Entry.readInt("Enter a room id",
                rooms.stream().map(Room::getIdRoom).toList());
        roomDAO.delete(roomId);
    }

    private List<Room> getAllRooms() {
        return roomDAO.getData();
    }

    private void addEnigmaToRoom(List<Integer> roomIds) throws CallFailedException {
        Integer roomId = Entry.readInt("Enter a room id", roomIds);
        String name = Entry.readString("Give a name for the enigma");
        Double price = Entry.readDouble("Enter a price for the enigma");
        Enigma enigma = itemFactory.createEnigma(name, price);
        try {
            enigmaDAO.addEnigma(enigma, roomId);
            observable.notifySubscribers("New enigma created '" + enigma.getName() + "' for room with id: " + roomId);
        } catch (CallFailedException e) {
            throw new CallFailedException("Enigma could not be added to room in DDBB" + e.getMessage());
        }
    }

    private List<Enigma> getEnigmasForRoom(List<Integer> roomIds) {
        Integer roomId = Entry.readInt("Enter a room id", roomIds);
        return enigmaDAO.getAllEnigmasByRoom(roomId);
    }

    private List<Enigma> getAllEnigmas() {
        return enigmaDAO.getData();
    }

    private void deleteEnigma(List<Enigma> enigmas) throws CallFailedException {
        Integer enigmaId = Entry.readInt("Enter an enigma id",
                enigmas.stream().map(Item::getItemId).toList());
        List<Clue> clues = clueDAO.getAllCluesByEnigma(enigmaId);
        boolean confirm = true;
        if (!clues.isEmpty()) {
            System.out.println("Deleting enigma " + enigmaId + " will also delete clues:");
            clues.forEach(System.out::println);
            confirm = Entry.readBoolean("Do you confirm you want to delete the enigma " + enigmaId + " (Y/N)?");
        }

        if (confirm) {
            for (Clue clue : clues) {
                try {
                    clueDAO.delete(clue.getItemId());
                } catch (CallFailedException e) {
                    throw new CallFailedException("Clue could not be deleted from DDBB");
                }
            }
            try {
                userDAO.deleteUsersWithEnigma(enigmaId);
                enigmaDAO.delete(enigmaId);
            } catch (CallFailedException e) {
                throw new CallFailedException("Enigma could not be deleted from DDBB" + e.getMessage());
            } catch (DeleteUserFailedException e) {
                throw new CallFailedException("User with enigma could not be deleled from DDBB" + e.getMessage());
            }
        }
    }

    private void addDecorationToRoom(List<Integer> roomIds) throws CallFailedException {
        Integer roomId = Entry.readInt("Enter a room id", roomIds);
        String name = Entry.readString("Give a name for the new decoration object");
        Double price = Entry.readDouble("Enter a price for the decoration");
        Material material = Entry.readMaterial("Enter a material for the decoration (wood/plastic/paper/glass/metal)");
        Integer quantity = Entry.readInt("How many decoration objects do you have?");
        Decoration decoration = itemFactory.createDecoration(name, price, material, quantity);
        decorationDAO.addDecoration(decoration, roomId);
    }

    private List<Decoration> getAllDecoration() {
        return decorationDAO.getData();
    }

    private void deleteDecoration(List<Decoration> decorations) throws CallFailedException {
        Integer decorationId = Entry.readInt("Enter a decoration id",
                decorations.stream().map(Item::getItemId).toList());
        decorationDAO.delete(decorationId);
    }

    private void addClueForEnigma(List<Integer> enigmaIds) throws CallFailedException {
        Integer enigmaId = Entry.readInt("Enter an enigma id", enigmaIds);
        String name = Entry.readString("Give a name for the new clue");
        Double price = Entry.readDouble("Enter a price for the clue");
        Theme theme = Entry.readTheme("Give a Theme for the clue (detective/futurist/cowboys)");
        Clue clue = itemFactory.createClue(name, price, theme);
        try {
            clueDAO.addClue(clue, enigmaId);
            observable.notifySubscribers("New clue created '" + clue.getName() + "' for enigma with id: " + enigmaId);
        } catch (CallFailedException e) {
            throw new CallFailedException("Clue could not be added to DDBB. " + e.getMessage());
        }
    }


    private List<Clue> getAllClues() {
        return clueDAO.getData();
    }

    private void deleteClue(List<Clue> clues) throws CallFailedException {
        Integer clueId = Entry.readInt("Enter a clue id",
                clues.stream().map(Item::getItemId).toList());
        clueDAO.delete(clueId);
    }
}

package manager;

import DAO.implementation.EnigmaDAOImpl;
import DAO.implementation.GiftDAOImpl;
import exception.CallFailedException;
import model.User;
import model.item.implementations.Enigma;
import model.item.implementations.Gift;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameManager {
    private final List<User> players;
    private final Integer roomId;
    private final EnigmaDAOImpl enigmaDAO;
    private final GiftDAOImpl giftDAO;
    HashMap<User, Enigma> certificates;
    HashMap<User, Gift> gifts;

    public GameManager(List<User> players, Integer roomId){
        this.players = players;
        this.roomId = roomId;
        this.enigmaDAO = new EnigmaDAOImpl();
        this.giftDAO = new GiftDAOImpl();
    }

    public void playGame() {
        System.out.println("Trying to get out of the room!...");
        this.certificates = resolveEnigmas();
        this.gifts = grantGifts();
        System.out.println("Game has ended, Congratulations!...");
        this.certificates.forEach((user, enigma)
                -> System.out.println(user.getName() + " has resolved successfully the enigma "
                + enigma.getName()));
        this.gifts.forEach((user, gift)
                -> System.out.println(user.getName() + " is awarded gift: " + gift.getName()));
    }

    public HashMap<User, Enigma> resolveEnigmas(){

        List<Enigma> enigmas = this.enigmaDAO.getAllEnigmasByRoom(roomId);
        HashMap<User, Enigma> certificates = new HashMap<>();

        enigmas.forEach(enigma -> {
            for (User player : players){
                if (solveEnigma()){
                    certificates.put(player, enigma);
                    break;
                }
            }
        });

        if(!certificates.isEmpty()){
            certificates.forEach(((user, enigma)
                    -> {
                try {
                    enigmaDAO.addEnigmaToUser(user.getId(), enigma.getItemId());
                } catch (CallFailedException e) {
                    System.out.println("Enigma coult not be asigned to user in DDBB " + user.getId());
                }
            }));
        }
        return certificates;
    }

    public boolean solveEnigma(){
        return new Random().nextBoolean();
    }

    public HashMap<User, Gift> grantGifts() {
        HashMap<User, Gift> grantedGifts = new HashMap<>();
        List<Gift> availableGifts = giftDAO.getData();
        if (availableGifts.isEmpty()) {
            return grantedGifts;
        }

        this.certificates.forEach(((user, enigma) -> {
            Gift gift = availableGifts.get(new Random().nextInt(availableGifts.size()));
            grantedGifts.put(user, gift);
        }));

        if(!grantedGifts.isEmpty()) grantedGifts.forEach((user, gift)
                -> {
            try {
                giftDAO.assignGiftToUser( gift.getItemId(), user.getId());
            } catch (CallFailedException e) {
                System.out.println("Gift could not be assigned to user in DDBB " + user.getId());
            }
        });
        return grantedGifts;
    }
}
package manager;

import DAO.implementation.EnigmaDAOImpl;
import DAO.implementation.GiftDAOImpl;
import DAO.implementation.UserDAOImpl;
import exception.CallFailedException;
import model.User;
import model.item.implementations.Enigma;
import model.item.implementations.Gift;
import exception.IncorrectMenuOptionException;
import subscription.Observable;
import utils.Entry;
import utils.MenuUserOptions;

import java.util.List;

public class UserManager {

    private static UserManager instance;
    private final UserDAOImpl daoUser;
    private final GiftDAOImpl daoGift;
    private final EnigmaDAOImpl daoEnigma;

    private Observable observable;

    private UserManager(){
        this.daoUser = new UserDAOImpl();
        this.daoGift = new GiftDAOImpl();
        this.daoEnigma = new EnigmaDAOImpl();
    }

    public static UserManager getInstance(){
        if (instance == null) instance = new UserManager();
        return instance;
    }

    public void setObservable(Observable observable) {
        this.observable = observable;
    }

    public void start() {
        boolean close = false;
        int selectedMenuOption = -1;

        do {
            try {
                selectedMenuOption = menu();
                switch (selectedMenuOption) {
                    case 1:
                        createUser();
                        break;
                    case 2:
                        subscribeUser();
                        break;
                    case 3:
                        printCertificates();
                        break;
                    case 4:
                        showGifts();
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

    private int menu() throws IncorrectMenuOptionException {
        System.out.println("\nMenu User:");
        for (int i = 1; i <= MenuUserOptions.options.length; i++) {
            System.out.println( i + ". " + MenuUserOptions.options[i-1]);
        }
        System.out.println("0. " + MenuUserOptions.close);

        int menuOption = Entry.readInt("Select a menu option between 0 and " +
                MenuUserOptions.options.length + ".");
        if (menuOption < 0 || menuOption > MenuUserOptions.options.length)
            throw new IncorrectMenuOptionException("Menu option should be between 0 and " +
                    MenuUserOptions.options.length + ".");
        else return menuOption;
    }

    public void createUser() throws CallFailedException {
        String name = Entry.readString("Please type user's name: ");
        String email = Entry.readString("Please type user's email: ");
        Boolean isSubscriber = Entry.readBoolean("Does the user want to subscribe to notifications?, Yes > Y, No > N");
        User user = new User(name, email, isSubscriber);
        try {
            this.daoUser.add(user);
            if (isSubscriber) {
                observable.subscribe(user);
            }
        } catch (CallFailedException e) {
            throw new CallFailedException("User could not be created in DDBB. " + e.getMessage());
        }
    }

    public void subscribeUser() throws CallFailedException {
        List<User> users = getData();
        if(!users.isEmpty()){
            User user = selectUser(users);
            Boolean isSubscriber = Entry.readBoolean("Subscribe user to newsletter? Yes > Y, No > N");
            user.setIsSuscriber(isSubscriber);
            try {
                daoUser.updateUser(user);
                if (isSubscriber) {
                    observable.subscribe(user);
                } else {
                    observable.unsubscribe(user);
                }
            } catch (CallFailedException e) {
               throw new CallFailedException("User could not be updated in DDBB. " + e.getMessage());
            }
        }
    }

    public void printCertificates() {
        List<User> users = getData();
        if(!users.isEmpty()){
            User currentUser = selectUser(users);
            final String userName = currentUser.getName();
            List<Enigma> enigmas;
            enigmas = this.daoEnigma.getAllEnigmasByUser(currentUser);
            if (enigmas.isEmpty()) System.out.println("Sorry, the player " + userName +
                    " has not solved any enigma yet.");
            else{
                enigmas.forEach(e -> System.out.println("This escape room certifies that player " + userName +
                        " solved successfully the enigma '" + e.getName() + "'."));
            }
        }
    }

    public void showGifts(){
        List<User> users = getData();
        if(!users.isEmpty()){
            User user = selectUser(users);
            final String userName = user.getName();
            List<Gift> gifts;
            gifts = this.daoGift.getAllGiftsByUser(user.getId());
            if (gifts.isEmpty()) System.out.println("Sorry, the player " + userName +
                    " has not won any reward yet");
            else{
                System.out.println("The player " + userName + " has won the following rewards:");
                gifts.forEach(System.out::println);
            }
        }
    }

    public List<User> getAllUsers() {
        return daoUser.getData();
    }

    public User selectUser(List<User> users) {
        System.out.println("--- USER LIST ---");
        Integer id;
        User currentUser;
        users.forEach(System.out::println);

        id = Entry.readInt("Select user id >> ", users.stream().map(User::getId).toList());
        currentUser = users.stream().filter(user -> user.getId().equals(id))
                                    .findFirst().orElse(null);
        return currentUser;
    }

    public List<User> getData(){
        List<User> users = this.daoUser.getData();
        if (users.isEmpty()) System.out.println("Sorry, no user found in database.");
        return users;
    }
}



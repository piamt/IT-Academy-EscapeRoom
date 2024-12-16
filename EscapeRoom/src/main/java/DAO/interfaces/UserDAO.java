package DAO.interfaces;

import exception.CallFailedException;
import exception.DeleteUserFailedException;
import model.User;

import java.util.List;

public interface UserDAO extends DAO<User>{
    void add(User user)  throws CallFailedException;
    User getUser(Integer id);
    void updateUser(User user) throws CallFailedException;
    List<User> getUsersWithEnigma(Integer enigmaId);
    void deleteUsersWithEnigma(Integer enigmaId) throws DeleteUserFailedException;
}

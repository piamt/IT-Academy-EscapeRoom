package DAO.implementation;

import DAO.Parser;
import DAO.interfaces.UserDAO;
import exception.CallFailedException;
import exception.DeleteUserFailedException;
import model.User;
import connection.DbConnection;
import connection.DbConnectionImpl;
import connection.attribute.Attribute;
import DAO.Query;
import connection.callback.ParsingCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class UserDAOImpl implements UserDAO, ParsingCallback<User> {

    DbConnection dbConnection = DbConnectionImpl.getInstance();
    Parser<User> parser = new Parser<>(this);

    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String IS_SUBSCRIBER = "isSubscriber";
    private static final String IDUSER = "iduser";

    @Override
    public User getUser(Integer id) {
        List<Attribute> queryAttributeList = Arrays.asList(
                new Attribute<>(IDUSER, null, Integer.class)
        );
        List<Attribute> outputAttributes = Arrays.asList(
                new Attribute<>(IDUSER, null, Integer.class),
                new Attribute<>(NAME, null, String.class),
                new Attribute<>(EMAIL, null, String.class),
                new Attribute<>(IS_SUBSCRIBER, null, Boolean.class));

        HashSet<Attribute> attributeValues = dbConnection
                                    .query(Query.GETUSER, queryAttributeList, outputAttributes)
                                    .getFirst();

        if (attributeValues.isEmpty()) return null;

        User user = new User();
        parser.parseObject(user, attributeValues);
        return user;
    }

    @Override
    public void updateUser(User user) throws CallFailedException  {
        List<Attribute> attributeList = new ArrayList<>();
        attributeList.add(new Attribute<>(user.getName(), String.class));
        attributeList.add(new Attribute<>(user.getEmail(), String.class));
        attributeList.add(new Attribute<>(user.isSuscriber(), Boolean.class));
        attributeList.add(new Attribute<>(user.getId(), Integer.class));
        dbConnection.create(Query.UPDATEUSER, attributeList);
    }

    public void add(User user) throws CallFailedException {
        List<Attribute> attributeList = new ArrayList<>();
        attributeList.add(new Attribute<>(user.getName(), String.class));
        attributeList.add(new Attribute<>(user.getEmail(), String.class));
        attributeList.add(new Attribute<>(user.isSuscriber(), Boolean.class));
        dbConnection.create(Query.CREATEUSER, attributeList);
    }

    @Override
    public List<User> getData() {
        List<User> users = new ArrayList<>();

        List<Attribute> queryAttributeList = List.of();
        List<Attribute> outputAttributes = Arrays.asList(
                new Attribute<>(IDUSER, null, Integer.class),
                new Attribute<>(NAME, null, String.class),
                new Attribute<>(EMAIL, null, String.class),
                new Attribute<>(IS_SUBSCRIBER, null, Boolean.class));

        return this.getUsers(Query.SHOWUSERS, queryAttributeList, outputAttributes);
    }

    private List<User> getUsers(String query, List<Attribute> queryAttributes, List<Attribute> outputAttributes) {

        List<User> users = new ArrayList<>();

        List<HashSet<Attribute>> usersList = dbConnection.query(query, queryAttributes, outputAttributes);

        if (usersList.isEmpty()) return List.of();

        for (HashSet<Attribute> attributeValues: usersList) {
            User user = new User();
            parser.parseObject(user, attributeValues);
            users.add(user);
        }

        return users;
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<User> getUsersWithEnigma(Integer enigmaId) {
        List<Attribute> queryAttributeList = List.of(new Attribute<>(enigmaId, Integer.class));

        List<Attribute> outputAttributes = Arrays.asList(
                new Attribute<>(IDUSER, null, Integer.class),
                new Attribute<>(NAME, null, String.class),
                new Attribute<>(EMAIL, null, String.class),
                new Attribute<>(IS_SUBSCRIBER, null, Boolean.class));

        return this.getUsers(Query.GETUSERSWITHENIGMA, queryAttributeList, outputAttributes);
    }

    @Override
    public void deleteUsersWithEnigma(Integer enigmaId) throws DeleteUserFailedException {
        List<Attribute> attributeList = new ArrayList<>();
        attributeList.add(new Attribute<>(enigmaId, Integer.class));
        try {
            dbConnection.delete(Query.DELETEUSERHASENIGMA, attributeList);
        } catch (CallFailedException e) {
            throw new DeleteUserFailedException(e.getMessage());
        }
    }

    @Override
    public void onCallbackString(User object, Attribute<String> attribute) {
        switch (attribute.getName()){
            case NAME -> object.setName(attribute.getValue());
            case EMAIL -> object.setEmail(attribute.getValue());
        }
    }

    @Override
    public void onCallbackInteger(User object, Attribute<Integer> attribute) {
        if (attribute.getName().equals(IDUSER)) object.setId(attribute.getValue());
    }

    @Override
    public void onCallbackDouble(User object, Attribute<Double> attribute) {}

    @Override
    public void onCallbackBoolean(User object, Attribute<Boolean> attribute){
        if (attribute.getName().equals(IS_SUBSCRIBER)) object.setIsSuscriber(attribute.getValue());
    }
}

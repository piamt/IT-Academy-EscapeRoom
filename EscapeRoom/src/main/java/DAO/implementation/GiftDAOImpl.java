package DAO.implementation;

import DAO.Parser;
import DAO.interfaces.GiftDAO;
import exception.CallFailedException;
import model.item.ItemFactory;
import model.item.implementations.Gift;
import model.item.implementations.ItemFactoryImpl;
import connection.DbConnection;
import connection.DbConnectionImpl;
import connection.attribute.Attribute;
import DAO.Query;
import connection.callback.ParsingCallback;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class GiftDAOImpl implements GiftDAO, ParsingCallback<Gift> {

    DbConnection dbConnection = DbConnectionImpl.getInstance();
    Parser<Gift> parser = new Parser<>(this);
    ItemFactory itemFactory = new ItemFactoryImpl();

    private static final String IDGIFT = "idgift";
    private static final String NAME = "name";
    private static final String PRICE = "price";

    @Override
    public void addGift(Gift gift) throws CallFailedException {
        List<Attribute> attributeList = new ArrayList<>();
        attributeList.add(new Attribute<>(gift.getName(), String.class));
        dbConnection.create(Query.CREATEGIFT, attributeList);
    }

    @Override
    public List<Gift> getAllGiftsByUser(Integer userId) {
        List<Attribute> queryAttributeList = List.of(new Attribute<>(userId, Integer.class));
        List<Attribute> outputAttributes = Arrays.asList(
                new Attribute<>(IDGIFT, null, Integer.class),
                new Attribute<>(NAME, null, String.class));

        return this.getGifts(Query.GETGIFTBYUSER, queryAttributeList, outputAttributes);
    }

    @Override
    public Gift getGiftById(Integer itemId) {
        List<Attribute> queryAttributeList = List.of(new Attribute<>(itemId, Integer.class));
        List<Attribute> outputAttributes = Arrays.asList(
                new Attribute<>(IDGIFT, null, Integer.class),
                new Attribute<>(NAME, null, String.class));

        return this.getGifts(Query.GETGIFTBYID, queryAttributeList, outputAttributes).getFirst();
    }

    @Override
    public void assignGiftToUser(Integer giftId, Integer userId) throws CallFailedException {
        List<Attribute> attributeList = new ArrayList<>();
        attributeList.add(new Attribute<>(giftId, Integer.class));
        attributeList.add(new Attribute<>(userId, Integer.class));
        dbConnection.create(Query.CREATEUSERHASGIFT, attributeList);
    }

    @Override
    public List<Gift> getData() {
        List<Attribute> queryAttributeList = List.of();
        List<Attribute> outputAttributes = Arrays.asList(
                new Attribute<>(IDGIFT, null, Integer.class),
                new Attribute<>(NAME, null, String.class));

        return this.getGifts(Query.GETALLGIFTS, queryAttributeList, outputAttributes);
    }

    private List<Gift> getGifts(String query, List<Attribute> queryAttributes, List<Attribute> outputAttributes) {

        List<Gift> gifts = new ArrayList<>();

        List<HashSet<Attribute>> giftList = dbConnection.query(query, queryAttributes, outputAttributes);

        if (giftList.isEmpty()) return List.of();

        for (HashSet<Attribute> attributeValues: giftList) {
            Gift gift = itemFactory.createGift();
            parser.parseObject(gift, attributeValues);
            gifts.add(gift);
        }

        return gifts;
    }

    @Override
    public void delete(Integer id) throws CallFailedException {
        List<Attribute> queryAttributeList = new ArrayList<>();
        queryAttributeList.add(new Attribute(id, Integer.class));
        dbConnection.delete(Query.DELETEGIFT, queryAttributeList);
    }

    @Override
    public void onCallbackString(Gift object, Attribute<String> attribute) {
        if (attribute.getName().equals(NAME)) {
            object.setName(attribute.getValue());
        }
    }

    @Override
    public void onCallbackInteger(Gift object, Attribute<Integer> attribute) {
        if (attribute.getName().equals(IDGIFT)) {
            object.setItemId(attribute.getValue());
        }
    }

    @Override
    public void onCallbackDouble(Gift object, Attribute<Double> attribute) {
        if (attribute.getName().equals(PRICE)) {
            object.setPrice(attribute.getValue());
        }
    }

    @Override
    public void onCallbackBoolean(Gift object, Attribute<Boolean> attribute) {}
}

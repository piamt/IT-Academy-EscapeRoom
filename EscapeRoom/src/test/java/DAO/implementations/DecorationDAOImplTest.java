package DAO.implementations;

import DAO.implementation.DecorationDAOImpl;
import DAO.interfaces.DecorationDAO;
import exception.CallFailedException;
import model.enums.Material;
import model.item.ItemFactory;
import model.item.implementations.Decoration;
import model.item.implementations.ItemFactoryImpl;
import connection.attribute.Attribute;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DecorationDAOImplTest {

    ItemFactory itemFactory;
    DecorationDAO decorationDAO;
    Decoration decoration;
    Integer roomId;
    DBConnectionMock dbConnection;

    @BeforeEach
    void init() {
        this.itemFactory = new ItemFactoryImpl();
        this.dbConnection = new DBConnectionMock();
        this.decorationDAO = new DecorationDAOImpl(dbConnection);
        this.decoration = itemFactory.createDecoration("Deco name", 43.6, Material.METAL, 3);
        this.roomId = 6;
    }

    @Test
    void givenDecorationDAO_whenAddDecoration_ThenQueryAttributesAsExpected() {
        try {
            decorationDAO.addDecoration(decoration, roomId);
            Assertions.assertEquals(dbConnection.queryAttributes.size(), 5);
            Assertions.assertEquals(dbConnection.queryAttributes.getFirst(), new Attribute<String>(this.decoration.getName(), String.class));
            Assertions.assertEquals(dbConnection.queryAttributes.get(1), new Attribute<String>(this.decoration.getMaterial().name(), String.class));
            Assertions.assertEquals(dbConnection.queryAttributes.get(2), new Attribute<Double>(this.decoration.getPrice(), Double.class));
            Assertions.assertEquals(dbConnection.queryAttributes.get(3), new Attribute<Integer>(this.decoration.getQuantity(), Integer.class));
            Assertions.assertEquals(dbConnection.queryAttributes.get(4), new Attribute<Integer>(roomId, Integer.class));
        } catch (CallFailedException e) {
            Assertions.fail();
        }
    }

    @Test
    void givenDecorationDAO_whenAddDecoration_ThenExpectedArgumentsQuantityInQueryString() {
        try {
            decorationDAO.addDecoration(decoration, roomId);
            long count = dbConnection.query.chars().filter(ch -> ch == '?').count();
            Assertions.assertEquals(dbConnection.queryAttributes.size(), count);
        }  catch (CallFailedException e) {
            Assertions.fail();
        }
    }
}
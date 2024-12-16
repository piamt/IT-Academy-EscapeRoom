package DAO.implementations;

import DAO.implementation.RoomDAOImpl;
import DAO.interfaces.RoomDAO;
import exception.CallFailedException;
import model.Room;
import model.enums.Level;
import connection.attribute.Attribute;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

public class RoomDAOImplTest {

    RoomDAO roomDAO;
    DBConnectionMock dbConnection;

    @BeforeEach
    void init() {
        this.dbConnection = new DBConnectionMock();
        this.roomDAO = new RoomDAOImpl(dbConnection);
    }

    @ParameterizedTest
    @CsvSource({"Room,123.50,HIGH,4", "Charles,12.0,LOW,5"})
    void givenRoomDAO_whenAddRoom_ThenQueryAttributesAsExpected(ArgumentsAccessor argumentsAccessor) {
        String name = argumentsAccessor.getString(0);
        Double price = argumentsAccessor.get(1, Double.class);
        Level level = Level.valueOf(argumentsAccessor.get(2, String.class));
        Integer escapeRoomId = argumentsAccessor.get(3, Integer.class);
        Room room = new Room(name, price, level);

        try {
            roomDAO.addRoom(room, escapeRoomId);
            Assertions.assertEquals(dbConnection.queryAttributes.size(), 4);
            Assertions.assertEquals(dbConnection.queryAttributes.getFirst(), new Attribute<String>(room.getName(), String.class));
            Assertions.assertEquals(dbConnection.queryAttributes.get(1), new Attribute<Double>(room.getPrice(), Double.class));
            Assertions.assertEquals(dbConnection.queryAttributes.get(2), new Attribute<String>(room.getLevel().name(), String.class));
            Assertions.assertEquals(dbConnection.queryAttributes.get(3), new Attribute<Integer>(escapeRoomId, Integer.class));
        } catch (CallFailedException e) {
            Assertions.fail();
        }
    }

    @Test
    void givenRoomDAO_whenAddRoom_ThenExpectedArgumentsQuantityInQueryString() {
        try {
            roomDAO.addRoom(new Room("Charles", 43.5, Level.MEDIUM), 5);
            long count = dbConnection.query.chars().filter(ch -> ch == '?').count();
            Assertions.assertEquals(dbConnection.queryAttributes.size(), count);
        } catch (CallFailedException e) {
            Assertions.fail();
        }
    }
}

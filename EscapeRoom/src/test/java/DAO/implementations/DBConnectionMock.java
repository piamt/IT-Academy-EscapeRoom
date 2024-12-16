package DAO.implementations;

import connection.DbConnection;
import connection.attribute.Attribute;
import exception.CallFailedException;

import java.util.HashSet;
import java.util.List;

public class DBConnectionMock implements DbConnection {

    String query;
    List<Attribute> queryAttributes;

    @Override
    public void create(String query, List<Attribute> queryAttributes) throws CallFailedException {
        this.query = query;
        this.queryAttributes = queryAttributes;
    }

    @Override
    public void delete(String query, List<Attribute> queryAttributes)  throws CallFailedException {

    }

    @Override
    public List<HashSet<Attribute>> query(String query, List<Attribute> queryAttributes, List<Attribute> outputAttributes) {
        return List.of();
    }
}

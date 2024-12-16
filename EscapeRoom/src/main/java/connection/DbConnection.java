package connection;

import connection.attribute.Attribute;
import exception.CallFailedException;

import java.util.HashSet;
import java.util.List;

public interface DbConnection {
    public void create(String query, List<Attribute> queryAttributes) throws CallFailedException;
    public void delete(String query, List<Attribute> queryAttributes) throws CallFailedException;
    public List<HashSet<Attribute>> query(String query, List<Attribute> queryAttributes, List<Attribute> outputAttributes);
}


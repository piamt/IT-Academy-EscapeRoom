package DAO.interfaces;

import exception.CallFailedException;

import java.util.List;

public interface DAO<T> {
    List<T> getData();
    void delete(Integer id) throws CallFailedException;
}

package com.example.megacitycab.daos;

import java.util.List;

public interface GenericDAO<T> {
    boolean add(T entity);
    T getById(int id);
    List<T> getAll();
    boolean update(T entity);
    boolean delete(int id);
    List<T> search(String search);
}

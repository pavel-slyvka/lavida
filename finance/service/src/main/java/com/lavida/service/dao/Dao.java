package com.lavida.service.dao;

import java.util.List;

public interface Dao<T> {
	T getById(int id);
	void put(T t);
	void update(T t);
    List<T> getAll();
    void delete(int id);

}

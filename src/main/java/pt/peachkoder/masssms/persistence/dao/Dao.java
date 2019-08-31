package pt.peachkoder.masssms.persistence.dao;

import java.util.List;

public interface Dao<T> {

    T get(int id);

    T get(String type);

    List<T> getAll();

    void save(T t);

    void update(T t, String[] params);

    void delete(T t);

}

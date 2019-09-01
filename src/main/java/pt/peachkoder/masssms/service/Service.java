package pt.peachkoder.masssms.service;

import java.util.List;

public interface Service<T> {

    List<T> getList();

    T get(int id);

    T get(String id);

    void set(T t);
}

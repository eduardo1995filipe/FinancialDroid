package bagarrao.financialdroid.database;


import java.util.Vector;

public interface Database <T>{

    void insert(T element);

    void delete(T element);

    void update(T oldElement, T newElement);

    Vector<T> selectAll();

    void deleteAll();
}

package edu.csf.persistence.base;

import java.util.Optional;

public interface DAOInterface<K, V extends IdentityInterface<K>> {

    void put(V object);

    Optional<V> get(K key);
}

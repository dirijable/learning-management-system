package com.dirijable.labs.lms.repository;

import java.util.Optional;

public interface CrudRepository<E, I> {
    E save(E entity);

    Optional<E> findById(I id);

    void deleteById(I id);

}

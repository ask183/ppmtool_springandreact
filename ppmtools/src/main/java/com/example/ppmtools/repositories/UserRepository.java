package com.example.ppmtools.repositories;

import com.example.ppmtools.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
    User getById(Long id);

//    Optional<User> findById(Long aLong);    //Optional prevents null pointer exception

}

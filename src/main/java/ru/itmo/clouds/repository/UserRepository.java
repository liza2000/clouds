package ru.itmo.clouds.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.clouds.entity.EUser;

import java.util.*;
@Repository
public interface UserRepository extends CrudRepository<EUser, Long> {
     Optional<EUser> findByLogin(String login);
}
package ru.itmo.clouds.repository;

import org.springframework.data.repository.CrudRepository;
import ru.itmo.clouds.entity.EUser;

import java.util.*;

public interface UserRepository extends CrudRepository<EUser, Long> {
     Optional<EUser> findByLogin( String login);
}
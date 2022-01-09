package ru.itmo.clouds.repository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import java.util.*;
import javax.persistence.*;

public interface UserRepository extends CrudRepository<EUser, Long> {
     Optional<EUser> findByLogin( String login);
}
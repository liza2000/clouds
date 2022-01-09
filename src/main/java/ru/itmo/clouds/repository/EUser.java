package ru.itmo.clouds.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public
class EUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id = 0L;
    String login = "";
    String password = "";
}

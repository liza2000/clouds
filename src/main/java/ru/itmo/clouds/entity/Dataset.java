package ru.itmo.clouds.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Dataset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Date created;
    String name;
    String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    EUser user;
}

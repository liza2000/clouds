package ru.itmo.clouds.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Entity
@AllArgsConstructor
public class DatasetVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String message;
    Date created;

    @ManyToOne
    @JoinColumn(name = "ds_id")
    Dataset dataset;
}

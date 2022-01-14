package ru.itmo.clouds.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PicCounter{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "ds_id", unique = true, nullable = false)
    Dataset dataset;

    @Column(nullable = false)
    Long lastPicId = 0L;
}

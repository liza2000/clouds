package ru.itmo.clouds.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Version {
    @Id
    @GeneratedValue
    private Long id;

    String message;

    @ManyToOne
    @JoinColumn(name = "ds_id")
    Dataset dataset;

    @ManyToOne
    @JoinColumn(name = "user_id")
    EUser user;

    @Column(columnDefinition = "timestamp")
    @CreationTimestamp
    Date created;



}

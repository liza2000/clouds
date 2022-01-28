package ru.itmo.clouds.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Experiment {

    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "version_id")
    Version version;
    Date startEx;
    Date endEx;
    String status;
    String result;
    String title;
    String description;

    @ManyToOne
    @JoinColumn(name="user_id")
    EUser user;
}

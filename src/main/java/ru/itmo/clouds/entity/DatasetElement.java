package ru.itmo.clouds.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DatasetElement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String path;
    @Column(nullable = false)
    String name;

    Long picId;

    @Column(nullable = false)
    Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "version_id")
    Version version;

    @ManyToOne
    @JoinColumn(name = "ds_id")
    Dataset dataset;
}

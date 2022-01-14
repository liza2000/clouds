package ru.itmo.clouds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.clouds.entity.DatasetExperiment;

public interface DatasetExperimentRepository extends JpaRepository<DatasetExperiment, Long> {

}

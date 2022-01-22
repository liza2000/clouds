package ru.itmo.clouds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.clouds.entity.Experiment;

@Repository
public interface DatasetExperimentRepository extends JpaRepository<Experiment, Long> {

}

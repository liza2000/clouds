package ru.itmo.clouds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.clouds.entity.Dataset;

public interface DatasetRepository extends JpaRepository<Dataset, Long> {

}

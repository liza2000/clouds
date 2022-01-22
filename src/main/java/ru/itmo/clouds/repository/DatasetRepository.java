package ru.itmo.clouds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.clouds.entity.Dataset;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset, Long> {

}

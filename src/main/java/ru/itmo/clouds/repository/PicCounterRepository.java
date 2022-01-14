package ru.itmo.clouds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.clouds.entity.Dataset;
import ru.itmo.clouds.entity.PicCounter;

public interface PicCounterRepository extends JpaRepository<PicCounter, Long> {
    PicCounter findPicCounterByDataset(Dataset dataset);
}

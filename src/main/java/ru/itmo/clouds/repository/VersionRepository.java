package ru.itmo.clouds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.clouds.entity.Dataset;
import ru.itmo.clouds.entity.Version;

import java.util.List;

public interface VersionRepository  extends JpaRepository<Version, Long> {
        List<Version> findAllByDataset(Dataset dataset);
}

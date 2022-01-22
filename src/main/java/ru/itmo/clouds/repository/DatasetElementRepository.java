package ru.itmo.clouds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itmo.clouds.entity.Dataset;
import ru.itmo.clouds.entity.DatasetElement;
import ru.itmo.clouds.entity.Version;

import java.util.List;

@Repository
public interface DatasetElementRepository extends JpaRepository<DatasetElement, Long> {
    List<DatasetElement> findAllByPicIdAndDatasetOrderByVersionDesc(Long picId, Dataset dataset);
    @Query(value = "select * from dataset_element de distinct pic_id " +
            "where deleted = false and version_id <= :(version.id)" +
            " and version_id = (select id from version " +
            "               where ds_id=:(version.dsId)" +
            "               and created=(select max(created) from version " +
            "                               where ds_id=:(version.dsId)))" +
            "                                and id in( select version_id from dataset_element where pic_id=de.pic_id)",
            nativeQuery = true)
    List<DatasetElement> findDatasetElementsByVersion(Version version);
}

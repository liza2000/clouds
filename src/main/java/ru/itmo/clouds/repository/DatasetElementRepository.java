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
    @Query(value = "select * from dataset_element as de" +
            "    where deleted = false" +
            "    and version_id = (select max(da.version_id) from dataset_element as da where de.ds_id=:dsId and da.pic_id = de.pic_id and da.version_id<=:versionId)", nativeQuery = true)
    List<DatasetElement> findDatasetElementsByVersion(Long versionId, Long dsId);

    DatasetElement findDatasetElementsByDatasetAndVersionAndPicId(Dataset dataset, Version version, Long picId);

}

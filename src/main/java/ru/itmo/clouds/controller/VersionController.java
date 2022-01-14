package ru.itmo.clouds.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.itmo.clouds.auth.UserDetailsImpl;
import ru.itmo.clouds.entity.*;
import ru.itmo.clouds.repository.*;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@AllArgsConstructor
class VersionRequest {
     String message;
     Long dsId;
     List<PicData> addRequest;
     List<PicData> updateRequest;
     List<Long> deleteRequest;
}



@AllArgsConstructor
@NoArgsConstructor
class VersionResponse{
    Date created;
    String message;
    List<PicData> picList;
}




@AllArgsConstructor
@NoArgsConstructor
class PicData {
    Long pic_id;
    File pic;
    String name;
}



@Controller
@AllArgsConstructor
@RestController
@RequestMapping("/dataset/version")
public class VersionController {
    private final Logger logger  = LoggerFactory.getLogger(VersionController.class);

    private UserRepository userRepository;
    private DatasetRepository datasetRepository;
    private VersionRepository versionRepository;
    private DatasetElementRepository datasetElementRepository;
    private PicCounterRepository picCounterRepository;


    @Value("${clouds.store.defaultPath}")
    private final String defaultPath = "";

    @PostMapping
    ResponseEntity<Object> commit(@RequestBody VersionRequest request){
        EUser user = userRepository.findByLogin(((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin()).orElseThrow(EntityNotFoundException::new);
        Dataset dataset = datasetRepository.findById(request.dsId).orElseThrow(EntityNotFoundException::new);
        Version version = versionRepository.save(new Version(0L,request.message,dataset,user,new Date()));
        String date = new SimpleDateFormat("yyyy-MM-dd").format(version.getCreated());
        if (request.addRequest!=null) {
            PicCounter picCounter = picCounterRepository.findPicCounterByDataset(dataset);
            Long lastId = picCounter.getLastPicId();
            for (PicData picData : request.addRequest) {
                datasetElementRepository.save(new DatasetElement(0L,
                        defaultPath + "/" + dataset.getName() + "/" + ++lastId + "_" + date,
                        picData.name,
                        lastId,
                        false,
                        version));
                // TODO: 14.01.2022 загрузка файла
            }
            picCounter.setLastPicId(lastId);
            picCounterRepository.save(picCounter);
        }
        if (request.updateRequest != null)
            for (PicData picData : request.updateRequest) {
                DatasetElement lastElement = datasetElementRepository.findAllByPicIdOrderByVersionDesc(picData.pic_id).get(0);
                String path = lastElement.getPath();
                if (picData.pic!=null){
                    // TODO: 14.01.2022 загрузка файла
                    path = defaultPath + "/" + dataset.getName() + "/" + picData.pic_id + "_" + date;
                }

                datasetElementRepository.save(new DatasetElement(0L,
                        path,
                        picData.name==null || picData.name.trim().isEmpty()?lastElement.getName(): picData.name,
                        lastElement.getPicId(),
                        false,
                        version));
            }
        if (request.deleteRequest!=null)
            for (Long id: request.deleteRequest){
                DatasetElement lastElement = datasetElementRepository.findAllByPicIdOrderByVersionDesc(id).get(0);
                datasetElementRepository.save(new DatasetElement(0L,
                        lastElement.getPath(),
                        lastElement.getName(),
                        lastElement.getPicId(),
                        true,
                        version));
            }

        return ResponseEntity.ok(dataset);
    }

    @GetMapping
    public ResponseEntity<List<VersionResponse>> getVersions(@RequestBody Long dsId){
        Dataset dataset = datasetRepository.findById(dsId).orElseThrow(EntityNotFoundException::new);
        List<Version> versions = versionRepository.findAllByDataset(dataset);
        List<VersionResponse> response = new ArrayList<>();

        for(Version version: versions) {
            List<DatasetElement> datasetElements = datasetElementRepository.findDatasetElementsByVersion(version);
            List<PicData> picData = new ArrayList<>();
            for (DatasetElement d : datasetElements)
                picData.add(new PicData(d.getPicId(),new File(d.getPath()), d.getName()));
            response.add(new VersionResponse(version.getCreated(), version.getMessage(), picData));
        }
        return ResponseEntity.ok(response);

    }

    @GetMapping("{id}")
    public ResponseEntity<List<VersionResponse>> getVersion(@PathVariable Long id){
        Dataset dataset = datasetRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Version version = versionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            List<DatasetElement> datasetElements = datasetElementRepository.findDatasetElementsByVersion(version);
             List<VersionResponse> response = new ArrayList<>();
            List<PicData> picData = new ArrayList<>();
            for (DatasetElement d : datasetElements)
                picData.add(new PicData(d.getPicId(),new File(d.getPath()), d.getName()));
            response.add(new VersionResponse(version.getCreated(), version.getMessage(), picData));
        return ResponseEntity.ok(response);
    }


}

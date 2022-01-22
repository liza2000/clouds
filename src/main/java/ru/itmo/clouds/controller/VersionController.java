package ru.itmo.clouds.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itmo.clouds.auth.UserDetailsImpl;
import ru.itmo.clouds.entity.*;
import ru.itmo.clouds.repository.*;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@AllArgsConstructor
@Data
@NoArgsConstructor
class VersionRequest {
     String message;
     Long dsId;
     List<PicData> addRequest;
     List<PicData> updateRequest;
     List<Long> deleteRequest;
}



@AllArgsConstructor
@NoArgsConstructor
@Data
class VersionResponse{
    Long id;
    Date created;
    String message;
    List<PicData> picList;
}




@AllArgsConstructor
@NoArgsConstructor
@Data
class PicData {
    Long pic_id;
    File pic;
    String name;
}



@Controller
@AllArgsConstructor
@RestController
@RequestMapping("/api/version")
public class VersionController {
    private final Logger logger  = LoggerFactory.getLogger(VersionController.class);

    private UserRepository userRepository;
    private DatasetRepository datasetRepository;
    private VersionRepository versionRepository;
    private DatasetElementRepository datasetElementRepository;
    private PicCounterRepository picCounterRepository;


    @Value("${clouds.store.path}")
    private static String defaultPath = "F:/4course/clouds/client_service/folder";

    @RequestMapping(method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> commit(@RequestPart(name = "addFile", required = false) List<MultipartFile> addFiles,
                                  @RequestParam(name = "updateFile", required = false) List<MultipartFile> updateFiles,
                                  @RequestParam(name = "updatePicId", required = false) List<Long> updatePicIds,
                                  @RequestParam(name = "deletePicId", required = false) List<Long> deletePicIds,
                                  @RequestParam String message,
                                  @RequestParam Long dsId) throws IOException {

        EUser user = userRepository.findByLogin(
                ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin()).orElseThrow(() -> new EntityNotFoundException("user not found"));
        Dataset dataset = datasetRepository.findById(dsId).orElseThrow(() -> new EntityNotFoundException("dataset not found"));
        Version version = versionRepository.save(new Version(0L, message,dataset,user,new Date()));
        String date = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(version.getCreated());
        if (addFiles!=null) {
            PicCounter picCounter = picCounterRepository.findPicCounterByDataset(dataset);
            Long lastId = picCounter.getLastPicId();
            for (MultipartFile file : addFiles) {
                String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                String path = defaultPath + "/" + dataset.getName() + "/" + ++lastId + "_" + date+"."+extension;
                datasetElementRepository.save(new DatasetElement(0L,
                        path,
                        file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf(".")),
                        lastId,
                        false,
                        version,
                        dataset));
                File save = new File(path);
                file.transferTo(save);
            }
            picCounter.setLastPicId(lastId);
            picCounterRepository.save(picCounter);
        }

        if (updateFiles!=null && updatePicIds!=null && updateFiles.size()==updatePicIds.size())
            for (int i = 0; i<updateFiles.size();i++) {
                Long picId = updatePicIds.get(i);
                MultipartFile file = updateFiles.get(i);
                String extension = updateFiles.get(i).getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                String path = defaultPath + "/" + dataset.getName() + "/" + picId + "_" + date+"."+extension;

                datasetElementRepository.save(new DatasetElement(0L,
                        path,
                        file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf(".")),
                        picId,
                        false,
                        version,
                        dataset));
                File save = new File(path);
                updateFiles.get(i).transferTo(save);
            }
        if (deletePicIds!=null)
            for (Long id: deletePicIds){

                DatasetElement lastElement = datasetElementRepository.findAllByPicIdAndDatasetOrderByVersionDesc(id, dataset).get(0);
                String path = defaultPath + "/" + dataset.getName() + "/" + id + "_" + date+"." + lastElement.getPath().substring(lastElement.getPath().lastIndexOf("."));

                datasetElementRepository.save(new DatasetElement(0L,
                        path,
                        lastElement.getName(),
                        lastElement.getPicId(),
                        true,
                        version,
                        dataset));
            }

        return ResponseEntity.ok(dataset);
    }

    @GetMapping
    public ResponseEntity<List<VersionResponse>> getVersions(@RequestBody Long dsId){
        Dataset dataset = datasetRepository.findById(dsId).orElseThrow(() -> new EntityNotFoundException("dataset not found"));
        List<Version> versions = versionRepository.findAllByDataset(dataset);
        List<VersionResponse> response = new ArrayList<>();

        for(Version version: versions) {
            List<DatasetElement> datasetElements = datasetElementRepository.findDatasetElementsByVersion(version.getId(), dsId);
            List<PicData> picData = new ArrayList<>();
            for (DatasetElement d : datasetElements)
                picData.add(new PicData(d.getPicId(),new File(d.getPath()), d.getName()));
            response.add(new VersionResponse(version.getId(),version.getCreated(), version.getMessage(), picData));
        }
        return ResponseEntity.ok(response);

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<List<VersionResponse>> getVersion(@PathVariable Long id){
        Version version = versionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("version not found"));
            List<DatasetElement> datasetElements = datasetElementRepository.findDatasetElementsByVersion(version.getId(), version.getDataset().getId());
             List<VersionResponse> response = new ArrayList<>();
            List<PicData> picData = new ArrayList<>();
            for (DatasetElement d : datasetElements)
                picData.add(new PicData(d.getPicId(),new File(d.getPath()), d.getName()));
            response.add(new VersionResponse(version.getId(),version.getCreated(), version.getMessage(), picData));
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/file/{dsId}/{verId}/{picId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public FileSystemResource getFile(@PathVariable Long dsId, @PathVariable Long verId, @PathVariable Long picId){
        Dataset dataset = datasetRepository.findById(dsId).orElseThrow(() -> new EntityNotFoundException("dataset not found"));
        Version version = versionRepository.findById(verId).orElseThrow(() -> new EntityNotFoundException("version not found"));
        DatasetElement datasetElement = datasetElementRepository.findDatasetElementsByDatasetAndVersionAndPicId(dataset, version,picId);
        if (datasetElement==null || datasetElement.getDeleted())
            throw new EntityNotFoundException("file not found");
        return new FileSystemResource(datasetElement.getPath());

    }


}

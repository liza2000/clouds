package ru.itmo.clouds.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.clouds.repository.DatasetExperimentRepository;


@AllArgsConstructor
@NoArgsConstructor
@Data
class ExperimentRequest{
    Long dsId;
    Long versionId;
    String title;
    String description;
}
@AllArgsConstructor
@RestController
@RequestMapping("/api/experiment")
public class ExperimentController {

    DatasetExperimentRepository experimentRepository;


    @PostMapping()
    public ResponseEntity<String> launchExperiment(@RequestBody ExperimentRequest request){
       return null;
    }
}

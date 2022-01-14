package ru.itmo.clouds.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.itmo.clouds.auth.JwtUtils;
import ru.itmo.clouds.auth.UserDetailsImpl;
import ru.itmo.clouds.entity.Dataset;
import ru.itmo.clouds.repository.DatasetRepository;
import ru.itmo.clouds.entity.EUser;
import ru.itmo.clouds.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;


@AllArgsConstructor
class DatasetRequest {
    Long id;
    String name;
    String description;
}



@Controller
@AllArgsConstructor
@RestController
@RequestMapping("/dataset")
public class DatasetController {
    private final Logger logger  = LoggerFactory.getLogger(DatasetController.class);

    private UserRepository userRepository;
    private DatasetRepository datasetRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private PasswordEncoder encoder;

    @PostMapping
    ResponseEntity<Object> addDataset(@RequestBody DatasetRequest addRequest){
        EUser user = userRepository.findByLogin(((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin()).orElseThrow(EntityNotFoundException::new);
        Dataset dataset = new Dataset(0L,new Date(), addRequest.name,addRequest.description,user);
        dataset = datasetRepository.save(dataset);
        return ResponseEntity.ok(dataset);
    }

    @PutMapping
    ResponseEntity<Object> updateDataset(@RequestBody DatasetRequest addRequest){
        Dataset dataset = datasetRepository.getOne(addRequest.id);
        dataset.setName(addRequest.name);
        dataset.setDescription(addRequest.description);
        datasetRepository.save(dataset);
        return ResponseEntity.ok(dataset);
    }

    @GetMapping("{id}")
    ResponseEntity<Object> getDataset(@PathVariable Long id){
        Dataset dataset = datasetRepository.getOne(id);
        return ResponseEntity.ok(dataset);
    }

    @GetMapping
    ResponseEntity<Object> getDatasets(){
        List<Dataset> dataset = datasetRepository.findAll();
        return ResponseEntity.ok(dataset);
    }



}

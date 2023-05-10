package com.webkit640.backend.controller;


import com.webkit640.backend.dto.TraineeDto;
import com.webkit640.backend.dto.response.ResponseWrapper;
import com.webkit640.backend.entity.Trainee;
import com.webkit640.backend.service.logic.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/trainee")
public class TraineeController {

    private final TraineeService traineeService;

    @Autowired
    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping("")
    public ResponseEntity<?> addTrainee(@AuthenticationPrincipal int id) {
        Trainee trainee = traineeService.saveTrainee(id);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.
                        fromCurrentRequest().
                        path("/{id}").
                        buildAndExpand(trainee.getId()).
                        toUri()).
                body(
                        ResponseWrapper.addObject(
                                TraineeDto.SaveTraineeResponseDto.entityToDto(trainee), HttpStatus.CREATED)
                );
    }
}

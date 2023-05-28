package com.webkit640.backend.controller;


import com.webkit640.backend.config.annotation.Admin;
import com.webkit640.backend.dto.TraineeDto;
import com.webkit640.backend.dto.response.ResponseWrapper;
import com.webkit640.backend.entity.Trainee;
import com.webkit640.backend.service.logic.TraineeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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
    @ApiOperation(value = "Add trainee", notes = "<h2>Add trainee</h2>")
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
    @Admin
    @GetMapping("")
    @ApiOperation(value = "Search trainee", notes = "<h2>Search trainee</h2>")
    public ResponseEntity<?> searchTrainee(
            @AuthenticationPrincipal int id,
            @RequestParam(required = false) String cardinal,
            @RequestParam(required = false) String school,
            @RequestParam(required = false) String major) {

        return ResponseEntity.ok().body(
                ResponseWrapper.addObject(TraineeDto.TraineeListResponseDto.entitiesToDto(
                        traineeService.searchTrainee(cardinal,school,major)),HttpStatus.OK)
        );
    }
}

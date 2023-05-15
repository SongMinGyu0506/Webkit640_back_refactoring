package com.webkit640.backend.service.logic;

import com.webkit640.backend.entity.Trainee;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TraineeService {
    Trainee saveTrainee(int id);

    List<Trainee> searchTrainee(String cardinal, String school, String major);

}

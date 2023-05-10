package com.webkit640.backend.service.logic;

import com.webkit640.backend.entity.Trainee;
import org.springframework.stereotype.Component;

@Component
public interface TraineeService {
    Trainee saveTrainee(int id);
}

package com.webkit640.backend.service.impl;

import com.webkit640.backend.entity.Applicant;
import com.webkit640.backend.entity.Trainee;
import com.webkit640.backend.repository.ApplicantRepository;
import com.webkit640.backend.repository.TraineeRepository;
import com.webkit640.backend.service.logic.ApplicationService;
import com.webkit640.backend.service.logic.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final ApplicantRepository applicantRepository;

    @Autowired
    public TraineeServiceImpl(TraineeRepository traineeRepository, ApplicantRepository applicantRepository) {
        this.traineeRepository = traineeRepository;
        this.applicantRepository = applicantRepository;
    }

    @Override
    public Trainee saveTrainee(int id) {
        Applicant applicant = applicantRepository.findByMemberId(id);
        Trainee trainee = Trainee.builder()
                .cardinal(String.valueOf(LocalDate.now().getYear()))
                .applicant(applicant)
                .build();
        Trainee save = traineeRepository.save(trainee);
        applicant.setTrainee(trainee);
        applicantRepository.save(applicant);

        return save;
    }
}

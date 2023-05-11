package com.webkit640.backend.service.logic;

import com.webkit640.backend.entity.Applicant;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Component
public interface ApplicationService {
    HashMap<String,Object> saveEntity(int id, Applicant applicant);

    void adminSelection(List<String> emails);

    void selectionConfirmation(int id);
    List<Applicant> getApplicantList(String year, String school, String major);
    List<Applicant> getApplicantList(String email);
}

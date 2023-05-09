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
}

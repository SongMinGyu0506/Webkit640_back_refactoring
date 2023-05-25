package com.webkit640.backend.service.logic;

import com.webkit640.backend.entity.MainPageEntity;
import org.springframework.stereotype.Component;

@Component
public interface MainPageService {
    MainPageEntity read();
    MainPageEntity create(MainPageEntity mainPageEntity);
    void adminModification(int memberId);
}

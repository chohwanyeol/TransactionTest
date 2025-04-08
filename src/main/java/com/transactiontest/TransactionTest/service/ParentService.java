package com.transactiontest.TransactionTest.service;

import com.transactiontest.TransactionTest.domain.LogEntity;
import com.transactiontest.TransactionTest.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParentService {

    private final LogRepository logRepository;
    private final ChildService childService;

    @Transactional
    public void writeParentLog() {
        logRepository.save(LogEntity.builder().message("Parent Log").build());
        try {
            childService.writeChildLog();
        } catch (Exception e) {
            System.out.println("자식에서 예외 발생했지만 부모는 무시하고 진행함");
        }
    }
}

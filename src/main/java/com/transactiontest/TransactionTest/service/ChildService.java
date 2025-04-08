package com.transactiontest.TransactionTest.service;

import com.transactiontest.TransactionTest.domain.LogEntity;
import com.transactiontest.TransactionTest.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChildService {

    private final LogRepository logRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void writeChildLog() throws Exception {
        logRepository.save(
                LogEntity.builder().message("Child log with checked exception").build()
        );
        throw new Exception("Checked 예외지만 롤백하게 만들었음");
    }

}

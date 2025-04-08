package com.transactiontest.TransactionTest.controller;

import com.transactiontest.TransactionTest.service.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TxTestController {

    private final ParentService parentService;

    @GetMapping("/test")
    public String test() {
        try {
            parentService.writeParentLog();
        } catch (Exception e) {
            return "예외 발생: " + e.getMessage();
        }
        return "정상 처리됨";
    }
}

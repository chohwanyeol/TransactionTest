package com.transactiontest.TransactionTest.repository;
import com.transactiontest.TransactionTest.domain.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
}
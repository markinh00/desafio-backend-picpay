package com.picpaydesafio.picpaydesafio.repositories;

import com.picpaydesafio.picpaydesafio.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

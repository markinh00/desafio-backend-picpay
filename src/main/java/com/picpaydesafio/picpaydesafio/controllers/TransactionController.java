package com.picpaydesafio.picpaydesafio.controllers;

import com.picpaydesafio.picpaydesafio.domain.transaction.Transaction;
import com.picpaydesafio.picpaydesafio.dtos.transaction.TransactionDTO;
import com.picpaydesafio.picpaydesafio.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Transactions", description = "Endpoints for handling financial transactions between users")
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService service;

    @Operation(
            summary = "Create a transaction",
            description = "Performs a financial transaction between users. Returns the created transaction details."
    )
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transaction) throws Exception {
        Transaction newTransaction = this.service.createTransaction(transaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.OK);
    }
}

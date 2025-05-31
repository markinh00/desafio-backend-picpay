package com.picpaydesafio.picpaydesafio.services;

import com.picpaydesafio.picpaydesafio.domain.transaction.Transaction;
import com.picpaydesafio.picpaydesafio.domain.user.User;
import com.picpaydesafio.picpaydesafio.dtos.transaction.TransactionDTO;
import com.picpaydesafio.picpaydesafio.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TransactionAuthService transactionAuthService;

    private void saveTransaction(Transaction transaction){
        this.repository.save(transaction);
    }


    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User sender = this.userService.findUserById(transaction.senderId());
        User receiver = this.userService.findUserById(transaction.receiverId());

        this.userService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = this.transactionAuthService.authorizeTransaction();
        if (!isAuthorized) {
            throw new Exception("Transaction not Authorized");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.saveTransaction(newTransaction);
        this.userService.saveUser(receiver);
        this.userService.saveUser(sender);

        this.notificationService.sendNotification(sender, "Transaction sent successfully");
        this.notificationService.sendNotification(receiver, "Transaction received");

        return newTransaction;
    }

    public List<Transaction> findAllTransactions(){
        return this.repository.findAll();
    }
}

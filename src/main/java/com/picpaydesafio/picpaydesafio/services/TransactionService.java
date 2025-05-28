package com.picpaydesafio.picpaydesafio.services;

import com.picpaydesafio.picpaydesafio.domain.transaction.Transaction;
import com.picpaydesafio.picpaydesafio.domain.user.User;
import com.picpaydesafio.picpaydesafio.dtos.authorization.AuthorizationResponseDTO;
import com.picpaydesafio.picpaydesafio.dtos.authorization.AuthorizationStatus;
import com.picpaydesafio.picpaydesafio.dtos.transaction.TransactionDTO;
import com.picpaydesafio.picpaydesafio.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RestTemplate restTemplate;

    public boolean authorizeTransaction() {
        String url = "https://util.devi.tools/api/v2/authorize";
        AuthorizationResponseDTO authResponse = restTemplate.getForObject(url, AuthorizationResponseDTO.class);

        if (authResponse == null) {
            return false;
        }

        if (authResponse.status() != AuthorizationStatus.success) {
            return false;
        }

        return authResponse.data().authorization();
    }

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User sender = userService.findUserById(transaction.senderId());
        User receiver = userService.findUserById(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = this.authorizeTransaction();
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

        this.repository.save(newTransaction);
        this.userService.saveUser(receiver);
        this.userService.saveUser(sender);

        this.notificationService.sendNotification(sender, "Transaction sent successfully");
        this.notificationService.sendNotification(receiver, "Transaction received");

        return newTransaction;
    }
}

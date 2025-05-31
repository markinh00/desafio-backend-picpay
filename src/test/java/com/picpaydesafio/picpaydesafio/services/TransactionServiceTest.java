package com.picpaydesafio.picpaydesafio.services;

import com.picpaydesafio.picpaydesafio.domain.user.User;
import com.picpaydesafio.picpaydesafio.domain.user.UserType;
import com.picpaydesafio.picpaydesafio.dtos.transaction.TransactionDTO;
import com.picpaydesafio.picpaydesafio.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;


class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private TransactionAuthService transactionAuthService;

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    private final User commonUser = new User();
    private final User merchantUser = new User();

    @BeforeEach
    void setUp() {
        this.commonUser.setId(1L);
        this.commonUser.setName("Maria");
        this.commonUser.setEmail("maria@gmail.com");
        this.commonUser.setPassword("123456");
        this.commonUser.setDocument("12345678910");
        this.commonUser.setBalance(new BigDecimal(100));
        this.commonUser.setUserType(UserType.COMMON);

        this.merchantUser.setId(2L);
        this.merchantUser.setName("John");
        this.merchantUser.setEmail("john@gmail.com");
        this.merchantUser.setPassword("123456");
        this.merchantUser.setDocument("12345678911");
        this.merchantUser.setBalance(new BigDecimal(100));
        this.merchantUser.setUserType(UserType.MERCHANT);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create a transaction successfully")
    void createTransactionCase1() throws Exception {
        when(userService.findUserById(1L)).thenReturn(commonUser);
        when(userService.findUserById(2L)).thenReturn(merchantUser);

        when(transactionAuthService.authorizeTransaction()).thenReturn(true);

        TransactionDTO request = new TransactionDTO(new BigDecimal(50), commonUser.getId(), merchantUser.getId());
        transactionService.createTransaction(request);

        verify(repository, times(1)).save(any());

        commonUser.setBalance(new BigDecimal(50));
        verify(userService, times(1)).saveUser(commonUser);

        merchantUser.setBalance(new BigDecimal(150));
        verify(userService, times(1)).saveUser(merchantUser);

        verify(notificationService, times(1)).sendNotification(commonUser, "Transaction sent successfully");
        verify(notificationService, times(1)).sendNotification(merchantUser, "Transaction received");
    }

    @Test
    @DisplayName("Should throw an exception when transaction is not allowed")
    void createTransactionCase2() throws Exception {
        when(userService.findUserById(1L)).thenReturn(commonUser);
        when(userService.findUserById(2L)).thenReturn(merchantUser);

        when(transactionAuthService.authorizeTransaction()).thenReturn(false);

        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            TransactionDTO request = new TransactionDTO(new BigDecimal(50), commonUser.getId(), merchantUser.getId());
            transactionService.createTransaction(request);
        });

        String expectedErrorMessage = "Transaction not Authorized";
        Assertions.assertEquals(expectedErrorMessage, thrown.getMessage());
    }

    @Test
    @DisplayName("Should throw an exception when a MERCHANT user tries to make a transaction")
    void createTransactionCase3() throws Exception {
        when(userService.findUserById(merchantUser.getId())).thenReturn(merchantUser);
        when(userService.findUserById(commonUser.getId())).thenReturn(commonUser);

        Exception exception = new Exception("User of type MERCHANT is not allowed to make transactions");
        doThrow(exception).when(userService).validateTransaction(merchantUser, new BigDecimal(50));

        TransactionDTO request = new TransactionDTO(new BigDecimal(50), merchantUser.getId(), commonUser.getId());

        Exception thrown = Assertions.assertThrows(Exception.class, () -> transactionService.createTransaction(request));

        String expectedMessage = "User of type MERCHANT is not allowed to make transactions";
        Assertions.assertEquals(expectedMessage, thrown.getMessage());

        verify(transactionAuthService, never()).authorizeTransaction();
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw an exception when a user tries to make a transaction with insufficient balance")
    void createTransactionCase4() throws Exception {
        when(userService.findUserById(merchantUser.getId())).thenReturn(merchantUser);
        when(userService.findUserById(commonUser.getId())).thenReturn(commonUser);

        Exception exception = new Exception("User does not have enough balance");
        doThrow(exception).when(userService).validateTransaction(commonUser, new BigDecimal(1000));

        TransactionDTO request = new TransactionDTO(new BigDecimal(1000), commonUser.getId(), merchantUser.getId());

        Exception thrown = Assertions.assertThrows(Exception.class, () -> transactionService.createTransaction(request));

        String expectedMessage = "User does not have enough balance";
        Assertions.assertEquals(expectedMessage, thrown.getMessage());

        verify(transactionAuthService, never()).authorizeTransaction();
        verify(repository, never()).save(any());
    }
}
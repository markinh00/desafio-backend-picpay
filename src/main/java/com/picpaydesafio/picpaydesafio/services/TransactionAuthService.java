package com.picpaydesafio.picpaydesafio.services;

import com.picpaydesafio.picpaydesafio.dtos.transactionAuth.TransactionAuthResponseDTO;
import com.picpaydesafio.picpaydesafio.dtos.transactionAuth.TransactionAuthStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class TransactionAuthService {
    @Autowired
    private RestTemplate restTemplate;

    public boolean authorizeTransaction() {
        String url = "https://util.devi.tools/api/v2/authorize";
        TransactionAuthResponseDTO authResponse = restTemplate.getForObject(url, TransactionAuthResponseDTO.class);

        if (authResponse == null) {
            return false;
        }

        if (authResponse.status() != TransactionAuthStatus.success) {
            return false;
        }

        return authResponse.data().authorization();
    }
}

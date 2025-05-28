package com.picpaydesafio.picpaydesafio.dtos.user;

import com.picpaydesafio.picpaydesafio.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO(
        String name,
        String email,
        String password,
        String document,
        BigDecimal balance,
        UserType userType
) {
}

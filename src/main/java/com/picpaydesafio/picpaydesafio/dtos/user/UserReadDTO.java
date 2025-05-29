package com.picpaydesafio.picpaydesafio.dtos.user;

import com.picpaydesafio.picpaydesafio.domain.user.User;
import java.math.BigDecimal;

public record UserReadDTO(
        Long id,
        String name,
        String email,
        String document,
        BigDecimal balance,
        String userType
) {
    public static UserReadDTO fromEntity(User user) {
        return new UserReadDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getDocument(),
                user.getBalance(),
                user.getUserType().name()
        );
    }

}

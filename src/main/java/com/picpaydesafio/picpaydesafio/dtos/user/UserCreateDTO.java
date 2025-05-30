package com.picpaydesafio.picpaydesafio.dtos.user;

import com.picpaydesafio.picpaydesafio.domain.user.UserType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record UserCreateDTO(
        @NotBlank(message = "'name' is a required field")
        String name,

        @NotBlank(message = "'email' is a required field")
        @Email(message = "field 'email' must be a valid email")
        String email,

        @NotBlank(message = "'password' is a required field")
        String password,

        @NotNull(message = "'document' is a required field")
        @Pattern(regexp = "\\d{11}")
        String document,

        @NotNull(message = "'balance' is a required field")
        @DecimalMin(value = "0.0", message = "'balance must not be less than 0'")
        BigDecimal balance,

        @NotNull(message = "'userType' is a required field")
        UserType userType
) {
}

package com.picpaydesafio.picpaydesafio.dtos.user;

import com.picpaydesafio.picpaydesafio.domain.user.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UserUpdateDTO (
  String name,

  @Email(message = "field 'email' must be a valid email")
  String email,

  @Pattern(regexp = "\\d{11}")
  String document,

  UserType userType
){}

package com.picpaydesafio.picpaydesafio.dtos.user;

import com.picpaydesafio.picpaydesafio.domain.user.UserType;
import com.picpaydesafio.picpaydesafio.validation.ValidEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UserUpdateDTO (
  String name,

  @Pattern(regexp = "\\d{11}")
  String document,

  @Email(message = "field 'email' must be a valid email")
  String email,

  @ValidEnum(enumClass = UserType.class, message = "'userType' must be COMMON or MERCHANT")
  String userType
){}

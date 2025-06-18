package com.picpay_simplified.dtos;

import java.math.BigDecimal;

import com.picpay_simplified.domain.user.UserType;

public record UserDTO(String firstName, String lastName, String document, String email, BigDecimal balance, String password, UserType userType) {
}

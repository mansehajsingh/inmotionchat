package com.inmotionchat.core.data.dto;

public record UserDTO(
        String email,
        String username,
        String password,
        String firstName,
        String lastName
) {}

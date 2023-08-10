package com.inmotionchat.core.data.dto;

import com.inmotionchat.core.data.postgres.identity.User;

public record UserProfileDTO(Long id, String email, String displayName) {

    public UserProfileDTO(User user) {
        this(user.getId(), user.getEmail(), user.getDisplayName());
    }

}

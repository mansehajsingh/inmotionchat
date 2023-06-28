package com.inmotionchat.core.data.dto;

import com.inmotionchat.core.models.RoleType;

import java.util.Set;

public record RoleDTO(String name, Long tenantId, RoleType roleType, Set<String> permissions) {
}

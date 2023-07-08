package com.inmotionchat.core.data.dto;

import java.util.Set;

public record TenantDTO(String name, UserDTO rootUser, Set<String> resolutionDomains) {}

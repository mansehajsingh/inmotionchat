package com.inmotionchat.core.data.dto;

import java.util.List;

public record ChatboxDTO(String name, Long journeyId, List<String> whitelistedDomains) {}

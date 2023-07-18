package com.inmotionchat.core.data.dto;

import java.util.Map;

public record EdgeDTO(int sourceIndex, int destinationIndex, Map<String, Object> data) {}

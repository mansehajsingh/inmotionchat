package com.inmotionchat.core.data.dto;

import com.inmotionchat.core.models.NodeType;

import java.util.Map;

public record NodeDTO(NodeType type, Map<String, Object> data) {}

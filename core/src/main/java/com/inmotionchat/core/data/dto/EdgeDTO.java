package com.inmotionchat.core.data.dto;

import com.inmotionchat.core.data.postgres.journey.Edge;

import java.util.Map;

public record EdgeDTO(Long id, Long sourceId, Long destinationId, Map<String, Object> template) {

    public EdgeDTO(Edge edge) {
        this(edge.getId(), edge.getSource().getId(), edge.getDestination().getId(), edge.getTemplateAsMap());
    }

}

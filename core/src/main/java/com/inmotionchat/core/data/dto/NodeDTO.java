package com.inmotionchat.core.data.dto;

import com.inmotionchat.core.data.postgres.journey.Node;
import com.inmotionchat.core.data.postgres.journey.NodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record NodeDTO(Long id, NodeType type, Map<String, Object> template, List<EdgeDTO> edges) {

    public NodeDTO(Node node) {
        this(node.getId(), node.getType(), node.getTemplateAsMap(), new ArrayList<>());
    }

}

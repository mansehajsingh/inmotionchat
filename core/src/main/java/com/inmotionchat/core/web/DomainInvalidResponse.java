package com.inmotionchat.core.web;

import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.util.validation.Violation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomainInvalidResponse {

    public final List<Map<String, Object>> fields = new ArrayList<>();

    public DomainInvalidResponse(DomainInvalidException e) {

        Map<String, List<String>> reasonsByField = new HashMap<>();
        Map<String, Boolean> protectionByField = new HashMap<>();
        Map<String, Object> valueByField = new HashMap<>();

        for (Violation v : e.getViolations()) {
            if (reasonsByField.containsKey(v.getField())) {
                reasonsByField.get(v.getField()).add(v.getReason());
            } else {
                List<String> reasons = new ArrayList<>();
                reasons.add(v.getReason());
                reasonsByField.put(v.getField(), reasons);
                protectionByField.put(v.getField(), v.isProtectedValue());
                valueByField.put(v.getField(), v.getValue());
            }
        }

        for (String field : reasonsByField.keySet()) {
            Map<String, Object> fieldObjects = new HashMap<>();
            fieldObjects.put("field", field);
            fieldObjects.put("violations", reasonsByField.get(field));

            if (!protectionByField.get(field)) {
                fieldObjects.put("value", valueByField.get(field));
                fieldObjects.put("protected", false);
            } else {
                fieldObjects.put("protected", true);
            }

            fields.add(fieldObjects);
        }

    }

}

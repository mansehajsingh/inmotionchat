package com.inmotionchat.notifications.model;

public record Email(String recipient, String subject, String templateName, String htmlContent) {}

package com.inmotionchat.core.data.dto;

public record AsyncMessageDTO(Long inboxGroupId, String title, String replyTo, String content) {
}

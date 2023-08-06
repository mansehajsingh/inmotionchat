package com.inmotionchat.journeys.chatbox;

import com.inmotionchat.core.data.ArchivingDomainService;
import com.inmotionchat.core.data.dto.ChatboxDTO;
import com.inmotionchat.core.data.postgres.journey.Chatbox;

public interface ChatboxService extends ArchivingDomainService<Chatbox, ChatboxDTO> {
}

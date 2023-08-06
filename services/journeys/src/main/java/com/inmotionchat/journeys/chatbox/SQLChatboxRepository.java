package com.inmotionchat.journeys.chatbox;

import com.inmotionchat.core.data.SQLArchivingRepository;
import com.inmotionchat.core.data.postgres.journey.Chatbox;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLChatboxRepository extends SQLArchivingRepository<Chatbox> {
}

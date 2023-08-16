package com.inmotionchat.journeys.chatbox;

import com.inmotionchat.core.data.postgres.journey.Chatbox;
import com.inmotionchat.smartpersist.SmartJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLChatboxRepository extends SmartJPARepository<Chatbox, Long> {
}

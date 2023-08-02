package com.inmotionchat.journeys.graph;

import com.inmotionchat.core.data.postgres.journey.InboxGroupEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLInboxGroupEndpointRepository extends JpaRepository<InboxGroupEndpoint, Long> {
}

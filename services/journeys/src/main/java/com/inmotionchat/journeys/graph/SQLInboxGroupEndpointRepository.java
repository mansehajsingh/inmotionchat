package com.inmotionchat.journeys.graph;

import com.inmotionchat.core.data.postgres.journey.InboxGroupEndpoint;
import com.inmotionchat.smartpersist.SmartJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLInboxGroupEndpointRepository extends SmartJPARepository<InboxGroupEndpoint, Long> {
}

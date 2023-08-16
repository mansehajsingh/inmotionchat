package com.inmotionchat.journeys.journey;

import com.inmotionchat.core.data.postgres.journey.Journey;
import com.inmotionchat.smartpersist.SmartJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLJourneyRepository extends SmartJPARepository<Journey, Long> {
}

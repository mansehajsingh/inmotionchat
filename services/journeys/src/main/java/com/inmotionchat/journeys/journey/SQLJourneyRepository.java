package com.inmotionchat.journeys.journey;

import com.inmotionchat.core.data.SQLArchivingRepository;
import com.inmotionchat.core.data.postgres.journey.Journey;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLJourneyRepository extends SQLArchivingRepository<Journey> {
}

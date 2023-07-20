package com.inmotionchat.journeys.journey;

import com.inmotionchat.core.data.ArchivingDomainService;
import com.inmotionchat.core.data.dto.JourneyDTO;
import com.inmotionchat.core.data.postgres.journey.Journey;

public interface JourneyService extends ArchivingDomainService<Journey, JourneyDTO> {
}

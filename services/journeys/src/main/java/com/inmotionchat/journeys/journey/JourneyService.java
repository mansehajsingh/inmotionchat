package com.inmotionchat.journeys.journey;

import com.inmotionchat.core.data.ArchivingDomainService;
import com.inmotionchat.core.data.dto.GraphDTO;
import com.inmotionchat.core.data.dto.JourneyDTO;
import com.inmotionchat.core.data.postgres.journey.Journey;
import com.inmotionchat.core.data.postgres.journey.Node;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;

import java.util.List;

public interface JourneyService extends ArchivingDomainService<Journey, JourneyDTO> {

    List<Node> retrieveNodes(Long tenantId, Long journeyId) throws NotFoundException;

    List<Node> updateGraph(Long tenantId, Long journeyId, GraphDTO graphDTO) throws NotFoundException, ConflictException, DomainInvalidException;

}

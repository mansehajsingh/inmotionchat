package com.inmotionchat.journeys.journey;

import com.inmotionchat.core.data.AbstractArchivingDomainService;
import com.inmotionchat.core.data.dto.JourneyDTO;
import com.inmotionchat.core.data.postgres.journey.Journey;
import com.inmotionchat.core.util.query.SearchCriteriaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JourneyServiceImpl extends AbstractArchivingDomainService<Journey, JourneyDTO> implements JourneyService {

    protected final static Logger log = LoggerFactory.getLogger(JourneyServiceImpl.class);

    protected final static SearchCriteriaMapper mapper = new SearchCriteriaMapper()
            .key("name", String.class);

    @Autowired
    protected JourneyServiceImpl(SQLJourneyRepository repository) {
        super(Journey.class, JourneyDTO.class, log, repository, mapper);
    }

}

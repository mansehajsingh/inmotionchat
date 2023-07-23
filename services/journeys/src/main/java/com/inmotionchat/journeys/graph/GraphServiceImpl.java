package com.inmotionchat.journeys.graph;

import com.inmotionchat.core.data.postgres.journey.Journey;
import com.inmotionchat.core.data.postgres.journey.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphServiceImpl implements GraphService {

    protected final SQLNodeRepository sqlNodeRepository;

    @Autowired
    public GraphServiceImpl(SQLNodeRepository sqlNodeRepository) {
        this.sqlNodeRepository = sqlNodeRepository;
    }

    public List<Node> retrieveByJourney(Journey journey) {
        return this.sqlNodeRepository.findAllByJourney(journey);
    }

}

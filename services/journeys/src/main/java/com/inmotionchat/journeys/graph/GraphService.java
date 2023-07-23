package com.inmotionchat.journeys.graph;

import com.inmotionchat.core.data.postgres.journey.Journey;
import com.inmotionchat.core.data.postgres.journey.Node;

import java.util.List;

public interface GraphService {

    List<Node> retrieveByJourney(Journey journey);

}

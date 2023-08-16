package com.inmotionchat.journeys.graph;

import com.inmotionchat.core.data.postgres.journey.Journey;
import com.inmotionchat.core.data.postgres.journey.Node;
import com.inmotionchat.smartpersist.SmartJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SQLNodeRepository extends SmartJPARepository<Node, Long> {

    List<Node> findAllByJourney(Journey journey);

}

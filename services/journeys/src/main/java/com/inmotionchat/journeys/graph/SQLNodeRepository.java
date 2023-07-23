package com.inmotionchat.journeys.graph;

import com.inmotionchat.core.data.postgres.journey.Journey;
import com.inmotionchat.core.data.postgres.journey.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SQLNodeRepository extends JpaRepository<Node, Long> {

    List<Node> findAllByJourney(Journey journey);

}

package com.inmotionchat.journeys.journey;

import com.inmotionchat.core.data.AbstractArchivingDomainService;
import com.inmotionchat.core.data.ThrowingTransactionTemplate;
import com.inmotionchat.core.data.TransactionTemplateFactory;
import com.inmotionchat.core.data.dto.EdgeDTO;
import com.inmotionchat.core.data.dto.GraphDTO;
import com.inmotionchat.core.data.dto.JourneyDTO;
import com.inmotionchat.core.data.dto.NodeDTO;
import com.inmotionchat.core.data.postgres.journey.*;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.util.query.SearchCriteriaMapper;
import com.inmotionchat.journeys.graph.SQLInboxGroupEndpointRepository;
import com.inmotionchat.journeys.graph.SQLNodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Service
public class JourneyServiceImpl extends AbstractArchivingDomainService<Journey, JourneyDTO> implements JourneyService {

    protected final static Logger log = LoggerFactory.getLogger(JourneyServiceImpl.class);

    protected final static SearchCriteriaMapper mapper = new SearchCriteriaMapper()
            .key("name", String.class);

    protected final SQLNodeRepository sqlNodeRepository;

    protected final ThrowingTransactionTemplate transactionTemplate;

    protected final SQLInboxGroupEndpointRepository sqlInboxGroupEndpointRepository;

    @Autowired
    protected JourneyServiceImpl(SQLJourneyRepository repository,
                                 SQLNodeRepository sqlNodeRepository,
                                 PlatformTransactionManager transactionManager,
                                 SQLInboxGroupEndpointRepository sqlInboxGroupEndpointRepository) {
        super(Journey.class, JourneyDTO.class, log, repository, mapper);
        this.sqlNodeRepository = sqlNodeRepository;
        this.transactionTemplate = TransactionTemplateFactory.getThrowingTransactionTemplate(transactionManager);
        this.sqlInboxGroupEndpointRepository = sqlInboxGroupEndpointRepository;
    }

    @Override
    public List<Node> retrieveNodes(Long tenantId, Long journeyId) throws NotFoundException {
        Journey journey = retrieveById(tenantId, journeyId);
        return this.sqlNodeRepository.findAllByJourney(journey);
    }

    @Override
    public List<Node> updateGraph(Long tenantId, Long journeyId, GraphDTO graphDTO) throws NotFoundException, ConflictException, DomainInvalidException {
        // Preparing journey object
        Journey journey = retrieveById(tenantId, journeyId);

        List<Node> nodes = new ArrayList<>();
        for (NodeDTO nodeDTO : graphDTO.nodes()) {
            Node node = new Node(journey, nodeDTO.type(), nodeDTO.template(), new ArrayList<>());
            nodes.add(node);
        }

        int nodeIdx = 0;

        // second pass to create and fill edges
        for (NodeDTO nodeDTO : graphDTO.nodes()) {
            List<Edge> edges = new ArrayList<>();

            for (EdgeDTO edgeDTO : nodeDTO.edges()) {
                Edge edge = new Edge(
                        journey,
                        nodes.get(edgeDTO.sourceId().intValue()),
                        nodes.get(edgeDTO.destinationId().intValue()),
                        edgeDTO.template()
                );

                edges.add(edge);
            }

            nodes.get(nodeIdx).setEdges(edges);
            nodeIdx++;
        }

        journey.setNodes(nodes);

        journey.validate();
        journey.validateForUpdate();

        this.transactionTemplate.execute(status -> {
            List<Node> oldNodes = this.sqlNodeRepository.findAllByJourney(journey);
            this.sqlNodeRepository.deleteAll(oldNodes);

            Journey createdJourney = this.repository.update(journey);

            List<InboxGroupEndpoint> inboxGroupEndpoints = new ArrayList<>();
            for (Node node : createdJourney.getNodes()) {
                if (node.getType() == NodeType.INBOX_GROUP) inboxGroupEndpoints.add(new InboxGroupEndpoint(node));
            }
            this.sqlInboxGroupEndpointRepository.saveAllAndFlush(inboxGroupEndpoints);

            return createdJourney;
        });

        return nodes;
    }

}

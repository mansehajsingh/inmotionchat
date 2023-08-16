package com.inmotionchat.journeys.journey;

import com.inmotionchat.core.audit.AuditAction;
import com.inmotionchat.core.audit.AuditLog;
import com.inmotionchat.core.audit.AuditManager;
import com.inmotionchat.core.data.AbstractArchivingDomainService;
import com.inmotionchat.core.data.ThrowingTransactionTemplate;
import com.inmotionchat.core.data.TransactionTemplateFactory;
import com.inmotionchat.core.data.dto.EdgeDTO;
import com.inmotionchat.core.data.dto.GraphDTO;
import com.inmotionchat.core.data.dto.JourneyDTO;
import com.inmotionchat.core.data.dto.NodeDTO;
import com.inmotionchat.core.data.postgres.journey.*;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.journeys.graph.SQLInboxGroupEndpointRepository;
import com.inmotionchat.journeys.graph.SQLNodeRepository;
import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;
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

    protected final SQLNodeRepository sqlNodeRepository;

    protected final ThrowingTransactionTemplate transactionTemplate;

    protected final SQLInboxGroupEndpointRepository sqlInboxGroupEndpointRepository;

    @Autowired
    protected JourneyServiceImpl(SQLJourneyRepository repository,
                                 SQLNodeRepository sqlNodeRepository,
                                 PlatformTransactionManager transactionManager,
                                 IdentityContext identityContext,
                                 SQLInboxGroupEndpointRepository sqlInboxGroupEndpointRepository,
                                 AuditManager auditManager,
                                 JourneyAuditActionProvider journeyAuditActionProvider) {
        super(Journey.class, JourneyDTO.class, log, transactionManager, identityContext, repository, auditManager, journeyAuditActionProvider);
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
    public List<Node> updateGraph(Long tenantId, Long journeyId, GraphDTO graphDTO) throws NotFoundException, ConflictException, DomainInvalidException, UnauthorizedException {
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
            Journey updatedJourney = this.repository.update(journey);

            List<InboxGroupEndpoint> inboxGroupEndpoints = new ArrayList<>();
            for (Node node : updatedJourney.getNodes()) {
                if (node.getType() == NodeType.INBOX_GROUP) inboxGroupEndpoints.add(new InboxGroupEndpoint(node));
            }
            this.sqlInboxGroupEndpointRepository.saveAllAndFlush(inboxGroupEndpoints);

            this.auditManager.save(new AuditLog(
                    AuditAction.UPDATE_JOURNEY_GRAPH,
                    tenantId,
                    identityContext.getRequester().userId(),
                    journey,
                    graphDTO
            ));

            return updatedJourney;
        });

        return nodes;
    }

}

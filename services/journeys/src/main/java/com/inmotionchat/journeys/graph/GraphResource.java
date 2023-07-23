package com.inmotionchat.journeys.graph;

import com.inmotionchat.core.data.dto.GraphDTO;
import com.inmotionchat.core.data.dto.NodeDTO;
import com.inmotionchat.core.data.postgres.journey.Journey;
import com.inmotionchat.core.data.postgres.journey.Node;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.PermissionException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.core.models.Permission;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.journeys.journey.JourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.inmotionchat.core.models.Permission.READ_JOURNEYS;
import static com.inmotionchat.core.web.AbstractResource.PATH;

@RestController
@RequestMapping(PATH + "/journeys/{journeyId}/graph")
public class GraphResource {

    private final IdentityContext identityContext;

    private final JourneyService journeyService;

    private final GraphService graphService;

    protected final Permission[] READ_PERMISSIONS = { READ_JOURNEYS };

    @Autowired
    public GraphResource(IdentityContext identityContext, JourneyService journeyService, GraphService graphService) {
        this.identityContext = identityContext;
        this.journeyService = journeyService;
        this.graphService = graphService;
    }

    @GetMapping
    public GraphDTO get(@PathVariable Long tenantId, @PathVariable Long journeyId) throws UnauthorizedException, NotFoundException, PermissionException {
        if (!isCorrectTenant(tenantId))
            throw new UnauthorizedException("Not authorized to fetch a graph for this tenant.");

        throwIfMissingPermissions(READ_PERMISSIONS);

        Journey journey = this.journeyService.retrieveById(tenantId, journeyId);
        List<Node> nodes = this.graphService.retrieveByJourney(journey);

        return new GraphDTO(nodes.stream().map(NodeDTO::new).toList());
    }

    protected boolean isCorrectTenant(Long tenantId) {
        return this.identityContext.getRequester().getTenantId().equals(tenantId);
    }

    protected void throwIfMissingPermissions(Permission[] permissions) throws PermissionException {
        List<String> missingPermissions = new ArrayList<>();

        for (Permission p : permissions) {
            if (!this.identityContext.getRequester().hasPermission(p)) {
                missingPermissions.add(p.value());
            }
        }

        if (!missingPermissions.isEmpty()) {
            throw new PermissionException(missingPermissions);
        }
    }

}

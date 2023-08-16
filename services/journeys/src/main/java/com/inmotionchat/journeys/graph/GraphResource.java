package com.inmotionchat.journeys.graph;

import com.inmotionchat.core.data.dto.GraphDTO;
import com.inmotionchat.core.data.dto.NodeDTO;
import com.inmotionchat.core.data.postgres.journey.Node;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.PermissionException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.core.models.Permission;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.core.web.WebUtils;
import com.inmotionchat.journeys.journey.JourneyService;
import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.inmotionchat.core.models.Permission.EDIT_JOURNEYS;
import static com.inmotionchat.core.models.Permission.READ_JOURNEYS;
import static com.inmotionchat.core.web.AbstractResource.PATH;

@RestController
@RequestMapping(PATH + "/journeys/{journeyId}/graph")
public class GraphResource {

    private final IdentityContext identityContext;

    private final JourneyService journeyService;

    protected final Permission[] READ_PERMISSIONS = { READ_JOURNEYS };

    protected final Permission[] EDIT_PERMISSIONS = { EDIT_JOURNEYS };

    @Autowired
    public GraphResource(IdentityContext identityContext, JourneyService journeyService) {
        this.identityContext = identityContext;
        this.journeyService = journeyService;
    }

    @GetMapping
    public GraphDTO get(@PathVariable Long tenantId, @PathVariable Long journeyId) throws UnauthorizedException, NotFoundException, PermissionException {
        if (!WebUtils.isCorrectTenant(identityContext, tenantId))
            throw new UnauthorizedException("Not authorized to fetch a graph for this tenant.");

        WebUtils.throwIfMissingPermissions(identityContext, READ_PERMISSIONS);

        List<Node> nodes = this.journeyService.retrieveNodes(tenantId, journeyId);

        return new GraphDTO(nodes.stream().map(NodeDTO::new).toList());
    }

    @PutMapping
    public ResponseEntity<?> update(@PathVariable Long tenantId, @PathVariable Long journeyId, @RequestBody GraphDTO graphDTO) throws PermissionException, UnauthorizedException, DomainInvalidException, NotFoundException, ConflictException {
        if (!WebUtils.isCorrectTenant(identityContext, tenantId))
            throw new UnauthorizedException("Not authorized to update a graph for this tenant.");

        WebUtils.throwIfMissingPermissions(identityContext, EDIT_PERMISSIONS);

        this.journeyService.updateGraph(tenantId, journeyId, graphDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

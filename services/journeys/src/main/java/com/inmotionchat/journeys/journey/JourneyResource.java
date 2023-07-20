package com.inmotionchat.journeys.journey;

import com.inmotionchat.core.data.dto.JourneyDTO;
import com.inmotionchat.core.data.postgres.journey.Journey;
import com.inmotionchat.core.models.Permission;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.core.web.AbstractResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.inmotionchat.core.models.Permission.EDIT_JOURNEYS;
import static com.inmotionchat.core.models.Permission.READ_JOURNEYS;
import static com.inmotionchat.core.web.AbstractResource.PATH;

@RestController
@RequestMapping(PATH + "/journeys")
public class JourneyResource extends AbstractResource<Journey, JourneyDTO> {

    protected static final Permission[] READ_PERMISSIONS = { READ_JOURNEYS };
    protected static final Permission[] CREATE_PERMISSIONS = { EDIT_JOURNEYS };

    @Autowired
    public JourneyResource(IdentityContext identityContext, JourneyService journeyService) {
        super(identityContext, journeyService);
    }

    @Override
    protected int getDefaultPageSize() {
        return 100;
    }

    @Override
    protected int getMaximumPageSize() {
        return 300;
    }

    @Override
    protected boolean isCreateEnabled() {
        return true;
    }

    @Override
    public Permission[] getCreatePermissions() {
        return CREATE_PERMISSIONS;
    }

    @Override
    protected boolean isGetEnabled() {
        return true;
    }

    @Override
    public Permission[] getGetPermissions() {
        return READ_PERMISSIONS;
    }

    @Override
    public boolean isSearchEnabled() {
        return true;
    }

    @Override
    protected Permission[] getSearchPermissions() {
        return READ_PERMISSIONS;
    }

}

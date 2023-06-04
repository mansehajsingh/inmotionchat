package com.inmotionchat.organizations.web;

import com.inmotionchat.core.data.dto.OrganizationDTO;
import com.inmotionchat.core.domains.Organization;
import com.inmotionchat.core.web.AbstractResource;
import com.inmotionchat.organizations.service.contract.OrganizationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.inmotionchat.core.web.AbstractResource.PATH;

@RestController
@RequestMapping(PATH + "/organizations")
public class OrganizationResource extends AbstractResource<Organization, OrganizationDTO> {

    protected OrganizationResource(OrganizationService organizationService) {
        super(organizationService);
    }

    @Override
    protected boolean isCreateEnabled() {
        return true;
    }

}

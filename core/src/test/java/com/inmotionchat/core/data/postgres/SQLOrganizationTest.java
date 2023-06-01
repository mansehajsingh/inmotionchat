package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.UnitTest;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SQLOrganizationTest extends UnitTest {

    protected SQLOrganization validOrganization = new SQLOrganization(
            "OrgName", "A really cool description.");

    @Test
    public void test_validSQLOrganization() throws DomainInvalidException {
        validOrganization.validate();
    }

    @Test
    public void test_invalidName() throws DomainInvalidException {
        SQLOrganization organization = validOrganization.copy();

        organization.setName(null);
        assertThrows(DomainInvalidException.class, organization::validate);

        organization.setName("");
        assertThrows(DomainInvalidException.class, organization::validate);
    }

    @Test
    public void test_invalidDescription() throws DomainInvalidException {
        SQLOrganization organization = validOrganization.copy();

        organization.setDescription(null);
        assertThrows(DomainInvalidException.class, organization::validate);

        organization.setDescription("");
        assertThrows(DomainInvalidException.class, organization::validate);
    }

}

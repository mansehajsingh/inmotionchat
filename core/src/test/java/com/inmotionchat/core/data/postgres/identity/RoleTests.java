package com.inmotionchat.core.data.postgres.identity;

import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.models.RoleType;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.inmotionchat.core.models.Permission.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoleTests {

    private static final String validName = "New Role";
    private static final Set<String> validPermissions = new HashSet<>() {{
        add(READ_ROLE.value());
        add(DELETE_ROLE.value());
        add(READ_JOURNEYS.value());
    }};

    @Test
    public void validateRole_Success() throws DomainInvalidException {
        RoleDTO roleDTO = new RoleDTO(validName, RoleType.CUSTOM, validPermissions);
        Role role = new Role(5L, roleDTO);
        role.validate();
    }

    @Test(expected = DomainInvalidException.class)
    public void validateRole_NullName() throws DomainInvalidException {
        RoleDTO roleDTO = new RoleDTO(null, RoleType.CUSTOM, validPermissions);
        Role role = new Role(5L, roleDTO);
        role.validate();
    }

    @Test(expected = DomainInvalidException.class)
    public void validateRole_EmptyNameName() throws DomainInvalidException {
        RoleDTO roleDTO = new RoleDTO("", RoleType.CUSTOM, validPermissions);
        Role role = new Role(5L, roleDTO);
        role.validate();
    }

    @Test(expected = DomainInvalidException.class)
    public void validateRole_NullPermissions() throws DomainInvalidException {
        RoleDTO roleDTO = new RoleDTO(validName, RoleType.CUSTOM, null);
        Role role = new Role(5L, roleDTO);
        role.validate();
    }

    @Test(expected = DomainInvalidException.class)
    public void validateRole_UnrecognizedPermission() throws DomainInvalidException {
        Set<String> unrecognizedPermissions = new HashSet<>() {{ add("no permission like this"); }};
        RoleDTO roleDTO = new RoleDTO(null, RoleType.CUSTOM, unrecognizedPermissions);
        Role role = new Role(5L, roleDTO);
        role.validate();
    }

    @Test
    public void roleType_ROOT() {
        RoleDTO roleDTO = new RoleDTO(validName, RoleType.ROOT, validPermissions);
        Role role = new Role(5L, roleDTO);

        assertTrue(role.root);
        assertFalse(role.restricted);
        assertEquals(RoleType.ROOT, role.getRoleType());
    }

    @Test
    public void roleType_RESTRICTED() {
        RoleDTO roleDTO = new RoleDTO(validName, RoleType.RESTRICTED, validPermissions);
        Role role = new Role(5L, roleDTO);

        assertFalse(role.root);
        assertTrue(role.restricted);
        assertEquals(RoleType.RESTRICTED, role.getRoleType());
    }
    @Test
    public void roleType_CUSTOM() {
        RoleDTO roleDTO = new RoleDTO(validName, RoleType.CUSTOM, validPermissions);
        Role role = new Role(5L, roleDTO);

        assertFalse(role.root);
        assertFalse(role.restricted);
        assertEquals(RoleType.CUSTOM, role.getRoleType());
    }



}

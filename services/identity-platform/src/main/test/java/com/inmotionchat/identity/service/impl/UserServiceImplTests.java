package com.inmotionchat.identity.service.impl;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.data.dto.VerifyDTO;
import com.inmotionchat.core.data.events.PersistUserEvent;
import com.inmotionchat.core.data.events.StreamEventPublisher;
import com.inmotionchat.core.data.events.UnverifiedUserEvent;
import com.inmotionchat.core.data.postgres.identity.Role;
import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.core.data.postgres.identity.User;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.identity.postgres.SQLUserRepository;
import com.inmotionchat.identity.service.contract.FirebaseWrapper;
import com.inmotionchat.identity.service.contract.RoleService;
import com.inmotionchat.identity.service.contract.UserService;
import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.transaction.PlatformTransactionManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceImplTests {

    protected UserService userService;

    protected PlatformTransactionManager mockTxManager;
    protected  SQLUserRepository mockUserRepository;
    protected RoleService mockRoleService;
    protected StreamEventPublisher mockStreamEventPublisher;
    protected FirebaseWrapper mockFbWrapper;

    @Before
    public void setUp() {
        mockTxManager = mock(PlatformTransactionManager.class);
        mockUserRepository = mock(SQLUserRepository.class);
        mockRoleService = mock(RoleService.class);
        mockStreamEventPublisher = mock(StreamEventPublisher.class);
        mockFbWrapper = mock(FirebaseWrapper.class);

        userService = new UserServiceImpl(
                mockTxManager,
                mockUserRepository,
                mockRoleService,
                mockStreamEventPublisher,
                mockFbWrapper
        );
    }

    @Test
    public void createEmailPasswordUser_Success() throws ConflictException, DomainInvalidException, NotFoundException, FirebaseAuthException, UnauthorizedException {
        UserDTO userDTO = new UserDTO(
                "email@email.com",
                "@MyAwesomePassword123",
                "Bob",
                5L
        );

        String expectedUid = "5a5a5a5a";
        UserRecord record = mock(UserRecord.class);
        when(record.getUid()).thenReturn(expectedUid);

        when(mockFbWrapper.create(any())).thenReturn(record);
        String uid = userService.createEmailPasswordUser(userDTO);

        assertEquals(expectedUid, uid);
    }

    @Test
    public void createEmailPasswordUser_EventPublished() throws FirebaseAuthException, ConflictException, DomainInvalidException, NotFoundException, UnauthorizedException {
        UserDTO userDTO = new UserDTO(
                "email@email.com",
                "@MyAwesomePassword123",
                "Bob",
                5L
        );

        String expectedUid = "5a5a5a5a";
        UserRecord record = mock(UserRecord.class);
        when(record.getUid()).thenReturn(expectedUid);
        when(record.getEmail()).thenReturn(userDTO.email());
        when(record.getDisplayName()).thenReturn(userDTO.displayName());

        when(mockFbWrapper.create(any())).thenReturn(record);

        userService.createEmailPasswordUser(userDTO);

        ArgumentCaptor<UnverifiedUserEvent> captor = ArgumentCaptor.forClass(UnverifiedUserEvent.class);
        verify(mockStreamEventPublisher).publish(captor.capture());

        UnverifiedUserEvent event = captor.getValue();

        assertEquals(userDTO.email(), event.email());
        assertEquals(userDTO.displayName(), event.displayName());
        assertEquals(expectedUid, event.uid());
    }

    @Test
    public void verifyEmailPasswordUser_PublishEvent_UpdateUser() throws FirebaseAuthException, ConflictException, UnauthorizedException, NotFoundException, DomainInvalidException {
        String uid = "5a5a5a5a";
        String email = "email@email.com";
        String displayName = "FirstName LastName";

        UserRecord record = mock(UserRecord.class);
        when(record.getUid()).thenReturn(uid);
        when(record.getEmail()).thenReturn(email);
        when(record.getDisplayName()).thenReturn(displayName);
        when(record.isEmailVerified()).thenReturn(false);
        when(mockFbWrapper.getUser(any())).thenReturn(record);

        Role role = mock(Role.class);
        Tenant tenant = mock(Tenant.class);
        when(tenant.getId()).thenReturn(5L);
        when(role.getTenant()).thenReturn(tenant);
        when(mockRoleService.assignInitialRole(any())).thenReturn(role);

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getTenant()).thenReturn(tenant);
        when(user.getEmail()).thenReturn(email);
        when(user.getUid()).thenReturn(uid);
        when(user.getDisplayName()).thenReturn(displayName);
        when(user.getTenant()).thenReturn(tenant);
        when(mockUserRepository.save(any())).thenReturn(user);

        VerifyDTO verifyDTO = new VerifyDTO(5L);

        userService.verifyEmailPasswordUser(uid, verifyDTO);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(mockUserRepository).save(userCaptor.capture());

        User capUser = userCaptor.getValue();
        assertEquals(email, capUser.getEmail());
        assertEquals(displayName, capUser.getDisplayName());
        assertEquals(uid, capUser.getUid());
        assertEquals(tenant.getId(), capUser.getTenant().getId());

        ArgumentCaptor<PersistUserEvent> eventCaptor = ArgumentCaptor.forClass(PersistUserEvent.class);
        verify(mockStreamEventPublisher).publish(eventCaptor.capture());

        PersistUserEvent event = eventCaptor.getValue();
        assertEquals(email, event.email());
        assertEquals(uid, event.uid());
        assertEquals(displayName, event.displayName());
        assertEquals(tenant.getId(), event.tenantId());
        assertEquals(user.getId(), event.userId());
    }

}

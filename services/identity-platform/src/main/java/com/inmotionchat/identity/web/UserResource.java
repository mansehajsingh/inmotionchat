package com.inmotionchat.identity.web;

import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.core.web.AbstractResource;
import com.inmotionchat.identity.service.UserService;
import com.inmotionchat.identity.web.dto.VerifyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.inmotionchat.core.web.AbstractResource.PATH;

@RestController
@RequestMapping(PATH + "/users")
public class UserResource extends AbstractResource<User, UserDTO> {

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @PatchMapping("/{id}")
    public void verify(@PathVariable Long id, @RequestBody VerifyUser verifyUser) throws UnauthorizedException, NotFoundException, ConflictException, DomainInvalidException {
        this.userService.verify(id, verifyUser.verificationCode);
    }

}

package com.inmotionchat.identity.web;

import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.web.AbstractResource;
import com.inmotionchat.identity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.inmotionchat.core.web.AbstractResource.PATH;

@RestController
@RequestMapping(PATH + "/users")
public class UserResource extends AbstractResource<User, UserDTO> {

    @Autowired
    public UserResource(UserService userService) {
        super(userService);
    }

}

package com.inmotionchat.identity.service;

import com.inmotionchat.core.data.ArchivingDomainService;
import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.domains.User;

public interface UserService extends ArchivingDomainService<User, UserDTO> {
}

package com.inmotionchat.identity.service.contract;

import java.util.Set;

public interface TokenProvider {

    String issueAccessToken(Long userId, Long roleId, Long tenantId, Set<String> permissions);

}

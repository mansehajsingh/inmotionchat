package com.inmotionchat.identity.service.contract;

public interface RoleChangeRecord {

    boolean roleHasChanged(Long roleId, Long userId);

}

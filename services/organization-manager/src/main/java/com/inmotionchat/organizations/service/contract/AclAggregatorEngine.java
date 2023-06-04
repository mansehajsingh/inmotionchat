package com.inmotionchat.organizations.service.contract;

import com.inmotionchat.core.domains.Role;
import com.inmotionchat.core.domains.models.AclAggregate;

/**
 * Request scoped service that allows you to fetch Acls efficiently
 * without having to drill AclAggregates down method call depths.
 * Request scope gives this service thread safety for using a temporary
 * in memory store of AclAggregates by Role.
 */
public interface AclAggregatorEngine {

    AclAggregate getAcls(Role role, Class ...domains);

}

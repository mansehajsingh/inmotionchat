package com.inmotionchat.organizations.postgres;

import com.inmotionchat.core.data.SQLArchivingRepository;
import com.inmotionchat.core.data.postgres.SQLMembership;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLMembershipRepository extends SQLArchivingRepository<SQLMembership> {
}

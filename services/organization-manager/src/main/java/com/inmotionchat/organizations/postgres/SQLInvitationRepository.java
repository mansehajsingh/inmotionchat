package com.inmotionchat.organizations.postgres;

import com.inmotionchat.core.data.SQLRepository;
import com.inmotionchat.core.data.postgres.SQLInvitation;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLInvitationRepository extends SQLRepository<SQLInvitation> {
}

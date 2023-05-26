package com.inmotionchat.core.data;

import com.inmotionchat.core.data.postgres.AbstractDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface SQLRepository<T extends AbstractDomain> extends JpaRepository<T, Long> {
}

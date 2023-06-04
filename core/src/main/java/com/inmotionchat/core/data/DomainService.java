package com.inmotionchat.core.data;

import com.inmotionchat.core.domains.Domain;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.security.AuthenticationDetails;
import com.inmotionchat.core.util.query.SearchCriteria;
import com.inmotionchat.core.util.query.SearchCriteriaMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

public interface DomainService<T extends Domain<T>, DTO> {

    default SearchCriteria<?>[] getSearchCriteriaFromParameters(
            SearchCriteriaMapper mapper, MultiValueMap<String, Object> parameters
    ) {

        List<SearchCriteria<?>> searchCriteria = new ArrayList<>();

        for (String key : mapper.keys()) {
            if (parameters.containsKey(key)) {
                searchCriteria.add(
                        mapper.map(key, parameters.get(key).get(0))
                );
            }
        }

        return searchCriteria.toArray(new SearchCriteria[0]);
    }

    default Long requestingUserId() {
        return ((AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUserId();
    }

    T retrieveById(Long id) throws NotFoundException;

    Page<? extends T> search(Pageable pageable, MultiValueMap<String, Object> parameters);

    Page<? extends T> search(Pageable pageable, SearchCriteria<?> ...criteria);

    T create(DTO prototype) throws DomainInvalidException, ConflictException, NotFoundException;

    T update(Long id, DTO prototype) throws DomainInvalidException, NotFoundException, ConflictException;

    T delete(Long id) throws NotFoundException, ConflictException;

}

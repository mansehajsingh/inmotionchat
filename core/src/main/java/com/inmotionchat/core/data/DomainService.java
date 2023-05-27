package com.inmotionchat.core.data;

import com.inmotionchat.core.domains.Domain;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.util.query.SearchCriteria;
import com.inmotionchat.core.util.query.SearchCriteriaMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public T retrieveById(Long id) throws NotFoundException;

    public Page<? extends T> search(MultiValueMap<String, Object> parameters, Pageable pageable);

    public T create(DTO prototype) throws DomainInvalidException, ConflictException;

    public T update(Long id, DTO prototype) throws DomainInvalidException, NotFoundException, ConflictException;

    public T delete(Long id) throws NotFoundException, ConflictException;

}

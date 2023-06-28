package com.inmotionchat.core.data;

import com.inmotionchat.core.data.annotation.DomainUpdate;
import com.inmotionchat.core.data.postgres.AbstractDomain;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.ServerException;
import com.inmotionchat.core.util.query.SearchCriteria;
import com.inmotionchat.core.util.query.SearchCriteriaMapper;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDomainService<D extends AbstractDomain<D>, DTO> implements DomainService<D, DTO> {

    private final SQLRepository<D> repository;

    private final Logger log;

    private final Class<D> type;

    private final Class<DTO> dtoType;

    private final SearchCriteriaMapper searchCriteriaMapper;

    protected AbstractDomainService(
            Class<D> type,
            Class<DTO> dtoType,
            Logger log,
            SQLRepository<D> repository,
            SearchCriteriaMapper searchCriteriaMapper
    ) {
        this.type = type;
        this.dtoType = dtoType;
        this.log = log;
        this.repository = repository;
        this.searchCriteriaMapper = searchCriteriaMapper;
    }

    protected SearchCriteria<?>[] getSearchCriteriaFromParameters(
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

    @Override
    public D retrieveById(Long id) throws NotFoundException {
        return this.repository.findById(id).orElseThrow(
                () -> new NotFoundException("No " + this.type.getSimpleName() + " with id " + id + " could be found."));
    }

    @Override
    public Page<? extends D> search(Pageable pageable, MultiValueMap<String, Object> parameters) {
        return search(pageable, getSearchCriteriaFromParameters(this.searchCriteriaMapper, parameters));
    }

    @Override
    public Page<? extends D> search(Pageable pageable, SearchCriteria<?> ...criteria) {
        return this.repository.filter(pageable, criteria);
    }

    @Override
    public D create(DTO prototype) throws DomainInvalidException, ConflictException, NotFoundException, ServerException {
        try {
            Constructor<D> ctor = this.type.getConstructor(dtoType);
            D entity = ctor.newInstance(prototype);
            entity.validate();
            entity.validateForCreate();

            return this.repository.store(entity);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("No constructor for DTO type {} was found in {}.", dtoType.getSimpleName(), type.getSimpleName());
            throw new ServerException();
        }
    }

    @Override
    public D update(Long id, DTO prototype) throws DomainInvalidException, NotFoundException, ConflictException, ServerException {
        try {
            D retrieved = retrieveById(id);

            Method domainUpdater = null;

            for (Method m : this.type.getDeclaredMethods())
                if (m.isAnnotationPresent(DomainUpdate.class))
                    domainUpdater = m;

            if (domainUpdater == null)
                throw new NoSuchMethodException();

            D updated = this.type.cast(domainUpdater.invoke(retrieved, prototype));
            updated.validate();
            updated.validateForUpdate();

            return this.type.cast(this.repository.update(updated));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("No method annotated with @" + DomainUpdate.class.getSimpleName() + " with type {} was found in {}.",
                    dtoType.getSimpleName(), type.getSimpleName());
            throw new ServerException();
        }
    }

    @Override
    public D delete(Long id) throws NotFoundException, ConflictException {
        D retrieved = retrieveById(id);
        this.repository.deleteById(id);
        return retrieved;
    }

}

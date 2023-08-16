package com.inmotionchat.core.data;

import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.InMotionException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import com.inmotionchat.smartpersist.exception.SmartQueryException;
import org.springframework.lang.Nullable;
import org.springframework.transaction.support.TransactionTemplate;

public class ThrowingTransactionTemplate {

    private final TransactionTemplate transactionTemplate;

    public ThrowingTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Nullable
    public <T> T execute(ThrowingTransactionCallback<T> action) throws ConflictException, NotFoundException, DomainInvalidException, UnauthorizedException {
        final var cause = new Object() { Exception thrownException = null; };

        T result = this.transactionTemplate.execute(status -> {
            try {
                return action.doInTransaction(status);
            } catch (InMotionException | SmartQueryException e) {
                cause.thrownException = e;
                status.setRollbackOnly();
            }

            return null;
        });

        if (cause.thrownException instanceof ConflictException c) throw c;

        if (cause.thrownException instanceof NotFoundException n) throw n;

        if (cause.thrownException instanceof DomainInvalidException d) throw d;

        if (cause.thrownException instanceof UnauthorizedException u) throw u;

        return result;
    }

}

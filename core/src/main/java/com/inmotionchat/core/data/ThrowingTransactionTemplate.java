package com.inmotionchat.core.data;

import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.InMotionException;
import com.inmotionchat.core.exceptions.NotFoundException;
import org.springframework.lang.Nullable;
import org.springframework.transaction.support.TransactionTemplate;

public class ThrowingTransactionTemplate {

    private final TransactionTemplate transactionTemplate;

    public ThrowingTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Nullable
    public <T> T execute(ThrowingTransactionCallback<T> action) throws ConflictException, NotFoundException, DomainInvalidException {
        final var cause = new Object() { InMotionException thrownException = null; };

        T result = this.transactionTemplate.execute(status -> {
            try {
                return action.doInTransaction(status);
            } catch (InMotionException e) {
                cause.thrownException = e;
                status.setRollbackOnly();
            }

            return null;
        });

        if (cause.thrownException instanceof ConflictException c) throw c;

        if (cause.thrownException instanceof NotFoundException n) throw n;

        if (cause.thrownException instanceof DomainInvalidException d) throw d;

        return result;
    }

}

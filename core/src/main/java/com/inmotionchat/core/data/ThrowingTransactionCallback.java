package com.inmotionchat.core.data;

import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.ServerException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import org.springframework.lang.Nullable;
import org.springframework.transaction.TransactionStatus;

@FunctionalInterface
public interface ThrowingTransactionCallback<T> {
    @Nullable
    T doInTransaction(TransactionStatus status) throws ConflictException, NotFoundException, DomainInvalidException, UnauthorizedException, ServerException;
}

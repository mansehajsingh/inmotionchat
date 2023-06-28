package com.inmotionchat.core.data;

import com.inmotionchat.core.exceptions.*;
import org.springframework.lang.Nullable;
import org.springframework.transaction.TransactionStatus;

@FunctionalInterface
public interface ThrowingTransactionCallback<T> {
    @Nullable
    T doInTransaction(TransactionStatus status) throws ConflictException, NotFoundException, DomainInvalidException, UnauthorizedException, ServerException;
}

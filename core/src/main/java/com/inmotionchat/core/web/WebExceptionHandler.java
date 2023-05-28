package com.inmotionchat.core.web;

import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WebExceptionHandler {

    private static Logger getClassLogger(Exception e) throws ClassNotFoundException {
        return LoggerFactory.getLogger(
                Class.forName(e.getStackTrace()[0].getClassName()));
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<MessageResponse> handleNotFoundException(NotFoundException e) throws ClassNotFoundException {
        Logger log = getClassLogger(e);
        log.debug(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<ConflictResponse> handleConflictException(ConflictException e) throws ClassNotFoundException {
        Logger log = getClassLogger(e);
        log.debug("Constraint: {}, Message: {}", e.getViolatedConstraint(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ConflictResponse(
                e.getViolatedConstraint(), e.getMessage()));
    }

    @ExceptionHandler(value = DomainInvalidException.class)
    public ResponseEntity<DomainInvalidResponse> handleDomainInvalidException(DomainInvalidException e) {
        // TODO: add logging
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new DomainInvalidResponse(e));
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<MessageResponse> handleUnauthorizedException(UnauthorizedException e) throws ClassNotFoundException {
        Logger log = getClassLogger(e);
        log.debug("Unauthorized Exception: {}. ", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(e.getMessage()));
    }

    @ExceptionHandler(value = MethodUnsupportedException.class)
    public ResponseEntity<String> handleMethodUnsupportedException(MethodUnsupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP Method not allowed.");
    }

}

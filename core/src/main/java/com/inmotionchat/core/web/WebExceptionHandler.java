package com.inmotionchat.core.web;

import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.PermissionException;
import com.inmotionchat.core.exceptions.ServerException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WebExceptionHandler {

    private static Logger getClassLogger(Exception e) throws ClassNotFoundException {
        return LoggerFactory.getLogger(
                Class.forName(e.getStackTrace()[0].getClassName()));
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) throws ClassNotFoundException {
        Logger log = getClassLogger(e);
        log.debug("NotFoundException: Constraint: {}, Message: {}", e.getConstraint(), e.getMessage());
        if (e.getConstraint().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ConstraintResponse(e.getConstraint(), e.getMessage()));
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<?> handleConflictException(ConflictException e) throws ClassNotFoundException {
        Logger log = getClassLogger(e);
        log.debug("ConflictException: Constraint: {}, Message: {}", e.getConstraint(), e.getMessage());
        if (e.getConstraint().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ConstraintResponse(e.getConstraint(), e.getMessage()));
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

    @ExceptionHandler(value = PermissionException.class)
    public ResponseEntity<MissingPermissionsResponse> handlePermissionException(PermissionException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new MissingPermissionsResponse(e.getMissingPermissions()));
    }

    @ExceptionHandler(value = ServerException.class)
    public ResponseEntity<String> handleServerException(ServerException e) throws ClassNotFoundException {
        getClassLogger(e).error("Unexpected ServerException was triggered: {}", e.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong: " + e.getMessage());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}

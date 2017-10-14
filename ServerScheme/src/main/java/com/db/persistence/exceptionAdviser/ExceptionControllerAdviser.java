package com.db.persistence.exceptionAdviser;

import com.db.persistence.remote_exception.DatabaseValidationRemoteException;
import com.db.persistence.remote_exception.ObjectInstanceRemoteException;
import com.db.persistence.remote_exception.ObjectNotFoundRemoteException;
import com.db.persistence.remote_exception.QueryRemoteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.PostConstruct;

@ControllerAdvice
public class ExceptionControllerAdviser {

    @PostConstruct
    public void init() {
        System.out.println("Initializing Exception Control Adviser");
    }

    @ExceptionHandler(ObjectNotFoundRemoteException.class)
    public ResponseEntity<ObjectNotFoundRemoteException> exceptionHandler(ObjectNotFoundRemoteException ex) {
        ObjectNotFoundRemoteException err = new ObjectNotFoundRemoteException(ex.getMessage());
        return new ResponseEntity<ObjectNotFoundRemoteException>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DatabaseValidationRemoteException.class)
    public ResponseEntity<DatabaseValidationRemoteException> exceptionHandler(DatabaseValidationRemoteException ex) {
        DatabaseValidationRemoteException err = new DatabaseValidationRemoteException(ex.getMessage());
        return new ResponseEntity<DatabaseValidationRemoteException>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ObjectInstanceRemoteException.class)
    public ResponseEntity<ObjectInstanceRemoteException> exceptionHandler(ObjectInstanceRemoteException ex) {
        ObjectInstanceRemoteException err = new ObjectInstanceRemoteException(ex.getMessage());
        return new ResponseEntity<ObjectInstanceRemoteException>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(QueryRemoteException.class)
    public ResponseEntity<QueryRemoteException> exceptionHandler(QueryRemoteException ex) {
        QueryRemoteException err = new QueryRemoteException(ex.getMessage());
        return new ResponseEntity<QueryRemoteException>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

package com.workflex.demonic.exception;

public class WorkationNotFoundException extends RuntimeException {
    public WorkationNotFoundException(Long id) {
        super("Trip not found with id: " + id);
    }
}

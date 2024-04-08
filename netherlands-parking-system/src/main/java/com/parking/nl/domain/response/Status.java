package com.parking.nl.domain.response;

public enum Status {
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

package com.parking.nl.data.model;

public enum ParkingStatus {
    START("START"),
    END("END");

    private final String parkingStatus;

    ParkingStatus(String parkingStatus) {
        this.parkingStatus = parkingStatus;
    }

    public String getParkingStatus() {
        return parkingStatus;
    }
}

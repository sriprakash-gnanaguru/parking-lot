package com.parking.nl.service;

import com.parking.nl.data.model.ParkingVehicle;
import com.parking.nl.domain.request.ParkingRequest;
import com.parking.nl.domain.response.ParkingMonitoringResponse;

import java.util.List;

public interface ParkingSystemService {
    void registerParking(ParkingRequest registerParkingRequest);

    ParkingMonitoringResponse unregisterParking(String licensePlateNumber);

   public List<ParkingVehicle> findLicensePlateNumberByStatus(String status);
}

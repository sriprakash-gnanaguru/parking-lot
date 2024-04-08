package com.parking.nl.helper;

import com.parking.nl.data.model.ParkingStatus;
import com.parking.nl.data.model.ParkingVehicle;
import com.parking.nl.domain.request.UnregisteredVehiclesRequest;
import com.parking.nl.service.ParkingSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PenaltyHelper {
    ParkingSystemService service;

    @Autowired
    public PenaltyHelper(final ParkingSystemService service){
        this.service = service;
    }

    public List<UnregisteredVehiclesRequest> getPenaltyVehicles(List<UnregisteredVehiclesRequest> requests){
        List<String> paidVehicles = service.findLicensePlateNumberByStatus(ParkingStatus.START.getParkingStatus()).stream()
                .map(ParkingVehicle::getLicensePlateNumber).toList();
        return requests.stream().filter(request ->!paidVehicles.contains(request.getLicensePlateNumber())).toList();
    }



}

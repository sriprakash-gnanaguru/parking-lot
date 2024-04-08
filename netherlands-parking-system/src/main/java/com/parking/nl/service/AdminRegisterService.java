package com.parking.nl.service;

import com.parking.nl.domain.request.UnregisteredVehiclesRequest;

import java.util.List;

public interface AdminRegisterService {

    void persistUnregisterVehicles(List<UnregisteredVehiclesRequest> unregisteredVehicles);
}

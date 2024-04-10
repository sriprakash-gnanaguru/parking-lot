package com.parking.nl.service.impl;

import com.parking.nl.data.repository.UnRegisteredVehiclesRepository;
import com.parking.nl.domain.request.UnregisteredVehiclesRequest;
import com.parking.nl.helper.PenaltyHelper;
import com.parking.nl.mapper.UnRegisteredVehiclesMapper;
import com.parking.nl.service.AdminRegisterService;
import com.parking.nl.validator.StreetValidator;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AdminRegisterServiceImpl implements AdminRegisterService {
    UnRegisteredVehiclesRepository unRegisteredVehiclesRepository;
    UnRegisteredVehiclesMapper vehiclesMapper;
    StreetValidator streetValidator;
    PenaltyHelper helper;

    @Autowired
    public AdminRegisterServiceImpl(final UnRegisteredVehiclesRepository unRegisteredVehiclesRepository,StreetValidator streetValidator,PenaltyHelper helper){
        this.unRegisteredVehiclesRepository = unRegisteredVehiclesRepository;
        this.vehiclesMapper =  Mappers.getMapper(UnRegisteredVehiclesMapper.class);
        this.streetValidator = streetValidator;
        this.helper = helper;

    }
    @Override
    public void persistUnregisterVehicles(List<UnregisteredVehiclesRequest> unregisteredVehicles) {
        streetValidator.validate(unregisteredVehicles);
        List<UnregisteredVehiclesRequest>  penaltyVehicles = helper.getPenaltyVehicles(unregisteredVehicles);
        penaltyVehicles.forEach(penaltyvehicle -> {
            unRegisteredVehiclesRepository.save(vehiclesMapper.translate(penaltyvehicle));
        });
        log.info("Unregistered Vehicles are persisted successfully in the system");
    }

}
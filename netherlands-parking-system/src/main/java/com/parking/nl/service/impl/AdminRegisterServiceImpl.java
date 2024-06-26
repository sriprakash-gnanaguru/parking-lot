package com.parking.nl.service.impl;

import com.parking.nl.data.model.UnRegsiteredVehicles;
import com.parking.nl.data.repository.StreetRepository;
import com.parking.nl.data.repository.UnRegisteredVehiclesRepository;
import com.parking.nl.domain.request.StreetRequest;
import com.parking.nl.domain.request.UnregisteredVehiclesRequest;
import com.parking.nl.exception.ServiceException;
import com.parking.nl.helper.PenaltyHelper;
import com.parking.nl.mapper.StreetMapper;
import com.parking.nl.mapper.UnRegisteredVehiclesMapper;
import com.parking.nl.service.AdminRegisterService;
import com.parking.nl.service.tariff.TariffCalculator;
import com.parking.nl.validator.CheckInDateValidator;
import com.parking.nl.validator.StreetValidator;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.parking.nl.common.Constants.*;

@Service
@Slf4j
public class AdminRegisterServiceImpl implements AdminRegisterService {
    UnRegisteredVehiclesRepository unRegisteredVehiclesRepository;
    StreetRepository streetRepository;
    UnRegisteredVehiclesMapper vehiclesMapper;
    StreetMapper streetMapper;
    StreetValidator streetValidator;
    CheckInDateValidator checkInDateValidator;
    PenaltyHelper helper;
    TariffCalculator tariffCalculator;

    @Autowired
    public AdminRegisterServiceImpl(UnRegisteredVehiclesRepository unRegisteredVehiclesRepository,StreetValidator streetValidator,PenaltyHelper helper,TariffCalculator tariffCalculator,CheckInDateValidator checkInDateValidator,StreetRepository streetRepository){
        this.unRegisteredVehiclesRepository = unRegisteredVehiclesRepository;
        this.vehiclesMapper =  Mappers.getMapper(UnRegisteredVehiclesMapper.class);
        this.streetMapper =Mappers.getMapper(StreetMapper.class);;
        this.helper = helper;
        this.streetValidator = streetValidator;
        this.tariffCalculator = tariffCalculator;
        this.checkInDateValidator = checkInDateValidator;
        this.streetRepository = streetRepository;


    }
    @Override
    public List<String> persistUnregisterVehicles(List<UnregisteredVehiclesRequest> unregisteredVehicles) {
        List<String> output = new ArrayList<>();
        streetValidator.validate(unregisteredVehicles);
        checkInDateValidator.validate(unregisteredVehicles);
        List<UnregisteredVehiclesRequest>  penaltyVehicles = helper.getPenaltyVehicles(unregisteredVehicles);
        penaltyVehicles.forEach(penaltyvehicle -> {
            UnRegsiteredVehicles unRegsiteredVehicle = vehiclesMapper.translate(penaltyvehicle);
            BigDecimal penality = tariffCalculator.calculateTariff(unRegsiteredVehicle.getStreetName(),unRegsiteredVehicle.getObservationTime(), LocalDateTime.now());
            unRegsiteredVehicle.setPrice(penality);
            unRegisteredVehiclesRepository.save(unRegsiteredVehicle);
            output.add(SPACE +unRegsiteredVehicle.getLicensePlateNumber()+COLON+EURO+penality);
        });
        log.info("Unregistered Vehicles are persisted successfully in the system");
        return output;
    }

    @Override
    public  List<String>  addStreets(List<StreetRequest> streetRequests) {
        List<String> output = new ArrayList<>();
        streetRequests.forEach(streetRequest -> {
            if(streetRepository.findByNameIgnoreCase(streetRequest.getStreetName()).isEmpty()){
                streetRepository.save(streetMapper.translate(streetRequest));
                output.add(streetRequest.getStreetName());
            }else{
                throw new ServiceException("Street already present in the streets:"+streetRequest.getStreetName());
            }
        });
        log.info("Streets are persisted successfully in the system");
        return output;
    }

}

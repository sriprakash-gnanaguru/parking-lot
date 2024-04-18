package com.parking.nl.service.impl;

import com.parking.nl.data.model.ParkingStatus;
import com.parking.nl.data.model.ParkingVehicle;
import com.parking.nl.data.repository.ParkingVehiclesRepository;
import com.parking.nl.data.repository.StreetRepository;
import com.parking.nl.domain.request.ParkingRequest;
import com.parking.nl.domain.response.ParkingMonitoringResponse;
import com.parking.nl.domain.response.Status;
import com.parking.nl.exception.BusinessException;
import com.parking.nl.exception.InvalidInputException;
import com.parking.nl.mapper.ParkingVehiclesMapper;
import com.parking.nl.service.ParkingSystemService;
import com.parking.nl.service.tariff.TariffCalculator;
import com.parking.nl.validator.CheckInDateValidator;
import com.parking.nl.validator.LicensePlateValidator;
import com.parking.nl.validator.StreetValidator;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.parking.nl.common.Constants.PARKING_SERVICE_MSG;
import static com.parking.nl.data.repository.ParkingVehiclesSpecification.findVehicleByLicensePlateNumber;
import static com.parking.nl.data.repository.ParkingVehiclesSpecification.findVehicleByStatus;

@Service
@Slf4j
public class ParkingSystemServiceImpl implements ParkingSystemService {
    ParkingVehiclesRepository parkingRepository;
    ParkingVehiclesMapper parkingMapper;
    StreetRepository streetRepository;
    TariffCalculator tariffCalculator;
    StreetValidator StreetValidator;

    CheckInDateValidator checkInDateValidator;
    LicensePlateValidator licenseValidator;

    @Autowired
    public ParkingSystemServiceImpl(final  ParkingVehiclesRepository parkingRepository,final StreetRepository streetRepository,final TariffCalculator tariffCalculator, final StreetValidator StreetValidator , final LicensePlateValidator licenseValidator, final CheckInDateValidator checkInDateValidator){
        this.parkingRepository = parkingRepository;
        this.streetRepository = streetRepository;
        this.parkingMapper =  Mappers.getMapper(ParkingVehiclesMapper.class);
        this.tariffCalculator = tariffCalculator;
        this.licenseValidator = licenseValidator;
        this.StreetValidator = StreetValidator;
        this.checkInDateValidator = checkInDateValidator;
    }
    @Transactional
    @Override
    public void registerParking(ParkingRequest registerParkingRequest) {
        StreetValidator.validate(registerParkingRequest.getStreetName());
        checkInDateValidator.validate(registerParkingRequest.getCheckInDateTime());
        Specification<ParkingVehicle> spec = Specification.where(findVehicleByLicensePlateNumber(registerParkingRequest.getLicensePlateNumber())).and(findVehicleByStatus(ParkingStatus.START.getParkingStatus()));
        if(!parkingRepository.findAll(spec).isEmpty()){
            log.error("Vehicle already have active session");
            throw new BusinessException("Unable to create new session for the vehicle with active session");
        }
        parkingRepository.save(parkingMapper.translate(registerParkingRequest));
    }

    @Transactional
    @Override
    public ParkingMonitoringResponse unregisterParking(String licensePlateNumber){
        BigDecimal parkingFee;
        Specification<ParkingVehicle> spec = Specification.where(findVehicleByLicensePlateNumber(licensePlateNumber)).and(findVehicleByStatus(ParkingStatus.START.getParkingStatus()));
        if(parkingRepository.findAll(spec).isEmpty()){
            log.error("Vehicle has no active session in the parking");
            throw new InvalidInputException("No active parking session for vehicle:"+licensePlateNumber);
        }else{
            ParkingVehicle parkingVehicle = parkingRepository.findAll(spec).get(0);
            LocalDateTime endTime = LocalDateTime.now();
            parkingFee = tariffCalculator.calculateTariff(parkingVehicle.getStreetName(),parkingVehicle.getStartTime(),endTime);
            parkingFee = parkingFee.compareTo(BigDecimal.ZERO)<0? BigDecimal.ZERO:parkingFee;
            parkingVehicle.setPrice(parkingFee);
            parkingVehicle.setStatus(ParkingStatus.END.getParkingStatus());
            parkingVehicle.setEndTime(endTime);
            parkingRepository.save(parkingVehicle);
        }
        return ParkingMonitoringResponse.builder().message(PARKING_SERVICE_MSG).status(Status.SUCCESS).parkingFee(parkingFee).build();
    }

    @Override
    public List<ParkingVehicle> findLicensePlateNumberByStatus(String status) {
        Specification<ParkingVehicle> spec = Specification.where(findVehicleByStatus(ParkingStatus.START.getParkingStatus()));
        return parkingRepository.findAll(spec);
    }
}

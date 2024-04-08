package com.parking.nl.service.impl;

import com.parking.nl.data.model.ParkingStatus;
import com.parking.nl.data.model.ParkingVehicle;
import com.parking.nl.data.repository.ParkingVehiclesRepository;
import com.parking.nl.data.repository.StreetRepository;
import com.parking.nl.domain.request.ParkingRequest;
import com.parking.nl.domain.response.OutputResponse;
import com.parking.nl.domain.response.Status;
import com.parking.nl.exception.BusinessException;
import com.parking.nl.exception.InvalidInputException;
import com.parking.nl.mapper.ParkingVehiclesMapper;
import com.parking.nl.service.ParkingSystemService;
import com.parking.nl.service.tariff.TariffCalculator;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.parking.nl.data.repository.ParkingVehiclesSpecification.findVehicleByLicensePlateNumber;
import static com.parking.nl.data.repository.ParkingVehiclesSpecification.findVehicleByStatus;

@Service
@Slf4j
public class ParkingSystemServiceImpl implements ParkingSystemService {
    ParkingVehiclesRepository parkingRepository;
    ParkingVehiclesMapper parkingMapper;
    StreetRepository streetRepository;
    TariffCalculator tariffCalculator;

    @Autowired
    public ParkingSystemServiceImpl(final  ParkingVehiclesRepository parkingRepository,final StreetRepository streetRepository,TariffCalculator tariffCalculator){
        this.parkingRepository = parkingRepository;
        this.streetRepository = streetRepository;
        this.parkingMapper =  Mappers.getMapper(ParkingVehiclesMapper.class);
        this.tariffCalculator = tariffCalculator;
    }
    @Transactional
    @Override
    public void registerParking(ParkingRequest registerParkingRequest) {
        if(streetRepository.findByNameIgnoreCase(registerParkingRequest.getStreetName()).isEmpty()){
            log.error("Street name  is not found in Street:",registerParkingRequest.getStreetName());
            throw new InvalidInputException("Street name is not found");
        }
        Specification<ParkingVehicle> spec = Specification.where(findVehicleByLicensePlateNumber(registerParkingRequest.getLicensePlateNumber())).and(findVehicleByStatus(ParkingStatus.START.getParkingStatus()));
        if(!parkingRepository.findAll(spec).isEmpty()){
            log.error("Vehicle already have active session");
            throw new BusinessException("Unable to create new session for the vehicle with active session");
        }
        parkingRepository.save(parkingMapper.translate(registerParkingRequest));
    }

    @Transactional
    @Override
    public OutputResponse unregisterParking(String licensePlateNumber){
        BigDecimal parkingFee;
        Specification<ParkingVehicle> spec = Specification.where(findVehicleByLicensePlateNumber(licensePlateNumber)).and(findVehicleByStatus(ParkingStatus.START.getParkingStatus()));
        if(parkingRepository.findAll(spec).isEmpty()){
            log.error("Vehicle has no active session in the parking");
            throw new BusinessException("No active parking session for vehicle:"+licensePlateNumber);
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
        return OutputResponse.builder().message("Active session of parking vehicle had ended succesfully").status(Status.SUCCESS).parkingFee(parkingFee.toPlainString()).build();
    }

    @Override
    public List<ParkingVehicle> findLicensePlateNumberByStatus(String status) {
        Specification<ParkingVehicle> spec = Specification.where(findVehicleByStatus(ParkingStatus.START.getParkingStatus()));
        return parkingRepository.findAll(spec);
    }
}

package com.parking.nl.service;

import com.parking.nl.data.model.ParkingStatus;
import com.parking.nl.data.model.ParkingVehicle;
import com.parking.nl.data.repository.ParkingVehiclesRepository;
import com.parking.nl.domain.request.ParkingRequest;
import com.parking.nl.domain.response.ParkingMonitoringResponse;
import com.parking.nl.exception.InvalidInputException;
import com.parking.nl.exception.ServiceException;
import com.parking.nl.service.impl.ParkingSystemServiceImpl;
import com.parking.nl.service.tariff.TariffCalculator;
import com.parking.nl.validator.CheckInDateValidator;
import com.parking.nl.validator.LicensePlateValidator;
import com.parking.nl.validator.StreetValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static com.parking.nl.data.repository.ParkingVehiclesSpecification.findVehicleByLicensePlateNumber;
import static com.parking.nl.data.repository.ParkingVehiclesSpecification.findVehicleByStatus;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(SpringExtension.class)
public class ParkingSystemServiceTest {
    @Mock
    private ParkingVehiclesRepository parkingRepository;
    @Mock
    private TariffCalculator tariffCalculator;
    @Mock
    private TariffService tariffService;
    @Mock
    private  StreetValidator StreetValidator;
    @Mock
    private  LicensePlateValidator licensePlateValidator;
    @Mock
    private CheckInDateValidator checkInDateValidator;
    @InjectMocks
    private ParkingSystemServiceImpl service;

    @Test
    @DisplayName("Positive Scenario to test the save operation for vehicles")
    public void testRegisterParking(){
        ParkingRequest request = ParkingRequest.builder().licensePlateNumber("NL-HJ-987").checkInDateTime(LocalDateTime.now()).streetName("Java").build();
        Mockito.when(parkingRepository.findByLicensePlateNumberAndStatus(request.getLicensePlateNumber(), ParkingStatus.START.getParkingStatus()))
                .thenReturn(Optional.empty());
        Mockito.when(tariffService.loadTariffMetaData()).thenReturn(Collections.singletonMap("Torreslaan",  BigDecimal.ONE));
        service.registerParking(request);
        Mockito.verify(parkingRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Negative Scenario for invalid input")
    public void testRegisterParkingForActiveParking() {
        ParkingRequest request = ParkingRequest.builder().licensePlateNumber("NL-HJ-987").checkInDateTime(LocalDateTime.now()).streetName("Azure").build();
        ParkingVehicle parkingVehicle =  ParkingVehicle.builder().licensePlateNumber("NL-HJ-987").status(ParkingStatus.START.getParkingStatus()).build();
        Specification<ParkingVehicle> spec = Specification.where(findVehicleByLicensePlateNumber(request.getLicensePlateNumber())).and(findVehicleByStatus(ParkingStatus.START.getParkingStatus()));
        Mockito.when(parkingRepository.findAll(spec)).thenReturn(Collections.singletonList(parkingVehicle));
        Mockito.when(tariffService.loadTariffMetaData()).thenReturn(Collections.singletonMap("Java",  BigDecimal.ONE));
        doThrow(new InvalidInputException("Street is not found")).when(StreetValidator).validate(request.getStreetName());
        Assertions.assertThrows(InvalidInputException.class,()->service.registerParking(request));
    }

    @Test
    @DisplayName("Positive scenario to unregister the vehicle and getting the fee")
    public void testUnRegisterParking() {
        String licensePlatNumber = "NL-HJ-987";
        ParkingVehicle vehicle = ParkingVehicle.builder().licensePlateNumber(licensePlatNumber).streetName("Java").startTime(LocalDateTime.now()).build();
        doNothing().when(licensePlateValidator).validate(any());
        doNothing().when(checkInDateValidator).validate(LocalDateTime.now());
        Specification<ParkingVehicle> spec = Specification.where(findVehicleByLicensePlateNumber(any())).and(findVehicleByStatus(ParkingStatus.START.getParkingStatus()));
        Mockito.when(parkingRepository.findAll(spec)).thenReturn(Collections.singletonList(vehicle));
        Mockito.when(tariffCalculator.calculateTariff(any(),any(),any())).thenReturn(BigDecimal.ONE);
        ParkingMonitoringResponse response = service.unregisterParking(licensePlatNumber);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getParkingFee(),BigDecimal.ONE);
    }

    @Test
    @DisplayName("Negative scenario to unregister the vehicle with exception for Invalid input")
    public void testUnRegisterParkingWithInvalidInput() {
        String licensePlateNumber = null;
        doThrow(new ServiceException("No license plate number")).when(licensePlateValidator).validate(any());
        Assertions.assertThrows(ServiceException.class,()->service.unregisterParking(licensePlateNumber));
    }

    @Test
    @DisplayName("Positive Scenario to find the vehicle by license plate number with active status")
    public void testFindLicensePlateNumberByStatus(){
        ParkingVehicle vehicle = ParkingVehicle.builder().licensePlateNumber("NL-HJ-987").streetName("Java").status(ParkingStatus.START.getParkingStatus()).startTime(LocalDateTime.now()).build();
        Specification<ParkingVehicle> spec = Specification.where(findVehicleByStatus(ParkingStatus.START.getParkingStatus()));
        Mockito.when(parkingRepository.findAll(spec)).thenReturn(Collections.singletonList(vehicle));
        Assertions.assertDoesNotThrow(()->service.findLicensePlateNumberByStatus("NL-HJ-987"));

    }


}

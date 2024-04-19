package com.parking.nl.validator;

import com.parking.nl.domain.request.UnregisteredVehiclesRequest;
import com.parking.nl.exception.InvalidInputException;
import com.parking.nl.service.TariffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StreetValidator{
    TariffService tariffService;

    @Autowired
    public StreetValidator(final TariffService tariffService){
        this.tariffService = tariffService;
    }

    public void validate(List<UnregisteredVehiclesRequest> unregisteredVehiclesRequest) {
        Map<String, BigDecimal> parkingTariffMetaData = tariffService.loadTariffMetaData();
        List<String> streetNames = unregisteredVehiclesRequest.stream()
                .map(UnregisteredVehiclesRequest::getStreetName).filter(streetName -> !parkingTariffMetaData.containsKey(streetName)).toList();
        if (!streetNames.isEmpty()) {
            log.error("Invalid street names: ", streetNames);
            throw new InvalidInputException("Invalid street names: " + String.join(",", streetNames));
        }
    }
    public void validate(String streetName){
        Map<String, BigDecimal> parkingTariffMetaData = tariffService.loadTariffMetaData();
        if(!parkingTariffMetaData.containsKey(streetName)){
            log.error("Street name  is not found in Street:",streetName);
            throw new InvalidInputException("Street name is not found");
        }
    }
}

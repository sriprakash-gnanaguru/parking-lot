package com.parking.nl.validator;

import com.parking.nl.data.model.Street;
import com.parking.nl.domain.request.UnregisteredVehiclesRequest;
import com.parking.nl.exception.InvalidInputException;
import com.parking.nl.service.TariffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        Map<String, Integer> parkingTariffMetaData = tariffService.loadTariffMetaData();
        List<String> streetNames = unregisteredVehiclesRequest.stream()
                .map(UnregisteredVehiclesRequest::getStreetName).filter(streetName -> !parkingTariffMetaData.containsKey(streetName)).toList();
        if (!streetNames.isEmpty()) {
            log.error("Invalid street names: ", streetNames);
            throw new InvalidInputException("Invalid street names: " + String.join(",", streetNames));
        }
    }
}

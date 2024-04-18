package com.parking.nl.validator;

import com.parking.nl.domain.request.UnregisteredVehiclesRequest;
import com.parking.nl.exception.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.parking.nl.common.Constants.*;
import static com.parking.nl.common.Constants.EMPTY;

@Slf4j
@Component
public class CheckInDateValidator {

    public void validate(LocalDateTime checkInDateTime){
        if(checkInDateTime != null && checkInDateTime.isAfter(LocalDateTime.now())){
            log.error("Check-In-Date should not be in future value");
            throw new InvalidInputException("Check-In-Date should not be in future value");
        }
    }

    public void validate(List<UnregisteredVehiclesRequest> unregisteredVehiclesRequest) {
        List<LocalDateTime> futureDates =  unregisteredVehiclesRequest.stream().map(UnregisteredVehiclesRequest::getCheckInDateTime).filter(localDateTime -> localDateTime.isAfter(LocalDateTime.now())).toList();
        if(!futureDates.isEmpty()){
            log.error("Following Check-In-Date are in future value: "+futureDates.toString().replace(OPEN_BRACKET,EMPTY).replace(CLOSE_BRACKET,EMPTY));
            throw new InvalidInputException("Following Check-In-Date are in future value: "+futureDates.toString().replace(OPEN_BRACKET,EMPTY).replace(CLOSE_BRACKET,EMPTY));
        }

    }
}

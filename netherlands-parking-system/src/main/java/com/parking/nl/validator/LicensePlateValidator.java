package com.parking.nl.validator;

import com.parking.nl.exception.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LicensePlateValidator {

    public void validate(String licensePlateNumber){
        if(licensePlateNumber == null || licensePlateNumber.isEmpty()){
            log.error("license Plate Number is empty");
            throw new InvalidInputException("license Plate Number is empty");
        }
    }
}

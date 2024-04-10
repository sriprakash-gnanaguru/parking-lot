package com.parking.nl.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.parking.nl.common.Constants.DATE_PATTERN_YYYY_MM_DD_HH_MM;

public class CommonUtil {

    public static String formateDate(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN_YYYY_MM_DD_HH_MM );
        return date.format(formatter);
    }

}

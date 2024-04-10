package com.parking.nl.service;

import com.parking.nl.data.model.UnRegsiteredVehicles;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ReportSchedulerService {

    List<UnRegsiteredVehicles> findByIsNotified(Boolean flg);

    void save(List<UnRegsiteredVehicles> unRegisteredVehicles);

}


package com.parking.nl.service;

import com.parking.nl.data.model.UnRegsiteredVehicles;

import java.util.List;

public interface ReportSchedulerService {

    List<UnRegsiteredVehicles> findByIsNotified(Boolean flg);

    void save(List<UnRegsiteredVehicles> unRegisteredVehicles);

}


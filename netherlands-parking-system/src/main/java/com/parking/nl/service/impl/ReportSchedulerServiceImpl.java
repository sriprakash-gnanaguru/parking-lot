package com.parking.nl.service.impl;

import com.parking.nl.data.model.UnRegsiteredVehicles;
import com.parking.nl.data.repository.UnRegisteredVehiclesRepository;
import com.parking.nl.service.ReportSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportSchedulerServiceImpl implements ReportSchedulerService {

    UnRegisteredVehiclesRepository repository;

    @Autowired
    public ReportSchedulerServiceImpl(UnRegisteredVehiclesRepository repository){
        this.repository = repository;
    }

    @Override
    public List<UnRegsiteredVehicles> findByIsNotified(Boolean flg) {
        return repository.findByisNotified(flg);
    }

    @Override
    public void save(List<UnRegsiteredVehicles> unRegisteredVehicles) {
        unRegisteredVehicles.forEach(unRegsiteredVehicle->{
            unRegsiteredVehicle.setNotified(Boolean.TRUE);
            repository.save(unRegsiteredVehicle);
        });
    }
}

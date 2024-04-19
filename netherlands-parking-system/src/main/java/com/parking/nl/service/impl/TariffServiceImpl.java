package com.parking.nl.service.impl;

import com.parking.nl.data.model.Street;
import com.parking.nl.data.repository.StreetRepository;
import com.parking.nl.service.TariffService;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TariffServiceImpl implements TariffService {
    private StreetRepository streetRepository;

    @Autowired
    public TariffServiceImpl(final StreetRepository streetRepository){
        this.streetRepository = streetRepository;
    }

    @PostConstruct
    @Cacheable("tariffMetaData")
    public Map<String, BigDecimal> loadTariffMetaData() {
        List<Street> parkingTariffMetaDataList = streetRepository.findAll();
        return parkingTariffMetaDataList.stream().collect(Collectors.toMap(Street::getName, Street::getPricePerMinute));
    }

}

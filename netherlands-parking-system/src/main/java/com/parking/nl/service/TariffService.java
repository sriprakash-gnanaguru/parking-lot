package com.parking.nl.service;

import java.math.BigDecimal;
import java.util.Map;

public interface TariffService {

    public Map<String, BigDecimal> loadTariffMetaData();

}

package com.parking.nl.service;

import com.parking.nl.data.model.Street;
import com.parking.nl.data.repository.StreetRepository;
import com.parking.nl.service.impl.TariffServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TariffServiceTest {
    @Mock
    private StreetRepository streetRepository;
    @InjectMocks
    TariffServiceImpl service;

    @Test
    @DisplayName("Positive Scenario to retrieve the data from Street ")
    public void testLoadTariffMetaData() {
        when(streetRepository.findAll()).thenReturn(Collections.singletonList(Street.builder().streetId(1L).name("Java").pricePerMinute(BigDecimal.TEN).build()));
        Map<String, BigDecimal> data = service.loadTariffMetaData();
        Assertions.assertNotNull(data);
        Assertions.assertEquals(data.get("Java"),BigDecimal.TEN);
    }
}


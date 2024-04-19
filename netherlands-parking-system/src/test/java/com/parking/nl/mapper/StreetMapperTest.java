package com.parking.nl.mapper;

import com.parking.nl.data.model.Street;
import com.parking.nl.domain.request.StreetRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreetMapperTest {
    private StreetMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(StreetMapper.class);
    }

    @Test
    @DisplayName("translate request into Model correctly")
    public void testTranslate() {
        StreetRequest request = StreetRequest.builder().price(new BigDecimal(10.75)).streetName("Azure").build();
        Street model = mapper.translate(request);
        assertEquals("Azure", model.getName());
        assertEquals(new BigDecimal(10.75), model.getPricePerMinute());
    }
}

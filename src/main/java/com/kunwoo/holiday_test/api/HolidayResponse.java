package com.kunwoo.holiday_test.api.dto;

import java.time.LocalDate;

public record HolidayResponse(
        Long id,
        String countryCode,
        LocalDate date,
        Integer year,
        String localName,
        String name,
        Boolean global,
        String type
) {}
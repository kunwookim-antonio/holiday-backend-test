package com.kunwoo.holiday_test.external.nager;

public record NagerHolidayResponse(
        String date,        // "2024-01-01"
        String localName,
        String name,
        String countryCode,
        Boolean global,
        String type
) {}
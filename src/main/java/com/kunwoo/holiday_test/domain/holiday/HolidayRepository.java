package com.kunwoo.holiday_test.domain.holiday;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    List<Holiday> findByCountryCodeAndYear(String countryCode, Integer year);

    void deleteByCountryCodeAndYear(String countryCode, Integer year);

    boolean existsByCountryCodeAndDateAndName(String countryCode, LocalDate date, String name);
}
package com.kunwoo.holiday_test.api;

import com.kunwoo.holiday_test.api.dto.HolidayResponse;
import com.kunwoo.holiday_test.domain.holiday.Holiday;
import com.kunwoo.holiday_test.domain.holiday.HolidayRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HolidayQueryController {

    private final HolidayRepository holidayRepository;

    public HolidayQueryController(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    @GetMapping("/api/holidays")
    @Operation(
            summary = "공휴일 조회",
            description = "DB에 저장된 공휴일을 연도 + 국가 코드 기준으로 조회합니다."
    )
    public List<HolidayResponse> getHolidays(
            @RequestParam
            @Parameter(description = "연도", example = "2024")
            int year,
            @RequestParam
            @Parameter(description = "국가 코드", example = "KR")
            String countryCode
    ) {
        List<Holiday> holidays = holidayRepository.findByCountryCodeAndYear(countryCode, year);

        return holidays.stream()
                .map(h -> new HolidayResponse(
                        h.getId(),
                        h.getCountryCode(),
                        h.getDate(),
                        h.getYear(),
                        h.getLocalName(),
                        h.getName(),
                        h.getGlobal(),
                        h.getType()
                ))
                .toList();
    }
}
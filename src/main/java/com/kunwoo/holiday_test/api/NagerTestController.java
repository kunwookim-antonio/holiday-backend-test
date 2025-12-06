package com.kunwoo.holiday_test.api;

import com.kunwoo.holiday_test.external.nager.NagerClient;
import com.kunwoo.holiday_test.external.nager.NagerHolidayResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NagerTestController {

    private final NagerClient nagerClient;

    public NagerTestController(NagerClient nagerClient) {
        this.nagerClient = nagerClient;
    }

    @GetMapping("/api/nager/holidays")
    @Operation(summary = "외부 공휴일 API 테스트", description = "Nager.Date API에서 직접 공휴일 목록을 조회하는 테스트용 API")
    public List<NagerHolidayResponse> getPublicHolidays(
            @RequestParam @Parameter(description = "연도", example = "2024") int year,
            @RequestParam @Parameter(description = "국가 코드", example = "KR") String countryCode
    ) {
        return nagerClient.getPublicHolidays(year, countryCode);
    }
}
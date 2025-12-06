package com.kunwoo.holiday_test.api;

import com.kunwoo.holiday_test.external.nager.ExternalApiUnavailableException;
import com.kunwoo.holiday_test.service.holiday.HolidaySyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/holidays")
public class HolidayAdminController {

    private final HolidaySyncService holidaySyncService;

    public HolidayAdminController(HolidaySyncService holidaySyncService) {
        this.holidaySyncService = holidaySyncService;
    }

    @PostMapping("/initial-load")
    @Operation(
            summary = "초기 공휴일 데이터 적재",
            description = "2020~2025년까지 특정 국가의 공휴일 데이터를 외부 API에서 가져와 DB에 적재합니다."
    )
    public ResponseEntity<Void> initialLoad(
            @RequestParam
            @Parameter(description = "국가 코드", example = "KR")
            String countryCode
    ) {
        holidaySyncService.initialLoadForCountry(countryCode);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "특정 연도 공휴일 재동기화",
            description = "특정 연도 + 특정 국가의 공휴일 데이터를 외부 API에서 새로 가져와 DB에 반영합니다."
    )
    public ResponseEntity<Void> refresh(
            @RequestParam
            @Parameter(description = "연도", example = "2024")
            int year,
            @RequestParam
            @Parameter(description = "국가 코드", example = "KR")
            String countryCode
    ) {
        holidaySyncService.refreshYearAndCountry(year, countryCode);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(
            summary = "특정 연도 공휴일 삭제",
            description = "특정 연도 + 특정 국가의 공휴일 데이터를 DB에서 삭제합니다."
    )
    public ResponseEntity<Void> delete(
            @RequestParam
            @Parameter(description = "연도", example = "2024")
            int year,
            @RequestParam
            @Parameter(description = "국가 코드", example = "KR")
            String countryCode
    ) {
        holidaySyncService.deleteYearAndCountry(year, countryCode);
        return ResponseEntity.noContent().build();
    }

    /**
     * 외부 API 장애 시 503으로 응답
     */
    @ExceptionHandler(ExternalApiUnavailableException.class)
    public ResponseEntity<String> handleExternalApiUnavailable(ExternalApiUnavailableException e) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(e.getMessage());
    }
}
package com.kunwoo.holiday_test.batch;

import com.kunwoo.holiday_test.service.holiday.HolidaySyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.time.ZoneId;

@Component
public class HolidayBatchScheduler {

    private static final Logger log = LoggerFactory.getLogger(HolidayBatchScheduler.class);

    private final HolidaySyncService holidaySyncService;

    public HolidayBatchScheduler(HolidaySyncService holidaySyncService) {
        this.holidaySyncService = holidaySyncService;
    }

    /**
     * 매년 1월 2일 01:00 KST에
     * - 전년도(year-1)
     * - 금년도(year)
     * 공휴일 데이터를 자동 동기화한다.
     */
    @Scheduled(cron = "0 0 1 2 1 *", zone = "Asia/Seoul")
    public void syncPreviousAndCurrentYear() {
        int currentYear = Year.now(ZoneId.of("Asia/Seoul")).getValue();
        int previousYear = currentYear - 1;
        String countryCode = "KR"; // 필요하면 설정값으로 빼도 됨

        log.info("배치 공휴일 동기화 시작 - previousYear={}, currentYear={}, countryCode={}",
                previousYear, currentYear, countryCode);

        // 전년도 동기화
        holidaySyncService.refreshYearAndCountry(previousYear, countryCode);
        // 금년도 동기화
        holidaySyncService.refreshYearAndCountry(currentYear, countryCode);

        log.info("배치 공휴일 동기화 완료 - previousYear={}, currentYear={}, countryCode={}",
                previousYear, currentYear, countryCode);
    }
}
package com.kunwoo.holiday_test.external.nager;

import java.util.List;

public interface NagerClient {

    /**
     * 특정 연도 + 특정 국가의 공휴일 리스트 조회
     */
    List<NagerHolidayResponse> getPublicHolidays(int year, String countryCode);
}
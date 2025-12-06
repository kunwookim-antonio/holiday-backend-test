package com.kunwoo.holiday_test.service.holiday;

import com.kunwoo.holiday_test.domain.holiday.Holiday;
import com.kunwoo.holiday_test.domain.holiday.HolidayRepository;
import com.kunwoo.holiday_test.external.nager.NagerClient;
import com.kunwoo.holiday_test.external.nager.NagerHolidayResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HolidaySyncService {

    private static final Logger log = LoggerFactory.getLogger(HolidaySyncService.class);

    private final NagerClient nagerClient;
    private final HolidayRepository holidayRepository;

    public HolidaySyncService(NagerClient nagerClient,
                              HolidayRepository holidayRepository) {
        this.nagerClient = nagerClient;
        this.holidayRepository = holidayRepository;
    }

    // (date + name) 조합을 key로 쓰기 위한 내부 레코드
    private record HolidayKey(LocalDate date, String name) {}

    /**
     * 특정 연도 + 국가의 공휴일을 외부 API에서 새로 가져와 DB에 반영한다.
     * → 날짜 + 이름 기준으로 upsert
     */
    @Transactional
    public void refreshYearAndCountry(int year, String countryCode) {
        log.info("공휴일 재동기화 시작 - year={}, countryCode={}", year, countryCode);

        // 1) 외부 API 호출
        List<NagerHolidayResponse> responses = nagerClient.getPublicHolidays(year, countryCode);

        // 2) 해당 연도+국가의 기존 데이터 전부 조회
        List<Holiday> existing = holidayRepository.findByCountryCodeAndYear(countryCode, year);

        // 3) (date + name)을 key로 맵 구성
        Map<HolidayKey, Holiday> existingByKey = existing.stream()
                .collect(Collectors.toMap(
                        h -> new HolidayKey(h.getDate(), h.getName()),
                        Function.identity()
                ));

        List<Holiday> toSave = new ArrayList<>();
        Set<HolidayKey> apiKeys = new HashSet<>();

        // 4) API 응답 하나씩 돌면서 upsert 처리
        for (NagerHolidayResponse r : responses) {
            LocalDate date = LocalDate.parse(r.date());
            HolidayKey key = new HolidayKey(date, r.name());
            apiKeys.add(key);

            Holiday entity = existingByKey.get(key);

            if (entity == null) {
                // (1) 기존에 없는 (날짜+이름) → 새로 insert
                entity = new Holiday(
                        countryCode,
                        date,
                        year,
                        r.localName(),
                        r.name(),
                        r.global(),
                        r.type()
                );
            } else {
                // (2) 이미 있는 (날짜+이름) → 값만 update
                entity.updateFromApiResponse(
                        countryCode,
                        year,
                        r.localName(),
                        r.name(),
                        r.global(),
                        r.type()
                );
            }

            toSave.add(entity);
        }

        // 5) 바뀐 애들/새로운 애들 한 번에 저장
        holidayRepository.saveAll(toSave);

        // 6) API 응답에는 없는데 DB에만 남아있는 (날짜+이름) 데이터는 삭제
        List<Holiday> toDelete = existing.stream()
                .filter(h -> !apiKeys.contains(new HolidayKey(h.getDate(), h.getName())))
                .toList();

        if (!toDelete.isEmpty()) {
            holidayRepository.deleteAll(toDelete);
        }

        log.info("공휴일 재동기화 완료 - year={}, countryCode={}, upserted={}, deleted={}",
                year, countryCode, toSave.size(), toDelete.size());
    }

    /**
     * 2020~2025년까지 특정 국가의 공휴일을 한번에 적재하는 초기 로딩용 함수
     */
    @Transactional
    public void initialLoadForCountry(String countryCode) {
        for (int year = 2020; year <= 2025; year++) {
            refreshYearAndCountry(year, countryCode);
        }
    }

    @Transactional
    public void deleteYearAndCountry(int year, String countryCode) {
        holidayRepository.deleteByCountryCodeAndYear(countryCode, year);
    }
}
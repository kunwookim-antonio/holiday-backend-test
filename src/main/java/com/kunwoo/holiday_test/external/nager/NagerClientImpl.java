package com.kunwoo.holiday_test.external.nager;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class NagerClientImpl implements NagerClient {

    private static final Logger log = LoggerFactory.getLogger(NagerClientImpl.class);

    private static final String BASE_URL = "https://date.nager.at/api/v3";

    private final RestTemplate restTemplate;

    public NagerClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @CircuitBreaker(name = "nager", fallbackMethod = "getPublicHolidaysFallback")
    public List<NagerHolidayResponse> getPublicHolidays(int year, String countryCode) {
        String url = BASE_URL + "/PublicHolidays/{year}/{countryCode}";

        log.info("Call Nager API: {}, year={}, countryCode={}", url, year, countryCode);

        ResponseEntity<NagerHolidayResponse[]> response =
                restTemplate.getForEntity(url, NagerHolidayResponse[].class, year, countryCode);

        NagerHolidayResponse[] body = response.getBody();
        if (body == null) {
            return List.of();
        }
        return Arrays.asList(body);
    }

    /**
     * 서킷이 Open 상태이거나, 타임아웃/예외 발생 시 호출되는 fallback 메서드.
     * 시그니처: (원래 파라미터들..., Throwable t)
     */
    @SuppressWarnings("unused")
    private List<NagerHolidayResponse> getPublicHolidaysFallback(int year,
                                                                 String countryCode,
                                                                 Throwable t) {
        log.error("Nager API 호출 실패 (year={}, countryCode={}) - fallback 실행", year, countryCode, t);
        // 여기서는 커스텀 예외를 던져서 상위(Service/Controller)에서 처리하게 한다.
        throw new ExternalApiUnavailableException("공휴일 외부 API 연동 장애로 재동기화에 실패했습니다.", t);
    }
}
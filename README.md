공휴일 과제

## ✅ Tech Stack
- Java 21
- Spring Boot 3.x
- Spring Data JPA
- H2 Database
- Swagger (springdoc-openapi)
- Gradle

## ✅ Features
- Nager.Date 외부 API 연동
- 국가/연도별 공휴일 조회
- 공휴일 초기 적재 (Initial Load)
- 공휴일 재동기화 (Refresh)
- 중복 날짜 공휴일 처리 (예: 2025-05-05)

## ✅ API Docs
- Swagger UI  
  http://localhost:8080/swagger-ui.html

## ✅ How to Run
```bash
./gradlew bootRun
✅ Initial Data Load
http
코드 복사
POST /admin/holidays/initial-load?countryCode=KR
✅ Refresh
http
코드 복사
POST /admin/holidays/refresh?year=2025&countryCode=KR
✅ Notes
동일 날짜에 여러 공휴일이 존재할 수 있어
(country_code + date + name) 기준으로 UNIQUE 처리

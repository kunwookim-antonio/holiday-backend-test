공휴일 관리 API 과제

외부 공휴일 API(Nager.Date)를 활용하여
국가 및 연도별 공휴일을 조회·적재·재동기화·삭제할 수 있는 백엔드 서비스입니다.

✅ Tech Stack

Java 21

Spring Boot 3.x

Spring Data JPA

H2 Database

Swagger (springdoc-openapi)

Gradle

✅ Features

Nager.Date 외부 API 연동

국가 / 연도별 공휴일 조회

공휴일 초기 적재 (Initial Load)

공휴일 재동기화 (Refresh)

공휴일 데이터 삭제

동일 날짜에 여러 공휴일이 존재하는 경우 처리
(예: 2025-05-05 어린이날 + 부처님오신날)

✅ API Docs

Swagger UI를 통해 전체 API를 확인할 수 있습니다.

http://localhost:8080/swagger-ui.html

✅ How to Run
./gradlew bootRun

✅ 공휴일 조회 API
특정 연도 + 국가 공휴일 조회
GET /api/holidays?year=2025&countryCode=KR

Query Parameters
이름	설명	예시
year	조회 연도	2025
countryCode	국가 코드	KR
✅ Initial Data Load (초기 적재)

특정 국가의 공휴일 데이터를 2020 ~ 2025년까지 일괄 적재합니다.

POST /admin/holidays/initial-load?countryCode=KR


⚠️ 이미 존재하는 데이터가 있어도 UNIQUE 제약 조건을 위반하지 않도록
내부적으로 upsert 방식으로 처리됩니다.

✅ Refresh (재동기화)

특정 연도의 공휴일 데이터를 외부 API 기준으로 다시 동기화합니다.

POST /admin/holidays/refresh?year=2025&countryCode=KR


기존 데이터가 있으면 업데이트

없으면 신규 삽입

API 응답에 없는 날짜는 삭제

✅ Delete (공휴일 삭제)

특정 연도 + 국가의 공휴일 데이터를 DB에서 삭제합니다.

DELETE /admin/holidays?year=2025&countryCode=KR

✅ Notes

동일 날짜에 여러 공휴일이 존재할 수 있음

예: 2025-05-05 (어린이날, 부처님오신날)

이를 위해 공휴일 데이터는 아래 기준으로 UNIQUE 제약을 둠

(country_code + date + name)


초기 적재 및 재동기화 로직은 중복 데이터 삽입으로 인한 오류 없이
안전하게 동작하도록 설계됨

외부 API 장애를 고려한 예외 처리 및 서비스 안정성을 고려하여 구현

# 공휴일 관리 백엔드 과제

Nager.Date 외부 API를 연동하여 국가/연도별 공휴일을 저장·조회·관리하는 백엔드 서비스입니다.  

---

## ✅ Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Web / Spring Data JPA / Validation
- H2 Database (In-Memory)
- Swagger (springdoc-openapi-starter-webmvc-ui)
- Gradle

---

## ✅ 주요 기능 (Features)

- Nager.Date 외부 공휴일 API 연동
- 국가 / 연도별 공휴일 조회
- 공휴일 초기 로드
- 공휴일 재동기화
- 공휴일 삭제
- 동일 날짜에 여러 공휴일 처리
  - 예: 2025-05-05 (어린이날, 부처님오신날)

---

✅ 빌드 & 실행 방법
1. 빌드
./gradlew clean build

2. 실행
./gradlew bootRun


기본 포트: 8080

✅ Swagger / OpenAPI 확인 방법

Swagger UI
http://localhost:8080/swagger-ui/index.html

OpenAPI JSON
http://localhost:8080/v3/api-docs

✅ REST API 명세 요약

1️⃣ 공휴일 초기 적재 (Initial Load)

POST /admin/holidays/initial-load

설명

특정 국가의 공휴일 데이터를 2020 ~ 2025년까지 일괄 적재합니다.

Query Parameters
| 이름 | 타입 | 필수 | 설명 | 예시 |
|-----|------|------|------|------|
| countryCode | String | ✅ | 국가 코드 | KR |

요청 예시
POST /admin/holidays/initial-load?countryCode=KR

2️⃣ 공휴일 조회

GET /api/holidays

설명

국가 + 연도 기준으로 저장된 공휴일을 조회합니다.

| 이름 | 타입 | 필수 | 설명 | 예시 |
|-----|------|------|------|------|
| year | int | ✅ | 조회 연도 | 2025 |
| countryCode | String | ✅ | 국가 코드 | KR |

요청 예시
GET /api/holidays?year=2025&countryCode=KR

응답 예시
[
  {
    "id": 1,
    "countryCode": "KR",
    "date": "2025-05-05",
    "year": 2025,
    "localName": "어린이날",
    "name": "Children's Day",
    "global": true,
    "type": "Public"
  },
  {
    "id": 2,
    "countryCode": "KR",
    "date": "2025-05-05",
    "year": 2025,
    "localName": "부처님 오신 날",
    "name": "Buddha's Birthday",
    "global": true,
    "type": "Public"
  }
]

3️⃣ 공휴일 재동기화 (Refresh)

POST /admin/holidays/refresh

설명

기존 데이터 → update

신규 데이터 → insert

API에 존재하지 않는 데이터 → delete

Query Parameters
| 이름 | 타입 | 필수 | 설명 | 예시 |
|-----|------|------|------|------|
| year | int | ✅ | 대상 연도 | 2025 |
| countryCode | String | ✅ | 국가 코드 | KR |

요청 예시
POST /admin/holidays/refresh?year=2025&countryCode=KR

4️⃣ 공휴일 삭제

DELETE /admin/holidays

설명

특정 연도 + 국가의 공휴일 데이터를 일괄 삭제합니다.

Query Parameters
| 이름 | 타입 | 필수 | 설명 | 예시 |
|-----|------|------|------|------|
| year | int | ✅ | 대상 연도 | 2025 |
| countryCode | String | ✅ | 국가 코드 | KR |

요청 예시
DELETE /admin/holidays?year=2025&countryCode=KR

✅ 테스트 실행
./gradlew clean test

테스트가 성공하면 Gradle 콘솔에 BUILD SUCCESSFUL 로그가 출력됩니다.

테스트 성공 화면
<img width="1322" height="277" alt="스샷2" src="https://github.com/user-attachments/assets/cfa1a68e-d54c-4257-9ff4-61932545c074" />


✅ 설계 및 구현 참고 사항

동일 날짜에 여러 공휴일이 존재할 수 있으므로 다음 UNIQUE 제약을 사용합니다.

(country_code + date + name)


초기 적재 및 재동기화는 Upsert 방식으로 구현되어
중복 데이터로 인한 오류 없이 안전하게 동작합니다.

H2 In-Memory DB 특성상 서버 재기동 시 데이터는 초기화됩니다.

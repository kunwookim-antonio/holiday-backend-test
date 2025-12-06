# 📆 공휴일 관리 API 과제

외부 공휴일 API **Nager.Date**를 활용하여  
국가 및 연도별 공휴일을 조회·적재·재동기화·삭제할 수 있는 백엔드 서비스입니다.

---

## ✅ Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Data JPA
- H2 Database
- Swagger (springdoc-openapi)
- Gradle

---

## ✅ Features

- Nager.Date 외부 API 연동
- 국가 / 연도별 공휴일 **조회**
- 공휴일 **초기 적재 (Initial Load)**
- 공휴일 **재동기화 (Refresh)**
- 공휴일 데이터 **삭제**
- 동일 날짜에 여러 공휴일이 존재하는 경우 처리  
  (예: 2025-05-05 어린이날 + 부처님오신날)

---

## ✅ API Docs

Swagger UI를 통해 전체 API를 확인할 수 있습니다.

http://localhost:8080/swagger-ui.html

yaml
코드 복사

---

## ✅ How to Run

```bash
./gradlew bootRun

✅ 공휴일 조회 API
특정 연도 + 국가 공휴일 조회

✅ Initial Data Load (초기 적재)
특정 국가의 공휴일 데이터를 2020 ~ 2025년까지 일괄 적재합니다.

서버 최초 기동 후 공휴일 기본 데이터 적재 용도

이미 데이터가 존재해도 중복 오류가 발생하지 않도록 설계됨

✅ Refresh (재동기화)
특정 연도의 공휴일 데이터를 외부 API 기준으로 다시 동기화합니다.

기존 데이터가 있으면 update

없으면 insert

API 응답에 없어진 날짜는 delete

✅ Delete (공휴일 삭제)
특정 연도 + 국가의 공휴일 데이터를 DB에서 삭제합니다.

✅ Notes (설계 설명)
동일 날짜에 여러 공휴일이 존재할 수 있음

예: 2025-05-05 (어린이날, 부처님오신날)

이를 고려하여 공휴일 데이터는 다음 기준으로 UNIQUE 제약 조건을 가짐
(country_code + date + name)

초기 적재(Initial Load)와 재동기화(Refresh)는
모두 upsert 방식으로 동작하도록 구현

H2 인메모리 DB 사용으로 서버 재기동 시 데이터 초기화됨

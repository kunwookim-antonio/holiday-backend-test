package com.kunwoo.holiday_test.domain.holiday;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "holiday",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_holiday_country_date_name",
                        columnNames = {"country_code", "date", "name"}
                )
        }
)
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", nullable = false, length = 10)
    private String countryCode;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "holiday_year", nullable = false)
    private Integer year;  // year를 쓰되, 컬럼명만 holiday_year로 회피

    @Column(name = "local_name", nullable = false, length = 200)
    private String localName;

    @Column(nullable = false, length = 200)
    private String name;

    private Boolean global;

    @Column(length = 50)
    private String type;

    protected Holiday() {}

    public Holiday(String countryCode,
                   LocalDate date,
                   Integer year,
                   String localName,
                   String name,
                   Boolean global,
                   String type) {
        this.countryCode = countryCode;
        this.date = date;
        this.year = year;
        this.localName = localName;
        this.name = name;
        this.global = global;
        this.type = type;
    }

    // --- 업데이트용 메서드 ---
    public void updateFromApiResponse(String countryCode,
                                      Integer year,
                                      String localName,
                                      String name,
                                      Boolean global,
                                      String type) {
        this.countryCode = countryCode;
        this.year = year;
        this.localName = localName;
        this.name = name;
        this.global = global;
        this.type = type;
    }

    // --- getter ---
    public Long getId() { return id; }
    public String getCountryCode() { return countryCode; }
    public LocalDate getDate() { return date; }
    public Integer getYear() { return year; }
    public String getLocalName() { return localName; }
    public String getName() { return name; }
    public Boolean getGlobal() { return global; }
    public String getType() { return type; }
}
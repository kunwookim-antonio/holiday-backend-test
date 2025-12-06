package com.kunwoo.holiday_test.domain.country;

import jakarta.persistence.*;

@Entity
@Table(name = "country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String code;   // ex) "KR"

    @Column(nullable = false, length = 100)
    private String name;   // ex) "Korea, Republic of"

    protected Country() {} // JPA 기본 생성자

    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
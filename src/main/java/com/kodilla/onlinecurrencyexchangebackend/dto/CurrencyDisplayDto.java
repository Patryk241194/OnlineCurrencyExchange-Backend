package com.kodilla.onlinecurrencyexchangebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CurrencyDisplayDto {
    private Long id;
    private String code;
    private String name;

}

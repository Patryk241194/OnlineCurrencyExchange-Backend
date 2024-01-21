package com.kodilla.onlinecurrencyexchangebackend.dto.nbp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RateDto {

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("code")
    private String code;

    @JsonProperty("mid")
    private BigDecimal averageRate;

    @JsonProperty("bid")
    private BigDecimal buyingRate;

    @JsonProperty("ask")
    private BigDecimal sellingRate;
}

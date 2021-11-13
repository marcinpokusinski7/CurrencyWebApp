package com.compo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {
    private Integer currencyId;
    private String currencyName;
    private String currencyCode;
    private double currencyRate;
    private String currencyDate;
}

package com.compo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tb_currency", schema = "currencies")
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="currency_id", unique = true, nullable = false)
    private Integer currencyId;
    @Column(nullable = false)
    private String currencyName;
    @Column(nullable = false)
    private String currencyCode;
    @Column(nullable = false)
    private double currencyRate;
    @Column(nullable = false)
    private String currencyDate;
}

package com.ktscrap.model

import javax.persistence.*

@Entity
@Table(name = "tb_stock_date", schema = "currencies")
open class StockDate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    open var id: Long = 0,

    @Column(nullable = false)
    open var read_date: String = "",

    @Column(nullable = false)
    open var day_of_week: String = "",

    @Column(nullable = false)
    open var isHoliday: Boolean = false,

    @Column(nullable = false)
    open var isGPWOpen: Boolean = true,


    )
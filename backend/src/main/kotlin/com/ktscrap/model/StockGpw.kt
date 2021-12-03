package com.ktscrap.model

import javax.persistence.*

@Entity
@Table(name = "tb_gpw_stocks", schema = "currencies")
class StockGpw(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id", unique = true, nullable = false)
    var id: Long = 0,

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var rate: String = "",

    @Column(nullable = false)
    var change: String = "",

    @Column(nullable = false)
    var minRate: String = "",

    @Column(nullable = false)
    var maxRate: String = "",

    @Column(nullable = false)
    var volumen: String = "",

    @Column(nullable = false)
    var quantityTransaction: String = "",

    @Column(nullable = false)
    var stockDate_id: Int = 0
)
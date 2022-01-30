package com.ktscrap.model

import javax.persistence.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Entity
@Table(name = "tb_gpw_stock", schema = "currencies")
class StockGpw(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id", unique = true, nullable = false)
    var stock_id: Long = 0,

    @Column(nullable = false)
    var stock_name: String = "",

    @Column(nullable = false)
    var stock_rate: String = "",

    @Column(nullable = false)
    var stock_change: String = "",

    @Column(nullable = false)
    var stock_min: String = "",

    @Column(nullable = false)
    var stock_max: String = "",

    @Column(nullable = false)
    var stock_volume: String = "",

    @Column(nullable = false)
    var stock_quantity: String = "",

    //TODO date id do dodania
    @ManyToOne(cascade = [CascadeType.ALL])
    var date_id: StockDate = StockDate(),
    )
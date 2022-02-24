package com.ktscrap.model

import javax.persistence.*

@Entity
@Table(name = "tb_stock_date", schema = "currencies")
open class StockDate (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @JvmField open var id: Long = 0,

    @Column(nullable = false)
    @JvmField open var read_date: String = "",

    @OneToMany(targetEntity = StockGpw::class, cascade = [CascadeType.ALL])
    @JoinColumn(name ="stock_id")
    @JvmField open var listOfStocks: List<StockGpw> = ArrayList()
)
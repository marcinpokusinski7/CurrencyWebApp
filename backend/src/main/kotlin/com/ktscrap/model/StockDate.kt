package com.ktscrap.model

import java.time.DayOfWeek
import javax.persistence.*

@Entity
@Table(name = "tb_stock_date", schema = "currencies")
open class StockDate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @JvmField open var id: Long = 0,

    @Column(nullable = false)
    @JvmField open var read_date: String = "",

    @Column(nullable = false)
    @JvmField open var dayOfWeek: DayOfWeek? = null,

    @Column(nullable = false)
    @JvmField open var isHoliday: Boolean = false

)
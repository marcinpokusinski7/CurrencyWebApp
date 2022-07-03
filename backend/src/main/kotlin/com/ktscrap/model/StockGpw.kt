package com.ktscrap.model

import lombok.Builder
import javax.persistence.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

//JvmField is added because hibernate throwing error that
//getters/setter cannot be final, and open is not enough in this situation

@Entity
@Table(name = "tb_gpw_stock", schema = "currencies")
open class StockGpw(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id", unique = true, nullable = false)
    open var stock_id: Long = 0,

    @Column(nullable = false)
    open var stock_name: String = "",

    @Column(nullable = false)
    open var stock_rate: String = "",

    @Column(nullable = false)
    open var stock_change: String = "",

    @Column(nullable = false)
    open var stock_min: String = "",

    @Column(nullable = false)
    open var stock_max: String = "",

    @Column(nullable = false)
    open var stock_volume: String = "",

    @Column(nullable = false)
    open var stock_quantity: String = "",

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    @JvmField var stockDate: StockDate? = null
    )
package com.ktscrap.model

import javax.persistence.*

@Entity
@Table(name = "tb_stock_date", schema = "currencies")
class StockDate (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Long = 0,

    @Column(nullable = false)
    var read_Date: String = "",

    @OneToMany(cascade = [CascadeType.ALL],
    orphanRemoval = true)
    @JoinColumn(name = "date_id")
    var dateCollection: MutableList<StockGpw> = ArrayList()
)
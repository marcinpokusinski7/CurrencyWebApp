package com.ktscrap.model

import javax.persistence.*

@Entity
@Table(name = "tb_stockDate", schema = "currencies")
class StockDate (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Long = 0,

    @Column(nullable = false)
    var readDate: String = "",

    @OneToMany(cascade = [CascadeType.ALL],
    orphanRemoval = true)
    @JoinColumn(name = "date_id")
    var dateCollection: MutableList<StockGpw> = ArrayList()
)
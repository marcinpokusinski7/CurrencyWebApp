package com.ktscrap

import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import kotlin.concurrent.timerTask


class StockScrap {

    fun repeate() {
        Timer().schedule(timerTask {
            conn()
        },TimeUnit.SECONDS.convert(10, TimeUnit.SECONDS),86400000)
    }

    private fun conn() {
        val url = "x"
        val filterOutput = "Obserwuj spółkę"
        val doc = Jsoup.connect("$url").get()
        for (row in doc.select(".sortTable:first-of-type tr")) {
            if (row.select("td:nth-of-type(1)").equals(String().isBlank())) {
                continue
            } else {
                val stock = Stock()
                row.select("td:first-of-type a")
                    .map { col -> col.attr("title") }
                    .parallelStream()
                    .filter { it != filterOutput }
                    .forEach { stock.name = it }
                stock.rate = row.select("td:nth-of-type(2)")
                    .text()
                stock.change = row.select("td:nth-of-type(3)")
                    .text()
                stock.quantityTransaction = row.select("td:nth-of-type(5)")
                    .text()
                stock.volumen = row.select("td:nth-of-type(6)")
                    .text()
                stock.minRate = row.select("td:nth-of-type(9)")
                    .text()
                stock.maxRate = row.select("td:nth-of-type(8)")
                    .text()
                if (stock.name.isNullOrBlank()) {
                    continue
                } else {
                    println(
                        "Nazwa:${stock.name} Kurs:${stock.rate} Zmiana:${stock.change}".replace(",", ".") +
                                "  Ilość transakcji:${stock.quantityTransaction}  Wolumen:${stock.volumen}".replace(",", ".") +
                                "  Min:${stock.minRate} Max:${stock.maxRate}".replace(",", ".")
                    )
                }
            }
        }
    }
}


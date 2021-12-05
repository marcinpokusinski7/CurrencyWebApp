package com.ktscrap.Jobs

import com.ktscrap.dto.StockDateDto
import com.ktscrap.dto.StockGpwDto
import com.ktscrap.model.StockDate
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import java.lang.module.Configuration
import java.util.*
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import kotlin.concurrent.timerTask


@Component
class StockScrap {

 /*   @PostConstruct
    fun repeat() {
        Timer().schedule(timerTask {
            conn()
        },TimeUnit.SECONDS.convert(10, TimeUnit.SECONDS),10000)
    }*/
    @PostConstruct
    private fun conn() {
        val url = "https://www.bankier.pl/gielda/notowania/akcje"
        val filterOutput = "Obserwuj spółkę"
        val doc = Jsoup.connect("$url").get()
        for (row in doc.select(".sortTable:first-of-type tr")) {
            if (row.select("td:nth-of-type(1)").equals(String().isBlank())) {
                continue
            } else {
                val stockGpwDto = StockGpwDto()
                val stockDate = StockDateDto()
                row.select("td:first-of-type a")
                    .map { col -> col.attr("title") }
                    .parallelStream()
                    .filter { it != filterOutput }
                    .forEach { stockGpwDto.name = it }
                stockGpwDto.rate = row.select("td:nth-of-type(2)")
                    .text()
                stockGpwDto.change = row.select("td:nth-of-type(3)")
                    .text()
                stockGpwDto.quantityTransaction = row.select("td:nth-of-type(5)")
                    .text()
                stockGpwDto.volumen = row.select("td:nth-of-type(6)")
                    .text()
                stockGpwDto.minRate = row.select("td:nth-of-type(9)")
                    .text()
                stockGpwDto.maxRate = row.select("td:nth-of-type(8)")
                    .text()
                stockDate.readDate = row.select("td:nth-of-type(10)")
                    .text()
                if (stockGpwDto.name.isNullOrBlank()) {
                    continue
                } else {
                    //TODO add method with db save// add another method to read date just once
                   var session = dbConn.transaction().currentSession
                    println("Start new transaction: ${stockDate.readDate}")
                    val stockDate = StockDate()
                    session.beginTransaction()
                    session.close()
                }
            }
        }
    }
}


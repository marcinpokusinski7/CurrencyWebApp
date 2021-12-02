package com.ktscrap.Jobs

import com.ktscrap.dto.StockGpwDto
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import kotlin.concurrent.timerTask


@Component
class StockScrap {

    @PostConstruct
    fun repeat() {
        Timer().schedule(timerTask {
            conn()
        },TimeUnit.SECONDS.convert(10, TimeUnit.SECONDS),10000)
    }

    private fun conn() {
        val url = "https://www.bankier.pl/gielda/notowania/akcje"
        val filterOutput = "Obserwuj spółkę"
        val doc = Jsoup.connect("$url").get()
        for (row in doc.select(".sortTable:first-of-type tr")) {
            if (row.select("td:nth-of-type(1)").equals(String().isBlank())) {
                continue
            } else {
                val stockGpwDto = StockGpwDto()
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
                if (stockGpwDto.name.isNullOrBlank()) {
                    continue
                } else {
                    println(
                        "Nazwa:${stockGpwDto.name} Kurs:${stockGpwDto.rate} Zmiana:${stockGpwDto.change}".replace(",", ".") +
                                "  Ilość transakcji:${stockGpwDto.quantityTransaction}  Wolumen:${stockGpwDto.volumen}".replace(",", ".") +
                                "  Min:${stockGpwDto.minRate} Max:${stockGpwDto.maxRate}".replace(",", ".")
                    )
                }
            }
        }
    }
}


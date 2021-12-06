package com.ktscrap.Jobs

import com.ktscrap.model.StockDate
import com.ktscrap.model.StockGpw
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.annotation.PostConstruct


@Component
class StockScrap {
    private val session = dbConn.transaction().openSession()

    /*   @PostConstruct
    fun repeat() {
        Timer().schedule(timerTask {
            conn()
        },TimeUnit.SECONDS.convert(10, TimeUnit.SECONDS),10000)
    }*/
    @PostConstruct
    private fun conn() {
        val session = dbConn.transaction().openSession()
        val url = "https://www.bankier.pl/gielda/notowania/akcje"
        val filterOutput = "Obserwuj spółkę"
        val doc = Jsoup.connect("$url").get()
        for (row in doc.select(".sortTable:first-of-type tr")) {
            if (row.select("td:nth-of-type(1)").equals(String().isBlank())) {
                continue
            } else {
                val stockGpw = StockGpw()
                row.select("td:first-of-type a")
                    .map { col -> col.attr("title") }
                    .parallelStream()
                    .filter { it != filterOutput }
                    .forEach { stockGpw.name = it }
                stockGpw.rate = row.select("td:nth-of-type(2)")
                    .text()
                stockGpw.change = row.select("td:nth-of-type(3)")
                    .text()
                stockGpw.quantityTransaction = row.select("td:nth-of-type(5)")
                    .text()
                stockGpw.volumen = row.select("td:nth-of-type(6)")
                    .text()
                stockGpw.minRate = row.select("td:nth-of-type(9)")
                    .text()
                stockGpw.maxRate = row.select("td:nth-of-type(8)")
                    .text()
                if (stockGpw.name.isNullOrBlank()) {
                    continue
                } else {
                    itemsToDb(stockGpw, session)
                }
            }
        }
    }

    private fun itemsToDb(stockGpw: StockGpw, session: org.hibernate.Session) {
        val itemDate = StockDate()
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        itemDate.readDate = currentDate.format(formatter)
        addStocksToDb(stockGpw, itemDate, session)
    }

    private fun addStocksToDb(stockGpw: StockGpw, stockDate: StockDate, session: org.hibernate.Session) {
        try {
            val datefromdb = session.createNamedQuery("SELECT * FROM tb_stockdate ORDER BY ID DESC LIMIT 1")
            if (datefromdb.toString().isNotEmpty() && !stockDate.equals(datefromdb)) {
                session.save(stockDate)
                session.save(stockGpw)
            }

            println("Name:${stockGpw.name} and ${stockDate.readDate} ")
        } finally {
            session.close()
        }
    }
}


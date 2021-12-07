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
                    .forEach { stockGpw.stock_name = it }
                stockGpw.stock_rate = row.select("td:nth-of-type(2)")
                    .text()
                stockGpw.stock_change = row.select("td:nth-of-type(3)")
                    .text()
                stockGpw.stock_quantity = row.select("td:nth-of-type(5)")
                    .text()
                stockGpw.stock_volume = row.select("td:nth-of-type(6)")
                    .text()
                stockGpw.stock_min = row.select("td:nth-of-type(9)")
                    .text()
                stockGpw.stock_max = row.select("td:nth-of-type(8)")
                    .text()
                if (stockGpw.stock_name.isNullOrBlank()) {
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
        itemDate.read_Date = currentDate.format(formatter)
        addStocksToDb(stockGpw, itemDate, session)
    }

    private fun addStocksToDb(stockGpw: StockGpw, stockDate: StockDate, session: org.hibernate.Session) {
        val createQuery = "SELECT read_date FROM currencies.tb_stockdate ORDER BY ID DESC LIMIT 1"
        val query = session.createQuery(createQuery)
        try {
           val queryResultDate = query.queryString
            if (queryResultDate.toString().isNotEmpty() && !stockDate.equals(queryResultDate)) {
                session.save(queryResultDate)
                session.save(stockGpw)
            }else{
                println("Name:${stockGpw.stock_name} and ${stockDate.read_Date} ")
            }
        } finally {
            session.close()
        }
    }
}


package com.ktscrap.Jobs

import com.ktscrap.model.StockDate
import com.ktscrap.model.StockGpw
import org.hibernate.Session
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.annotation.PostConstruct


@Component
class StockScrap {
    private val session = dbConn.transaction().openSession()
    private val logger = LoggerFactory.getLogger(javaClass)
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
        val stockListContainer: ArrayList<StockGpw> = ArrayList()
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
                    addDateToStockRecord(stockGpw, session)
                    stockListContainer.add(stockGpw)
                }
            }
        }
        addStocksToDb(stockListContainer, session)
        session.close()
    }

    //TODO fix query reading from db to read latest id
    private fun addDateToStockRecord(stockGpw: StockGpw, session: org.hibernate.Session): StockGpw {
        val getDateOfLastRecord = "SELECT read_date FROM StockDate where id = '0' ORDER BY id DESC"
        val getDate = "SELECT read_date FROM StockDate ORDER BY id DESC"
        val query = session.createQuery(getDateOfLastRecord)
        val stockDate = StockDate()
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        stockDate.read_date = currentDate.format(formatter)
        val queryResultDate = query.singleResult.toString()

        //TODO fix query to retrieve an object it is possible to do an converter of stockdate by properties
        if (queryResultDate.isNotEmpty() && !stockDate.read_date.equals(queryResultDate) && queryResultDate.isNotEmpty()) {
            stockGpw.stockDate = stockDate
        } else if(stockDate.read_date.equals(queryResultDate)){
            val stockDateItem: StockDate
            val queryResultWithLatestDate = session.createQuery(getDate)
            stockDateItem = queryResultWithLatestDate.singleResult as StockDate
            if(stockDateItem.read_date.isNotEmpty()){
                stockGpw.stockDate = stockDateItem
            }
        } else{
            logger.info("Problem with retrieving data in StockScrap date find")
        }
        return stockGpw
    }

    private fun addStocksToDb(stockGpwCollection: ArrayList<StockGpw>, session: Session) {
        if(stockGpwCollection.isNotEmpty().or(false)){
            session.use { session ->
                for(stock in stockGpwCollection)
                {
                    session.save(stock)
                }
            }
        }
    }

}


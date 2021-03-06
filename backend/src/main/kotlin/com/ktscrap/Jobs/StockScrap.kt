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
import javax.persistence.NoResultException


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
        prepareDateRecord(session)
        //TODO maybe isolate to another method
        for (row in doc.select(".sortTable:first-of-type tr")) {
            if (row.select("td:nth-of-type(1)").equals(String().isBlank())) {
                continue
            } else {
                var stockGpw = StockGpw()
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
                    stockGpw = prepareStockRecord(stockGpw, session)
                    stockListContainer.add(stockGpw)

                }
            }
        }
        addStocksToDb(stockListContainer, session)
        session.close()
    }


    private fun prepareStockRecord(stockGpw: StockGpw, session: Session): StockGpw {
        val getLastDate =
            "SELECT id FROM StockDate ORDER BY id DESC"

        val query = session.createQuery(getLastDate)
        query.maxResults = 1
        val queryResultDate =
            session.get(StockDate::class.java, query.singleResult as Long) as StockDate//query.singleResult as StockDate
        val stockDate = LocalDateTime.now()
        val formatted = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedStockDate = stockDate.format(formatted)

        if (queryResultDate.read_date.isNotEmpty()) {
            if (queryResultDate.read_date == formattedStockDate) {
                stockGpw.stockDate = queryResultDate
            } else {
                stockGpw.stockDate?.read_date = formattedStockDate
            }
        } else {
            logger.info("Problem with retrieving data in StockScrap date find")
        }
        return stockGpw
    }

    private fun addStocksToDb(stockGpwCollection: ArrayList<StockGpw>, session: Session) {
        if (stockGpwCollection.isNotEmpty().or(false)) {
            session.use { session ->
                for (stock in stockGpwCollection) {
                    session.save(stock)
                }
            }
        }
    }

    //TODO add day of week, add if holiday and what holiday
    //TODO additionally check if try catch works properly after deleting records in db and inserting again
    private fun prepareDateRecord(session: Session) {
        val getLastDate =
            "SELECT read_date FROM StockDate ORDER BY id DESC"

        val query = session.createQuery(getLastDate)
        val queryResultDate: String
        query.maxResults = 1
        val stockDate = StockDate()
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        stockDate.read_date = currentDate.format(formatter)
        stockDate.day_of_week = currentDate.dayOfWeek.toString() //if error probably ccheck stockdate to string
        stockDate.isHoliday = checkIsWeekend(currentDate)
        //TODO refactor of method because is too big
        try {
            queryResultDate = query.singleResult.toString()
            if (queryResultDate.isNotEmpty() && stockDate.read_date != queryResultDate) {
                addDateToDb(stockDate, session)
            } else {
                logger.info("Problem with retrieving data or date already exists")
            }
        } catch (e: NoResultException) {
            logger.info("Query result is empty, no records found")
            if (stockDate.read_date.isNotEmpty()) {
                addDateToDb(stockDate, session)
            }
        }
    }

    //TODO Check if enum with holidays is there
    private fun checkIsWeekend(currDate: LocalDateTime): Boolean {
        if (currDate.dayOfWeek.toString() == "SUNDAY"
            || currDate.dayOfWeek.toString() == "SATURDAY"
        ) {
            return true
        }
        return false
    }

    //https://www.gpw.pl/szczegoly-sesji/#bez-sesji get dates from site, and create enums of month
    private fun checkIsGPWOpen(currDate: LocalDateTime, stockDate: StockDate): Boolean {
        if (checkIsWeekend(LocalDateTime.now())) {
            stockDate.isGPWOpen = false
        } else {//if gpw is not open then return gpw open false
            when (currDate.dayOfYear) {
                6 -> stockDate.isGPWOpen = true

            }
        }
        return stockDate.isGPWOpen
    }

    private fun addDateToDb(stockDate: StockDate, session: Session) {
        if (stockDate.read_date.isNotEmpty()) {
            session.use { session ->
                session.save(stockDate)
            }
        }
    }


}


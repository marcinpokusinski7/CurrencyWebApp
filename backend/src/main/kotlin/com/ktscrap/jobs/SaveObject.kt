package com.ktscrap.jobs

import com.ktscrap.model.StockDate
import com.ktscrap.model.StockGpw
import org.hibernate.Session

class SaveObject {

    fun addDateToDb(stockDate: StockDate, session: Session) {
        if (stockDate.read_date.isNotEmpty()) {
            session.use { session ->
                session.save(stockDate)
            }
        }
    }

    fun addStocksToDb(stockGpwCollection: ArrayList<StockGpw>, session: Session) {
        if (stockGpwCollection.isNotEmpty().or(false)) {
            session.use { session ->
                for (stock in stockGpwCollection) {
                    session.save(stock)
                }
            }
        }
    }
}
package com.ktscrap.Jobs

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

object dbConn {
    fun transaction(): SessionFactory {
        return Configuration()
            .configure("hibernate.cfg.xml")
            .buildSessionFactory()
    }
}

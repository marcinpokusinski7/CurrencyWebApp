package com.ktscrap.Jobs

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.springframework.stereotype.Component
import javax.transaction.Transactional

object dbConn {
    fun transaction(): SessionFactory {
        return Configuration()
            .configure("hibernate.cfg.xml")
            .buildSessionFactory()
    }
}

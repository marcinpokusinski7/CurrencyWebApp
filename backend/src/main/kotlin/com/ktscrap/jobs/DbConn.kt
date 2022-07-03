package com.ktscrap.jobs

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

object DbConn {
    fun transaction(): SessionFactory {
        return Configuration()
            .configure("hibernate.cfg.xml")
            .buildSessionFactory()
    }
}

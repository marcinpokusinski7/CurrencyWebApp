package com.ktscrap.repository

import com.ktscrap.model.StockDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
interface GpwDateRepository: JpaRepository<StockDate,Long> {
}
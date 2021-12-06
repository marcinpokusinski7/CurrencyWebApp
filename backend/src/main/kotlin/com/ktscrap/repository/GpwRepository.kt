package com.ktscrap.repository

import com.ktscrap.model.StockGpw
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
interface GpwRepository  : JpaRepository<StockGpw, Long>{
}
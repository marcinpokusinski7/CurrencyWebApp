package com.ktscrap.repository

import com.ktscrap.model.StockGpw
import org.springframework.data.jpa.repository.JpaRepository

interface GpwRepository  : JpaRepository<StockGpw, Long>{
}
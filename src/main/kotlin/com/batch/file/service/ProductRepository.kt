package com.batch.file.service

import com.batch.file.core.domain.product.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductRepository: JpaRepository<com.batch.file.core.domain.product.Product, Long> {

    @Query("select distinct(p.type) from Product p")
    fun findDistinctTypes(): List<String>
}
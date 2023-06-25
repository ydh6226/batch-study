package com.batch.file.core.chunk.processor

import com.batch.file.core.domain.product.Product
import com.batch.file.core.domain.product.ProductDto
import mu.KotlinLogging
import org.springframework.batch.item.ItemProcessor

class FileItemProcessor: ItemProcessor<com.batch.file.core.domain.product.ProductDto, com.batch.file.core.domain.product.Product> {

    private val logger = KotlinLogging.logger {}

    override fun process(dto: com.batch.file.core.domain.product.ProductDto): com.batch.file.core.domain.product.Product {
        logger.info { "${dto} 변환" }

        return com.batch.file.core.domain.product.Product(dto.name!!, dto.price!!, dto.type!!)
    }
}
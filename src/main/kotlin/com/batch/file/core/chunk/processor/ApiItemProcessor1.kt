package com.batch.file.core.chunk.processor

import com.batch.file.core.domain.product.ApiRequest
import com.batch.file.core.domain.product.Product
import org.springframework.batch.item.ItemProcessor

class ApiItemProcessor1: ItemProcessor<com.batch.file.core.domain.product.Product, com.batch.file.core.domain.product.ApiRequest> {
    override fun process(item: com.batch.file.core.domain.product.Product): com.batch.file.core.domain.product.ApiRequest? {
        return com.batch.file.core.domain.product.ApiRequest(
            id = item.id!!,
            productDto = item
        )
    }
}
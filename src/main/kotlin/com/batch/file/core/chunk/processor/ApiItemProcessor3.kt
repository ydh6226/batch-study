package com.batch.file.core.chunk.processor

import com.batch.file.core.domain.product.ApiRequest
import com.batch.file.core.domain.product.Product
import org.springframework.batch.item.ItemProcessor

class ApiItemProcessor3: ItemProcessor<Product, ApiRequest> {
    override fun process(item: Product): ApiRequest {
        return ApiRequest(
            id = item.id,
            productDto = item
        )
    }
}
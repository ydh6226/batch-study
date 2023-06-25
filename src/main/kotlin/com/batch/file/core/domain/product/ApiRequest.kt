package com.batch.file.core.domain.product

data class ApiRequest(
    val id: Long,
    val productDto: com.batch.file.core.domain.product.Product,
) {
}
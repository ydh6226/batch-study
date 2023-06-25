package com.batch.file.core.domain.product

data class ProductDto(
    var id: Long? = null,
    var name: String? = null,
    var price: Int? = null,
    var type: String? = null,
)
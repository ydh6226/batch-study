package com.batch.file.service

import com.batch.file.core.domain.product.ApiRequest

sealed class ApiService {

    abstract fun call(items: MutableList<out com.batch.file.core.domain.product.ApiRequest>)
}
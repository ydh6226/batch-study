package com.batch.file.service

import com.batch.file.core.domain.product.ApiRequest
import mu.KotlinLogging

class ApiService2: ApiService() {

    private val logger = KotlinLogging.logger {}

    override fun call(items: MutableList<out com.batch.file.core.domain.product.ApiRequest>) {
        logger.info { "call ApiService2: ${items}" }
    }

}
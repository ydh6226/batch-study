package com.batch.file.core.chunk.writer

import com.batch.file.core.domain.product.ApiRequest
import com.batch.file.service.ApiService
import mu.KotlinLogging
import org.springframework.batch.item.ItemWriter

class ApiItemWriter3(
    private val apiService: ApiService,
): ItemWriter<ApiRequest> {

    private val logger = KotlinLogging.logger {}

    override fun write(items: MutableList<out ApiRequest>) {
        logger.info { "ApiItemWriter3 write method call" }

        apiService.call(items)
    }
}
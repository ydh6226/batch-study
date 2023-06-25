package com.batch.file.core.classifier

import com.batch.file.core.domain.product.ApiRequest
import mu.KotlinLogging
import org.springframework.classify.Classifier

class WriterClassifier<C, T>(
    private val writerMap: Map<String, T>
): Classifier<C, T> {

    private val logger = KotlinLogging.logger {}

    override fun classify(apiRequest: C): T {
        logger.info { "WriterClassifier: ${apiRequest}" }

        val type = (apiRequest as com.batch.file.core.domain.product.ApiRequest).productDto.type
        return writerMap[type]!!
    }
}
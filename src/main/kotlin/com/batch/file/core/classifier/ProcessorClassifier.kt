package com.batch.file.core.classifier

import com.batch.file.core.domain.product.ApiRequest
import com.batch.file.core.domain.product.Product
import mu.KotlinLogging
import org.springframework.batch.item.ItemProcessor
import org.springframework.classify.Classifier

class ProcessorClassifier<C, T>(
    private val processorMap: Map<String, T>
): Classifier<C, T> {

    private val logger = KotlinLogging.logger {}

    override fun classify(product: C): T {
        logger.info { "ProcessorClassifier: ${product}" }

        val type = (product as com.batch.file.core.domain.product.Product).type
        return processorMap[type]!!
    }
}
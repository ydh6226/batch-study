package com.batch.file.core.classifier

import com.batch.file.core.domain.product.Product
import mu.KotlinLogging
import org.springframework.classify.Classifier

class ProcessorClassifier<C, T>(
    private val processorMap: Map<String, T>
): Classifier<C, T> {

    private val logger = KotlinLogging.logger {}

    override fun classify(product: C): T {
        logger.info { "ProcessorClassifier: ${product}" }

        val type = (product as Product).type
        return processorMap[type]!!
    }
}
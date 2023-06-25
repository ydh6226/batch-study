package com.batch.file.core.partition

import com.batch.file.service.ProductRepository
import mu.KotlinLogging
import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.item.ExecutionContext
import org.springframework.stereotype.Component

@Component
class ProductPartitioner(
    private val productRepository: ProductRepository
) : Partitioner {

    private val logger = KotlinLogging.logger {}

    override fun partition(gridSize: Int): MutableMap<String, ExecutionContext> {

        val types = productRepository.findDistinctTypes()

        logger.info { "ProductPartitioner.partition :: gridSize:${gridSize} :: type: ${types}" }

        val result = mutableMapOf<String, ExecutionContext>()
        types.forEachIndexed { index, type ->
            val executionContext = ExecutionContext().also {
                it.put("product", type)
            }
            result["partition${index}"] = executionContext
        }

        return result
    }

}
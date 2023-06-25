package com.batch.file.core.job.api

import com.batch.file.core.chunk.processor.ApiItemProcessor1
import com.batch.file.core.chunk.processor.ApiItemProcessor2
import com.batch.file.core.chunk.processor.ApiItemProcessor3
import com.batch.file.core.chunk.writer.ApiItemWriter1
import com.batch.file.core.chunk.writer.ApiItemWriter2
import com.batch.file.core.chunk.writer.ApiItemWriter3
import com.batch.file.core.classifier.ProcessorClassifier
import com.batch.file.core.classifier.WriterClassifier
import com.batch.file.core.domain.product.ApiRequest
import com.batch.file.core.domain.product.Product
import com.batch.core.partition.ProductPartitioner
import com.batch.service.ApiService1
import com.batch.service.ApiService2
import com.batch.service.ApiService3
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor
import org.springframework.batch.item.support.ClassifierCompositeItemWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource


@Configuration
class ApiStepConfiguration(
    private val stepBuilderFactory: StepBuilderFactory,
    private val entityManagerFactory: EntityManagerFactory,
    private val productPartitioner: ProductPartitioner,
) {

    @Bean
    fun apiMasterStep(): Step {
        return stepBuilderFactory.get("apiMasterStep")
            .partitioner(apiSlaveStep().name, productPartitioner)
            .step(apiSlaveStep())
            .gridSize(3)
            .taskExecutor(taskExecutor())
            .build()
    }

    @Bean
    fun apiSlaveStep(): Step {
        return stepBuilderFactory.get("apiSlaveStep")
            .chunk<com.batch.file.core.domain.product.Product, com.batch.file.core.domain.product.ApiRequest>(com.batch.file.core.job.api.ApiStepConfiguration.Companion.CHUNK_SIZE)
            .reader(itemReader(null))
            .processor(itemProcessor())
            .writer(itemWriter())
            .build()
    }

    @Bean
    @StepScope
    fun itemReader(
        @Value("#{stepExecutionContext['product']}") type: String?
    ): JpaPagingItemReader<com.batch.file.core.domain.product.Product> {
        return JpaPagingItemReaderBuilder<com.batch.file.core.domain.product.Product>()
            .name("productReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("select p from Product p where p.type = :type")
            .parameterValues(mapOf("type" to type))
            .build()
    }

    @Bean
    fun itemProcessor(): ItemProcessor<in com.batch.file.core.domain.product.Product, out com.batch.file.core.domain.product.ApiRequest> {

        val processorMap = mutableMapOf<String, ItemProcessor<com.batch.file.core.domain.product.Product, com.batch.file.core.domain.product.ApiRequest>>().also {
            it["1"] = com.batch.file.core.chunk.processor.ApiItemProcessor1()
            it["2"] = com.batch.file.core.chunk.processor.ApiItemProcessor2()
            it["3"] = com.batch.file.core.chunk.processor.ApiItemProcessor3()
        }

        val classifier =
            com.batch.file.core.classifier.ProcessorClassifier<com.batch.file.core.domain.product.Product, ItemProcessor<*, out com.batch.file.core.domain.product.ApiRequest>>(
                processorMap
            )
        return ClassifierCompositeItemProcessor<com.batch.file.core.domain.product.Product, com.batch.file.core.domain.product.ApiRequest>().also {
            it.setClassifier(classifier)
        }
    }

    @Bean
    fun itemWriter(): ItemWriter<com.batch.file.core.domain.product.ApiRequest> {
        val writerMap = mutableMapOf<String, ItemWriter<com.batch.file.core.domain.product.ApiRequest>>().also {
            it["1"] = com.batch.file.core.chunk.writer.ApiItemWriter1(ApiService1())
            it["2"] = com.batch.file.core.chunk.writer.ApiItemWriter2(ApiService2())
            it["3"] = com.batch.file.core.chunk.writer.ApiItemWriter3(ApiService3())
        }

        val classifier =
            com.batch.file.core.classifier.WriterClassifier<com.batch.file.core.domain.product.ApiRequest, ItemWriter<in com.batch.file.core.domain.product.ApiRequest>>(
                writerMap
            )
        return ClassifierCompositeItemWriter<com.batch.file.core.domain.product.ApiRequest>().also {
            it.setClassifier(classifier)
        }
    }

    fun taskExecutor(): TaskExecutor {
        return ThreadPoolTaskExecutor().also {
            it.corePoolSize = 3
            it.maxPoolSize = 6
            it.setThreadNamePrefix("api-thread-")
            it.initialize()
        }
    }

    companion object {
        private const val CHUNK_SIZE = 2
    }
}

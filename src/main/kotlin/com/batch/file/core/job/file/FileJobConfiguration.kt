package com.batch.file.core.job.file

import com.batch.file.core.chunk.processor.FileItemProcessor
import com.batch.file.core.domain.product.Product
import com.batch.file.core.domain.product.ProductDto
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import javax.persistence.EntityManagerFactory
import javax.persistence.PersistenceContext

@Configuration
class FileJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val entityMangerFactory: EntityManagerFactory,
) {

    @Bean
    fun fileJob(): Job {
        return jobBuilderFactory.get("fileJob")
            .start(fileStep())
            .build()
    }

    @Bean
    fun fileStep(): Step {
        return stepBuilderFactory.get("fileStep")
            .chunk<com.batch.file.core.domain.product.ProductDto, com.batch.file.core.domain.product.Product>(10)
            .reader(fileItemReader(null))
            .processor(fileItemProcessor())
            .writer(fileItemWriter())
            .build()
    }

    @Bean
    @StepScope
    fun fileItemReader(
        @Value("#{jobParameters['requestDate']}") requestDate: String?,
    ): FlatFileItemReader<com.batch.file.core.domain.product.ProductDto> {
        return FlatFileItemReaderBuilder<com.batch.file.core.domain.product.ProductDto>()
            .name("flatFile")
            .resource(ClassPathResource("product_${requestDate}.csv"))
            .fieldSetMapper(BeanWrapperFieldSetMapper())
            .targetType(com.batch.file.core.domain.product.ProductDto::class.java)
            .linesToSkip(1)
            .delimited()
            .delimiter(",")
            .names("id", "name", "price", "type")
            .build()
    }

    @Bean
    fun fileItemProcessor(): ItemProcessor<com.batch.file.core.domain.product.ProductDto, com.batch.file.core.domain.product.Product> {
        return com.batch.file.core.chunk.processor.FileItemProcessor()
    }

    @Bean
    fun fileItemWriter(): ItemWriter<com.batch.file.core.domain.product.Product> {
        return JpaItemWriterBuilder<com.batch.file.core.domain.product.Product>()
            .entityManagerFactory(entityMangerFactory)
            .usePersist(true)
            .build()
    }
}

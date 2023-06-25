package com.batch.file.core.job.file

import com.batch.file.core.chunk.processor.FileItemProcessor
import com.batch.file.core.domain.product.Product
import com.batch.file.core.domain.product.ProductDto
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
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import javax.persistence.EntityManagerFactory

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
            .chunk<ProductDto, Product>(10)
            .reader(fileItemReader(null))
            .processor(fileItemProcessor())
            .writer(fileItemWriter())
            .build()
    }

    @Bean
    @StepScope
    fun fileItemReader(
        @Value("#{jobParameters['requestDate']}") requestDate: String?,
    ): FlatFileItemReader<ProductDto> {
        return FlatFileItemReaderBuilder<ProductDto>()
            .name("flatFile")
            .resource(ClassPathResource("product_${requestDate}.csv"))
            .fieldSetMapper(BeanWrapperFieldSetMapper())
            .targetType(ProductDto::class.java)
            .linesToSkip(1)
            .delimited()
            .delimiter(",")
            .names("id", "name", "price", "type")
            .build()
    }

    @Bean
    fun fileItemProcessor(): ItemProcessor<ProductDto, Product> {
        return FileItemProcessor()
    }

    @Bean
    fun fileItemWriter(): ItemWriter<Product> {
        return JpaItemWriterBuilder<Product>()
            .entityManagerFactory(entityMangerFactory)
            .usePersist(true)
            .build()
    }
}

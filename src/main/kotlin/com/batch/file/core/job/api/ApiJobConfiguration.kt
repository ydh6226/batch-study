package com.batch.file.core.job.api

import com.batch.file.core.listener.JobListener
import com.batch.file.core.tasklet.ApiEndTasklet
import com.batch.file.core.tasklet.ApiStartTasklet
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApiJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val jobStep: Step,
) {

    @Bean
    fun apiJob(): Job {
        return jobBuilderFactory.get("apiJob")
            .listener(JobListener())
            .start(apiStep1())
            .next(jobStep)
            .next(apiStep2())
            .build()
    }

    @Bean
    fun apiStep1(): Step {
        return stepBuilderFactory.get("apiStep1")
            .tasklet(ApiStartTasklet())
            .build()
    }

    @Bean
    fun apiStep2(): Step {
        return stepBuilderFactory.get("apiStep2")
            .tasklet(ApiEndTasklet())
            .build()
    }
}
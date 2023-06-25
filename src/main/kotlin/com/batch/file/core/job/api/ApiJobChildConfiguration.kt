package com.batch.file.core.job.api

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApiJobChildConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val apiMasterStep: Step,
    private val jobLauncher: JobLauncher,
) {

    @Bean
    fun jobStep(): Step {
        return stepBuilderFactory.get("jobStep")
            .job(childJob())
            .launcher(jobLauncher)
            .build()
    }

    @Bean
    fun childJob(): Job {
        return jobBuilderFactory.get("childJob")
            .start(apiMasterStep)
            .build()
    }
}
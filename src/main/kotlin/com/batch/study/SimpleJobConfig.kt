package com.batch.study

import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.IllegalStateException

@Configuration
class SimpleJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
) {

    private val logger = KotlinLogging.logger {}

    @Bean
    fun simpleJob(): Job {
        return jobBuilderFactory.get("simpleJob")
            .start(simpleStep1(null))
            .next(simpleStep2(null))
            .build()
    }

    @Bean
    @JobScope
    fun simpleStep1(
        @Value("#{jobParameters[requestDate]}") requestDate: String?,
    ): Step {
        return stepBuilderFactory.get("simpleStep1")
            .tasklet { _, _ ->
//                throw IllegalStateException("step1에서 실패했습니다!")
                logger.info { ">> this is step1 :: requestDate: ${requestDate}" }
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    @JobScope
    fun simpleStep2(
        @Value("#{jobParameters[requestDate]}") requestDate: String?,
    ): Step {
        return stepBuilderFactory.get("simpleStep2")
            .tasklet { _, _ ->
                logger.info { ">> this is step2 :: requestDate: ${requestDate}" }
                RepeatStatus.FINISHED
            }
            .build()
    }


}
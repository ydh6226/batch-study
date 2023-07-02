package com.batch.study

import mu.KotlinLogging
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StepNextConditionalJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
) {

    private val logger = KotlinLogging.logger {}

    @Bean
    fun stepNextConditionalJob(): Job {
        return jobBuilderFactory.get("stepNextConditionalJob")
            .start(conditionalJobStep1())
                .on("FAILED")
                .to(conditionalJobStep3())
                .on("*")
                .end()
            .from(conditionalJobStep1())
                .on("*")
                .to(conditionalJobStep2())
                .next(conditionalJobStep3())
                .on("*")
                .end()
            .end()
            .build()
    }

    @Bean
    fun conditionalJobStep1(): Step {
        return stepBuilderFactory.get("step1")
            .tasklet { contribution, _ ->
                logger.info { ">> step1" }

//                contribution.exitStatus = ExitStatus.FAILED
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun conditionalJobStep2(): Step {
        return stepBuilderFactory.get("step2")
            .tasklet { _, _ ->
                logger.info { ">> step2" }
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun conditionalJobStep3(): Step {
        return stepBuilderFactory.get("step3")
            .tasklet { _, _ ->
                logger.info { ">> step3" }
                RepeatStatus.FINISHED
            }
            .build()
    }
}
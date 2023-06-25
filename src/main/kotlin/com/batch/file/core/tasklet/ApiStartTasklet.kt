package com.batch.file.core.tasklet

import mu.KotlinLogging
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component

class ApiStartTasklet: Tasklet {

    private val logger = KotlinLogging.logger {}

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        logger.info { "ApiStartTasklet execute" }
        return RepeatStatus.FINISHED
    }
}
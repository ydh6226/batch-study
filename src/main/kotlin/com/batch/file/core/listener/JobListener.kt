package com.batch.file.core.listener

import mu.KotlinLogging
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener

class JobListener: JobExecutionListener {

    private val logger = KotlinLogging.logger {}

    override fun beforeJob(jobExecution: JobExecution) {
        logger.info { "${jobExecution.jobInstance.jobName} 시작" }
    }

    override fun afterJob(jobExecution: JobExecution) {
        val elapsedTime = jobExecution.endTime!!.time - jobExecution.startTime!!.time
        logger.info { "${jobExecution.jobInstance.jobName} 종료. 총 소요시간 = ${elapsedTime} ms" }
    }
}
package com.practice.batch.demobatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private static RepeatStatus execute(StepContribution c, ChunkContext r) {
        System.out.println("Step 2 Executed");
        return RepeatStatus.FINISHED;
    }

    private static RepeatStatus execute2(StepContribution c, ChunkContext r) {
        System.out.println("Executed Step 3");
        return RepeatStatus.FINISHED;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("Executed Step1");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    public Step step2(){
        return stepBuilderFactory.get("step2")
                .tasklet(JobConfiguration::execute)
                .build();
    }


    public Step step3(){
        return stepBuilderFactory.get("step3")
                .tasklet(
                        JobConfiguration::execute2
                )
                .build();
    }
    @Bean
    public Job helloWorldJob() {
        return jobBuilderFactory.get("transition")
                .start(step1())
                .next(step2())
                .next(step3())
                .build();
    }



}

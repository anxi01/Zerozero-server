package com.zerozero.region.batch.job;

import com.zerozero.core.domain.infra.mongodb.region.Region;
import com.zerozero.core.domain.infra.mongodb.region.RegionMongoRepository;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class CacheRegionCoordinatesJob {

  private final JobLauncher jobLauncher;

  private final JobRepository jobRepository;

  private final PlatformTransactionManager platformTransactionManager;

  private final RegionMongoRepository regionMongoRepository;

  public void cacheRegionCoordinatesJobScheduler() {
    JobParameters jobParameters = new JobParametersBuilder()
        .addDate("time", new Date())
        .toJobParameters();
    try {
      jobLauncher.run(cacheRegionCoordinatesJob(), jobParameters);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public Job cacheRegionCoordinatesJob() {
    final String JOB_NAME = "cacheRegionCoordinatesJob";
    return new JobBuilder(JOB_NAME, jobRepository)
        .start(cacheRegionCoordinatesStep())
        .build();
  }

  @Bean
  public Step cacheRegionCoordinatesStep() {
    final String STEP_NAME = "cacheRegionCoordinatesStep";
    return new StepBuilder(STEP_NAME, jobRepository)
        .<Region, Region>chunk(100, platformTransactionManager)
        .reader(regionGeoJsonReader())
        .writer(cacheRegionCoordinatesToMongoDB())
        .build();
  }

  @Bean
  public ItemReader<Region> regionGeoJsonReader() {
    return new JsonItemReaderBuilder<Region>()
        .name("regionGeoJsonReader")
        .resource(new ClassPathResource("region.geojson"))
        .jsonObjectReader(new JacksonJsonObjectReader<>(Region.class))
        .build();
  }

  @Bean
  public ItemWriter<Region> cacheRegionCoordinatesToMongoDB() {
    return new RepositoryItemWriterBuilder<Region>()
        .repository(regionMongoRepository)
        .build();
  }
}

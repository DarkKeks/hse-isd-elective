package ru.darkkeks.isdelectivebackend;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
@EnableConfigurationProperties(ConversionProperties.class)
public class IsdElectiveBackendConfiguration {

    @Bean
    public ScheduledExecutorService executorService() {
        return new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
    }
}

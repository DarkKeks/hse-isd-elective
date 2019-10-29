package ru.darkkeks.isdelectivebackend;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
@EnableConfigurationProperties(ConversionProperties.class)
public class IsdElectiveBackendConfiguration {

    private ConversionProperties conversionProperties;

    public IsdElectiveBackendConfiguration(ConversionProperties conversionProperties) {
        this.conversionProperties = conversionProperties;
    }

    @Bean
    @Qualifier("convert")
    public ExecutorService executorService() {
        return new ThreadPoolExecutor(
                conversionProperties.getMaxConcurrentConversions(),
                conversionProperties.getMaxConcurrentConversions(),
                0, TimeUnit.SECONDS,
                new SynchronousQueue<>(), //
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}

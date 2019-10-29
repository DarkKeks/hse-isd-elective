package ru.darkkeks.isdelectivebackend;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("conversion")
public class ConversionProperties {

    /**
     * Time limit for video conversion operation in seconds
     */
    private int timeLimitSeconds = 10;

    /**
     * Maximum allowed amount of concurrently running conversion operations
     */
    private int maxConcurrentConversions = 2;

    public void setTimeLimitSeconds(int timeLimitSeconds) {
        this.timeLimitSeconds = timeLimitSeconds;
    }

    public int getTimeLimitSeconds() {
        return timeLimitSeconds;
    }

    public int getMaxConcurrentConversions() {
        return maxConcurrentConversions;
    }

    public void setMaxConcurrentConversions(int maxConcurrentConversions) {
        this.maxConcurrentConversions = maxConcurrentConversions;
    }
}

package ru.darkkeks.isdelectivebackend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("properties-test")
public class ConversionPropertiesTest {

    @Autowired
    private ConversionProperties conversionProperties;

    @Test
    public void shouldPopulateProperties() {
        assertEquals(conversionProperties.getMaxConcurrentConversions(), 1337);
        assertEquals(conversionProperties.getTimeLimitSeconds(), 228);
    }
}

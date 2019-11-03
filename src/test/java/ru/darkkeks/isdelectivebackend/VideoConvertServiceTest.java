package ru.darkkeks.isdelectivebackend;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class VideoConvertServiceTest {

    private static final String EXAMPLE_FILES_DIRECTORY = "example-files";

    @Autowired
    private VideoConvertService videoConvertService;

    @Autowired
    private ConversionProperties conversionProperties;

    @Autowired
    private VideoCompareService compareService;

    private Stream<Path> getSampleFiles(String tag) throws IOException {
        return Files.list(Paths.get(EXAMPLE_FILES_DIRECTORY)).filter(path -> path.getFileName().toString().contains(tag));
    }

    private Stream<Path> getSuccessFiles() throws IOException {
        return getSampleFiles("success");
    }

    private Stream<Path> getTimeoutFiles() throws IOException {
        return getSampleFiles("timeout");
    }

    private Stream<VideoFormat> getSupportedFormats() {
        return Arrays.stream(VideoFormat.values());
    }

    @TestFactory
    public Stream<DynamicTest> testSuccessTestFactory() throws IOException {
        return getSuccessFiles().flatMap(file -> getSupportedFormats().map(format -> {
            return dynamicTest(file.getFileName().toString(), () -> {
                convertSuccessfully(file, format);
            });
        }));
    }

    private void convertSuccessfully(Path file, VideoFormat format) throws IOException, EncoderException {
        MockMultipartFile multipartFile = new MockMultipartFile(file.getFileName().toString(),
                Files.newInputStream(file));
        ConversionResult conversionResult = videoConvertService.convertSync(multipartFile, format,
                conversionProperties.getTimeLimitSeconds(), TimeUnit.SECONDS);

        assertEquals(ConversionResult.Status.OK, conversionResult.getStatus());
        assertTrue(compareService.compare(file.toFile(), conversionResult.getFile()));
    }
}
package ru.darkkeks.isdelectivebackend;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.*;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;

@Service
public class VideoConvertService {

    private ScheduledExecutorService executorService;

    public VideoConvertService(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    public ConversionResult convertSync(MultipartFile file, VideoFormat targetFormat, long timeLimit, TimeUnit unit) {
        Conversion conversion = new Conversion();
        try {
            return conversion.finish(convert(file, targetFormat).get(timeLimit, unit));
        } catch (InterruptedException exception) {
            return conversion.error(exception);
        } catch (ExecutionException exception) {
            return conversion.error(exception.getCause());
        } catch (TimeoutException e) {
            return conversion.timeout();
        }
    }

    @Async
    public Future<File> convert(MultipartFile file, VideoFormat targetFormat) {
        Encoder encoder = new Encoder();

        try {
            Path tempDirectory = Files.createTempDirectory("video-converter");
            Path input = tempDirectory.resolve("input_file");
            Path output = tempDirectory.resolve("output_file");
            File outputFile = output.toFile();

            file.transferTo(input);

            EncodingAttributes attributes = new EncodingAttributes();
            attributes.setFormat(targetFormat.getEncoderName());
            attributes.setAudioAttributes(new AudioAttributes());
            attributes.setVideoAttributes(new VideoAttributes());

            return executorService.submit(() -> {
                encoder.encode(new MultimediaObject(input.toFile()), outputFile, attributes);
                return outputFile;
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static class Conversion {
        private Instant start;

        public Conversion() {
            this.start = Instant.now();
        }

        public long getElapsedTime() {
            return Duration.between(start, Instant.now()).toMillis();
        }

        public ConversionResult finish(File file) {
            return new ConversionResult(file, ConversionResult.Status.OK, getElapsedTime(), null);
        }

        public ConversionResult timeout() {
            return new ConversionResult(null, ConversionResult.Status.TIMEOUT, getElapsedTime(), null);
        }

        public ConversionResult error(Throwable e) {
            return new ConversionResult(null, ConversionResult.Status.ERROR, getElapsedTime(), e);
        }
    }

}

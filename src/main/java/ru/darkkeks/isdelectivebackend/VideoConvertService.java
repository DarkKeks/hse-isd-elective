package ru.darkkeks.isdelectivebackend;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.darkkeks.isdelectivebackend.exception.VideoConvertTimeoutException;
import ws.schild.jave.*;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.*;

@Service
public class VideoConvertService {

    private ScheduledExecutorService executorService;

    public VideoConvertService(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    public File convertSync(MultipartFile file, VideoFormat targetFormat, long timeLimit, TimeUnit unit) {
        Future<File> future = convert(file, targetFormat);
        try {
            return future.get(timeLimit, unit);
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Something went wrong with conversion", e);
        } catch (TimeoutException e) {
            throw new VideoConvertTimeoutException();
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

}

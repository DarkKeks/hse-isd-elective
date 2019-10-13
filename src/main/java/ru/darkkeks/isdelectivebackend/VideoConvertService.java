package ru.darkkeks.isdelectivebackend;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class VideoConvertService {

    public File convert(MultipartFile file, VideoFormat targetFormat) {
        Encoder encoder = new Encoder();

        try {
            Path tempDirectory = Files.createTempDirectory("video-converter");
            Path input = tempDirectory.resolve("input_file");
            Path output = tempDirectory.resolve("output_file");
            file.transferTo(input);

            EncodingAttributes attributes = new EncodingAttributes();
            attributes.setFormat(targetFormat.getEncoderName());
            attributes.setAudioAttributes(new AudioAttributes());
            attributes.setVideoAttributes(new VideoAttributes());
            encoder.encode(new MultimediaObject(input.toFile()), output.toFile(), attributes);

            return output.toFile();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (EncoderException e) {
            throw new RuntimeException(e);
        }
    }
}

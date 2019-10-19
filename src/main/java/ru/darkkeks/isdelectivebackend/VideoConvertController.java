package ru.darkkeks.isdelectivebackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.darkkeks.isdelectivebackend.exception.InvalidVideoFormatException;

import java.io.File;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/convert")
public class VideoConvertController {

    private final VideoConvertService videoConvertService;

    @Autowired
    public VideoConvertController(VideoConvertService videoConvertService) {
        this.videoConvertService = videoConvertService;
    }

    @GetMapping("/formats")
    public ResponseEntity supportedFormats() {
        return ResponseEntity.ok(VideoFormat.values());
    }

    @PostMapping(
            value = "/{targetFormatName}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity convert(@PathVariable String targetFormatName, @RequestBody MultipartFile file) {
        VideoFormat format = VideoFormat.getByName(targetFormatName);
        if (format == null) {
            throw new InvalidVideoFormatException(targetFormatName);
        }
        if (file == null) {
            throw new IllegalArgumentException("File not provided");
        }

        File result = videoConvertService.convertSync(file, format, 10, TimeUnit.SECONDS);
        return ResponseEntity.ok(new FileSystemResource(result));
    }
}

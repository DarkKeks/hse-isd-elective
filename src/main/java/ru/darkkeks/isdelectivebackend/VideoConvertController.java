package ru.darkkeks.isdelectivebackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.darkkeks.isdelectivebackend.exception.InvalidVideoFormatException;
import ru.darkkeks.isdelectivebackend.exception.VideoConvertErrorException;
import ru.darkkeks.isdelectivebackend.exception.VideoConvertTimeoutException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/convert")
public class VideoConvertController {

    private final ConversionProperties conversionProperties;
    private final ConversionOperationRepository repository;
    private final VideoConvertService videoConvertService;

    @Autowired
    public VideoConvertController(ConversionProperties conversionProperties, ConversionOperationRepository repository,
                                  VideoConvertService videoConvertService) {
        this.conversionProperties = conversionProperties;
        this.repository = repository;
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
    public ResponseEntity convert(@PathVariable String targetFormatName, @RequestBody MultipartFile file,
                                  HttpServletRequest session) {

        VideoFormat format = VideoFormat.getByName(targetFormatName);

        if (format == null) {
            throw new InvalidVideoFormatException(targetFormatName);
        }

        if (file == null) {
            throw new IllegalArgumentException("File not provided");
        }

        ConversionResult conversionResult = videoConvertService.convertSync(file, format,
                conversionProperties.getTimeLimitSeconds(), TimeUnit.SECONDS);
        ConversionResult.Status status = conversionResult.getStatus();

        ConversionOperation operation = new ConversionOperation(new Date(), session.getRemoteAddr(),
                file.getOriginalFilename(), format, status, conversionResult.getTimeElapsed());

        repository.save(operation);

        if (status == ConversionResult.Status.TIMEOUT) {
            throw new VideoConvertTimeoutException();
        }

        if (status == ConversionResult.Status.ERROR) {
            throw new VideoConvertErrorException(conversionResult.getException());
        }

        return ResponseEntity.ok(new FileSystemResource(conversionResult.getFile()));
    }
}

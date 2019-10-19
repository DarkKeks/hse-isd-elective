package ru.darkkeks.isdelectivebackend;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum VideoFormat {
    FLV("flv", "flv", ".flv"),
    MP4("mp4", "mp4", ".mp4"),
    MKV("mkv", "matroska", ".mkv"),
    MOV("mov", "mov", ".mov");

    private String name;
    private String encoderName;
    private String extension;

    VideoFormat(String name, String encoderName, String extension) {
        this.name = name;
        this.encoderName = encoderName;
        this.extension = extension;
    }

    public String getName() {
        return name;
    }

    public String getEncoderName() {
        return encoderName;
    }

    public String getExtension() {
        return extension;
    }

    public static VideoFormat getByName(String targetFormat) {
        for(VideoFormat format : VideoFormat.values()) {
            if (format.getName().equalsIgnoreCase(targetFormat)) {
                return format;
            }
        }
        return null;
    }
}

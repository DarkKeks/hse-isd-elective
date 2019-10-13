package ru.darkkeks.isdelectivebackend;

public enum VideoFormat {
    FLV("flv", "flv", ".flv"),
    MP4("mp4", "mp4", ".mp4");

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

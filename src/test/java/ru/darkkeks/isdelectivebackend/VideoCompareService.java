package ru.darkkeks.isdelectivebackend;

import org.springframework.stereotype.Service;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;

import java.io.File;

@Service
public class VideoCompareService {

    public boolean compare(File original, File converted) throws EncoderException {
        MultimediaInfo originalInfo = getInfo(original);
        MultimediaInfo convertedInfo = getInfo(converted);

        boolean result = true;
        result &= Math.abs(originalInfo.getDuration() - convertedInfo.getDuration()) < 100;
        result &= originalInfo.getVideo().getFrameRate() == convertedInfo.getVideo().getFrameRate();
        result &= originalInfo.getVideo().getSize().getHeight() == convertedInfo.getVideo().getSize().getHeight();
        result &= originalInfo.getVideo().getSize().getWidth() == convertedInfo.getVideo().getSize().getWidth();
        result &= originalInfo.getAudio().getSamplingRate() == convertedInfo.getAudio().getSamplingRate();

        return result;
    }

    private MultimediaInfo getInfo(File file) throws EncoderException {
        return new MultimediaObject(file).getInfo();
    }

}

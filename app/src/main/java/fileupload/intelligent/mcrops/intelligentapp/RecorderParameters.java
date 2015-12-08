package fileupload.intelligent.mcrops.intelligentapp;

/**
 * Created by User on 7/8/2015.
 */

import android.os.Build;

import org.bytedeco.javacpp.avcodec;


public class RecorderParameters {

    private static boolean AAC_SUPPORTED = Build.VERSION.SDK_INT >= 10;
    private int videoCodec = avcodec.AV_CODEC_ID_MPEG4;
    private int videoFrameRate = 30;
    //private int videoBitrate = 500 *1000;
    private int videoQuality = 24;

    private String videoOutputFormat = AAC_SUPPORTED ? "mp4" : "3gp";

    public String getVideoOutputFormat() {
        return videoOutputFormat;
    }


    public int getVideoCodec() {
        return videoCodec;
    }



    public int getVideoFrameRate() {
        return videoFrameRate;
    }



    public int getVideoQuality() {
        return videoQuality;
    }

    public void setVideoQuality(int videoQuality) {
        this.videoQuality = videoQuality;
    }



}

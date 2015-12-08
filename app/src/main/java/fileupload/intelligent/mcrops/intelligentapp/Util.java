package fileupload.intelligent.mcrops.intelligentapp;

/**
 * Created by User on 7/8/2015.
 */

import android.app.Activity;
import android.content.ContentValues;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.provider.MediaStore.Video;
import android.view.Surface;

import java.io.File;
import java.util.Comparator;
import java.util.List;


public class Util {
    public static  ContentValues videoContentValues = null;
    public static String getRecordingTimeFromMillis(long millis)
    {
        String strRecordingTime = null;
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        int hours = minutes / 60;

        if(hours >= 0 && hours < 10)
            strRecordingTime = "0" + hours + ":";
        else
            strRecordingTime = hours + ":";

        if(hours > 0)
            minutes = minutes % 60;

        if(minutes >= 0 && minutes < 10)
            strRecordingTime += "0" + minutes + ":";
        else
            strRecordingTime += minutes + ":";

        seconds = seconds % 60;

        if(seconds >= 0 && seconds < 10)
            strRecordingTime += "0" + seconds ;
        else
            strRecordingTime += seconds ;

        return strRecordingTime;

    }


    public static int determineDisplayOrientation(Activity activity, int defaultCameraId) {
        int displayOrientation = 0;
        if(Build.VERSION.SDK_INT >  Build.VERSION_CODES.FROYO)
        {
            CameraInfo cameraInfo = new CameraInfo();
            Camera.getCameraInfo(defaultCameraId, cameraInfo);

            int degrees  = getRotationAngle(activity);


            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                displayOrientation = (cameraInfo.orientation + degrees) % 360;
                displayOrientation = (360 - displayOrientation) % 360;
            } else {
                displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
            }
        }
        return displayOrientation;
    }

    public static int getRotationAngle(Activity activity)
    {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees  = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;

            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        return degrees;
    }



    public static String createFinalPath()
    {
        long dateTaken = System.currentTimeMillis();
        String title = CONSTANTS.FILE_START_NAME + dateTaken;
        String filename = title + CONSTANTS.VIDEO_EXTENSION;
        String filePath = genrateFilePath(String.valueOf(dateTaken), true, null);
        ContentValues values = new ContentValues(7);
        values.put(Video.Media.TITLE, title);
        values.put(Video.Media.DISPLAY_NAME, filename);
        values.put(Video.Media.DATE_TAKEN, dateTaken);
        values.put(Video.Media.MIME_TYPE, "video/3gpp");
        values.put(Video.Media.DATA, filePath);
        videoContentValues = values;

        return filePath;
    }

    private static String genrateFilePath(String uniqueId, boolean isFinalPath, File tempFolderPath)
    {
        String fileName = CONSTANTS.FILE_START_NAME + uniqueId + CONSTANTS.VIDEO_EXTENSION;
        String dirPath = "";
        if(isFinalPath)
        {
            new File(CONSTANTS.CAMERA_FOLDER_PATH).mkdirs();
            dirPath = CONSTANTS.CAMERA_FOLDER_PATH;
        }
        else
            dirPath = tempFolderPath.getAbsolutePath();
        String filePath = dirPath + "/" + fileName;
        return filePath;
    }
    public static String createTempPath(File tempFolderPath )
    {
        long dateTaken = System.currentTimeMillis();
        String filePath = genrateFilePath(String.valueOf(dateTaken), false, tempFolderPath);
        return filePath;
    }



    public static File getTempFolderPath()
    {
        File tempFolder = new File(CONSTANTS.TEMP_FOLDER_PATH +"_" +System.currentTimeMillis());
        return tempFolder;
    }


    public static List<Camera.Size> getResolutionList(Camera camera)
    {
        Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();


        return previewSizes;
    }

    public static RecorderParameters getRecorderParameter(int currentResolution)
    {
        RecorderParameters parameters = new RecorderParameters();
        if(currentResolution ==  CONSTANTS.RESOLUTION_HIGH_VALUE)
        {
            parameters.setVideoQuality(0);
        }
        else if(currentResolution ==  CONSTANTS.RESOLUTION_MEDIUM_VALUE)
        {
            parameters.setVideoQuality(20);
        }
        else if(currentResolution == CONSTANTS.RESOLUTION_LOW_VALUE)
        {
            parameters.setVideoQuality(32);
        }
        return parameters;
    }

    public static class ResolutionComparator implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size size1, Camera.Size size2) {
            if(size1.height != size2.height)
                return size1.height -size2.height;
            else
                return size1.width - size2.width;
        }
    }


}

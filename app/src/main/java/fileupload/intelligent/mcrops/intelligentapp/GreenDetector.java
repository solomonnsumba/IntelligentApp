package fileupload.intelligent.mcrops.intelligentapp;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;

import static org.bytedeco.javacpp.opencv_core.countNonZero;
import static org.bytedeco.javacpp.opencv_core.inRange;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2HSV;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.resize;


/**
 * Created by linuxlite on 11/9/15.
 */
public class GreenDetector
{
    private GreenDetectionListener detectionListener;
    private Scalar lowerBound;
    private Scalar upperBound;
    private Mat imgHSV;
    private Mat mask;
    private Size size;
    private static int THRESHOLD = 1;

    public GreenDetector(GreenDetectionListener detectionListener){
        this.detectionListener = detectionListener;
        this.lowerBound = new Scalar(38,50,50,0);
        this.upperBound = new Scalar(75,255,255,0);
        this.imgHSV = new Mat();
        this.mask = new Mat();
        this.size = new Size(180, 180);
    }
    public void checkImage(IplImage evaluationSample){
            Mat resizedImage = new Mat();
            Mat imageMatrix = new Mat(evaluationSample,true);
            resize(imageMatrix, resizedImage, size);
            cvtColor(resizedImage, imgHSV, CV_BGR2HSV);
            inRange(imgHSV, new Mat(lowerBound), new Mat(upperBound),mask);

            int number_of_green_pixels = countNonZero(mask);
            if(number_of_green_pixels >= THRESHOLD){
                detectionListener.isLeafy( number_of_green_pixels);
            }else{
                detectionListener.isNotLeafy(number_of_green_pixels);
            }

    }
}

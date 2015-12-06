package fileupload.intelligent.mcrops.intelligentapp;

/**
 * Created by linuxlite on 11/9/15.
 */
public interface GreenDetectionListener {

    void isLeafy(int number_of_green_pixels);

    void isNotLeafy(int number_of_green_pixels);
}

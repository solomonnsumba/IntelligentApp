package fileupload.intelligent.mcrops.intelligentapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.bytedeco.javacpp.opencv_core;

import android.net.Uri;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.Utils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String MIOTAG =null;
    private Uri fileUri;
    private Button btnCapturePicture;
    private GreenDetector greenDetector;
    private opencv_core.IplImage image;
    private static boolean WRONG_IMAGE_POINTED_AT = Boolean.FALSE;
    private ImageView imageView;
    private String mPath;

    final String TAG = "Hello World";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        WRONG_IMAGE_POINTED_AT = Boolean.FALSE;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btnCapturePicture = (Button)findViewById(R.id.btnCapturePicture);

        btnCapturePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });
    }

    private void captureImage() {

        File tempFile = null;
        try {
            tempFile = File.createTempFile("camera", ".png", getExternalCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPath = tempFile.getAbsolutePath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image

                Bitmap bmp = BitmapFactory.decodeFile(mPath);
                isBlurredImage(bmp);

                launchUploadActivity(true);

              /*

              try {
                  greenDetector = new GreenDetector(new GreenDetectionListener() {
                      @Override
                      public void isLeafy(int number_of_green_pixels) {
                          Log.i("GREEN", String.format("WAS OK %d PIXELS", number_of_green_pixels));

                          launchUploadActivity(true);

                      }

                      @Override
                      public void isNotLeafy(int number_of_green_pixels) {
                          Log.i("GREEN", String.format("WAS OK NOT %d PIXELS", number_of_green_pixels));
                          Toast.makeText(getApplicationContext(), "No Leaf Identified", Toast.LENGTH_SHORT).show();
                      }
                  });
              }catch(Exception e){

              }
                greenDetector.checkImage(image);
                // launching upload activity

                /*try{
            	Bitmap photo = (Bitmap) data.getExtras().get("data");
            	if(!isBlurredImage(photo)){
            	launchUploadActivity(false);
            	}
            	}catch(NullPointerException e){
            		e.printStackTrace();
            	}
            	*/


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }



    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    private void launchUploadActivity(boolean isImage){
        //greenDetector.checkImage();
        Intent i = new Intent(MainActivity.this, UploadActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        startActivity(i);
    }


    private void destroyCamera(){
        try{
            MainActivity.WRONG_IMAGE_POINTED_AT = Boolean.TRUE;
            startActivity(new Intent(this,MainActivity.class));

        }
        catch (Exception exe){
            exe.printStackTrace();
        }

    }

    private boolean isBlurredImage(Bitmap image) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inDither = true;
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;

        int l = CvType.CV_8UC1;
        Mat matImage = new Mat();
        Utils.bitmapToMat(image, matImage);
        Mat matImageGrey = new Mat();
        Imgproc.cvtColor(matImage, matImageGrey, Imgproc.COLOR_BGR2GRAY);

        Mat dst2 = new Mat();
        Utils.bitmapToMat(image, dst2);

        Mat laplacianImage = new Mat();
        dst2.convertTo(laplacianImage, l);
        Imgproc.Laplacian(matImageGrey, laplacianImage, CvType.CV_8U);
        Mat laplacianImage8bit = new Mat();
        laplacianImage.convertTo(laplacianImage8bit, l);
        System.gc();

        Bitmap bmp = Bitmap.createBitmap(laplacianImage8bit.cols(),
                laplacianImage8bit.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(laplacianImage8bit, bmp);

        int[] pixels = new int[bmp.getHeight() * bmp.getWidth()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(),
                bmp.getHeight());
        if (bmp != null)
            if (!bmp.isRecycled()) {
                bmp.recycle();

            }
        int maxLap = -16777216;

        for (int i = 0; i < pixels.length; i++) {

            if (pixels[i] > maxLap) {
                maxLap = pixels[i];
            }
        }
        int soglia = -6118750;

        if (maxLap < soglia || maxLap == soglia) {
            Log.i(MIOTAG, "--------->blur image<------------");
            // launchUploadActivity(true);
            return true;
        } else {
            Log.i(MIOTAG, "----------->Not blur image<------------");
            //launchUploadActivity(true);
            return false;
        }
    }
}

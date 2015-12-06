package fileupload.intelligent.mcrops.intelligentapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.net.Uri;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.bytedeco.javacpp.opencv_core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    private Button btnCapturePicture;
    private GreenDetector greenDetector;
    private opencv_core.IplImage image;
    private static boolean WRONG_IMAGE_POINTED_AT = Boolean.FALSE;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                launchUploadActivity(true);

              /*  photo.copyPixelsToBuffer(image.getByteBuffer());

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
}

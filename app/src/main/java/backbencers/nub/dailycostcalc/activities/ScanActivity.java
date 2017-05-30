package backbencers.nub.dailycostcalc.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import backbencers.nub.dailycostcalc.R;
import backbencers.nub.dailycostcalc.constant.Constant;
import backbencers.nub.dailycostcalc.custom.SaveOption;

public class ScanActivity extends AppCompatActivity {

    private Camera camera; // camera class variable
    private SurfaceHolder surfaceHolder; // variable to hold surface for surfaceView which means display
    boolean camCondition = false;  // conditional variable for camera preview checking and set to false

    private File picFile;

    private CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;

    private SurfaceView cameraView;
    private TextView tvImageText;
    private Button btnScanSave, btnScanCancel;

    private Context context;

    private String TAG = "ScanActivity";

    private String category;


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int id = v.getId();

            if(id == R.id.btnScanSave){
                //sendDataToSave();
                camera.takePicture(null, null, null, mPictureCallback);
                Toast.makeText(context, "Saved successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else if(id == R.id.btnScanCancel){
                moveToMain();
            }

        }
    };

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera c) {

            FileOutputStream outStream = null;

            File image_file = getDirc();

            if(!image_file.exists() && !image_file.mkdirs()){

                Toast.makeText(getApplicationContext(), "Can not create directory to save image",Toast.LENGTH_SHORT).show();
                return;
            }

            String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

            String photoFile = "DCC"+date+".jpg";
            String fileName = image_file.getAbsolutePath()+"/"+photoFile;

            picFile = new File(fileName);

            try {

                // Directory and name of the photo. We put system time
                // as a postfix, so all photos will have a unique file name.

                outStream = new FileOutputStream(picFile);
                outStream.write(data);
                outStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }

        }
    };

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            camera = Camera.open();   // opening camera
            camera.setDisplayOrientation(90);   // setting camera preview orientation

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // TODO Auto-generated method stub
            // stop the camera
            if(camCondition){
                camera.stopPreview(); // stop preview using stopPreview() method
                camCondition = false; // setting camera condition to false means stop
            }
            // condition to check whether your device have camera or not
            if (camera != null){
                try {
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setColorEffect(Camera.Parameters.EFFECT_NONE); //applying effect on camera
                    camera.setParameters(parameters); // setting camera parameters
                    camera.setPreviewDisplay(surfaceHolder); // setting preview of camera
                    camera.startPreview();  // starting camera preview

                    camCondition = true; // setting camera to true which means having camera
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            camera.stopPreview();  // stopping camera preview
            camera.release();       // releasing camera
            camera = null;          // setting camera to null when left
            camCondition = false;   // setting camera condition to false also when exit from application
//            Log.d("MainActivity","Surface Destroyed");
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case RequestCameraPermissionID:{

                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

                        return;
                    }

                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            }


        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        getSupportActionBar().setTitle("Scan");

        getWindow().setFormat(PixelFormat.UNKNOWN);

        context = getApplicationContext();

        Intent intent = getIntent();
        category = intent.getStringExtra(Constant.CATEGORY_BUNDLE);

        inits();

        //initCamera();

    }

    private File getDirc(){

        File dirc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        return new File(dirc,"DailyCostCalc");

    }

    private void sendDataToSave(){

        //new Debit("27-04-17","Dinnar","Biriani",125.00);
//                tvImageText.setText("Description        Amount\nBiriani         125.00\nBurhani         30.00\nTotal        155.00");
        if(tvImageText.getText().toString().trim().length() > 0){

            String text = tvImageText.getText().toString().trim();

            SaveOption saveOption = new SaveOption(ScanActivity.this,category,text);

            saveOption.showDetailData();


        } else{
            Toast.makeText(ScanActivity.this, "No text found", Toast.LENGTH_SHORT).show();
        }
    }

    //Back to the Debit

    private void moveToMain(){

        Intent intent = new Intent(ScanActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }



    private void inits(){

        cameraView = (SurfaceView) findViewById(R.id.cameraView);
        tvImageText = (TextView) findViewById(R.id.tvImageText);

        btnScanSave = (Button) findViewById(R.id.btnScanSave);
        btnScanCancel = (Button) findViewById(R.id.btnScanCancel);

        surfaceHolder = cameraView.getHolder();
        // adding call back to this context means MainActivity
        surfaceHolder.addCallback(callback);
        // to set surface type
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);

        //Actions

        btnScanSave.setOnClickListener(onClickListener);
        btnScanCancel.setOnClickListener(onClickListener);

    }



//    private void initCamera(){
//
//        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
//
//        if (!textRecognizer.isOperational()) {
//
//            Log.w(TAG, "Detection dependencies are not yet available");
//
//        } else {
//
//            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
//                    .setFacing(CameraSource.CAMERA_FACING_BACK)
//                    .setRequestedPreviewSize(1280, 1024)
//                    .setRequestedFps(2.0f)
//                    .setAutoFocusEnabled(true)
//                    .build();
//
//            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
//
//
//                @Override
//                public void surfaceCreated(SurfaceHolder holder) {
//                    try {
//
//                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//
//                            ActivityCompat.requestPermissions(ScanActivity.this,
//                                    new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
//
//                            return;
//                        }
//                        cameraSource.start(cameraView.getHolder());
//
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//                }
//
//                @Override
//                public void surfaceDestroyed(SurfaceHolder holder) {
//
//                    cameraSource.stop();
//                }
//            });
//
//            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
//                @Override
//                public void release() {
//
//                }
//
//                @Override
//                public void receiveDetections(Detector.Detections<TextBlock> detections) {
//
//                    final SparseArray<TextBlock> items = detections.getDetectedItems();
//
//                    if(items.size() != 0){
//
//                        tvImageText.post(new Runnable(){
//
//                            @Override
//                            public void run() {
//
//                                StringBuilder stringBuilder = new StringBuilder();
//
//                                for(int i=0; i<items.size(); ++i){
//
//                                    TextBlock item = items.valueAt(i);
//
//                                    stringBuilder.append(item.getValue());
//                                    stringBuilder.append("\n");
//                                }
//
//                                tvImageText.setText(stringBuilder.toString());
//
//                            }
//                        });
//                    }
//
//                }
//            });
//
//        }
//    }


}

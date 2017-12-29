package com.fourthstatelab.neuroimpression;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity /*implements SurfaceHolder.Callback*/ {

    VideoView preview;
    int REQUEST_VIDEO_CAPTURE=1;
    TextView nameView;
    FloatingActionButton fb;
    ImageView doneView;

    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    public MediaRecorder mrec = new MediaRecorder();
    private Button startRecording = null;

    String filePath = "/sdcard/video.webm";
    ProgressBar bar;

    File video;
    private Camera mCamera;
    int time =5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(null , "Video starting");
        fb= findViewById(R.id.record);
        nameView = findViewById(R.id.name);
        doneView = findViewById(R.id.submit);
        bar = findViewById(R.id.prog);
        nameView.setVisibility(View.GONE);
        doneView.setVisibility(View.GONE);
        bar.setVisibility(View.GONE);

        /*mCamera = Camera.open();

        surfaceView =findViewById(R.id.surface_camera);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mCamera.setDisplayOrientation(90);
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        doneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(android.os.Build.VERSION.SDK_INT== Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                    }
                    else{
                        Log.d("file","Uploading");
                        File file = new File(filePath);
                        uploadFile(config.link+"/fileFromMobile",file);
                    }
                }
                else{
                    Log.d("file","Uploading");
                    File file = new File(filePath);
                    uploadFile(config.link+"/fileFromMobile",file);
                }
            }
        });

    }

    public void uploadFile(String serverURL, File file) {
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.readTimeout(200, TimeUnit.SECONDS);
        b.writeTimeout(600, TimeUnit.SECONDS);
        OkHttpClient client = b.build();
        bar.setVisibility(View.VISIBLE);
        Log.d("File nema",file.getName());
        try {

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("name",nameView.getText().toString())
                    .addFormDataPart("file",nameView.getText().toString()+".webm",RequestBody.create(MediaType.parse("video/webm"),file))
                    .build();

            Request request = new Request.Builder()
                    .url(serverURL)
                    .post(requestBody)
                    .build();

            //Response response = client.newCall(request).execute();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("Respinse","Failed");
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("Respinse",response.body().string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bar.setVisibility(View.GONE);
                        }
                    });
                }
            });

        } catch (Exception ex) {
            // Handle the error
            Log.d("Error","Couldnt execite");
            ex.printStackTrace();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 0, 0, "StartRecording");
        menu.add(0, 1, 0, "StopRecording");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case 0:
                try {
                    //startRecording();
                } catch (Exception e) {
                    String message = e.getMessage();
                    Log.i(null, "Problem Start"+message);
                    mrec.release();
                }
                break;

            case 1: //GoToAllNotes
                mrec.stop();
                mrec.release();
                mrec = null;
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    Handler handler = new Handler();

    public void startRecording(View view)
    {
        mrec = new MediaRecorder();  // Works well
        mCamera.unlock();

        mrec.setCamera(mCamera);

        mrec.setPreviewDisplay(surfaceHolder.getSurface());
        mrec.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        mrec.setAudioSource(MediaRecorder.AudioSource.MIC);
        //mrec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //mrec.setOutputFormat(MediaRecorder.OutputFormat.WEBM);
        mrec.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mrec.setOutputFile("/sdcard/video.webm");

        mrec.setMaxDuration(time*1000); // 10 seconds
        mrec.setMaxFileSize(100000000);

        mrec.setOrientationHint(90);
        try {
            mrec.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mrec.start();
        fb.setVisibility(View.GONE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Recording","Done");
                nameView.setVisibility(View.VISIBLE);
                doneView.setVisibility(View.VISIBLE);
            }
        },time*1000);
    }

    protected void stopRecording() {
        mrec.stop();
        mrec.release();
        mCamera.release();
    }

    private void releaseMediaRecorder(){
        if (mrec != null) {
            mrec.reset();   // clear recorder configuration
            mrec.release(); // release the recorder object
            mrec = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera != null){
            Camera.Parameters params = mCamera.getParameters();
            mCamera.setParameters(params);
        }
        else {
            Toast.makeText(getApplicationContext(), "Camera not available!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
    }*/

    int requestCode = 2;
    public void onCaptureClick(View view){
        if(android.os.Build.VERSION.SDK_INT== Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA,Manifest.permission.INTERNET}, requestCode);
            }
            else{
                capture();
            }
        }
        else{
            capture();
        }
    }

    void capture(){
        File mediaFile = new File(filePath);
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            Uri videoUri = Uri.fromFile(mediaFile);

            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            takeVideoIntent.putExtra(android.provider.MediaStore.EXTRA_VIDEO_QUALITY, 1);
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,time);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Video saved to:\n" +
                    data.getData(), Toast.LENGTH_LONG).show();
            nameView.setVisibility(View.VISIBLE);
            doneView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                capture();

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }
        }
        if (requestCode == 2) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "internet permission granted", Toast.LENGTH_LONG).show();
                Log.d("file","Uploading");
                File file = new File(filePath);
                uploadFile(config.link+"/fileFromMobile",file);

            } else {

                Toast.makeText(this, "internet permission denied", Toast.LENGTH_LONG).show();

            }
        }
    }
}

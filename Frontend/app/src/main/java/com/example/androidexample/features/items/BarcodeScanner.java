package com.example.androidexample.features.items;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;

import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

public class BarcodeScanner extends AppCompatActivity implements View.OnClickListener{
    private static final String[] CAMERA_PERMISSION = {Manifest.permission.CAMERA};;
    private static final int CAMERA_REQUEST_CODE = 10;
    private PreviewView cameraPreview;
    private Button scanButton, backToEditButton;
    public TextView hintView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private String URL_ZXING = "https://zxing.org/w/decode";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);


        cameraPreview = findViewById(R.id.cameraPreview);
        hintView = findViewById(R.id.hintView);
        scanButton = findViewById(R.id.scanButton);
        backToEditButton = findViewById(R.id.backToEditButton);

        scanButton.setOnClickListener(this);
        backToEditButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.scanButton){
            if(hasCameraPermission()){
                if(imageCapture != null){
                    takePhoto();
                }else{
                    startCamera();
                }

            }else{
                requestPermission();
            }
        }else if(view.getId() == R.id.backToEditButton){
            Intent intent = new Intent(BarcodeScanner.this, EditAndDeleteItem.class);
            intent.putExtra("id", hintView.getText());
            startActivity(intent);
        }

    }

    private void startCamera(){
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindCamera(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(this));



    }

    private void bindCamera(@NonNull ProcessCameraProvider cameraProvider){

        //bind preview to screen to show live camera
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());

        // select back facing camera
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

        // build image capture to take picture when button is pressed

        imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build();


        //bind everyting to the camera
        cameraProvider.unbindAll();// in case something already has alrady been bound to it
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector,preview,imageCapture);



    }

    private void takePhoto(){
        if (imageCapture == null) return;

        hintView.setText("taking picture");
        scanButton.setEnabled(false);// make sure two scans cannot be happening at the same time

        File photoFile = new File(getCacheDir(), "barcode_scan.jpg");
        if (photoFile.exists()) {
            photoFile.delete();
        }

        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                        try {
                            // Read file without the Build.VERSION check
                            java.io.FileInputStream fis = new java.io.FileInputStream(photoFile);
                            byte[] imageBytes = new byte[(int) photoFile.length()];
                            fis.read(imageBytes);
                            fis.close();

                            Log.d("ZXing", "Image size: " + imageBytes.length + " bytes"); // verify bytes exist

                            sendPostToZXing(imageBytes);

                        } catch (IOException e) {
                            hintView.setText("Failed to read image file.");
                            scanButton.setEnabled(true);
                            Log.e("ZXing", "File read error: " + e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ImageCaptureException error) {
                        hintView.setText("failed to capture image try again");
                        scanButton.setEnabled(true);
                    }
                }
        );


    }

    public void sendPostToZXing(byte[] photoBytes) {
        hintView.setText("Analyzing image");
        final String delimeter = "ThisHasGottaBeUniqueRight";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_ZXING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        parseHtmlForValueAndShowResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse != null) {
                            String body = new String(error.networkResponse.data);
                            Log.e("ZXing", "Status: " + error.networkResponse.statusCode);
                            Log.e("ZXing", "Body: " + body);
                            hintView.setText("Error: " + error.networkResponse.statusCode);
                        }
                    }
                }
        ) {

            @Override
            public String getBodyContentType() {
                return "multipart/form-data;boundary=" + delimeter;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    ByteArrayOutputStream body = new ByteArrayOutputStream();
                    String header = "--" + delimeter + "\r\n" + "Content-Disposition: form-data; name=\"f\"; filename=\"barcode_scan.jpg\"\r\n"
                            + "Content-Type: image/jpeg\r\n\r\n";
                    body.write(header.getBytes("UTF-8"));
                    body.write(photoBytes);
                    body.write(("\r\n--" + delimeter + "--\r\n").getBytes());
                    return body.toByteArray();
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.d(uee.toString());
                    return null;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


        };
        stringRequest.setShouldCache(false);
        hintView.setText("sending");
        Log.d("ZXing", "Sending request to ZXing...");
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void parseHtmlForValueAndShowResult(String html){
        if(!html.contains("Decode Succeeded")){
            hintView.setText("No Barcode Found Try again");
            return;
        }
        int startIndex = html.indexOf("<pre>");
        int endIndex = html.indexOf("</pre>");

        if (startIndex == -1 || endIndex == -1){
            hintView.setText("Error parsing string");
            return;

        }

        String barcodeValue = html.substring(startIndex + 5, endIndex);
        hintView.setText(barcodeValue);


    }


    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            hintView.setText("Camera permission is required to scan.");
        }
    }




}


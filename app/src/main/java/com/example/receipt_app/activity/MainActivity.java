package com.example.receipt_app.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.receipt_app.R;
import com.example.receipt_app.model.ReceiptViewModel;
import com.example.receipt_app.view.ReceiptHistory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final String IMAGE_TYPE = "image/*";
    private static final String JPEG = "image/jpeg";
    private static final String PNG = "image/png";
    private static final String FILE_PROVIDER_KEY = "com.example.receipt_app.fileprovider";
    private static final String INTENT_KEY = "receiptPictureURI";
    private static final String IMAGE_PREFIX_NAME = "JPEG_";
    private static final String JPEG_EXT = ".jpg";
    private static final String DATE_STAMP = "yyyyMMdd_HHmmss";
    //String currentPhotoPath;
    private static Uri cameraPhotoURI;
    ReceiptViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get view model
        viewModel = ViewModelProviders.of(this).get(ReceiptViewModel.class);

        Button accessGalleryBtn = findViewById(R.id.galleryBtn);
        accessGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickReceiptFromGallery();
            }
        });

        Button accessCameraBtn = findViewById(R.id.cameraBtn);
        accessCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureReceiptFromCamera();
            }
        });

        Button receiptHistoryBtn = findViewById(R.id.receiptLogBtn);
        receiptHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), ReceiptHistory.class);
                startActivity(startIntent);
            }
        });

    }

    private void pickReceiptFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent selectPictureIntent = new Intent(Intent.ACTION_PICK);

        // Sets the type as image/*. This ensures only components of type image are selected
        selectPictureIntent.setType(IMAGE_TYPE);
        //pass an extra array with the accepted mime types. Ensures only components with these MIME types as targeted.
        String[] acceptedMimeTypes = {JPEG, PNG};
        selectPictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, acceptedMimeTypes);

        // Launching the Intent
        startActivityForResult(selectPictureIntent, GALLERY_REQUEST_CODE);
    }

    private void captureReceiptFromCamera() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch (IOException ex){
                ex.printStackTrace();
            }
            if (photoFile != null){
                cameraPhotoURI = FileProvider.getUriForFile(this, FILE_PROVIDER_KEY, photoFile);
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoURI);
                startActivityForResult(takePhotoIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) {
            String receiptURIString = "";

            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    if(data.getData() == null){
                        return;
                    }
                    receiptURIString = data.getData().toString();
                    break;
                case CAMERA_REQUEST_CODE:
                    receiptURIString = cameraPhotoURI.toString();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + requestCode);
            }
            Intent startIntent = new Intent(getApplicationContext(), SubmissionActivity.class);
            startIntent.putExtra(INTENT_KEY, receiptURIString);
            startActivity(startIntent);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat(DATE_STAMP).format(new Date());
        String imageFileName = IMAGE_PREFIX_NAME + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, JPEG_EXT, storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        //currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}


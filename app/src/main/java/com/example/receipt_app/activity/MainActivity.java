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
        selectPictureIntent.setType("image/*");
        //pass an extra array with the accepted mime types. Ensures only components with these MIME types as targeted.
        String[] acceptedMimeTypes = {"image/jpeg", "image/png"};
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
                cameraPhotoURI = FileProvider.getUriForFile(this, "com.example.receipt_app.fileprovider", photoFile);
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoURI);
                startActivityForResult(takePhotoIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) {
            Uri receiptURI;
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
            startIntent.putExtra("receiptPictureURI", receiptURIString);
            startActivity(startIntent);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        //currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}


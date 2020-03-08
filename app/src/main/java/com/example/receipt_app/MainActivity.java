package com.example.receipt_app;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button pictureBtn = (Button) findViewById(R.id.pictureBtn);
        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });

        Button cameraBtn = (Button) findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureFromCamera();
            }
        });

        Button secondActivityBtn = (Button) findViewById(R.id.secondActivityBtn);
        secondActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), SecondActivity.class);
                //startIntent.putExtra("com.example.receipt_app.SOMETHING", "HELLO WORLD!");
                startActivity(startIntent);
            }
        });

        Button googleBtn = (Button) findViewById(R.id.googleBtn);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String google = "http://old.reddit.com/r/leagueoflegends";
                Uri webaddress = Uri.parse(google);

                Intent gotoGoogle = new Intent(Intent.ACTION_VIEW, webaddress);
                if (gotoGoogle.resolveActivity(getPackageManager()) != null) {
                    startActivity(gotoGoogle);
                }
            }
        });

        final TextView textView = (TextView) findViewById(R.id.text);


    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    private void captureFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    String imageSelected = selectedImage.toString();

                    Intent startIntent = new Intent(getApplicationContext(), SecondActivity.class);
                    startIntent.putExtra("com.example.receipt_app.SOMETHING", imageSelected);
                    startActivity(startIntent);
                    break;
            }
    }
}


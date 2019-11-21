package com.example.androidlabs;

import android.util.Log;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "activity_profile";

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageButton mImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent dataFromPreviousPage = getIntent();
        String emailFromPage1 = dataFromPreviousPage.getStringExtra("typedEmail");

        EditText typedEmail = findViewById(R.id.editText4);
        typedEmail.setText(emailFromPage1);

        //getting the imageButton
        mImageButton = (ImageButton)findViewById(R.id.imageButton4);

        if(mImageButton != null){
            mImageButton.setOnClickListener(clk -> {
                dispatchTakePictureIntent();
            }); // end clickListener
        }

        //use a Lambda function to set a click listener to go to the activity_chatroom
        Button chatRoomButton = (Button)findViewById(R.id.button4);
        if( chatRoomButton!= null){
            chatRoomButton.setOnClickListener(clk -> {
                Intent goToChatRoomPage = new Intent(this, ChatRoomActivity.class);
                startActivityForResult(goToChatRoomPage, 30);
            });
        }


        //use a Lambda function to set a click listener to go to the activity_chatroom
        Button weatherButton = (Button)findViewById(R.id.button7);
        if( weatherButton!= null){
            weatherButton.setOnClickListener(clk -> {
                Intent goToWeatherPage = new Intent(this, WeatherForecast.class);
                startActivityForResult(goToWeatherPage, 30);
            });
        }

        Button toToolBar = findViewById(R.id.goToolBar);
        if (toToolBar != null) {
            toToolBar.setOnClickListener(clk -> {
                Intent goToToolbarPage = new Intent(this, TestToolbar.class);
                startActivity(goToToolbarPage);
            });
        }


        Log.e(ACTIVITY_NAME, "In function: OnCreate()");
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //don't get the parameters part and the super part
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
        Log.e(ACTIVITY_NAME, "In function: onActivityResult()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In function: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function: onDestroy()");
    }

}

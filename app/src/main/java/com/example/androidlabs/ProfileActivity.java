package com.example.androidlabs;

import android.util.Log;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.EditText;

import android.widget.TextView;

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
        ImageButton mImageButton = (ImageButton)findViewById(R.id.imageButton4);

        if(mImageButton != null){
            mImageButton.setOnClickListener(clk -> {
                dispatchTakePictureIntent();
            }); // end clickListener
        }

        Log.e(ACTIVITY_NAME, "we're logging from oncreate() method!");
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
        Log.e(ACTIVITY_NAME, "we're logging from onActivityResult() method!");
    }

}

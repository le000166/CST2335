package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailInput = findViewById(R.id.emailInput);

        EditText emailPassword = findViewById(R.id.emailPassword);

        SharedPreferences prefs = getSharedPreferences("UserEmail", MODE_PRIVATE);
        String previous = prefs.getString("Email", "");

        emailInput.setText(previous);

        //use a Lambda function to set a click listener
        Button login = (Button)findViewById(R.id.loginButton);
        if( login!= null){
            login.setOnClickListener(clk -> {
                Intent goToPage2 = new Intent(MainActivity.this, ProfileActivity.class);

                goToPage2.putExtra("typedEmail", emailInput.getText().toString());

                startActivityForResult(goToPage2, 30);
            });
        }

    }



    @Override
    protected void onPause() {
        super.onPause();

        EditText emailInput = findViewById(R.id.emailInput);

        SharedPreferences prefs = getSharedPreferences("UserEmail", MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Email", emailInput.getText().toString());

        editor.commit();

    }


}

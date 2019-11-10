package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForecast extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "WeatherForcast";
    String iconName;
    //String imageUrl = "http://openweathermap.org/img/w/" + iconName + ".png";


    TextView temperatureText;
    TextView minText;
    TextView maxText;
    TextView uvRating;
    ImageView weatherImage;

    ProgressBar myProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        //getting the progressBar
        myProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        temperatureText = (TextView) findViewById(R.id.temperature);
        minText = (TextView) findViewById(R.id.min);
        maxText = (TextView) findViewById(R.id.max);
        uvRating = (TextView) findViewById(R.id.uvRating);
        weatherImage = (ImageView) findViewById(R.id.weatherImage);


        ForecastQuery theQuery = new ForecastQuery();
        theQuery.execute();
        myProgressBar.setVisibility(View.VISIBLE);

        //Custom
        Button refreshButton = (Button)findViewById(R.id.refreshButton);
        if( refreshButton!= null){
            refreshButton.setOnClickListener(clk -> {

                try{Thread.sleep(500);} catch(Exception e){ }
                onRestart();
            });
        }

    }

    protected static Bitmap getImage(URL url) {

        HttpURLConnection iconConn = null;
        try {
            iconConn = (HttpURLConnection) url.openConnection();
            iconConn.connect();
            int response = iconConn.getResponseCode();
            if (response == 200) {
                return BitmapFactory.decodeStream(iconConn.getInputStream());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (iconConn != null) {
                iconConn.disconnect();
            }
        }
    }

    public boolean fileExistance(String fileName) {
//        Log.i(ACTIVITY_NAME, getBaseContext().getFileStreamPath(fileName).toString());
        File file = getBaseContext().getFileStreamPath(fileName);
        return file.exists();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(WeatherForecast.this, WeatherForecast.class);  //your class

        startActivity(i);
        finish();
    }





    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        String currentTemperature;
        String min;
        String max;
        String iconName;
        Bitmap image = null;
        String ret = null;
        String uvRatingValue;

        @Override
        protected String doInBackground(String... strings) {
            ret = null;
            String queryURL = "http://api.openweathermap.org/data/2.5/" +
                    "weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
                // Connecting to the server


            for(int i = 0; i < 100; i++)
            {

                publishProgress(i);


                try {
                    URL url = new URL(queryURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream inStream = urlConnection.getInputStream();

                    //Set up the XML parser:
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(inStream, "UTF-8");

                    //Iterate over the XML tags:
                    int EVENT_TYPE;         //While not the end of the document:
                    while ((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT) {
                        switch (EVENT_TYPE) {
                            case START_TAG:         //This is a start tag < ... >
                                String tagName = xpp.getName(); // What kind of tag?
                                if (tagName.equals("temperature")) {
                                    //saving the string tag with names
                                    //XMLPullParser has a getAttributeValue(String namespace, String name)
                                    // function which will give you those values
                                    currentTemperature = xpp.getAttributeValue(null, "value");
                                    //publishProgress(25);

                                    min = xpp.getAttributeValue(null, "min");
                                    //ublishProgress(50);

                                    max = xpp.getAttributeValue(null, "max");
                                    //publishProgress(75);

                                } else if (tagName.equals("weather")) {
                                    iconName = xpp.getAttributeValue(null, "icon");
//                                    iconName = "11d";
                                    String iconNameFile = iconName + ".png";

                                    //If the Image file exists, then you don’t need to re-download it, just read it from your disk:
                                    if (fileExistance(iconNameFile)) {
                                        FileInputStream inputStream = null;
                                        try {
                                            inputStream = new FileInputStream(getBaseContext().getFileStreamPath(iconNameFile));
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }

                                        image = BitmapFactory.decodeStream(inputStream);
//                                        Write Log.i() messages showing which filename you are looking for,
//                                        and a message saying if you found the image locally, or if you need to download it.
                                        Log.i(ACTIVITY_NAME, "Image already exists");
                                    } else {
                                        URL iconUrl = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                                        image = getImage(iconUrl);
                                        FileOutputStream outputStream = openFileOutput(iconNameFile, Context.MODE_PRIVATE);
                                        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                        outputStream.flush();
                                        outputStream.close();
                                        Log.i(ACTIVITY_NAME, "Adding new image");
                                    }
                                    Log.i(ACTIVITY_NAME, "file name=" + iconNameFile);

//                                    publishProgress(100);
                                }
                                break;
                            case END_TAG:           //This is an end tag: </ ... >
                                break;
                            case TEXT:              //This is text between tags < ... > Hello world </ ... >
                                break;
                        }
                        xpp.next(); // move the pointer to next XML element
                    }

                } catch (MalformedURLException mfe) {
                    ret = "Malformed URL exception";
                } catch (IOException ioe) {
                    ret = "IO Exception. Is the Wifi connected?";
                } catch (XmlPullParserException pe) {
                    ret = "XML Pull exception. The XML is not properly formed";
                }

                //Getting UV Rating

                double lat = 45.348945, lon = -75.759389;

                String jsonUrl = "http://api.openweathermap.org/data/2.5/uvi?" +
                        "appid=7e943c97096a9784391a981c4d878b22&lat="+lat+"&lon="+lon;

                try {       // Connect to the server:
                    URL url = new URL(jsonUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream inStream = urlConnection.getInputStream();

                    //Set up the JSON object parser:
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }

                    JSONObject obj = new JSONObject(sb.toString());
                    uvRatingValue = obj.getString("value");
                    Log.d("Response: ", "> " + line);

                }
                catch (MalformedURLException e) { e.printStackTrace(); }
                catch(Exception e){ e.printStackTrace();}
            }
                //What is returned here will be passed as a parameter to onPostExecute:
                return ret;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myProgressBar.setMax(100);
        }

        @Override                   //Type 3 of Inner Created Class
        protected void onPostExecute(String results) {
                super.onPostExecute(results);
            String degree = Character.toString((char) 0x00B0);
                temperatureText.setText(temperatureText.getText()+" "+currentTemperature+degree+"C");
                minText.setText(minText.getText()+" "+min+degree+"C");
                maxText.setText(maxText.getText()+" "+max+degree+"C");
                uvRating.setText(uvRating.getText()+" "+uvRatingValue);
            weatherImage.setImageBitmap(image);
            myProgressBar.setVisibility(View.INVISIBLE);
        }

        @Override                       //Type 2 of Inner Class
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            myProgressBar.setProgress(values[0]);
            myProgressBar.setVisibility(View.VISIBLE);
        }
    }



}

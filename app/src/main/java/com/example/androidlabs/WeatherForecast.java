package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForecast extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "Weather_Forecast_Activity";

    private Context thisApp ;

    TextView valueText;
    TextView minText;
    TextView maxText;
    TextView uvRating;

    ProgressBar myProgessBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        thisApp = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        //getting the progressBar
        myProgessBar = (ProgressBar) findViewById(R.id.progressBar);

        ForecastQuery theQuery = new ForecastQuery();
        theQuery.execute();

    }


    private class ForecastQuery extends AsyncTask<String, Integer, String[]> {

        String currentTemperature;
        String min;
        String max;
        String iconName;

        String ret = null;

        @Override
        protected void onPreExecute(){
            valueText = (TextView)findViewById(R.id.temperature);
            minText = (TextView)findViewById(R.id.min);
            maxText = (TextView)findViewById(R.id.max);
            uvRating = (TextView)findViewById(R.id.uvRating);
        }



        @Override
        protected String doInBackground(String... strings) {





            String queryURL = "http://api.openweathermap.org/data/2.5/" +
                    "weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";

            try {       // Connect to the server:
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //Set up the XML parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");

                //Iterate over the XML tags:
                int EVENT_TYPE;         //While not the end of the document:
                while((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT)
                {
                    switch(EVENT_TYPE)
                    {
                        case START_TAG:         //This is a start tag < ... >
                            String tagName = xpp.getName(); // What kind of tag?
                            if(tagName.equals("temperature"))
                            {

                                //saving the string tag with names
                                //XMLPullParser has a getAttributeValue(String namespace, String name)
                                // function which will give you those values
                                currentTemperature = xpp.getAttributeValue(null, "value");
                                publishProgress(25, 50, 75);


                                  min = xpp.getAttributeValue(null, "min");
                                publishProgress(25, 50, 75);

                                  max = xpp.getAttributeValue(null, "max");
                                publishProgress(25, 50, 75);
                            } else if (tagName.equals("weather")) {
                                iconName = xpp.getAttributeValue(null, "icon");
                                publishProgress(25, 50, 75);
                            }
                            break;
                        case END_TAG:           //This is an end tag: </ ... >
                            break;
                        case TEXT:              //This is text between tags < ... > Hello world </ ... >
                            break;
                    }
                    xpp.next(); // move the pointer to next XML element
                }
            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?";}
            catch(XmlPullParserException pe){ ret = "XML Pull exception. The XML is not properly formed" ;}
            //What is returned here will be passed as a parameter to onPostExecute:

            return ret;
        }

        @Override                   //Type 3
        protected void onPostExecute(String...values) {



                super.onPostExecute(values);

                minText.setText(values[0]+" is " +values[1]);

            myProgessBar.setVisibility(ProgressBar.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            myProgessBar.setVisibility(ProgressBar.VISIBLE);

            myProgessBar.setProgress(values[0]);
        }
    }


}

package com.example.rifat.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class WeatherActivity extends Activity {

    protected static final String ACTIVITY_NAME = "WeatherActivity";
    private ProgressBar progressBar;
    private TextView textViewCurrentTemp;
    private TextView textViewMaxTemp;
    private TextView textViewMinTemp;
    private ImageView imageViewWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        textViewCurrentTemp = findViewById(R.id.textViewCurrentTemp);
        textViewMaxTemp = findViewById(R.id.textViewMaxTemp);
        textViewMinTemp = findViewById(R.id.textViewMinTemp);
        imageViewWeather = findViewById(R.id.imageViewCurrentWeather);

        ForcastQuery task = new ForcastQuery();
        task.execute();
    }

    private class ForcastQuery extends AsyncTask<String, Integer, String>{
        private String windSpeed;
        private String currentTemp;
        private String maxTemp;
        private String minTemp;
        private Bitmap weatherImage;
        private final String ns = null;
        private String icon;

        @Override
        protected String doInBackground(String... strings) {

            getWeatherData();

            getWeatherImage();

            return null;
        }

        private void getWeatherData() {
            HttpURLConnection conn = null;

            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                InputStream in = conn.getInputStream();

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();
                parseWeatherDaata(parser);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (ProtocolException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        private void parseWeatherDaata (XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "current");
            while (parser.next() != XmlPullParser.END_TAG)
            {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();

                if (name.equals("temperature")) {
                    readTemperature(parser);
                } else if (name.equals("weather")) {
                    readWeather(parser);
                } else if (name.equals("wind")) {
                    readWind(parser);
                } else {
                    skip(parser);
                }
            }
        }

        private void readTemperature(XmlPullParser parser)throws XmlPullParserException, IOException{
            parser.require(XmlPullParser.START_TAG, ns, "temperature");
            currentTemp = parser.getAttributeValue(null, "value");
            publishProgress(25);
            minTemp = parser.getAttributeValue(null, "min");
            publishProgress(50);
            maxTemp = parser.getAttributeValue(null, "max");
            publishProgress(75);
            parser.nextTag();
            parser.require(XmlPullParser.END_TAG, ns, "temperature");
        }

        private void readWeather(XmlPullParser parser)throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "weather");
            icon = parser.getAttributeValue(null, "icon");
            parser.nextTag();
            parser.require(XmlPullParser.END_TAG, ns, "weather");
        }

        private void readWind(XmlPullParser parser) throws XmlPullParserException, IOException{
            parser.require(XmlPullParser.START_TAG, ns, "wind");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("speed")) {
                    readSpeed(parser);
                }
                else {
                        skip(parser);
                }
            }
            parser.require(XmlPullParser.END_TAG, ns, "wind");
        }

        private void readSpeed(XmlPullParser parser)throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "speed");
            windSpeed = parser.getAttributeValue(null, "value");
            parser.nextTag();
            parser.require(XmlPullParser.END_TAG, ns, "speed");
        }
        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }

        private void getWeatherImage(){
            String fileName = icon + ".png";
            Log.i(ACTIVITY_NAME, "Searching image file: "+fileName);

            if(fileExistance(fileName)){
                Log.i(ACTIVITY_NAME, "Found file locally");
                FileInputStream fis = null;
                try {
                    fis = openFileInput(fileName);
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                weatherImage = BitmapFactory.decodeStream(fis);

            }else {
                Log.i(ACTIVITY_NAME, "Downloading file");
                weatherImage = getImage("http://openweathermap.org/img/w/" + icon + ".png");
                publishProgress(100);

                FileOutputStream outputStream = null;
                try {
                    outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                    weatherImage.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        private  Bitmap getImage(String urlString) {
            try {
                URL url = new URL(urlString);
                return getImage(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }

        private Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void  onProgressUpdate(Integer... progress){
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result){
            textViewCurrentTemp.setText(currentTemp);
            textViewMaxTemp.setText(maxTemp);
            textViewMinTemp.setText(minTemp);
            imageViewWeather.setImageBitmap(weatherImage);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}

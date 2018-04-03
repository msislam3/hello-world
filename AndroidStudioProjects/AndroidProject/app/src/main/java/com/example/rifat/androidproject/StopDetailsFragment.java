package com.example.rifat.androidproject;


import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class StopDetailsFragment extends Fragment {
    private static final String ACTIVITY_NAME = "StopDetailsFragment";
    private long id=0;
    private int position = 0;
    private int stopNumber = 0;
    private boolean isTablet;

    private String stopNo;
    private String stopDescription;
    private String error;

    private TextView textViewStopNumber;
    private Button buttonRemoveStop;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getLong(OCTranspoDatabaseHelper.KEY_ID);
            stopNumber = bundle.getInt(OCTranspoDatabaseHelper.KEY_STOPNUMBER);
            position = bundle.getInt(StopsActivity.STOP_POSITION);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stop_details, container, false);

        textViewStopNumber = view.findViewById(R.id.stopNumberText);
        textViewStopNumber.setText(Integer.toString(stopNumber));

        buttonRemoveStop = view.findViewById(R.id.stopRemoveButton);
        buttonRemoveStop.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(isTablet){
                    StopsActivity stopsActivity = (StopsActivity) getActivity();
                    stopsActivity.removeStop(id, position);
                }else{
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(OCTranspoDatabaseHelper.KEY_ID, id);
                    resultIntent.putExtra(StopsActivity.STOP_POSITION, position);
                    getActivity().setResult(StopsActivity.STOP_DELETE_RESPONSE, resultIntent);
                    getActivity().finish();
                }
            }
        });

        progressBar = view.findViewById(R.id.stopDetailsProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        StopQuery task = new StopQuery();
        task.execute();

        return view;
    }
    
    public void setIsTablet(boolean isTablet){
        this.isTablet = isTablet;
    }

    private class StopQuery extends AsyncTask<Integer, Integer, Integer>{
        private final String ns = null;

        @Override
        protected Integer doInBackground(Integer... integers) {
            Log.i(ACTIVITY_NAME, "Starting background task");

            HttpURLConnection conn = null;

            try {
                URL url = new URL("https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="+stopNumber);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                InputStream in = conn.getInputStream();

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();
                parseStopData(parser);
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

            return 0;
        }

        private void parseStopData(XmlPullParser parser) throws XmlPullParserException, IOException{
            while (parser.next() != XmlPullParser.END_TAG)
            {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();
                Log.i(ACTIVITY_NAME, "tag: "+name);
                if (name.equalsIgnoreCase("soap:Body")) {

                } else if (name.equalsIgnoreCase("GetRouteSummaryForStopResponse")){

                } else if (name.equalsIgnoreCase("GetRouteSummaryForStopResult")){

                } else if (name.equalsIgnoreCase("StopNo")) {
                    readStopNo(parser);
                } else if (name.equalsIgnoreCase("StopDescription")) {
                    readStopDescription(parser);
                } else if (name.equalsIgnoreCase("Error")) {
                    readError(parser);
                } else if (name.equalsIgnoreCase("Routes")) {
                    readRoutes(parser);
                } else {
                    skip(parser);
                }
            }
        }

        private void readRoutes(XmlPullParser parser) throws XmlPullParserException, IOException{
            while (parser.next() != XmlPullParser.END_TAG)
            {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();
                Log.i(ACTIVITY_NAME, "tag: "+name);
                if (name.equalsIgnoreCase("Route")) {
                    readRoute(parser);
                } else {
                    skip(parser);
                }
            }
        }

        private void readRoute(XmlPullParser parser) throws XmlPullParserException, IOException{
            while (parser.next() != XmlPullParser.END_TAG)
            {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();
                Log.i(ACTIVITY_NAME, "tag: "+name);
                if (name.equalsIgnoreCase("RouteNo")) {
                    String routeNo = readText(parser);
                    Log.i(ACTIVITY_NAME, "routeNo: "+routeNo);
                } else if (name.equalsIgnoreCase("DirectionID")) {
                    String directionId = readText(parser);
                    Log.i(ACTIVITY_NAME, "directionId: "+directionId);
                } else if (name.equalsIgnoreCase("Direction")) {
                    String direction = readText(parser);
                    Log.i(ACTIVITY_NAME, "direction: "+direction);
                } else if (name.equalsIgnoreCase("RouteHeading")) {
                    String routeHeading = readText(parser);
                    Log.i(ACTIVITY_NAME, "routeHeading: "+routeHeading);
                } else {
                    skip(parser);
                }
            }
        }

        private void readError(XmlPullParser parser) throws XmlPullParserException, IOException{
            parser.require(XmlPullParser.START_TAG, ns, "Error");
            error = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "Error");
        }

        private void readStopDescription(XmlPullParser parser) throws XmlPullParserException, IOException{
            parser.require(XmlPullParser.START_TAG, ns, "StopDescription");
            stopDescription = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "StopDescription");
        }

        private void readStopNo(XmlPullParser parser) throws XmlPullParserException, IOException{
            parser.require(XmlPullParser.START_TAG, ns, "StopNo");
            stopNo = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "StopNo");
        }

        private String readText(XmlPullParser parser) throws XmlPullParserException, IOException{
            String result = "";
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            return result;
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

        @Override
        protected void onPostExecute(Integer integer) {
            progressBar.setVisibility(View.INVISIBLE);

            Log.i(ACTIVITY_NAME, "stopNo: "+stopNo);
            Log.i(ACTIVITY_NAME, "stopDescription: "+stopDescription);
            Log.i(ACTIVITY_NAME, "error: "+error);
        }
    }

}

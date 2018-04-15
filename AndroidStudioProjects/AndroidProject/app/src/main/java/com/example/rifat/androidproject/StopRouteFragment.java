package com.example.rifat.androidproject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class StopRouteFragment extends Fragment {
    private static final String ACTIVITY_NAME = "StopRouteFragment";

    private String stopNumber;
    private String routeNumber;
    private String routeDirection;
    private String routeHeading;
    private boolean isTablet;
    private ListView listViewTrips;
    private ArrayList<Trip> trips = new ArrayList();
    private TripsAdapter tripsAdapter;

    TextView textViewRouteNo;
    TextView textViewRouteDirection;
    TextView textViewRouteHeading;
    TextView textViewAverageAdjustedScheduleTime;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            stopNumber = bundle.getString(OCTranspoDatabaseHelper.KEY_STOPNUMBER);
            routeNumber = bundle.getString(StopDetailsFragment.ROUTE_NO);
            routeDirection = bundle.getString(StopDetailsFragment.ROUTE_DIRECTION);
            routeHeading = bundle.getString(StopDetailsFragment.ROUTE_HEADING);
        }

        View view = inflater.inflate(R.layout.fragment_stop_route, container, false);

        textViewRouteNo = view.findViewById(R.id.ocStopRouteTextViewRouteNo);
        textViewRouteDirection = view.findViewById(R.id.ocStopRouteTextViewRouteDirection);
        textViewRouteHeading = view.findViewById(R.id.ocStopRouteTextViewRouteHeading);
        textViewAverageAdjustedScheduleTime = view.findViewById(R.id.ocStopRouteTextViewRouteAdjustedSchedule);
        progressBar = view.findViewById(R.id.ocStopRouteProgressBar);
        listViewTrips = view.findViewById(R.id.ocStopRouteListViewRouteDetails);

        textViewRouteNo.setText(routeNumber);
        textViewRouteDirection.setText(routeDirection);
        textViewRouteHeading.setText(routeHeading);

        tripsAdapter = new TripsAdapter(getActivity());
        listViewTrips.setAdapter(tripsAdapter);

        progressBar.setVisibility(View.VISIBLE);

        RouteQuery task = new RouteQuery();
        task.execute();

        return view;
    }

    public void setIsTablet(boolean isTablet){this.isTablet = isTablet;}

    private class RouteQuery extends AsyncTask<Integer, Integer, Integer> {
        private final String ns = null;
        private final String REQUEST_GET = "GET";

        private final String TRIP_START_TIME = "TripStartTime";
        private final String TRIP_LATITUDE = "Latitude";
        private final String TRIP_LONGITUDE = "Longitude";
        private final String TRIP_GPS_SPEED = "GPSSpeed";
        private final String TRIP_ADJUSTED_TIME = "AdjustedScheduleTime";
        private final String TRIP_DESTINATION = "TripDestination";
        private final String ERROR = "Error";
        private final String TRIPS = "Trips";
        private final String TRIP = "Trip";
        private final String GET_NEXT_TRIPS_FOR_STOP_RESULT = "GetNextTripsForStopResult";
        private final String GET_NEXT_TRIPS_FOR_STOP_RESPONSE = "GetNextTripsForStopResponse";
        private final String SOAP_BODY = "soap:Body";
        private final String ROUTE = "Route";
        private final String ROUTE_DIRECTION = "RouteDirection";
        private final String DIRECTION = "Direction";

        private ArrayList<Trip> downloadedTrips = new ArrayList<>();
        private String  error;
        private String direction;
        private boolean errorExist = false;
        private AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        private double totalAdjustedTime = 0;

        @Override
        protected Integer doInBackground(Integer... integers) {
            Log.i(ACTIVITY_NAME, "Starting background task");

            HttpURLConnection conn = null;

            try {
                URL url = new URL("https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="+stopNumber+"&routeNo="+routeNumber);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod(REQUEST_GET);
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
                CreateDialog(R.string.ocStopDetailsQueryError);
                errorExist = true;
                //e.printStackTrace();
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
                if (name.equalsIgnoreCase(SOAP_BODY)) {

                } else if (name.equalsIgnoreCase(GET_NEXT_TRIPS_FOR_STOP_RESPONSE)){

                } else if (name.equalsIgnoreCase(GET_NEXT_TRIPS_FOR_STOP_RESULT)){

                } else if (name.equalsIgnoreCase(ROUTE)) {
                    readRoute(parser);
                }
                else if (name.equalsIgnoreCase(ERROR)) {
                    readError(parser);
                }
                else {
                    skip(parser);
                }
            }
        }

        private void readRoute(XmlPullParser parser) throws XmlPullParserException, IOException {
            while (parser.next() != XmlPullParser.END_TAG)
            {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();
                Log.i(ACTIVITY_NAME, "tag: "+name);
                if(name.equalsIgnoreCase(ROUTE_DIRECTION)){
                    readRouteDirection(parser);
                } else {
                    skip(parser);
                }
            }
        }

        private void readRouteDirection(XmlPullParser parser) throws XmlPullParserException, IOException {
            while (parser.next() != XmlPullParser.END_TAG)
            {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();
                Log.i(ACTIVITY_NAME, "tag: "+name);
                if (name.equalsIgnoreCase(DIRECTION)) {
                    readDirection(parser);
                } else if (name.equalsIgnoreCase(TRIPS)) {
                    if(direction!= null && routeDirection!=null && direction.equalsIgnoreCase(routeDirection)){
                        readTrips(parser);
                    }
                    else{
                        skip(parser);
                    }
                } else {
                    skip(parser);
                }
            }
        }

        private void readDirection(XmlPullParser parser) throws XmlPullParserException, IOException{
            parser.require(XmlPullParser.START_TAG, ns, DIRECTION);
            direction = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, DIRECTION);
        }

        private void readTrips(XmlPullParser parser) throws XmlPullParserException, IOException{
            while (parser.next() != XmlPullParser.END_TAG)
            {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();
                Log.i(ACTIVITY_NAME, "tag: "+name);
                if (name.equalsIgnoreCase(TRIP)) {
                    readTrip(parser);
                } else {
                    skip(parser);
                }
            }
        }

        private void readTrip(XmlPullParser parser) throws XmlPullParserException, IOException{
            Trip trip = new Trip();
            while (parser.next() != XmlPullParser.END_TAG)
            {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();
                Log.i(ACTIVITY_NAME, "tag: "+name);
                if (name.equalsIgnoreCase(TRIP_START_TIME)) {
                    String tripStartTime = readText(parser);
                    trip.setTripStartTime(tripStartTime);
                } else if (name.equalsIgnoreCase(TRIP_LATITUDE)) {
                    String tripLatitude = readText(parser);
                    trip.setLatitude(tripLatitude);
                } else if (name.equalsIgnoreCase(TRIP_LONGITUDE)) {
                    String tripLongitude = readText(parser);
                    trip.setLongitude(tripLongitude);
                } else if (name.equalsIgnoreCase(TRIP_GPS_SPEED)) {
                    String tripGPSSpeed = readText(parser);
                    trip.setGpsSpeed(tripGPSSpeed);
                }else if (name.equalsIgnoreCase(TRIP_ADJUSTED_TIME)) {
                    String tripAdjustedTime = readText(parser);
                    trip.setAdjustedScheduleTime(tripAdjustedTime);
                    if(tripAdjustedTime != null && !tripAdjustedTime.isEmpty()) {
                        double adjustedTime;
                        try{
                            adjustedTime = Double.parseDouble(tripAdjustedTime);
                        }catch(NumberFormatException ex){
                            adjustedTime = 0;
                        }
                        totalAdjustedTime += adjustedTime;
                    }
                }else if (name.equalsIgnoreCase(TRIP_DESTINATION)) {
                    String tripDestination = readText(parser);
                    trip.setDestination(tripDestination);
                } else {
                    skip(parser);
                }
            }
            downloadedTrips.add(trip);
        }

        private void readError(XmlPullParser parser) throws XmlPullParserException, IOException{
            parser.require(XmlPullParser.START_TAG, ns, ERROR);
            error = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, ERROR);
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

        private void CreateDialog(int messageId){

            builder.setMessage(messageId)
                    .setNeutralButton(R.string.ocOK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
        }

        @Override
        protected void onPostExecute(Integer integer) {

            if(errorExist){
                builder.create().show();
            } else if(downloadedTrips.size() > 0) {
                textViewAverageAdjustedScheduleTime.setText(String.format("%s %.2f",getString(R.string.ocAverageAdjustedScheduleTime),totalAdjustedTime/downloadedTrips.size()));
                trips.addAll(downloadedTrips);
                tripsAdapter.notifyDataSetChanged();
            }else{
                CreateDialog(R.string.ocNoTrip);
                builder.create().show();
            }

            if(error!= null && error.equals("2")){
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.ocTripQueryError), Toast.LENGTH_LONG).show();
            }else if(error!= null && !error.isEmpty()){
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.ocTripQueryError), Toast.LENGTH_LONG).show();
            }

            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private class TripsAdapter extends ArrayAdapter<Trip> {

        public TripsAdapter(Context context){
            super(context, 0);
        }

        @Override
        public int getCount(){
            return trips.size();
        }

        @Override
        public Trip getItem(int position) {
            return trips.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View result = inflater.inflate(R.layout.trip_row, null);

            TextView textViewTripAdjustTime = result.findViewById(R.id.ocTextViewTripAdjustedTime);
            TextView textViewTripDestination = result.findViewById(R.id.ocTextViewTripDestination);
            TextView textViewTripGPSSpeed = result.findViewById(R.id.ocTextViewTripGPSSpeed);
            TextView textViewTripLatitude = result.findViewById(R.id.ocTextViewTripLatitude);
            TextView textViewTripLongitude = result.findViewById(R.id.ocTextViewTripLongitude);
            TextView textViewTripStartTime = result.findViewById(R.id.ocTextViewTripStartTime);

            Trip trip = getItem(position);
            textViewTripAdjustTime.setText(trip.getAdjustedScheduleTime());
            textViewTripDestination.setText(trip.getDestination());
            textViewTripGPSSpeed.setText(trip.getGpsSpeed());
            textViewTripLatitude.setText(trip.getLatitude());
            textViewTripLongitude.setText(trip.getLongitude());
            textViewTripStartTime.setText(trip.getTripStartTime());

            return result;
        }
    }
}

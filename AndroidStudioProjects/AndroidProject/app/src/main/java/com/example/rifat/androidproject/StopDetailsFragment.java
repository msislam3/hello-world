package com.example.rifat.androidproject;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
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

public class StopDetailsFragment extends Fragment {
    private static final String ACTIVITY_NAME = "StopDetailsFragment";
    public static final String ROUTE_NO = "Route_No";
    public static final String ROUTE_DIRECTION = "Route_Direction";
    public static final String ROUTE_HEADING = "Route_Heading";

    private long id=0;
    private int position = 0;
    private int stopNumber = 0;
    private boolean isTablet;
    private String stopNo;
    private String stopDescription;
    private String error;
    private TextView textViewStopNumber;
    private TextView textVIewStopDescription;
    private Button buttonRemoveStop;
    private ProgressBar progressBar;
    private ListView listViewRoutes;
    private ArrayList<Route> routes = new ArrayList();
    private RoutesAdapter routesAdapter;

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

        textVIewStopDescription = view.findViewById(R.id.ocStopDescriptionText);
        listViewRoutes = view.findViewById(R.id.ocListViewRoutes);
        textViewStopNumber = view.findViewById(R.id.ocStopNumberText);
        buttonRemoveStop = view.findViewById(R.id.ocStopRemoveButton);
        progressBar = view.findViewById(R.id.ocStopDetailsProgressBar);

        textViewStopNumber.setText(Integer.toString(stopNumber));

        buttonRemoveStop.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(isTablet){
                    StopsActivity stopsActivity = (StopsActivity) getActivity();
                    stopsActivity.removeStopFromFragment(id, position);
                }else{
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(OCTranspoDatabaseHelper.KEY_ID, id);
                    resultIntent.putExtra(StopsActivity.STOP_POSITION, position);
                    getActivity().setResult(StopsActivity.STOP_DELETE_RESPONSE, resultIntent);
                    getActivity().finish();
                }
            }
        });

        listViewRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString(OCTranspoDatabaseHelper.KEY_STOPNUMBER, stopNo);
                bundle.putString(ROUTE_NO, routes.get(position).getRouteNo());
                bundle.putString(ROUTE_DIRECTION, routes.get(position).getRouteDirection());
                bundle.putString(ROUTE_HEADING, routes.get(position).getRouteHeading());

                if(isTablet) {
                    StopsActivity stopsActivity = (StopsActivity) getActivity();
                    stopsActivity.showRouteTrips(bundle);
                }else{
                    StopsDetails stopsDetailsActivity = (StopsDetails) getActivity();
                    stopsDetailsActivity.showRouteTrips(bundle);
                }
            }
        });

        progressBar.setVisibility(View.VISIBLE);

        routesAdapter = new RoutesAdapter(getActivity());
        listViewRoutes.setAdapter(routesAdapter);

        StopQuery task = new StopQuery();
        task.execute();

        return view;
    }
    
    public void setIsTablet(boolean isTablet){
        this.isTablet = isTablet;
    }

    private class StopQuery extends AsyncTask<Integer, Integer, Integer>{
        private final String ns = null;
        private final String REQUEST_GET = "GET";
        private final String ROUTE_NO = "RouteNo";
        private final String DIRECTION_ID = "DirectionID";
        private final String DIRECTION = "Direction";
        private final String ROUTE_HEADING = "RouteHeading";
        private final String STOP_NO = "StopNo";
        private final String STOP_DESCRIPTION = "StopDescription";
        private final String ERROR = "Error";
        private final String ROUTES = "Routes";
        private final String ROUTE = "Route";
        private final String GET_ROUTE_SUMMARY_FOR_STOP_RESULT = "GetRouteSummaryForStopResult";
        private final String GET_ROUTE_SUMMARY_FOR_STOP_RESPONSE = "GetRouteSummaryForStopResponse";
        private final String SOAP_BODY = "soap:Body";

        private ArrayList<Route> downloadedRoutes = new ArrayList<>();
        private AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        private boolean errorExist = false;

        @Override
        protected Integer doInBackground(Integer... integers) {
            Log.i(ACTIVITY_NAME, "Starting background task");

            HttpURLConnection conn = null;

            try {
                URL url = new URL("https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="+stopNumber);
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

                } else if (name.equalsIgnoreCase(GET_ROUTE_SUMMARY_FOR_STOP_RESPONSE)){

                } else if (name.equalsIgnoreCase(GET_ROUTE_SUMMARY_FOR_STOP_RESULT)){

                } else if (name.equalsIgnoreCase(STOP_NO)) {
                    readStopNo(parser);
                } else if (name.equalsIgnoreCase(STOP_DESCRIPTION)) {
                    readStopDescription(parser);
                } else if (name.equalsIgnoreCase(ERROR)) {
                    readError(parser);
                } else if (name.equalsIgnoreCase(ROUTES)) {
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
                if (name.equalsIgnoreCase(ROUTE)) {
                    readRoute(parser);
                } else {
                    skip(parser);
                }
            }
        }

        private void readRoute(XmlPullParser parser) throws XmlPullParserException, IOException{
            Route route = new Route();
            while (parser.next() != XmlPullParser.END_TAG)
            {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();
                Log.i(ACTIVITY_NAME, "tag: "+name);
                if (name.equalsIgnoreCase(ROUTE_NO)) {
                    String routeNo = readText(parser);
                    route.setRouteNo(routeNo);
                } else if (name.equalsIgnoreCase(DIRECTION_ID)) {
                    String directionId = readText(parser);
                    route.setRouteDirectionID(directionId);
                } else if (name.equalsIgnoreCase(DIRECTION)) {
                    String direction = readText(parser);
                    route.setRouteDirection(direction);
                } else if (name.equalsIgnoreCase(ROUTE_HEADING)) {
                    String routeHeading = readText(parser);
                    route.setRouteHeading(routeHeading);
                } else {
                    skip(parser);
                }
            }
            downloadedRoutes.add(route);
        }

        private void readError(XmlPullParser parser) throws XmlPullParserException, IOException{
            parser.require(XmlPullParser.START_TAG, ns, ERROR);
            error = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, ERROR);
        }

        private void readStopDescription(XmlPullParser parser) throws XmlPullParserException, IOException{
            parser.require(XmlPullParser.START_TAG, ns, STOP_DESCRIPTION);
            stopDescription = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, STOP_DESCRIPTION);
        }

        private void readStopNo(XmlPullParser parser) throws XmlPullParserException, IOException{
            parser.require(XmlPullParser.START_TAG, ns, STOP_NO);
            stopNo = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, STOP_NO);
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
            }else {
                textVIewStopDescription.setText(stopDescription);
                routes.addAll(downloadedRoutes);
                routesAdapter.notifyDataSetChanged();

                if (error != null && error.equals("10")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.ocNotValidStopNumber), Toast.LENGTH_LONG).show();
                } else if (error != null && error.equals("2")){
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.ocStopQueryError), Toast.LENGTH_LONG).show();
                } else if (error != null && !error.isEmpty()) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.ocStopQueryError), Toast.LENGTH_LONG).show();
                }
            }

            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private class RoutesAdapter extends ArrayAdapter<Route> {

        public RoutesAdapter(Context context){
            super(context, 0);
        }

        @Override
        public int getCount(){
            return routes.size();
        }

        @Override
        public Route getItem(int position) {
            return routes.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View result = inflater.inflate(R.layout.route_row, null);

            TextView routeNo = result.findViewById(R.id.ocTextViewRouteNo);
            TextView routeDirection = result.findViewById(R.id.ocTextVIewRouteDirection);
            TextView routeHeading = result.findViewById(R.id.ocTextViewRouteHeading);

            routeDirection.setText(getItem(position).getRouteDirection());
            routeNo.setText(getItem(position).getRouteNo());
            routeHeading.setText(getItem(position).getRouteHeading());

            return result;
        }
    }
}

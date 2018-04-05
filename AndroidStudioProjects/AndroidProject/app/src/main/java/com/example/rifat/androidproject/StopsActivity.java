package com.example.rifat.androidproject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StopsActivity extends Activity {
    private static final String ACTIVITY_NAME = "Stops";
    private static  final int STOP_DETAILS_REQUEST = 20;

    public static  final int STOP_DELETE_RESPONSE =30;
    public static  final String STOP_POSITION = "Position";

    private ListView listViewStops;
    private Button buttonAddStop;
    private EditText editTextAddStop;
    private RelativeLayout stopsLayout;

    private ArrayList<Integer> stops = new ArrayList<>();
    private StopsAdapter stopsAdapter;
    private SQLiteDatabase database;
    private Cursor cursor;
    private FrameLayout frame;
    private boolean frameExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);

        listViewStops = findViewById(R.id.ocListViewStops);
        buttonAddStop = findViewById(R.id.ocButtonAddStop);
        editTextAddStop = findViewById(R.id.ocEditTextAddStop);
        stopsLayout = findViewById(R.id.stopsLayout);  frame = findViewById(R.id.ocFrameStopDetails);
        frameExists = frame != null;

        buttonAddStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Check for valid stop number

                String stopString = editTextAddStop.getText().toString();

                if (!stopString.isEmpty()) {
                    Integer stop = Integer.valueOf(stopString);

                    if(stops.contains(stop)){
                        Toast.makeText(StopsActivity.this, R.string.ocStopAlreadyAdded, Toast.LENGTH_LONG).show();
                    }else {
                        stops.add(stop);
                        addStopToDB(stop);
                        editTextAddStop.setText("");
                        Toast.makeText(StopsActivity.this, R.string.ocStopAdded, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(StopsActivity.this, R.string.ocNotValidStopNumber, Toast.LENGTH_LONG).show();
                }
            }
        });

        listViewStops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putLong(OCTranspoDatabaseHelper.KEY_ID, id);
                bundle.putInt(OCTranspoDatabaseHelper.KEY_STOPNUMBER, stops.get(position));
                bundle.putInt(STOP_POSITION, position);

                if (frameExists) {
                    StopDetailsFragment fragment = new StopDetailsFragment();
                    fragment.setIsTablet(true);
                    fragment.setArguments(bundle);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.ocFrameStopDetails, fragment);
                    fragmentTransaction.commit();
                }else {
                    Intent intent = new Intent(StopsActivity.this, StopsDetails.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, STOP_DETAILS_REQUEST);
                }
            }
        });

        stopsAdapter = new StopsAdapter(this);
        listViewStops.setAdapter(stopsAdapter);

        populateStopsList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == STOP_DETAILS_REQUEST && resultCode == STOP_DELETE_RESPONSE){
            Bundle extras = data.getExtras();
            long id = extras.getLong(OCTranspoDatabaseHelper.KEY_ID);
            int position = extras.getInt(STOP_POSITION);
            
            removeStop(id, position);
        }
    }

    private void addStopToDB(Integer stop) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OCTranspoDatabaseHelper.KEY_STOPNUMBER, stop);
        database.insert(OCTranspoDatabaseHelper.STOP_TABLE_NAME, "Empty", contentValues);

        createCursor();
    }

    private void populateStopsList(){
        OCTranspoDatabaseHelper databaseHelper = new OCTranspoDatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        createCursor();

        int columnIndex = cursor.getColumnIndex(OCTranspoDatabaseHelper.KEY_STOPNUMBER);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Integer stop = cursor.getInt(columnIndex);
            stops.add(stop);
            cursor.moveToNext();
        }
    }

    public void removeStopFromFragment(long id, int position){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.remove(fm.findFragmentById(R.id.ocFrameStopDetails));
        fragmentTransaction.commit();
        removeStop(id,position);
    }

    private void removeStop(long id, int position) {
        //TODO: Undo Button
        Snackbar.make(listViewStops, R.string.ocStopRemoved, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        database.delete(OCTranspoDatabaseHelper.STOP_TABLE_NAME, OCTranspoDatabaseHelper.KEY_ID + "=?", new String[]{Long.toString(id)});
        stops.remove(position);
        stopsAdapter.notifyDataSetChanged();
        createCursor();

    }

    private void createCursor(){
        cursor = database.query(false, OCTranspoDatabaseHelper.STOP_TABLE_NAME, null,null,null,null,null,null,null);
    }

    private class StopsAdapter extends ArrayAdapter<Integer>{

        public StopsAdapter(Context context){
            super(context, 0);
        }

        @Override
        public int getCount(){
            return stops.size();
        }

        @Override
        public Integer getItem(int position) {
            return stops.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = StopsActivity.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.stop_row, null);
            TextView stopRow = result.findViewById(R.id.textViewStop);
            stopRow.setText(getItem(position).toString());
            return result;
        }

        @Override
        public long getItemId(int position) {
           cursor.moveToPosition(position);
            int columnIndex = cursor.getColumnIndex(OCTranspoDatabaseHelper.KEY_ID);
            return cursor.getLong(columnIndex);
        }
    }
}

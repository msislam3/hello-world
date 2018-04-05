package com.example.rifat.androidproject;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OCTranspoDatabaseHelper extends SQLiteOpenHelper{

    public static final String KEY_ID = "_id";
    public static final  String KEY_STOPNUMBER = "StopNumber";
    public static final String STOP_TABLE_NAME = "Stops";

    private static final String DATABASE_NAME = "OCTranspo.db";
    private static final int DATABASE_VERSION = 1;

    public OCTranspoDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+ STOP_TABLE_NAME +" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_STOPNUMBER+" INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop table
        onCreate(sqLiteDatabase);
    }
}

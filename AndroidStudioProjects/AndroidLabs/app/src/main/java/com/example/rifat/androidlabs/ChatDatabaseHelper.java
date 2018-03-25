package com.example.rifat.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public static final String KEY_ID = "ID";
    public static  final  String KEY_MESSAGE = "Message";
    public static final String TABLE_NAME = "MESSAGES";
    private static final String DATABASE_NAME = "Messages.db";
    private static final int VERSION_NUM =2;

    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + " ("+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MESSAGE + " TEXT);");
        Log.i("ChatDatabaseHelper","Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
        Log.i("ChatDatabaseHelper","Calling onUpgrade, oldVersion= "+oldVersion+" newVersion= "+newVersion);
    }
}

package com.example.rifat.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ChatWindow extends Activity {
    private static final String ACTIVITY_NAME = "ChatWindow";

    private ListView listView;
    private EditText editText;
    private Button sendButton;
    private ArrayList<String> chatMessages = new ArrayList<>();
    private ChatAdapter messageAdapter;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        listView = findViewById(R.id.listViewChat);
        editText = findViewById(R.id.editTextChat);
        sendButton = findViewById(R.id.buttonSend);

        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String message = editText.getText().toString();

                chatMessages.add(message);
                messageAdapter.notifyDataSetChanged();

                ContentValues contentValues = new ContentValues();
                contentValues.put(ChatDatabaseHelper.KEY_MESSAGE, message);
                db.insert(ChatDatabaseHelper.TABLE_NAME, "Empty", contentValues);

                editText.setText("");
            }
        });

        messageAdapter =new ChatAdapter( this );
        listView.setAdapter (messageAdapter);

        ChatDatabaseHelper chatDatabaseHelper = new ChatDatabaseHelper(this);
        db = chatDatabaseHelper.getWritableDatabase();

        Cursor cursor =  db.query(false, ChatDatabaseHelper.TABLE_NAME, new String[]{ChatDatabaseHelper.KEY_MESSAGE}, null, null, null, null, null, null); //db.rawQuery("SELECT * FROM "+ ChatDatabaseHelper.TABLE_NAME, null);

        Log.i(ACTIVITY_NAME, "Cursor's column count= "+cursor.getColumnCount());
        for ( int i = 0; i < cursor.getColumnCount(); i++){
            Log.i(ACTIVITY_NAME, "Column name= "+cursor.getColumnName(i));
        }

        int columnIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String message = cursor.getString(columnIndex);
            chatMessages.add(message);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: "+message);
            cursor.moveToNext();
        }

        cursor.close();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        db.close();
    }

    private class ChatAdapter extends ArrayAdapter<String>{

        public ChatAdapter(Context context){
            super(context, 0);
        }

        public int getCount(){
            return chatMessages.size();
        }

        public String getItem(int position){
            return chatMessages.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = result.findViewById(R.id.message_text);
            message.setText(   getItem(position)  ); // get the string at position
            return result;
        }

        public long getId(int position){
            return position;
        }

    }
}

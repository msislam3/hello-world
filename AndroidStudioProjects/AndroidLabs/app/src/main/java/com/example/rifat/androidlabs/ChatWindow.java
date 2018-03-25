package com.example.rifat.androidlabs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {
    private static final String ACTIVITY_NAME = "ChatWindow";
    private static final int MESSAGE_DETAILS_REQUEST = 40;

    public static final int MESSAGE_DELETE_RESPONSE = 30;
    public static final String POSITION = "POSITION";

    private ListView listView;
    private EditText editText;
    private Button sendButton;
    private ArrayList<String> chatMessages = new ArrayList<>();
    private ChatAdapter messageAdapter;
    private SQLiteDatabase db;
    private Cursor cursor;
    private FrameLayout frame;
    private boolean frameExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        listView = findViewById(R.id.listViewChat);
        editText = findViewById(R.id.editTextChat);
        sendButton = findViewById(R.id.buttonSend);
        frame = findViewById(R.id.frameChat);
        frameExists = frame != null;

        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String message = editText.getText().toString();

                chatMessages.add(message);
                messageAdapter.notifyDataSetChanged();

                ContentValues contentValues = new ContentValues();
                contentValues.put(ChatDatabaseHelper.KEY_MESSAGE, message);
                db.insert(ChatDatabaseHelper.TABLE_NAME, "Empty", contentValues);
                createCursor();
                editText.setText("");
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putLong(ChatDatabaseHelper.KEY_ID, id);
                bundle.putString(ChatDatabaseHelper.KEY_MESSAGE, chatMessages.get(position));
                bundle.putInt(POSITION, position);

                //On Tablet
                if (frameExists) {
                    MessageFragment fragment = new MessageFragment();
                    fragment.setIsTablet(true);
                    fragment.setArguments(bundle);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.frameChat, fragment);
                    fragmentTransaction.commit();
                } else {
                    //On Phone
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, MESSAGE_DETAILS_REQUEST);
                }
            }
        });

        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        ChatDatabaseHelper chatDatabaseHelper = new ChatDatabaseHelper(this);
        db = chatDatabaseHelper.getWritableDatabase();

        createCursor();

        Log.i(ACTIVITY_NAME, "Cursor's column count= " + cursor.getColumnCount());
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "Column name= " + cursor.getColumnName(i));
        }

        int columnIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String message = cursor.getString(columnIndex);
            chatMessages.add(message);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + message);
            cursor.moveToNext();
        }

    }

    private void createCursor(){
        cursor = db.query(false, ChatDatabaseHelper.TABLE_NAME, new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE}, null, null, null, null, null, null); //db.rawQuery("SELECT * FROM "+ ChatDatabaseHelper.TABLE_NAME, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MESSAGE_DETAILS_REQUEST && resultCode == MESSAGE_DELETE_RESPONSE) {
            Bundle extras = data.getExtras();
            long id = extras.getLong(ChatDatabaseHelper.KEY_ID);
            int position = extras.getInt(POSITION);
            deleteMessage(id, position);
            Log.i(ACTIVITY_NAME, "Returned to ChatWindow.onActivityResult: " + id);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        cursor.close();
        db.close();
    }

    public void deleteMessageFromFragment(long id, int position){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.remove(fm.findFragmentById(R.id.frameChat));
        fragmentTransaction.commit();
        deleteMessage(id,position);
        Log.i(ACTIVITY_NAME, "Fragment called ChatWindow.deleteMessageFromFragment: " + id);
    }

    private void deleteMessage(long id, int position){
        db.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID + "=?", new String[]{Long.toString(id)});
        chatMessages.remove(position);
        messageAdapter.notifyDataSetChanged();
        createCursor();
    }

    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context context) {
            super(context, 0);
        }

        public int getCount() {
            return chatMessages.size();
        }

        public String getItem(int position) {
            return chatMessages.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result;
            if (position % 2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position
            return result;
        }

        public long getId(int position) {
            return position;
        }

        public long getItemId(int position) {
            //using the same cursor is not a good idea. when data is added to the list, cursor does not get updated so throws an exception
            cursor.moveToPosition(position);
            int columnIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID);
            return cursor.getLong(columnIndex);
        }

    }
}

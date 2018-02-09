package com.example.rifat.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    private ListView listView;
    private EditText editText;
    private Button sendButton;
    private ArrayList<String> chatMessages = new ArrayList<>();
    private ChatAdapter messageAdapter;

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
                chatMessages.add(editText.getText().toString());
                messageAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

        messageAdapter =new ChatAdapter( this );
        listView.setAdapter (messageAdapter);

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

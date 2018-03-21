package com.example.rifat.androidlabs;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageFragment extends Fragment {

    private long id=0;
    private String message ="";
    private boolean isTablet = false;
    private int position = 0;

    public void setIsTablet(boolean isTablet){
        this.isTablet = isTablet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getLong(ChatDatabaseHelper.KEY_ID);
            message = bundle.getString(ChatDatabaseHelper.KEY_MESSAGE);
            position = bundle.getInt(ChatWindow.POSITION);
        }

        View view = inflater.inflate(R.layout.message_fragment, container, false);

        TextView textViewMessage =  view.findViewById(R.id.messageDetailsText);
        TextView textViewId = view.findViewById(R.id.idText);
        Button buttonDelete = view.findViewById(R.id.deleteButton);

        textViewMessage.setText(message);
        textViewId.setText(Long.toString(id));

        buttonDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(isTablet) {
                    ChatWindow chat = (ChatWindow) getActivity();
                    chat.deleteMessageFromFragment(id, position);
                }else{
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(ChatDatabaseHelper.KEY_ID, id);
                    resultIntent.putExtra(ChatWindow.POSITION, position);
                    getActivity().setResult(ChatWindow.MESSAGE_DELETE_RESPONSE, resultIntent);
                    getActivity().finish();
                }
            }
        });

        return view;
    }
}

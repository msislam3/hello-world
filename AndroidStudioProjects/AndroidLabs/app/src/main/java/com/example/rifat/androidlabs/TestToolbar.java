package com.example.rifat.androidlabs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class TestToolbar extends AppCompatActivity {

    FloatingActionButton fab;
    String message = "You selected item 1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        Toolbar myToolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(myToolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hello Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_one:
                Log.d("Toolbar", "Option one selected");
                Snackbar.make(fab, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.action_two:
                Log.d("Toolbar", "Option two selected");
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle(R.string.title_dialog);
                builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       finish();
                    }
                });
                builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder1.create();
                dialog.show();
                break;
            case R.id.action_three:
                Log.d("Toolbar", "Option three selected");
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                final View inflatedView = inflater.inflate(R.layout.dialog, null);
                builder2.setView(inflatedView)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                               EditText text = inflatedView.findViewById(R.id.new_message);
                               message = text.getText().toString();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog dialog2 = builder2.create();
                dialog2.show();
                break;
            case R.id.action_four:
                Context context = getApplicationContext();
                CharSequence text = "Version 1.0 by Rifat Shams";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                break;
        }

        return true;
    }
}

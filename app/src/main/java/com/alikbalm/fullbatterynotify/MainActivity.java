package com.alikbalm.fullbatterynotify;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button start, stop, status, browse_track;
    static TextView textStatus, textPercent, statusText;


    // тут нужно сохранить Uri файла который выбрал ользователь в SharedPreferences
    // чтоб при перезагрузке он сохранялся




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        status = findViewById(R.id.status);
        browse_track = findViewById(R.id.browse_track);

        statusText = findViewById(R.id.statusText);

        textStatus = findViewById(R.id.textStatus);
        textPercent = findViewById(R.id.textPercent);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this,MyService.class));
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(MainActivity.this,MyService.class));
            }
        });

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serviceStatus = MyService.myServiceIsActive ? "Active" : "Inactive";
                statusText.setText("Service is " + serviceStatus);
            }
        });

        browse_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browse = new Intent(Intent.ACTION_GET_CONTENT);
                browse.setType("audio/*");
                startActivityForResult(browse,1);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK){

            Uri uri= data.getData();
            Log.i("Data", uri.toString());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

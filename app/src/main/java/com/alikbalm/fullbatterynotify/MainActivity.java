package com.alikbalm.fullbatterynotify;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button start, stop, status;
    static TextView textStatus, textPercent, statusText;
    //ConstraintLayout constarint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // страртуем сервис при запуске приложения, после чего пользователь
        // может выходить из приложения, а служба будет работать в фоне
        startService(new Intent(MainActivity.this,MyService.class));

        //constarint = (ConstraintLayout) findViewById(R.id.constarint);
        getSupportActionBar().hide();

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        status = (Button) findViewById(R.id.status);

        statusText = (TextView) findViewById(R.id.statusText);

        textStatus = (TextView) findViewById(R.id.textStatus);
        textPercent = (TextView) findViewById(R.id.textPercent);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //statusText.setText("Start Service");
                startService(new Intent(MainActivity.this,MyService.class));
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //statusText.setText("Stop Service");
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


    }
}

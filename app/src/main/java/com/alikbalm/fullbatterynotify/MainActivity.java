package com.alikbalm.fullbatterynotify;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button start, stop, status, play, pause;
    static TextView textStatus, textPercent, statusText;
    //ConstraintLayout constraint;

    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // страртуем сервис при запуске приложения, после чего пользователь
        // может выходить из приложения, а служба будет работать в фоне
        //startService(new Intent(MainActivity.this,MyService.class));

        //constarint = (ConstraintLayout) findViewById(R.id.constarint);

//        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.batareya_sveta);




        getSupportActionBar().hide();

        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        status = findViewById(R.id.status);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);

        statusText = findViewById(R.id.statusText);

        textStatus = findViewById(R.id.textStatus);
        textPercent = findViewById(R.id.textPercent);

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

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer!=null ) mediaPlayer.stop();

                mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.facebook_ringtone_pop);
                mediaPlayer.start();


            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer!=null ) {mediaPlayer.stop();}
                else {
                    Log.i(" mediaplayer stop", " == null");
                }

            }
        });

    }
}

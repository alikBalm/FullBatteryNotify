package com.alikbalm.fullbatterynotify;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button start, stop, status, browse_track, default_track, play, stop_play;
    static TextView textStatus, textPercent, statusText;

    ImageView serviceStatus;

    // тут нужно сохранить Uri файла который выбрал пользователь в SharedPreferences
    // чтоб при перезагрузке он сохранялся

    SharedPreferences sp;

    static Uri uri;

    MediaPlayer mediaPlayer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = this.getSharedPreferences("com.alikbalm.fullbatterynotify", Context.MODE_PRIVATE);

        getAudioUriFromSPIfNotNull();



        getSupportActionBar().hide();

        serviceStatus = findViewById(R.id.serviceStatus);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        status = findViewById(R.id.status);
        browse_track = findViewById(R.id.browse_track);
        default_track = findViewById(R.id.default_track);

        statusText = findViewById(R.id.statusText);


        play = findViewById(R.id.play);
        stop_play = findViewById(R.id.stop_play);

        if (!MyService.myServiceIsActive) { startService(); }

        serviceStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyService.myServiceIsActive){
                    stopService();
                } else { startService();}
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(getApplicationContext(), uri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }



//                mediaPlayer.start();
            }
        });

        stop_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });



        textStatus = findViewById(R.id.textStatus);
        textPercent = findViewById(R.id.textPercent);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!MyService.myServiceIsActive) {
                    startService(new Intent(MainActivity.this,MyService.class));

                    Log.i("MyServiceStart", " NOT running start");



                } else {
                    Log.i("MyServiceStart", " running ");
                }
                setTextForService(true);

            }


        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MyService.myServiceIsActive) {
                    stopService(new Intent(MainActivity.this,MyService.class));

                    Log.i("MyServiceStop", " running stop");



                } else {
                    Log.i("MyServiceStop", " NOT running ");
                }
                setTextForService(false);
            }

        });

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextForService(null);
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

        default_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uri = null;

                sp.edit().remove("user_choise").apply();
            }
        });


    }

    void setTextForService(Boolean start){

        if (start==null){
            String serviceStatus = MyService.myServiceIsActive ? "Active" : "Inactive";
            statusText.setText("Service is " + serviceStatus);
        } else {
            String serviceStatus = start ? "Active" : "Inactive";
            statusText.setText("Service is " + serviceStatus);
        }
    }

    void getAudioUriFromSPIfNotNull(){


        String st = sp.getString("user_choise", null);

        if (st!=null){
            uri = Uri.parse(st);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK){

            uri= data.getData();
            sp.edit().putString("user_choise",uri.toString()).apply();
            Log.i("Data", uri.toString());
        }
        //initMediaPlayer();

        super.onActivityResult(requestCode, resultCode, data);
    }


    void initMediaPlayer(){

        mediaPlayer = uri!=null? MediaPlayer.create(getApplicationContext(),uri): null;
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    void startService(){
        startService(new Intent(MainActivity.this,MyService.class));
        serviceStatus.setImageResource(R.drawable.green);
        setTextForService(true);
    }
    void stopService(){
        stopService(new Intent(MainActivity.this,MyService.class));
        serviceStatus.setImageResource(R.drawable.red);
        setTextForService(false);
    }

}

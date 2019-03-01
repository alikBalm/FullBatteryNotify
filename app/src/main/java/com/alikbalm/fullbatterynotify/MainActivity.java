package com.alikbalm.fullbatterynotify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button start, stop, status, browse_track, play, stop_play;
    static TextView textStatus, textPercent, statusText;


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

        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        status = findViewById(R.id.status);
        browse_track = findViewById(R.id.browse_track);

        play = findViewById(R.id.play);
        stop_play = findViewById(R.id.stop_play);


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
}

package com.alikbalm.fullbatterynotify;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;


public class MyService extends Service {


    MediaPlayer mp;
    static boolean myServiceIsActive = false;
    int idRaw = R.raw.batareya_sveta;

    Uri UserChoiseAudioUri;
//    boolean charging = false;

    public MyService() {
    }

    @Override
    public void onCreate() {

        //регистрируем ресивер
        this.registerReceiver(this.mBatInfoReciever,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        initMediaPlayer();


        super.onCreate();
    }

    @Override
    public void onDestroy() {
        myServiceIsActive = false;

        destroyMediaPlayer();

        //удаляем регистрацию ресивера
        this.unregisterReceiver(this.mBatInfoReciever);

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        //Log.i("My Service", " onBind");

        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        myServiceIsActive = true;
        initMediaPlayer();

        return super.onStartCommand(intent, flags, startId);
    }



    // сам ресивер
     private BroadcastReceiver mBatInfoReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int percent = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            if (status==BatteryManager.BATTERY_STATUS_CHARGING) {

                    Log.i("CHARGING ", String.valueOf(percent));


                    if (percent==100) {
                        if (mp != null) {
                            mp.start();
                        } else {
                            initMediaPlayer();
                            mp.start();
                        }

                    }

            } else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING){


                Log.i("DISCHARGING ", String.valueOf(percent));

                destroyMediaPlayer();

            }

        }
    };



    void initMediaPlayer(){

        if (mp==null) {
            mp = MediaPlayer.create(getApplicationContext(), R.raw.walter_reed);
        }
        else {
            destroyMediaPlayer();
            initMediaPlayer();
        }
    }

    void destroyMediaPlayer(){

        if (mp!=null){
            mp.release();
            mp = null;
        }
    }

}

package com.alikbalm.fullbatterynotify;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;


public class MyService extends Service {


    MediaPlayer mp;
    static boolean myServiceIsActive = false;
    int idRaw =R.raw.batareya_sveta;
//    boolean charging = false;

    public MyService() {
    }

    @Override
    public void onCreate() {

        //регистрируем ресивер
        this.registerReceiver(this.mBatInfoReciever,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        mp = MediaPlayer.create(getApplicationContext(),R.raw.facebook_ringtone_pop);


        super.onCreate();
    }

    @Override
    public void onDestroy() {
        myServiceIsActive = false;

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

        return super.onStartCommand(intent, flags, startId);
    }

    // сам ресивер
     private BroadcastReceiver mBatInfoReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //делаем что то когда получено уведомление о заряде
            //здесь нужно запустиь mp.start() если заряжается, батарея заряжена на 100%
            // апосле отсоединения mp.stop()

            //int tempInt = 0;
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int percent = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            if (status==BatteryManager.BATTERY_STATUS_CHARGING) {
                //charging = true;

                //Toast.makeText(context, " Connected ", Toast.LENGTH_SHORT).show();
                //MainActivity.textStatus.setText(String.valueOf("CHARGING"));
                // здесь код для проигрыша звука при зарядке, и 100 % заряда предположительно


                //предположительно при лупинге тру
                    Log.i(" charging ", String.valueOf(percent));


                    // нужно копать MediaPlayer
                    mp.start();



            } else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING){
                //MainActivity.textStatus.setText(String.valueOf("DISCHARGING"));
                // здесь код для того чтоб остановить звуковоспроизведение,
                // т.е. когда отключат от зарядки
                //charging = false;

                Log.i(" discharging", String.valueOf(percent));


                mp.release();

            }

        }
    };




}

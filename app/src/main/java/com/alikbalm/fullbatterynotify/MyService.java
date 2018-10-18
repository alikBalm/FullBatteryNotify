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

    public MyService() {
    }

    @Override
    public void onCreate() {
       // Log.i("My Service", " onCreate");

        //mp = MediaPlayer.create(this, R.raw.batareya_sveta);
        //mp.setLooping(true);

        //регистрируем ресивер
        this.registerReceiver(this.mBatInfoReciever,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //Log.i("My Service", " onDestroy");
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

        //Log.i("My Service", " onStartCommand");
        myServiceIsActive = true;
        //watchCharge();
        return super.onStartCommand(intent, flags, startId);
    }

    // сам ресивер
    private BroadcastReceiver mBatInfoReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //делаем что то когда получено уведомление о заряде
            //здесь нужно запустиь mp.start() если заряжается, батарея заряжена на 100%
            // апосле отсоединения mp.stop()
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int percent = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            if (status==BatteryManager.BATTERY_STATUS_CHARGING) {
                //MainActivity.textStatus.setText(String.valueOf("CHARGING"));
                // здесь код для проигрыша звука при зарядке, и 100 % заряда предположительно
                if (percent==100) {

                //предположительно при лупинге тру

                    /*mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });*/
                    //mp.prepareAsync();
                    mp = MediaPlayer.create(MyService.this,R.raw.batareya_sveta);
                    mp.setLooping(true);
                    mp.start();

                }


            } else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING){
                //MainActivity.textStatus.setText(String.valueOf("DISCHARGING"));
                // здесь код для того чтоб остановить звуковоспроизведение,
                // т.е. когда отключат от зарядки
                if (mp != null) {

                    mp.stop();
                    mp.release();
                }
            }

            //MainActivity.textPercent.setText(String.valueOf(percent));


            /*if (status == BatteryManager.BATTERY_STATUS_CHARGING) {

                int percent = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                if (percent == 100) {
                    //mp.start();
                }
            } else {
                //mp.stop();
            }*/

            //int percent = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);


            //MainActivity.textPercent.setText(String.valueOf(percent));



        }
    };

    /*private void watchCharge() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);

        // Are we charging and charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);


        MainActivity.statusText.setText(String.valueOf(BatteryManager.BATTERY_STATUS_CHARGING));
        boolean isChargingAndCharged = status == BatteryManager.BATTERY_STATUS_CHARGING &&
                                       status == BatteryManager.BATTERY_STATUS_FULL;

    }*/
}

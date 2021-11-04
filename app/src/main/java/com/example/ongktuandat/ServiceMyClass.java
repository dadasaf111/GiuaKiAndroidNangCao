package com.example.ongktuandat;

import static com.example.ongktuandat.MainActivity2.mp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class ServiceMyClass extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service đã được tạo!", Toast.LENGTH_SHORT).show();
//        mp = MediaPlayer.create(this,R.raw.buonvuongmauao_nguyenhung);
//
//        mp.setLooping(false);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Bắt đầu mở nhạc! ", Toast.LENGTH_SHORT).show();
//        mp.start();

//        return super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service đã bị hủy!", Toast.LENGTH_SHORT).show();
//        mp.stop();
    }


}

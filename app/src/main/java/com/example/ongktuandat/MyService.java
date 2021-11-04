package com.example.ongktuandat;

import static com.example.ongktuandat.MyAplication.CHANEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Tuấn Đạt","MyService onCreate");
    }


    @Override
    public IBinder onBind(Intent intent) {

        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String strDataIntent = intent.getStringExtra("key_data_intent");
        sendNotification(strDataIntent);
        return START_NOT_STICKY;
    }


    public int add(int a, int b)
    {
        return a + b;
    }

    public int sub(int a, int b)
    {
        return a - b;
    }

    public int multi(int a, int b) { return a * b; }

    public int div(int a, int b)
    {
        return a / b;
    }

    public class MyBinder extends Binder{
        public MyService getService(){
            return MyService.this;
        };
    }

    private void sendNotification(String strData) {
        Intent intent = new Intent(this, MainActivity2.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this,CHANEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setContentText(strData)
                .setContentTitle("Alo 123")
                .setSound(null)
                .build();

        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Tuấn Đạt","MyService onDestroy");
    }
}

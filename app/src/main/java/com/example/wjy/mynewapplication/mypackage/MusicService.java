package com.example.wjy.mynewapplication.mypackage;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by WJY on 2018/3/20.
 */

public class MusicService extends Service {
    final static String TAG="MusicService";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: MusicService was created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onCreate: MusicService was destroyed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

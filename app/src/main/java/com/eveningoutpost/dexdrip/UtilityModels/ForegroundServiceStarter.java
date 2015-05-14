package com.eveningoutpost.dexdrip.UtilityModels;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by stephenblack on 12/25/14.
 */
public class ForegroundServiceStarter {
    private Service mService;
    private Context mContext;
    private boolean run_service_in_foreground = false;
    private Handler mHandler;


    public ForegroundServiceStarter(Context context, Service service) {
        mContext = context;
        mService = service;
        mHandler = new Handler(Looper.getMainLooper());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        run_service_in_foreground = prefs.getBoolean("run_service_in_foreground", false);
    }

    public void start() {
        if (run_service_in_foreground) {
            Log.e("FOREGROUND", "should be moving to foreground");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Notifications notification = Notifications.getInstance(mContext);
                    notification.ReadPerfs(mContext);
                    mService.startForeground(notification.ongoingNotificationId, notification.createOngoingNotification(new BgGraphBuilder(mContext)));
                }
            });
        }
    }

    public void stop() {
        if (run_service_in_foreground) {
            Log.e("FOREGROUND", "should be moving out of foreground");
            mService.stopForeground(true);
        }
    }

}
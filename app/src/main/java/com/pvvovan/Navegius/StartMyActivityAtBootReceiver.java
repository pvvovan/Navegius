package com.pvvovan.Navegius;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.hellojni.MainActivity;

/**
 * Created by vovan on 31.12.15.
 */
public class StartMyActivityAtBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }
}

package com.example.gkudva.android_nytimes_client.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.example.gkudva.android_nytimes_client.R;

import java.io.IOException;

/**
 * Created by gkudva on 20/09/17.
 */

public class Util {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            final java.lang.Process process = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = process.waitFor();
            return (exitValue == 0);
        }catch (IOException ex){
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showSnackBar(View view, Context context) {
        Snackbar.make(view,
                R.string.no_internet_text,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.snackbar_internet_action_text,
                        getSnackBarActionOnClickListener(context))
                .setActionTextColor(Color.YELLOW)
                .setDuration(5000)
                .show();
    }

    private static View.OnClickListener getSnackBarActionOnClickListener(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(settingsIntent);
            }
        };
    }

    public static int getColorCode(Context context, String category)
    {
        int[] actionbarColor = context.getResources().getIntArray(R.array.actionbar_color);
        int index = 0;
        try {
            if (category.contains("Sports")) {
                index = 0;
            } else if (category.contains("Fashion")) {
                index = 1;
            } else {
                index = 2;
            }
        }
        catch (Exception ex)
        {

        }
        return actionbarColor[index];
    }
}

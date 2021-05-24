package com.example.looknote;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadWorker extends Worker {
    public UploadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public ListenableWorker.Result doWork() {
        // Do the work here--in this case, upload the images.
        Weather weather = new Weather();
        weather.isBackGround = true;

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        weather.todayDate = sdf.format(mDate).substring(0, 8);
        weather.todayHour = sdf.format(mDate).substring(8, 10);

        Log.d("Debug", "todayHour: " + weather.todayHour);
        try {
            if (Integer.parseInt(weather.todayHour) >= 0 && Integer.parseInt(weather.todayHour) < 6)
                weather.todayDate = Integer.toString(Integer.parseInt(weather.todayDate) - 1);
        } catch(NumberFormatException ex){ }

        weather.mpageNo = "8";
        weather.getWeatherInfo();

        Log.d("Alarm", "MUYAHO");

        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }
}

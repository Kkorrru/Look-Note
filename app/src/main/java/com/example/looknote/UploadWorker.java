package com.example.looknote;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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
        GetWeather gw = new GetWeather();

        gw.getWeather();

        dbHelper helper = new dbHelper(this.getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        while(!gw.isFinish);
        gw.isFinish = false;

        Cursor cursor = db.rawQuery("SELECT * FROM record WHERE date_num="+gw.todayDate+"", null);
        if (!cursor.moveToFirst()) // 중복 방지
            db.execSQL("INSERT INTO record VALUES (null, '"+Integer.parseInt(gw.todayDate)+"', 0, '0', '0', '0', '0', '"+gw.max_tem+"', '"+gw.min_tem+"', '"+gw.sky+"')");
        Log.d("Debug", gw.todayDate + gw.max_tem + gw.min_tem + gw.sky);
        Log.d("Debug-BackGround", "HERE");

        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }
}

/* (date_num, satisf, top_c, bottom_c, acc, diary, max_tem, min_tem, sky)
 * date_num: INTEGER
 * satisf: INTEGER
 * top_c: TEXT
 * bottom_c: TEXT
 * acc: TEXT
 * diary: TEXT
 * max_tem: REAL
 * min_tem: REAL
 * sky: INTEGER
 */
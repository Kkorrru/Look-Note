package com.example.looknote;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UploadWorker extends Worker {
    GetWeather gw = new GetWeather();

    public UploadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public ListenableWorker.Result doWork() {
        // Do the work here--in this case, upload the images.
        Log.d("Debug-BackGround", "HERE");

        // ==========위치==========
        getLocation();

        dbHelper helper = new dbHelper(this.getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        // ==========날씨==========



        // ==========DB==========
        while(!gw.weatherIsFinish); // gw.getWeather() 끝날 때까지 기다리기
        gw.weatherIsFinish = false;
        Log.d("Debug-BackGround", gw.todayDate + gw.max_tem + gw.min_tem + gw.sky);


        Cursor cursor = db.rawQuery("SELECT * FROM record WHERE date_num="+gw.todayDate+"", null); // 중복 방지
        if (!cursor.moveToFirst()) {
            db.execSQL("INSERT INTO record VALUES (null, '" + Integer.parseInt(gw.todayDate) + "', 0, null, null, null, null, '" + gw.max_tem + "', '" + gw.min_tem + "', '" + gw.sky + "')");
            Log.d("DebugInsert", gw.todayDate + gw.max_tem + gw.min_tem + gw.sky);
            return Result.success();
        }

        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }

    void getLocation() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        List<Address>[] addresses = new List[]{null};
        Geocoder geocoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                gw.latitude = location.getLatitude();
                gw.longitude = location.getLongitude();
                //Log.d("Debug", Double.toString(latitude) + " " + Double.toString(longitude));


                // ==================주소==================
                try {
                    addresses[0] = geocoder.getFromLocation(37.283479198468555, 127.04656764446653, 1);
                    //addresses[0] = geocoder.getFromLocation(latitude*10/1/10, longitude*10/1/10, 1); //latitude: 위도, longtitude: 경도
                    gw.todayLocation = addresses[0].get(0).getAddressLine(0).toString();
                    Log.d("Debug-location", gw.todayLocation);
                    gw.todayLocation = gw.todayLocation.substring(gw.todayLocation.indexOf(" ") + 1);
                    gw.todayLocation = gw.todayLocation.substring(0, gw.todayLocation.indexOf(" ") + 1 + gw.todayLocation.substring(gw.todayLocation.indexOf(" ") + 1).indexOf(" "));

                    gw.hasLocation = true;
                    switch (gw.todayLocation.substring(0, 3)) {
                        case "서울특":
                            gw.mnx = "60";
                            gw.mny = "127";
                            break;
                        case "부산광":
                            gw.mnx = "98";
                            gw.mny = "76";
                            break;
                        case "대구광":
                            gw.mnx = "89";
                            gw.mny = "90";
                            break;
                        case "인천광":
                            gw.mnx = "55";
                            gw.mny = "124";
                            break;
                        case "광주광":
                            gw.mnx = "58";
                            gw.mny = "74";
                            break;
                        case "대전광":
                            gw.mnx = "67";
                            gw.mny = "100";
                            break;
                        case "울산광":
                            gw.mnx = "102";
                            gw.mny = "84";
                            break;

                        case "경기도":
                            gw.mnx = "60";
                            gw.mny = "120";
                            break;
                        case "강원도":
                            gw.mnx = "73";
                            gw.mny = "134";
                            break;
                        case "충청북":
                            gw.mnx = "69";
                            gw.mny = "107";
                            break;
                        case "충청남":
                            gw.mnx = "68";
                            gw.mny = "100";
                            break;
                        case "전라북":
                            gw.mnx = "63";
                            gw.mny = "89";
                            break;
                        case "전라남":
                            gw.mnx = "51";
                            gw.mny = "67";
                            break;
                        case "경상북":
                            gw.mnx = "89";
                            gw.mny = "91";
                            break;
                        case "경상남":
                            gw.mnx = "91";
                            gw.mny = "77";
                            break;
                        case "제주특":
                            gw.mnx = "52";
                            gw.mny = "38";
                            break;
                        default:
                            gw.mnx = "60";
                            gw.mny = "127";
                            gw.hasLocation = false;
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            public void onProviderEnabled(String provider) { }
            public void onProviderDisabled(String provider) { }
        };

        Context thisContext = (Context)this.getApplicationContext();
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                }
                if (ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_DENIED) {
                    //Toast.makeText(thisContext, /*"First enable LOCATION ACCESS in settings."*/"위치 액세스 권한 항상 허용을 거부할 시 앱 이용에 제한이 있을 수 있습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("Debug-BackGround", "HERE2");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000 * 60, 1000, locationListener); // 1000밀리컨드 = 1초, 1000미터 = 1키로미터
            }
        }, 0);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gw.getWeather();
            }
        }, 5500);
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
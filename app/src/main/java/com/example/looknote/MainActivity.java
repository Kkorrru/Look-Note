package com.example.looknote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

class dbHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "looknote.db";
    private static final int DATABASE_VERSION = 1;

    public dbHelper(Context context)
    {
        super(context, DATABASE_NAME ,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE record(_id INTEGER PRIMARY KEY AUTOINCREMENT, date_num INTEGER, satisf INTEGER, top_c TEXT, bottom_c TEXT, acc TEXT, diary TEXT, max_tem REAL, min_tem REAL, sky INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }

}

public class MainActivity extends AppCompatActivity {
//
    BottomNavigationView bottomNavigationView;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper helper = new dbHelper(this);
        try {
            db = helper.getWritableDatabase();
        }
        catch (SQLiteException ex)
        {
            db = helper.getReadableDatabase();
        }

        //최초 실행 여부 판단하는 구문
        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isFirst", false);
        if(first==false){
            Log.d("Debug-Is first Time?", "first");
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst",true);
            editor.commit();
            //앱 최초 실행시 하고 싶은 작업
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, /*MY_PERMISSIONS_REQUEST_LOCATION*/10);
            GetWeather gw = new GetWeather();
            gw.setWorkManger();
        }else{
            Log.d("Debug-Is first Time?", "not first");
        }

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            Toast.makeText(this, /*"First enable LOCATION ACCESS in settings."*/"설정에서 위치 액세스 권한 항상 허용을 거부할 시 앱 이용에 제한이 있을 수 있습니다.", Toast.LENGTH_LONG).show();
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, /*MY_PERMISSIONS_REQUEST_LOCATION*/10);
//        }

        bottomNavigationView = findViewById(R.id.bottomNavi);
        //bottomNavigationView.setItemIconTintList(null);

        //처음화면
        //FrameLayout에 fragment.xml 띄우기
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame,new Weather() ).commit();

        //바텀 네비케이션뷰 안의 아이템 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    //item 클릭 시, id값을 가져와 FrameLayout에 fragment.xml 띄운다
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Weather()).commit();
                        return true;
                    case R.id.diary:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Diary()).commit();
                        return true;
                    default: return false;
                }

                //return false;
            }
        });
    }
}
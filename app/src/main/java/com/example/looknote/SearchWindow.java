package com.example.looknote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchWindow extends AppCompatActivity{

    int mmyear;
    int mmmonth;
    int mmdate;
    int tid;
    int todayn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_window);

        dbHelper helper = new dbHelper(this);
        SQLiteDatabase db;
        db = helper.getWritableDatabase();

        Button editButton = (Button) findViewById(R.id.button_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InputWindow.class);
                intent.putExtra("year", mmyear);
                intent.putExtra("month", mmmonth);
                intent.putExtra("day", mmdate);
                startActivity(intent);

            }
        });

        Button backButton = (Button) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        Button delButton = (Button) findViewById(R.id.button_del);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todayn = mmyear * 10000;
                todayn += mmmonth * 100;
                todayn += mmdate;

                Cursor cursorss = db.rawQuery("SELECT * FROM record WHERE date_num="+todayn+"", null);

                cursorss.moveToNext();
                tid = cursorss.getInt(0) ;
                int tmax = cursorss.getInt(7);
                int tmin = cursorss.getInt(8);
                int tsky = cursorss.getInt(9);

                db.execSQL("INSERT INTO record VALUES (null, '"+todayn+"', 0, null, null, null, null, '"+tmax+"', '"+tmin+"', '"+tsky+"')");

                db.execSQL("DELETE FROM record WHERE _id = '" + tid + "';");

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });
    }



    @Override
    public void onStart()
    {


        super.onStart();
        Intent intent = getIntent();
        mmyear = intent.getExtras().getInt("year");
        mmmonth = intent.getExtras().getInt("month");
        mmdate = intent.getExtras().getInt("day");

        String mon = null;
        if(mmmonth == 1){
            mon = "January";
        }else if(mmmonth == 2){
            mon = "February";
        }else if(mmmonth == 3){
            mon = "March";
        }else if(mmmonth == 4){
            mon = "April";
        }else if(mmmonth == 5){
            mon = "May";
        }else if(mmmonth == 6){
            mon = "June";
        }else if(mmmonth == 7){
            mon = "July";
        }else if(mmmonth == 8){
            mon = "August";
        }else if(mmmonth == 9){
            mon = "September";
        }else if(mmmonth == 10){
            mon = "October";
        }else if(mmmonth == 11){
            mon = "November";
        }else if(mmmonth == 12){
            mon = "December";
        }

        TextView dateText = (TextView)findViewById(R.id.date);
        dateText.setText(mon+" "+mmdate);

        dbHelper helper = new dbHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor;
        int thisday = mmyear*10000+mmmonth*100+mmdate;
        cursor = db.rawQuery("SELECT * FROM record WHERE date_num = "+thisday+";", null);
        cursor.moveToNext();
        String date_num, satisf, top_c, bottom_c, acc, diary, max_tem = null, min_tem, sky;
        date_num = cursor.getString(cursor.getColumnIndex("date_num"));
        satisf = cursor.getString(cursor.getColumnIndex("satisf"));
        top_c = cursor.getString(cursor.getColumnIndex("top_c"));
        bottom_c = cursor.getString(cursor.getColumnIndex("bottom_c"));
        acc = cursor.getString(cursor.getColumnIndex("acc"));
        diary = cursor.getString(cursor.getColumnIndex("diary"));
        max_tem = cursor.getString(cursor.getColumnIndex("max_tem"));
        min_tem = cursor.getString(cursor.getColumnIndex("min_tem"));
        sky = cursor.getString(cursor.getColumnIndex("sky"));

        /*표정 세팅*/
        ImageView satImage = (ImageView)findViewById(R.id.face);
        if(satisf.equals("0")) satImage.setImageResource(R.drawable.none_big);
        else if(satisf.equals("1")) satImage.setImageResource(R.drawable.cold_big);
        else if(satisf.equals("2")) satImage.setImageResource(R.drawable.cool_big);
        else if(satisf.equals("3")) satImage.setImageResource(R.drawable.nice_big);
        else if(satisf.equals("4")) satImage.setImageResource(R.drawable.warm_big);
        else if(satisf.equals("5")) satImage.setImageResource(R.drawable.hot_big);

        /*하늘 세팅*/
        ImageView skyImage = (ImageView)findViewById(R.id.weather);
        switch (sky) {
            case "1": // 비
                skyImage.setImageResource(R.drawable.rain);
                break;
            case "2": // 비/눈
                skyImage.setImageResource(R.drawable.rainnsnow);
                break;
            case "3": // 눈
                skyImage.setImageResource(R.drawable.snow);
                break;
            case "4": // 소나기
                skyImage.setImageResource(R.drawable.shower);
                break;
            case "5": // 맑음 5
                skyImage.setImageResource(R.drawable.sunny);
                break;
            case "7": // 구름 많음 7
                skyImage.setImageResource(R.drawable.cloudy);
                break;
            case "8": // 흐림 8
                skyImage.setImageResource(R.drawable.blur);
                break;
        }

        /*기온 세팅*/
        TextView tempText = (TextView)findViewById(R.id.temperature);
        tempText.setText(max_tem+"°C/"+min_tem+"°C");

        /*상의 세팅*/
        TextView topText = (TextView)findViewById(R.id.upcloth);
        if(top_c == null) topText.setText(" ");
        else topText.setText(top_c);

        /*하의 세팅*/
        TextView downText = (TextView)findViewById(R.id.downcloth);
        downText.setText(bottom_c);

        /*악세서리 세팅*/
        TextView accText = (TextView)findViewById(R.id.accer);
        accText.setText(acc);

        /*일기 세팅*/
        TextView diaryText = (TextView)findViewById(R.id.recording);
        diaryText.setText(diary);


    }
}
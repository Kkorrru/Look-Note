package com.example.looknote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InputWindow extends AppCompatActivity {

    int iyear;
    int imonth;
    int idate;

    int todayn;

    int satisnum;

    //target
    int tid;
    float tmax;
    float tmin;
    int tsky;

    int flag;

    EditText toppad;
    EditText bottompad;
    EditText accpad;
    EditText notepad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_window);

        int o = 28;
        int i;
        flag=0;

        SQLiteDatabase db;
        dbHelper helper = new dbHelper(this);
        db = helper.getWritableDatabase();

        /*for(i=23;i<=o;i++)
        {
            db.execSQL("DELETE FROM record WHERE _id = '" + i + "';");
        }*/
        //Log.d("Debug", "delete plz");

        toppad = (EditText) findViewById(R.id.top_pad);
        bottompad = (EditText) findViewById(R.id.bottom_pad);
        accpad = (EditText) findViewById(R.id.acc_pad);
        notepad = (EditText) findViewById(R.id.note_pad);

        Button saveButton = (Button) findViewById(R.id.save_button);
        /*saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });*/
    }

    public void onStart()
    {
        super.onStart();
        Intent intent = getIntent();
        iyear = intent.getExtras().getInt("year");
        imonth = intent.getExtras().getInt("month");
        idate = intent.getExtras().getInt("day");
        String mon = null;

        todayn = iyear * 10000;
        todayn += imonth * 100;
        todayn += idate;

        if(imonth == 1){
            mon = "January";
        }else if(imonth == 2){
        mon = "February";
        }else if(imonth == 3){
            mon = "March";
        }else if(imonth == 4){
            mon = "April";
        }else if(imonth == 5){
            mon = "May";
        }else if(imonth == 6){
            mon = "June";
        }else if(imonth == 7){
            mon = "July";
        }else if(imonth == 8){
            mon = "August";
        }else if(imonth == 9){
            mon = "September";
        }else if(imonth == 10){
            mon = "October";
        }else if(imonth == 11){
            mon = "November";
        }else if(imonth == 12){
            mon = "December";
        }

        TextView dateText = (TextView)findViewById(R.id.date);
        dateText.setText(mon+" "+idate);

        dbHelper helper = new dbHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor;
        int thisday = iyear*10000+imonth*100+idate;

        TextView topText = (TextView)findViewById(R.id.top_pad);
        TextView downText = (TextView)findViewById(R.id.bottom_pad);
        TextView accText = (TextView)findViewById(R.id.acc_pad);
        TextView diaryText = (TextView)findViewById(R.id.note_pad);

        cursor = db.rawQuery("SELECT * FROM record WHERE date_num = "+thisday+";", null);

        int count = cursor.getCount();
        if(cursor.getCount()==0){
            topText.setText(null);
            downText.setText(null);
            accText.setText(null);
            diaryText.setText(null);
        }
        else{
            cursor.moveToNext();
            String top_c, bottom_c, acc, diary;
            top_c = cursor.getString(cursor.getColumnIndex("top_c"));
            bottom_c = cursor.getString(cursor.getColumnIndex("bottom_c"));
            acc = cursor.getString(cursor.getColumnIndex("acc"));
            diary = cursor.getString(cursor.getColumnIndex("diary"));

            topText.setText(top_c);
            downText.setText(bottom_c);
            accText.setText(acc);
            diaryText.setText(diary);
        }
    }

    public void satisOnclick(View v)
    {
        flag=1;
        switch (v.getId())
        {
            case R.id.sat_cold:
                satisnum=1;
                Log.d("Debug", "cold");
                break;
            case R.id.sat_cool:
                satisnum=2;
                break;
            case R.id.sat_nice:
                satisnum=3;
                break;
            case R.id.sat_warm:
                satisnum=4;
                break;
            case R.id.sat_hot:
                satisnum=5;
                break;

        }
    }

    public void saveOnclick(View v)
    {
        dbHelper helper = new dbHelper(this.getApplicationContext());
        SQLiteDatabase db;
        db = helper.getWritableDatabase();

        String toppads = toppad.getText().toString();
        String bottompads = bottompad.getText().toString();
        String accpads = accpad.getText().toString();
        String notepads = notepad.getText().toString();

        //DB에서 날짜 서치해서 온도와 sky값 가져와서 저장한 후에 그 라인 없애고 diary 내용 추가해서 넣기

        //Log.d("Debug", "before c");
        Cursor cursorss = db.rawQuery("SELECT * FROM record WHERE date_num="+todayn+"", null);
        //Log.d("Debug", "after c");

        Log.d("Debug", "before i");

        /////////////////////////////////////////////////
        while (cursorss.moveToNext())
        {
            tid = cursorss.getInt(0) ;
            if (flag==0)
            {
                satisnum = cursorss.getInt(2);
            }
            //satisnum = cursorss.getInt(2);
            tmax = cursorss.getInt(7);
            tmin = cursorss.getInt(8);
            tsky = cursorss.getInt(9);
            flag=0;
        }

        //tid = cursorss.getInt(1);

        /////////////////////////////////////////////////

        Log.d("DebugInsert tid", Integer.toString(tid));
        Log.d("DebugInsert tmax", Float.toString(tmax));
        Log.d("DebugInsert tmin", Float.toString(tmin));
        Log.d("DebugInsert tsky", Integer.toString(tsky));

        Log.d("Debug", "after i");

        db.execSQL("INSERT INTO record VALUES (null, '"+todayn+"', '"+satisnum+"', '"+toppads+"', '"+bottompads+"', '"+accpads+"', '"+notepads+"', '"+tmax+"', '"+tmin+"', '"+tsky+"')");

        Log.d("Debug", "todayTop: " + toppads);
        Log.d("Debug", "tmax: " + tmax);

        db.execSQL("DELETE FROM record WHERE _id = '" + tid + "';");

        Log.d("Debug", "save click");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
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
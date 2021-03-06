package com.example.looknote;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;


public class Diary extends Fragment {

    View v;
    Button next;
    Button prev;

    int calYear;
    int calMonth;
    int calDayofMon;
    int calStart;
    final int calLastDayOfMonth[] = {31,28,31,30,31,30,31,31,30,31,30,31};
    int calLastDay;
    Calendar today = Calendar.getInstance();
    Calendar cal;

    int dateStart;
    int dateEnd;

    GridListAdapter adapter;
    int imgList[] = {R.drawable.none_icon, R.drawable.cold_icon, R.drawable.cool_icon, R.drawable.nice_icon, R.drawable.warm_icon, R.drawable.hot_icon};


    public Diary(){
        setToday();
    }

    public void setToday() { //오늘 날짜 계산 후, calYear, calMonth, calDayofMon필드 초기화 메소드
        calYear = today.get(Calendar.YEAR);
        calMonth = today.get(Calendar.MONTH);
        calDayofMon = today.get(Calendar.DAY_OF_MONTH);
        makeCalData(today);
    }

    private void makeCalData(Calendar cal) { // calendar에 대한 data 초기화 메소드
        calStart = (cal.get(Calendar.DAY_OF_WEEK)+7-(cal.get(Calendar.DAY_OF_MONTH))%7)%7;
        if(calMonth == 1) calLastDay =  calLastDayOfMonth[calMonth]+leapCheck(calYear);
        else calLastDay = calLastDayOfMonth[calMonth];

    }

    private int leapCheck(int year){ // 윤년 확인 메소드
        if(year%4 == 0 && year%100 != 0 || year%400 == 0) return 1;
        else return 0;
    }

    public void moveMonth(int mon){ // 달력 월 이동 메소드
        calMonth += mon;
        if(calMonth>11) while(calMonth>11){
            calYear++;
            calMonth -= 12;
        } else if (calMonth<0) while(calMonth<0){
            calYear--;
            calMonth += 12;
        }
        cal = new GregorianCalendar(calYear,calMonth,calDayofMon);
        makeCalData(cal);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_diary, container, false);
        GridView gridView = (GridView) v.findViewById(R.id.mainMenu);
        adapter = new GridListAdapter();
        gridView.setAdapter(adapter);
        return v;
    }

    //달 세팅 메소드
    public String retMon(int month){
        switch(month) {
            case 0:
                return "JANUARY";
            case 1:
                return "FEBRUARY";
            case 2:
                return "MARCH";
            case 3:
                return "APRIL";
            case 4:
                return "MAY";
            case 5:
                return "JUNE";
            case 6:
                return "JULY";
            case 7:
                return "AUGUST";
            case 8:
                return "SEPTEMBER";
            case 9:
                return "OCTOBER";
            case 10:
                return "NOVEMBER";
            case 11:
                return "DECEMBER";
            default:
                break;
        }
        return null;
    }

    //시작할 때
    public void onStart() {
        super.onStart();


        dbHelper helper = new dbHelper(this.getContext());
        SQLiteDatabase db = helper.getWritableDatabase();




        //초기 달력 세팅
        TextView texMon = (TextView)getView().findViewById(R.id.month);
        TextView texYear = (TextView)getView().findViewById(R.id.year);
        texMon.setText(retMon(calMonth));
        texYear.setText(Integer.toString(calYear));

        /*어뎁터 비우기*/
        int cnt = adapter.getCount();
        for(int i = cnt-1; i>=0; i--){
            adapter.delItem(i);
        }
        /*빈 칸*/
        for(int i = 0; i<calStart; i++){
            adapter.addItem(new Griditem(calYear, calMonth," ", " ", 0));
        }
        /*날짜 및 상태 출력*/
        dateStart = calYear*10000 + (calMonth+1)*100;
        dateEnd = calLastDayOfMonth[calMonth]+dateStart;

        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM record WHERE date_num > "+dateStart+" and date_num <= "+dateEnd+";", null);
        int cnt_cur = 1;

        int count = cursor.getCount();
        String date_num, satisf, max_tem = null, min_tem;
        if(count==0){
            for(int j = 1; j<=calLastDay; j++){
                adapter.addItem(new Griditem(calYear, calMonth,Integer.toString(j), " ", imgList[0]));
            }
        }
        else{
            cursor.moveToNext();
            for(int j = 1; j<=calLastDay; j++) {
                date_num = cursor.getString(cursor.getColumnIndex("date_num"));

                if (Integer.parseInt(date_num) == (dateStart + j)) {
                    satisf = cursor.getString(cursor.getColumnIndex("satisf"));
                    max_tem = cursor.getString(cursor.getColumnIndex("max_tem"));
                    min_tem = cursor.getString(cursor.getColumnIndex("min_tem"));
                    Log.v("Debug+query", satisf + ", " + max_tem + ", " + min_tem);
                    adapter.addItem(new Griditem(calYear, calMonth,Integer.toString(j), max_tem + "°C/" + min_tem + "°C", imgList[Integer.parseInt(satisf)]));
                    if(cnt_cur<count) cursor.moveToNext();
                    cnt_cur++;
                }

                else {
                    adapter.addItem(new Griditem(calYear, calMonth,Integer.toString(j), " ", imgList[0]));
                }

            }
        }


        //버튼 처리
        //next 버튼
        next = (Button)getView().findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveMonth(1); //한 달 뒤로
                int cnt = adapter.getCount();
                for(int i = cnt-1; i>=0; i--){
                    adapter.delItem(i);
                }
                /*공백 출력*/
                for(int i = 0; i<calStart; i++){
                    adapter.addItem(new Griditem(calYear, calMonth," ", " ", 0));
                }

                /*날짜 및 상태 출력*/
                dateStart = calYear*10000 + (calMonth+1)*100;
                dateEnd = calLastDayOfMonth[calMonth]+dateStart;

                Cursor cursor;
                cursor = db.rawQuery("SELECT * FROM record WHERE date_num > "+dateStart+" and date_num <= "+dateEnd+";", null);
                int cnt_cur = 1;

                int count = cursor.getCount();
                String date_num, satisf, max_tem = null, min_tem;
                if(count==0){
                    for(int j = 1; j<=calLastDay; j++){
                        adapter.addItem(new Griditem(calYear, calMonth,Integer.toString(j), " ", imgList[0]));
                    }
                }
                else{
                    cursor.moveToNext();
                    for(int j = 1; j<=calLastDay; j++) {
                        date_num = cursor.getString(cursor.getColumnIndex("date_num"));

                        if (Integer.parseInt(date_num) == (dateStart + j)) {
                            satisf = cursor.getString(cursor.getColumnIndex("satisf"));
                            max_tem = cursor.getString(cursor.getColumnIndex("max_tem"));
                            min_tem = cursor.getString(cursor.getColumnIndex("min_tem"));
                            Log.v("Debug+query", satisf + ", " + max_tem + ", " + min_tem);
                            adapter.addItem(new Griditem(calYear, calMonth,Integer.toString(j), max_tem + "°C/" + min_tem + "°C", imgList[Integer.parseInt(satisf)]));
                            if(cnt_cur<count) cursor.moveToNext();
                            cnt_cur++;
                        }

                        else {
                            adapter.addItem(new Griditem(calYear, calMonth,Integer.toString(j), " ", imgList[0]));
                        }

                    }
                }

                texYear.setText(Integer.toString(calYear));
                texMon.setText(retMon(calMonth));
                adapter.notifyDataSetChanged();
            }
        });
        //prev버튼
        prev = (Button)getView().findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                moveMonth(-1);
                int cnt = adapter.getCount();
                for(int i = cnt-1; i>=0; i--){
                    adapter.delItem(i);
                }
                for(int i = 0; i<calStart; i++){ //빈칸
                    adapter.addItem(new Griditem(calYear, calMonth," ", " ", 0));
                }
                /*날짜 및 상태 출력*/
                dateStart = calYear*10000 + (calMonth+1)*100;
                dateEnd = calLastDayOfMonth[calMonth]+dateStart;

                Cursor cursor;
                cursor = db.rawQuery("SELECT * FROM record WHERE date_num > "+dateStart+" and date_num <= "+dateEnd+";", null);
                int cnt_cur = 1;

                int count = cursor.getCount();
                String date_num, satisf, max_tem = null, min_tem;
                if(count==0){
                    for(int j = 1; j<=calLastDay; j++){
                        adapter.addItem(new Griditem(calYear, calMonth,Integer.toString(j), " ", imgList[0]));
                    }
                }
                else{
                    cursor.moveToNext();
                    for(int j = 1; j<=calLastDay; j++) {
                        date_num = cursor.getString(cursor.getColumnIndex("date_num"));

                        if (Integer.parseInt(date_num) == (dateStart + j)) {
                            satisf = cursor.getString(cursor.getColumnIndex("satisf"));
                            max_tem = cursor.getString(cursor.getColumnIndex("max_tem"));
                            min_tem = cursor.getString(cursor.getColumnIndex("min_tem"));
                            Log.v("Debug+query", satisf + ", " + max_tem + ", " + min_tem);
                            adapter.addItem(new Griditem(calYear, calMonth, Integer.toString(j), max_tem + "°C/" + min_tem + "°C", imgList[Integer.parseInt(satisf)]));
                            if(cnt_cur<count) cursor.moveToNext();
                            cnt_cur++;
                        }

                        else {
                            adapter.addItem(new Griditem(calYear, calMonth,Integer.toString(j), " ", imgList[0]));
                        }

                    }
                }
                texYear.setText(Integer.toString(calYear));
                texMon.setText(retMon(calMonth));
                adapter.notifyDataSetChanged(); // 변경되었음을 어답터에 알려준다.
            }
        });

    }

    public int getYear(){
        return calYear;
    }
    public int getMonth(){
        return calMonth;
    }


}
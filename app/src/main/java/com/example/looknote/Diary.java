package com.example.looknote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;


public class Diary extends Fragment {


    View v;

    int y;
    int m;
    int d;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_diary, container, false);
        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        //setContentView(R.layout.fragment_diary);
        //TextView textView = v.findViewById(R.id.testdiary);
        CalendarView cal = (CalendarView)v.findViewById(R.id.calendarView);
        Calendar getcal = Calendar.getInstance();
        y = getcal.get(Calendar.YEAR);
        m = getcal.get(Calendar.MONTH)+1;
        d = getcal.get(Calendar.DATE);

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month +=1;
                y = year;
                m = month;
                d = dayOfMonth;

                //textView.setText(String.format("%d/%d/%d", year, month, dayOfMonth));

            }

        });

    Button historybutton = (Button) v.findViewById(R.id.his_button);
        historybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SearchWindow.class);
                intent.putExtra("year", y);
                intent.putExtra("month", m);
                intent.putExtra("day", d);
                startActivity(intent);
            }

        });
    }

}
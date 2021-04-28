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
import android.widget.EditText;
import android.widget.TextView;


public class Diary extends Fragment {

    View v;

    public interface OnDateListener{
        void onDateset(int myear, int mmonth, int mday);
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof OnDateListener)
        {
            onDateListener = (OnDateListener)context;
        }
//        else
//        {
//            throw new RuntimeException(context.toString()+" must implement");
//        }
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        onDateListener = null;
    }
    private OnDateListener onDateListener;

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

        CalendarView cal = (CalendarView)v.findViewById(R.id.calendarView);
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                onDateListener.onDateset(year,month,dayOfMonth);
            }
        });

        Button historybutton = (Button) v.findViewById(R.id.his_button);
        historybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SearchWindow.class);
                startActivity(intent);
            }
        });
    }

}
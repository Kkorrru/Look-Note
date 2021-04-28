package com.example.looknote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InputWindow extends AppCompatActivity {

    int iyear;
    int imonth;
    int idate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_window);

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onStart()
    {
        super.onStart();
        Intent intent = getIntent();
        iyear = intent.getExtras().getInt("year");
        imonth = intent.getExtras().getInt("month");
        idate = intent.getExtras().getInt("day");
        String mon = null;

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
    }
}
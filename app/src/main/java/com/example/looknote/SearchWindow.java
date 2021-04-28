package com.example.looknote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SearchWindow extends AppCompatActivity{

    int mmyear;
    int mmmonth;
    int mmdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_window);

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
    }
}
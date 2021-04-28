package com.example.looknote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SearchWindow extends AppCompatActivity implements Diary.OnDateListener{

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
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDateset(int myear, int mmonth, int mdate)
    {
        mmyear = myear;
        mmmonth = mmonth;
        mmdate = mdate;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        TextView dateText = (TextView)findViewById(R.id.date);

        dateText.setText(mmyear+"/"+mmdate);
    }
}
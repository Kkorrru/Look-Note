package com.example.looknote;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;


public class GridClickListener implements OnClickListener {



    Context context;
    int year;
    int month;
    String date;
    String degree;
    int Imono;

    public GridClickListener(Context context, int year, int month, String date, String degree, int Imono) {

        this.context = context;
        this.year = year;
        this.month = month;
        this.date = date;
        this.degree = degree;
        this.Imono = Imono;

    }



    public void onClick(View v) {

        if(degree.equals(" ")){
            Intent intent = new Intent(context, InputWindow.class);
            intent.putExtra("year", year);
            intent.putExtra("month", month+1);
            intent.putExtra("day", Integer.parseInt(date));
            context.startActivity(intent);

        }
        else{
            Intent intent1 = new Intent(context, SearchWindow.class);

            intent1.putExtra("year", year);
            intent1.putExtra("month", month+1);
            intent1.putExtra("day", Integer.parseInt(date));

            context.startActivity(intent1);
        }

    }
}
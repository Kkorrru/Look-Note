package com.example.looknote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
//
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
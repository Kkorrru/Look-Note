package com.example.looknote;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.work.WorkManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.looknote.GetWeather.max_tem;
import static com.example.looknote.GetWeather.min_tem;
import static com.example.looknote.GetWeather.periodicWorkRequest;


public class Weather extends Fragment {

    View v;
    GetWeather gw;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.fragment_weather, container, false); // Inflate the layout for this fragment
        gw = new GetWeather();

        gw.getDateTime();
        UpdateView();


        TextView temp= (TextView)v.findViewById(R.id.rec_temp);
        TextView top= (TextView)v.findViewById(R.id.rec_top);
        TextView bottom= (TextView)v.findViewById(R.id.rec_bottom);
        TextView acc= (TextView)v.findViewById(R.id.rec_acc);

        if(gw.today_tem >= 28){
            temp.setText("~28°C (한여름 날씨)");
            top.setText("  민소매, 반팔 티셔츠, 린넨 옷");
            bottom.setText("  반바지, 치마, 원피스");
            acc.setText(" ");
        }
        else if (gw.today_tem <28&&gw.today_tem>=23){
            temp.setText("23°C ~ 27°C (버틸만한 더위)");
            top.setText("  반팔, 얇은 셔츠 ");
            bottom.setText("  반바지, 면바지");
            acc.setText(" ");
        }
        else if (gw.today_tem <23&&gw.today_tem>=20){
            temp.setText("20°C ~ 22°C (활동하기 좋은 날씨)");
            top.setText("  얇은 가디건, 긴팔 티셔츠, 블라우스");
            bottom.setText("  면바지, 청바지, 슬랙스");
            acc.setText(" ");
        }
        else if (gw.today_tem <20&&gw.today_tem>=17){
            temp.setText("17°C ~ 19°C (좋지만 쌀쌀할 수도 있으니 주의!)");
            top.setText("  얇은 니트, 맨투맨, 후드티, 가디건, 얇은 자켓");
            bottom.setText("  청바지, 면바지");
            acc.setText(" ");
        }
        else if (gw.today_tem <17&&gw.today_tem>=12){
            temp.setText("12°C ~ 16°C (쌀쌀해진 날씨)");
            top.setText("  자켓, 가디건, 청자켓, 후드티, 셔츠, 야상");
            bottom.setText("  스타킹, 청바지, 면바지");
            acc.setText(" ");
        }
        else if (gw.today_tem <12&&gw.today_tem>=9){
            temp.setText("9°C ~ 11°C(트렌치가 어울리는 날씨)");
            top.setText("  도톰한 자켓, 트렌치코트, 야상, 점퍼, 니트");
            bottom.setText("  청바지, 스타킹");
            acc.setText(" ");
        }
        else if (gw.today_tem <9&&gw.today_tem>=5){
            temp.setText("5°C ~ 8°C (멋부리다간 얼어 죽는 날씨)");
            top.setText("  코트, 가죽 자켓, 발열내의, 니트, 기모티셔츠");
            bottom.setText("  청바지, 레깅스, 기모바지");
            acc.setText(" ");
        }
        else{
            temp.setText("4°C ~ (내가 입을 수 있는 최대한 두껍게!)");
            top.setText("  패딩, 두꺼운 코드, 기모제품, 누빔옷");
            bottom.setText("  ");
            acc.setText("  목도리, 장갑, 마스크, 방한용품");
        }

        return v;
    }

    void UpdateView() {
        Log.d("Debug-ForeGround", "HERE");
        dbHelper helper = new dbHelper(this.getContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        getLocation();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 현재 위치
                TextView textView = v.findViewById(R.id.todayLocation); textView.setText(gw.todayLocation);

                // 현재 날씨(T3H, SKY)
                if (Integer.parseInt(gw.todayHour) >= 0 && Integer.parseInt(gw.todayHour) < 3) gw.mpageNo = "69";
                else if (Integer.parseInt(gw.todayHour) >= 3 && Integer.parseInt(gw.todayHour) < 6) gw.mpageNo = "78";
                else if (Integer.parseInt(gw.todayHour) >= 6 && Integer.parseInt(gw.todayHour) < 9) gw.mpageNo = "7";
                else if (Integer.parseInt(gw.todayHour) >= 9 && Integer.parseInt(gw.todayHour) < 12) gw.mpageNo = "17";
                else if (Integer.parseInt(gw.todayHour) >= 12 && Integer.parseInt(gw.todayHour) < 15) gw.mpageNo = "28";
                else if (Integer.parseInt(gw.todayHour) >= 15 && Integer.parseInt(gw.todayHour) < 18) gw.mpageNo = "37";
                else if (Integer.parseInt(gw.todayHour) >= 18 && Integer.parseInt(gw.todayHour) < 21) gw.mpageNo = "49";
                else if (Integer.parseInt(gw.todayHour) >= 21) gw.mpageNo = "58";
                gw.getWeather();
            }
        }, 5500); // 5.5초 기다리고 위에 코드들 실행

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 현재 날씨
                Cursor cursor = db.rawQuery("SELECT * FROM record WHERE date_num="+gw.todayDate+"", null); // 중복 방지
                if (!cursor.moveToFirst())
                {
                    db.execSQL("INSERT INTO record VALUES (null, '"+Integer.parseInt(gw.todayDate)+"', 0, null, null, null, null, '"+gw.max_tem+"', '"+ min_tem+"', '"+gw.sky+"')");
                    Log.d("DebugInsert(Fore)", gw.todayDate + gw.max_tem + min_tem + gw.sky);
                }

                cursor = db.rawQuery("SELECT * FROM record WHERE date_num="+gw.todayDate+"", null); // 중복 방지
                String date_num, satisf, top_c, bottom_c, acc, diary, max_tem = null, min_tem, sky;
                cursor.moveToFirst(); // 처음엔 -1을 가르키고 있기 때문에 0(first)으로 이동 안 시켜주면 에러 남
                {
                    date_num = cursor.getString(cursor.getColumnIndex("date_num"));
                    satisf = cursor.getString(cursor.getColumnIndex("satisf"));
                    top_c = cursor.getString(cursor.getColumnIndex("top_c"));
                    bottom_c = cursor.getString(cursor.getColumnIndex("bottom_c"));
                    acc = cursor.getString(cursor.getColumnIndex("acc"));
                    diary = cursor.getString(cursor.getColumnIndex("diary"));
                    max_tem = cursor.getString(cursor.getColumnIndex("max_tem"));
                    min_tem = cursor.getString(cursor.getColumnIndex("min_tem"));
                    sky = cursor.getString(cursor.getColumnIndex("sky"));
                    Log.v("Debug-query", date_num + ", " + satisf + ", " + top_c + ", " + bottom_c + ", " + acc + ", " + diary + ", " + max_tem + ", " + min_tem + ", " + sky);

                    TextView textView;
                    textView = v.findViewById(R.id.todayTMN); textView.setText(min_tem + " ºC");
                    textView = v.findViewById(R.id.todayTMX); textView.setText(max_tem + " ºC");
                    textView = v.findViewById(R.id.todayT3H); textView.setText(GetWeather.today_tem + " ºC");

                    ImageView imageView;
                    imageView = (ImageView) v.findViewById(R.id.todaySKY);
                    switch (sky) {
                        case "1": // 비
                            imageView.setImageResource(R.drawable.rain);
                            break;
                        case "2": // 비/눈
                            imageView.setImageResource(R.drawable.rainnsnow);
                            break;
                        case "3": // 눈
                            imageView.setImageResource(R.drawable.snow);
                            break;
                        case "4": // 소나기
                            imageView.setImageResource(R.drawable.shower);
                            break;
                        case "5": // 맑음 5
                            imageView.setImageResource(R.drawable.sunny);
                            break;
                        case "7": // 구름 많음 7
                            imageView.setImageResource(R.drawable.cloudy);
                            break;
                        case "8": // 흐림 8
                            imageView.setImageResource(R.drawable.blur);
                            break;
                    }
                }
            }
        }, 10000);

        // 과거 비슷한 날씨에는?
//        Cursor cursor = db.rawQuery("SELECT * FROM record WHERE max_tem < max_tem + 1 and max_tem > max_tem - 1 ", null);
//
//        String[] from = new String[]{ "date_num", "max_tem", "min_tem", "sky", "top_c", "bottom_c", "acc" };
//        int[] to = { R.id.textView, R.id.textView2, R.id.textView19, R.id.textView5, R.id.textView20, R.id.textView21, R.id.textView22 };
//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this.getContext(), R.layout.listview_recommand, cursor, from, to);
//        ListView listView = (ListView)v.findViewById(R.id.recommand_listvew);
//        listView.setAdapter(adapter);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ListView listView = (ListView)v.findViewById(R.id.recommand_listvew);
                ListViewAdapter adapter = new ListViewAdapter();

                Cursor cursor = db.rawQuery("SELECT * FROM record WHERE max_tem < "+gw.max_tem+" + 2 and max_tem > "+gw.max_tem+" - 2 " +
                        "and min_tem < "+gw.min_tem+" + 2 and min_tem > "+gw.min_tem+" - 2 " +
                        "and date_num != "+gw.todayDate+"", null);

                String date_num, satisf, top_c, bottom_c, acc, diary, max_tem, min_tem, sky;
                while(cursor.moveToNext())
                {
                    date_num = cursor.getString(cursor.getColumnIndex("date_num"));
                    satisf = cursor.getString(cursor.getColumnIndex("satisf"));
                    top_c = cursor.getString(cursor.getColumnIndex("top_c"));
                    bottom_c = cursor.getString(cursor.getColumnIndex("bottom_c"));
                    acc = cursor.getString(cursor.getColumnIndex("acc"));
                    diary = cursor.getString(cursor.getColumnIndex("diary"));
                    max_tem = cursor.getString(cursor.getColumnIndex("max_tem"));
                    min_tem = cursor.getString(cursor.getColumnIndex("min_tem"));
                    sky = cursor.getString(cursor.getColumnIndex("sky"));

                    adapter.addItem(new ListViewitem(date_num, satisf, top_c, bottom_c, acc, diary, max_tem, min_tem, sky));
                }
                listView.setAdapter(adapter);
            }
        }, 11000);
    }

    void getLocation() {
        // ==================위치==================
        //requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, /*MY_PERMISSIONS_REQUEST_LOCATION*/10);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        List<Address>[] addresses = new List[]{null};
        Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                gw.latitude = location.getLatitude();
                gw.longitude = location.getLongitude();
                //Log.d("Debug", Double.toString(latitude) + " " + Double.toString(longitude));

                // ==================주소==================
                try {
                    addresses[0] = geocoder.getFromLocation(37.283479198468555, 127.04656764446653, 1);
                    //addresses[0] = geocoder.getFromLocation(latitude*10/1/10, longitude*10/1/10, 1); //latitude: 위도, longtitude: 경도
                    gw.todayLocation = addresses[0].get(0).getAddressLine(0).toString(); Log.d("Debug", gw.todayLocation);
                    gw.todayLocation = gw.todayLocation.substring(gw.todayLocation.indexOf(" ") + 1);
                    gw.todayLocation = gw.todayLocation.substring( 0, gw.todayLocation.indexOf(" ") + 1 + gw.todayLocation.substring(gw.todayLocation.indexOf(" ") + 1).indexOf(" ") );

                    gw.hasLocation = true;
                    switch (gw.todayLocation.substring(0, 3))
                    {
                        case "서울특":
                            gw.mnx = "60";
                            gw.mny = "127";
                            break;
                        case "부산광":
                            gw.mnx = "98";
                            gw.mny = "76";
                            break;
                        case "대구광":
                            gw.mnx = "89";
                            gw.mny = "90";
                            break;
                        case "인천광":
                            gw.mnx = "55";
                            gw.mny = "124";
                            break;
                        case "광주광":
                            gw.mnx = "58";
                            gw.mny = "74";
                            break;
                        case "대전광":
                            gw.mnx = "67";
                            gw.mny = "100";
                            break;
                        case "울산광":
                            gw.mnx = "102";
                            gw.mny = "84";
                            break;

                        case "경기도":
                            gw.mnx = "60";
                            gw.mny = "120";
                            break;
                        case "강원도":
                            gw.mnx = "73";
                            gw.mny = "134";
                            break;
                        case "충청북":
                            gw.mnx = "69";
                            gw.mny = "107";
                            break;
                        case "충청남":
                            gw.mnx = "68";
                            gw.mny = "100";
                            break;
                        case "전라북":
                            gw.mnx = "63";
                            gw.mny = "89";
                            break;
                        case "전라남":
                            gw.mnx = "51";
                            gw.mny = "67";
                            break;
                        case "경상북":
                            gw.mnx = "89";
                            gw.mny = "91";
                            break;
                        case "경상남":
                            gw.mnx = "91";
                            gw.mny = "77";
                            break;
                        case "제주특":
                            gw.mnx = "52";
                            gw.mny = "38";
                            break;
                        default:
                            gw.hasLocation = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            public void onProviderEnabled(String provider) { }
            public void onProviderDisabled(String provider) { }
        };
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        if(ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this.getContext(), "위치 액세스 권한 항상 허용을 거부할 시 앱 이용에 제한이 있습니다.", Toast.LENGTH_LONG).show();

            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, /*MY_PERMISSIONS_REQUEST_LOCATION*/10);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000 * 60, 1000, locationListener); // 1000밀리컨드 = 1초, 1000미터 = 1키로미터
    }
}
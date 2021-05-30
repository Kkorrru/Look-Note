package com.example.looknote;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.looknote.GetWeather.max_tem;


public class Weather extends Fragment {

    View v;
    GetWeather gw = new GetWeather();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.fragment_weather, container, false); // Inflate the layout for this fragment

        getLocation();

        UpdateView();

        return v;
    }

    void UpdateView() {
        dbHelper helper = new dbHelper(this.getContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        // 현재 날씨
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM record WHERE date_num="+gw.todayDate+"", null);
        String date_num, satisf, top_c, bottom_c, acc, diary, max_tem = null, min_tem, sky;
        while (cursor.moveToNext()) {
            date_num = cursor.getString(cursor.getColumnIndex("date_num"));
            satisf = cursor.getString(cursor.getColumnIndex("satisf"));
            top_c = cursor.getString(cursor.getColumnIndex("top_c"));
            bottom_c = cursor.getString(cursor.getColumnIndex("bottom_c"));
            acc = cursor.getString(cursor.getColumnIndex("acc"));
            diary = cursor.getString(cursor.getColumnIndex("diary"));
            max_tem = cursor.getString(cursor.getColumnIndex("max_tem"));
            min_tem = cursor.getString(cursor.getColumnIndex("min_tem"));
            sky = cursor.getString(cursor.getColumnIndex("sky"));
            Log.v("Debug+query", date_num + ", " + satisf + ", " + top_c + ", " + bottom_c + ", " + acc + ", " + diary + ", " + max_tem + ", " + min_tem + ", " + sky);

            TextView textView;
            textView = v.findViewById(R.id.todayTMN);
            textView.setText(min_tem + " ºC");

            textView = v.findViewById(R.id.todayTMX);
            textView.setText(max_tem + " ºC");

            textView = v.findViewById(R.id.todayT3H);
            textView.setText(GetWeather.today_tem + " ºC");

            ImageView imageView;
            imageView = (ImageView) v.findViewById(R.id.todaySKY);
            switch (GetWeather.sky) {
                case 1: // 비
                    imageView.setImageResource(R.drawable.rain);
                    break;
                case 2: // 비/눈
                    imageView.setImageResource(R.drawable.rainnsnow);
                    break;
                case 3: // 눈
                    imageView.setImageResource(R.drawable.snow);
                    break;
                case 4: // 소나기
                    imageView.setImageResource(R.drawable.shower);
                    break;
                case 5: // 맑음 5
                    imageView.setImageResource(R.drawable.sunny);
                    break;
                case 7: // 구름 많음 7
                    imageView.setImageResource(R.drawable.cloudy);
                    break;
                case 8: // 흐림 8
                    imageView.setImageResource(R.drawable.blur);
                    break;
            }
        }

        // 과거 비슷한 날씨에는?
        cursor = db.rawQuery("SELECT * FROM record WHERE max_tem < max_tem + 1 and max_tem > max_tem - 1 ", null);

        String[] from = new String[]{ "date_num", "max_tem", "min_tem", "sky", "top_c", "bottom_c", "acc" };
        int[] to = { R.id.textView, R.id.textView2, R.id.textView19, R.id.textView5, R.id.top_pad2, R.id.top_pad5, R.id.top_pad6 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this.getContext(), R.layout.listview_recommand, cursor, from, to);
        ListView listView = (ListView)v.findViewById(R.id.recommand_listvew);
        listView.setAdapter(adapter);
    }

    void getLocation() {
        // ==================위치==================
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, /*MY_PERMISSIONS_REQUEST_LOCATION*/10);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        List<Address>[] addresses = new List[]{null};
        Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());

        LocationListener locationListener = new LocationListener() {
            @SuppressLint("SetTextI18n")
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
                    }

                    TextView tv = (TextView) v.findViewById(R.id.todayLocation);
                    tv.setText(gw.todayLocation);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                gw.mpageNo = "8";
//                gw.getWeatherInfo();
                gw.hasLocation = true;
                gw.setWorkManger();
                UpdateView();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) { }

            public void onProviderEnabled(String provider) { }

            public void onProviderDisabled(String provider) { }
        };
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, locationListener);
    }
}
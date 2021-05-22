package com.example.looknote;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Weather extends Fragment {

    // =====위치=====
    private int MY_PERMISSIONS_REQUEST_LOCATION = 10;

    // =====주소=====
    double latitude;
    double longitude;
    String todayLocation;

    // =====날씨=====
    //참고: https://m.blog.naver.com/PostView.nhn?blogId=ksseo63&logNo=221035949094&proxyReferer=https:%2F%2Fwww.google.com%2F
    private static final String WEATHER_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";
    private static final String SERVICE_KEY = "";
    WeatherInfoTask weatherTask;

    View v;
    TextView textview;
    ImageView imageView;
    String todayDate;
    String todayHour;
    String mpageNo;
    String mnx = "60";
    String mny = "127";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.fragment_weather, container, false); // Inflate the layout for this fragment


        // ==================위치==================
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        List<Address>[] addresses = new List[]{null};
        Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());

        LocationListener locationListener = new LocationListener() {
            @SuppressLint("SetTextI18n")
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.d("Debug", Double.toString(latitude) + " " + Double.toString(longitude));


                // ==================주소==================
                try {
                    //addresses[0] = geocoder.getFromLocation(latitude*10/1/10, longitude*10/1/10, 1); //latitude: 위도, longtitude: 경도
                    addresses[0] = geocoder.getFromLocation(37.283479198468555, 127.04656764446653, 1);
                    todayLocation = addresses[0].get(0).getAddressLine(0).toString(); Log.d("Debug", todayLocation);
                    todayLocation = todayLocation.substring(todayLocation.indexOf(" ") + 1);
                    todayLocation = todayLocation.substring( 0, todayLocation.indexOf(" ") + 1 + todayLocation.substring(todayLocation.indexOf(" ") + 1).indexOf(" ") );

                    switch (todayLocation.substring(0, 3))
                    {
                        case "서울특":
                            mnx = "60";
                            mny = "127";
                            break;
                        case "부산광":
                            mnx = "98";
                            mny = "76";
                            break;
                        case "대구광":
                            mnx = "89";
                            mny = "90";
                            break;
                        case "인천광":
                            mnx = "55";
                            mny = "124";
                            break;
                        case "광주광":
                            mnx = "58";
                            mny = "74";
                            break;
                        case "대전광":
                            mnx = "67";
                            mny = "100";
                            break;
                        case "울산광":
                            mnx = "102";
                            mny = "84";
                            break;

                        case "경기도":
                            mnx = "60";
                            mny = "120";
                            break;
                        case "강원도":
                            mnx = "73";
                            mny = "134";
                            break;
                        case "충청북":
                            mnx = "69";
                            mny = "107";
                            break;
                        case "충청남":
                            mnx = "68";
                            mny = "100";
                            break;
                        case "전라북":
                            mnx = "63";
                            mny = "89";
                            break;
                        case "전라남":
                            mnx = "51";
                            mny = "67";
                            break;
                        case "경상북":
                            mnx = "89";
                            mny = "91";
                            break;
                        case "경상남":
                            mnx = "91";
                            mny = "77";
                            break;
                        case "제주특":
                            mnx = "52";
                            mny = "38";
                            break;
                    }

                    TextView tv = (TextView) v.findViewById(R.id.todayLocation);
                    tv.setText(todayLocation);

                    if (Integer.parseInt(todayHour) >= 0 && Integer.parseInt(todayHour) < 6)
                        todayDate = Integer.toString(Integer.parseInt(todayDate) - 1);

                    mpageNo = "8";
                    getWeatherInfo();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) { }

            public void onProviderEnabled(String provider) { }

            public void onProviderDisabled(String provider) { }
        };
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return v;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, locationListener);


        // ==================날씨==================
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        todayDate = sdf.format(mDate).substring(0, 8);
        todayHour = sdf.format(mDate).substring(8, 10);

        Log.d("Debug", todayHour);

        if (Integer.parseInt(todayHour) >= 0 && Integer.parseInt(todayHour) < 6)
            todayDate = Integer.toString(Integer.parseInt(todayDate) - 1);

        mpageNo = "8";
        getWeatherInfo();

        return v;
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        outState.putStringArray("todayWeathers", todayWeathers);
//        outState.putBoolean("hasWeathers", hasWeathers);
//        super.onSaveInstanceState(outState);
//    }

    private void getWeatherInfo() {
        if(weatherTask != null) {
            weatherTask.cancel(true);
        }
        weatherTask = new WeatherInfoTask();
        weatherTask.execute();
    }

    private class WeatherInfoTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            // 참고: https://ming9mon.tistory.com/82
            String serviceKey = SERVICE_KEY;
            String numOfRows = "1";
            String pageNo = mpageNo; // TMN(아침 최저기온): 발표 시각 02:00, 예보 시각 06:00, 8번째에 있음 // TMX(낮 최고기온): 발표 시각 02:00, 예보 시각 15:00, 38번째에 있음 // T3H(3시간 기온): 7번째에 있음
            String dataType = "XML";
            String baseDate = todayDate;
            String baseTime = "0200"; // 최저/최고 기온은 02:00에만 발표함
            String nx = mnx;
            String ny = mny;

            HttpURLConnection conn = null;
            BufferedReader rd = null;
            StringBuilder sb = null;
            StringBuilder urlBuilder = new StringBuilder(WEATHER_URL); /*URL*/
            try {
                urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey);
                urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
                urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
                urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8"));
                Log.d("Debug", urlBuilder.toString());

                /*각각의 base_time 으로 검색 참고자료 참조: 규정된 시각 정보를 넣어주어야 함 */
                URL url = new URL(urlBuilder.toString());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/xml");
                //System.out.println("Response code: " + conn.getResponseCode());

                if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) { // 컴네에서 배웠던 그 HTTP 상태 코드
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            finally {
                if(conn != null) {
                    conn.disconnect();
                }
                if(rd != null) {
                    try {
                        rd.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.d("Debug", String.valueOf(sb));
            return String.valueOf(sb);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(mpageNo == "8")
            {
                int beginIndex = s.indexOf("<fcstValue>") + "<fcstValue>".length();
                int endIndex = s.indexOf("</fcstValue>");

                textview = v.findViewById(R.id.todayTMN);
                textview.setText(s.substring(beginIndex, endIndex - 2) + " ºC"); // 소숫점 없애려고 -2

                mpageNo = "38";
                getWeatherInfo();
                return;
            }
            if(mpageNo == "38")
            {
                int beginIndex = s.indexOf("<fcstValue>") + "<fcstValue>".length();
                int endIndex = s.indexOf("</fcstValue>");

                textview = v.findViewById(R.id.todayTMX);
                textview.setText(s.substring(beginIndex, endIndex - 2) + " ºC"); // 소숫점 없애려고 -2

                if (Integer.parseInt(todayHour) >= 0 && Integer.parseInt(todayHour) < 3)
                {
                    mpageNo = "69";
                }
                else if (Integer.parseInt(todayHour) >= 3 && Integer.parseInt(todayHour) < 6)
                {
                    mpageNo = "78";
                }
                else if (Integer.parseInt(todayHour) >= 6 && Integer.parseInt(todayHour) < 9)
                {
                    mpageNo = "7";
                }
                else if (Integer.parseInt(todayHour) >= 9 && Integer.parseInt(todayHour) < 12)
                {
                    mpageNo = "17";
                }
                else if (Integer.parseInt(todayHour) >= 12 && Integer.parseInt(todayHour) < 15)
                {
                    mpageNo = "28";
                }
                else if (Integer.parseInt(todayHour) >= 15 && Integer.parseInt(todayHour) < 18)
                {
                    mpageNo = "37";
                }
                else if (Integer.parseInt(todayHour) >= 18 && Integer.parseInt(todayHour) < 21)
                {
                    mpageNo = "49";
                }
                else if (Integer.parseInt(todayHour) >= 21)
                {
                    mpageNo = "58";
                }

                getWeatherInfo();
                return;
            }
            if(mpageNo == "69" || mpageNo == "78" || mpageNo == "7" || mpageNo == "17" || mpageNo == "28" || mpageNo == "37" || mpageNo == "49" || mpageNo == "58" || mpageNo == "7")
            {
                int beginIndex = s.indexOf("<fcstValue>") + "<fcstValue>".length();
                int endIndex = s.indexOf("</fcstValue>");

                textview = v.findViewById(R.id.todayT3H);
                textview.setText(s.substring(beginIndex, endIndex) + " ºC");

                mpageNo = "6";
                getWeatherInfo();
                return;
            }
            if(mpageNo == "6")
            {
                int beginIndex = s.indexOf("<fcstValue>") + "<fcstValue>".length();
                int endIndex = s.indexOf("</fcstValue>");

                //textview = v.findViewById(R.id.todaySKY);
                //textview.setText(s.substring(beginIndex, endIndex));

                imageView = (ImageView) v.findViewById(R.id.todaySKY);
                switch (s.substring(beginIndex, endIndex))
                {
                    case "1": // 맑음
                        imageView.setImageResource(R.drawable.nb01);
                        break;
                    case "3": // 구름 많음
                        imageView.setImageResource(R.drawable.nb03);
                        break;
                    case "4": // 흐림
                        imageView.setImageResource(R.drawable.nb04);
                        break;
                }

                //todayWeathers[3] = (String) textview.getText();

                return;
            }
        }
    }
}
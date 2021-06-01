package com.example.looknote;

import android.os.AsyncTask;
import android.util.Log;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GetWeather {

    // =====위치=====
    private int MY_PERMISSIONS_REQUEST_LOCATION = 10;
    // =====주소=====
    double latitude;
    double longitude;
    static String todayLocation;
    // =====날씨=====
    //참고: https://m.blog.naver.com/PostView.nhn?blogId=ksseo63&logNo=221035949094&proxyReferer=https:%2F%2Fwww.google.com%2F
    private static final String WEATHER_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";
    private static final String SERVICE_KEY = "6S0lcBiMRAFjVAw8ZZ6rbYFUawGLYfg4uF1LkmW05eidtnQrrANwIP70KKEHRoWyO2IW3gJtsBBGKJEi4%2B8gPw%3D%3D";
    WeatherInfoTask weatherTask;

    // View v; TextView textview; ImageView imageView;
    static String todayDate;
    static String todayHour;
    String mpageNo;
    static String mnx;
    static String mny;

    static double max_tem;
    static double min_tem;
    static int sky;
    static boolean weatherIsFinish;
    static boolean hasLocation;
    static double today_tem;

    static WorkRequest periodicWorkRequest;

    void setWorkManger() {
        // =================알람===================
        periodicWorkRequest = new PeriodicWorkRequest.Builder(UploadWorker.class, 15, TimeUnit.MINUTES)
                .build();
        WorkManager.getInstance().enqueue(periodicWorkRequest); //Operation mWorkManager =
    }

    void getDateTime() {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        todayDate = sdf.format(mDate).substring(0, 8);
        todayHour = sdf.format(mDate).substring(8, 10);

        Log.d("Debug", "todayDate: " + todayDate);
        Log.d("Debug", "todayHour: " + todayHour);

        if (Integer.parseInt(todayHour) >= 0 && Integer.parseInt(todayHour) < 2)
        {
            switch (todayDate.substring(4, 8))
            {
                case "0101":
                    todayDate = sdf.format(mDate).substring(0, 4) + "1231";
                    break;
                case "0201":
                    todayDate = sdf.format(mDate).substring(0, 4) + "0131";
                    break;
                case "0301":
                    todayDate = sdf.format(mDate).substring(0, 4) + "0228";
                    break;
                case "0401":
                    todayDate = sdf.format(mDate).substring(0, 4) + "0331";
                    break;
                case "0501":
                    todayDate = sdf.format(mDate).substring(0, 4) + "0430";
                    break;
                case "0601":
                    todayDate = sdf.format(mDate).substring(0, 4) + "0531";
                    break;
                case "0701":
                    todayDate = sdf.format(mDate).substring(0, 4) + "0630";
                    break;
                case "0801":
                    todayDate = sdf.format(mDate).substring(0, 4) + "0731";
                    break;
                case "0901":
                    todayDate = sdf.format(mDate).substring(0, 4) + "0831";
                    break;
                case "1001":
                    todayDate = sdf.format(mDate).substring(0, 4) + "0930";
                    break;
                case "1101":
                    todayDate = sdf.format(mDate).substring(0, 4) + "1031";
                    break;
                case "1201":
                    todayDate = sdf.format(mDate).substring(0, 4) + "1130";
                    break;
                default:
                    todayDate = Integer.toString(Integer.parseInt(sdf.format(mDate).substring(0, 8)) - 1);
            }
            Log.d("Debug", "todayDate2: " + todayDate.substring(4, 8));
        }
    }

    void getWeather() {
        // ==================날씨==================
        weatherIsFinish = false;
        getDateTime();

        mpageNo = "8";
        getWeatherInfo();
    }

    void getWeatherInfo() {
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
            String nx = "60";
            String ny = "127";

            if(hasLocation)
            {
                nx = mnx;
                ny = mny;
            }

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
                Log.d("Debug" + "URI", urlBuilder.toString());

                /*각각의 base_time 으로 검색 참고자료 참조: 규정된 시각 정보를 넣어주어야 함 */
                URL url = new URL(urlBuilder.toString());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/xml");
                //System.out.println("Response code: " + conn.getResponseCode());

                if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) { // 컴네에서 배웠던 그 HTTP 응답 상태 코드
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
            Log.d("Debug" + "Response", String.valueOf(sb));
            return String.valueOf(sb);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            MainActivity ma = new MainActivity();

            if(mpageNo == "8")
            {
                int beginIndex = s.indexOf("<fcstValue>") + "<fcstValue>".length();
                int endIndex = s.indexOf("</fcstValue>");

                min_tem = Double.parseDouble(s.substring(beginIndex, endIndex - 2));
//                textview = v.findViewById(R.id.todayTMN); textview.setText(s.substring(beginIndex, endIndex - 2) + " ºC"); // 소숫점 없애려고 -2

                mpageNo = "38";
                getWeatherInfo();
                return;
            }
            if(mpageNo == "38")
            {
                int beginIndex = s.indexOf("<fcstValue>") + "<fcstValue>".length();
                int endIndex = s.indexOf("</fcstValue>");

                max_tem = Double.parseDouble(s.substring(beginIndex, endIndex - 2));

                if (Integer.parseInt(todayHour) >= 0 && Integer.parseInt(todayHour) < 3) mpageNo = "7";//"69";
                else if (Integer.parseInt(todayHour) >= 3 && Integer.parseInt(todayHour) < 6) mpageNo = "7";//"78";
                else if (Integer.parseInt(todayHour) >= 6 && Integer.parseInt(todayHour) < 9) mpageNo = "7";
                else if (Integer.parseInt(todayHour) >= 9 && Integer.parseInt(todayHour) < 12) mpageNo = "17";
                else if (Integer.parseInt(todayHour) >= 12 && Integer.parseInt(todayHour) < 15) mpageNo = "28";
                else if (Integer.parseInt(todayHour) >= 15 && Integer.parseInt(todayHour) < 18) mpageNo = "37";
                else if (Integer.parseInt(todayHour) >= 18 && Integer.parseInt(todayHour) < 21) mpageNo = "49";
                else if (Integer.parseInt(todayHour) >= 21) mpageNo = "58";

                getWeatherInfo();
                return;
            }
            if(mpageNo == "69" || mpageNo == "78" || mpageNo == "7" || mpageNo == "17" || mpageNo == "28" || mpageNo == "37" || mpageNo == "49" || mpageNo == "58" || mpageNo == "7")
            {
                int beginIndex = s.indexOf("<fcstValue>") + "<fcstValue>".length();
                int endIndex = s.indexOf("</fcstValue>");

                today_tem = Integer.parseInt(s.substring(beginIndex, endIndex));

                mpageNo = "2";
                getWeatherInfo();
                return;
            }
            if(mpageNo == "2")
            {
                int beginIndex = s.indexOf("<fcstValue>") + "<fcstValue>".length();
                int endIndex = s.indexOf("</fcstValue>");

                sky = Integer.parseInt(s.substring(beginIndex, endIndex));
                if (sky == 0)
                {
                    mpageNo = "6";
                    getWeatherInfo();
                    return;
                }
                weatherIsFinish = true;
                return;
            }
            if(mpageNo == "6")
            {
                int beginIndex = s.indexOf("<fcstValue>") + "<fcstValue>".length();
                int endIndex = s.indexOf("</fcstValue>");

                sky = Integer.parseInt(s.substring(beginIndex, endIndex)) + 4;
                weatherIsFinish = true;
                return;
            }
        }
    }
}

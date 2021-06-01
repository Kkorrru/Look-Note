package com.example.looknote;

import android.util.Log;

public class ListViewitem {
    String date_num, satisf, top_c, bottom_c, acc, diary, max_tem, min_tem, sky;

    public ListViewitem(String date_num, String satisf, String top_c, String bottom_c, String acc, String diary, String max_tem, String min_tem, String sky){
        this.date_num = date_num;
        this.satisf = satisf;
        this.top_c = top_c;
        this.bottom_c = bottom_c;
        this.acc = acc;
        this.diary = diary;
        this.max_tem = max_tem;
        this.min_tem = min_tem;
        this.sky = sky;
    }

    public String getDate_num() {
        return date_num.substring(0, 4) + "." + date_num.substring(4, 6) + "." + date_num.substring(6, 8);
    }

    public String getSatisf() {
        return satisf;
    }

    public int getStaisfImage(String satisf) {
        switch (satisf)
        {
            case "1":
                return R.drawable.cold_icon;
            case "2":
                return R.drawable.cool_icon;
            case "3":
                return R.drawable.nice_icon;
            case "4":
                return R.drawable.warm_icon;
            case "5":
                return R.drawable.hot_icon;
            default:
                return R.drawable.none_icon;
        }
    }

    public String getTop_c() {
        return top_c;
    }

    public String getBottom_c() {
        return bottom_c;
    }

    public String getAcc() {
        return acc;
    }

    public String getDiary() {
        return diary;
    }

    public String getMax_tem() {
        return max_tem + " ºC";
    }

    public String getMin_tem() {
        return min_tem + " / ";
    }

    public String getSky() {
        switch (sky) {
            case "1": // 비
                return "비";
            case "2": // 비/눈
                return "비/눈";
            case "3": // 눈
                return "눈";
            case "4": // 소나기
                return "소나기";
            case "5": // 맑음
                return "맑음";
            case "7": // 구름 많음 7
                return "구름 많음";
            case "8": // 흐림 8
                return "흐림";
        }
        return sky;
    }
}

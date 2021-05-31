package com.example.looknote;

public class Griditem {
    private int year;
    private int month;
    private String day;
    private String degree;
    private int imgno;
    //final static int imgList[] = {R.drawable.none_icon, R.drawable.cold_icon, R.drawable.cool_icon, R.drawable.nice_icon, R.drawable.warm_icon, R.drawable.hot_icon};


    public Griditem(int year, int month, String day, String number, int imgno){
        this.year = year;
        this.month = month;
        this.day = day;
        this.degree = number;
        this.imgno = imgno;
    }

    public String getDay() {
        return day;
    }

    public String getDegree() {
        return degree;
    }

    public void setName(String name) {
        this.day = day;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public int getImgno() {
        return imgno;
    }

    public void setImgno(int imgno) {
        this.imgno = imgno;
    }

    public int getYear(){return year;}
    public void setYear(){this.year = year;}

    public int getMonth(){return month;}
    public void setMonth(){this.month = month;}
}
package com.example.looknote;

public class Griditem {
    private String name;
    private String number;
    private int imgno;
    //final static int imgList[] = {R.drawable.none_icon, R.drawable.cold_icon, R.drawable.cool_icon, R.drawable.nice_icon, R.drawable.warm_icon, R.drawable.hot_icon};


    public Griditem(String name, String number, int imgno){
        this.name = name;
        this.number = number;
        this.imgno = imgno;
    }
    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getImgno() {
        return imgno;
    }

    public void setImgno(int imgno) {
        this.imgno = imgno;
    }
}

package com.example.lenovo.timer.unils;

/**
 * Created by Lenovo on 2018/6/9.
 */

public  class TimeToSeconds {
    public static int ToSeconds(String Time){
        int mSeconds = 0;
        switch (Time){
            case "三十秒":
                mSeconds = 30;
                break;
            case "一分钟":
                mSeconds = 60;
                break;
            case "一分半":
                mSeconds = 90;
                break;
            case "两分钟":
                mSeconds = 120;
                break;
            case "两分半":
                mSeconds = 150;
                break;
            case "三分钟":
                mSeconds = 180;
                break;
            case "三分半":
                mSeconds = 210;
                break;
            case "四分钟":
                mSeconds = 240;
                break;
            case "四分半":
                mSeconds = 270;
                break;
            case "五分钟":
                mSeconds = 300;
                break;
        }
        return mSeconds;
    }
}

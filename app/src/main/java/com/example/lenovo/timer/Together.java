package com.example.lenovo.timer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.timer.unils.TimeToSeconds;

/**
 * Created by lenovo on 2018/3/28.
 */

public class Together extends AppCompatActivity {
    private CircleProgressView progressView1;
    private CircleProgressView progressView2;
    private Button button1;
    private Button button2;
    private int currentPro1 = 0;
    private int currentPro2 = 0;
    private boolean ifrun1=true;
    private boolean ifrun2=true;
    private boolean Sectionend1=false;
    private boolean Sectionend2=false;
    private int count1;
    private int count2;
    private MediaPlayer voice1=new MediaPlayer();
    private MediaPlayer voice2=new MediaPlayer();
    private LinearLayout ClockBk1;
    private LinearLayout ClockBk2;
    private int maxCount=240;
    private Button NextButton;
    private int Section = -1;
    String mWhichPeople1 = "";
    String mWhichPeople2 = "";
    String mTime = "240";
    Handler mHandler1 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    currentPro1 += 1;
                    progressView1.setProgress(currentPro1);
                    break;
            }
            return false;
        }
    });
    Handler mHandler2 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    currentPro2 += 1;
                    progressView2.setProgress(currentPro2);
                    break;
            }
            return false;
        }
    });
    ////////////////////////
    private class myThread1 extends Thread{
        public boolean ifThreadRun=true;
        private final Object lock1=new Object();
        private boolean ifpause = true;
        void pauseThread(){
            ifpause=true;

        }
        void resumeThread(){
            ifpause=false;
            synchronized (lock1){
                lock1.notifyAll();
            }
        }
        void onPause(){
            synchronized (lock1){
                try {
                    lock1.wait();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        void stops(){
            ifThreadRun=false;
            count1=0;
        }
        void restart(){
            ifThreadRun=true;
        }
        public void run(){
            //super.run();
            try {
                int index = 0;
                while (true){
                    while (ifThreadRun==true) {
                        //int i=0;
                        while (count1<maxCount){
                            while (ifpause) {
                                onPause();
                            }
                            Message mm = new Message();
                            mm.what = 1;
                            mHandler1.sendMessage(mm);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            count1+=1;
                            if(count1==maxCount-30){
                                voice1.start();
                            }
                        }
                        if(count1>=maxCount){
                            voice2.start();
                            Sectionend1=true;
                            ifrun1=false;
                            stops();
                        }
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
    }
    private class myThread2 extends Thread{
        public boolean ifThreadRun=true;
        private final Object lock2=new Object();
        private boolean ifpause = true;
        void pauseThread(){
            ifpause=true;

        }
        void resumeThread(){
            ifpause=false;
            synchronized (lock2){
                lock2.notifyAll();
            }
        }
        void onPause(){
            synchronized (lock2){
                try {
                    lock2.wait();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        void stops(){
            ifThreadRun=false;
            count2=0;
        }
        void restart(){
            ifThreadRun=true;
        }
        public void run(){
            //super.run();
            try {
                int index = 0;
                while (true){
                    while (ifThreadRun==true) {
                        //int i=0;
                        while (count2<maxCount){
                            while (ifpause) {
                                onPause();
                            }
                            Message mm = new Message();
                            mm.what = 1;
                            mHandler2.sendMessage(mm);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            count2+=1;
                            if(count2==maxCount-30){
                                voice1.start();
                            }
                        }
                        if(count2>=maxCount){
                            voice2.start();
                            ifrun2=false;
                            Sectionend2=true;
                            stops();
                        }
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
    }
    public void TwoClockListener(){
        final myThread1 myThread1=new myThread1();
        myThread1.start();
        final myThread2 myThread2=new myThread2();
        myThread2.start();
        ifrun1=false;
        ifrun2=false;
        ClockBk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ifrun1==true){
                    myThread1.pauseThread();
                    ifrun1=false;
                }
                else {
                    myThread1.resumeThread();
                    ifrun1=true;
                }
                if (Sectionend1==false){
                    myThread1.restart();
                }
                else {
                    count1=0;
                    currentPro1=0;
                    myThread1.restart();
                }
            }
        });
        ClockBk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ifrun2==true){
                    myThread2.pauseThread();
                    ifrun2=false;
                }
                else {
                    myThread2.resumeThread();
                    ifrun2=true;
                }
                if (Sectionend2==false){
                    myThread2.restart();
                }
                else {
                    count2=0;
                    currentPro2=0;
                    myThread2.restart();
                }
            }
        });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if (voice1 != null) {
            voice1.stop();
            voice1.release();
        }
        if (voice2 != null) {
            voice2.stop();
            voice2.release();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.together);
        final String str=getIntent().getStringExtra("style").toString();
        Section = getIntent().getIntExtra("Section",-1);
        mWhichPeople1 = getIntent().getStringExtra("WhichPeople1");
        mWhichPeople2 = getIntent().getStringExtra("WhichPeople2");
        mTime = getIntent().getStringExtra("Time");
        voice1=MediaPlayer.create(this,R.raw.thirty);
        voice2=MediaPlayer.create(this,R.raw.timeover);
        ClockBk1=(LinearLayout)findViewById(R.id.ClockBk1);
        ClockBk1.setBackgroundResource(R.mipmap.sun);
        ClockBk2=(LinearLayout)findViewById(R.id.ClockBk2);
        ClockBk2.setBackgroundResource(R.mipmap.moon);
        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);
        NextButton=(Button)findViewById(R.id.next);
        progressView1=(CircleProgressView)findViewById(R.id.pro1);
        progressView2=(CircleProgressView)findViewById(R.id.pro2);
        progressView1.setmOnProgressListener(new CircleProgressView.onProgressListener() {
            @Override
            public void onEnd() {
                // TODO Auto-generated method stub
                progressView1.setProgress(0);
            }
        });
        progressView2.setmOnProgressListener(new CircleProgressView.onProgressListener() {
            @Override
            public void onEnd() {
                // TODO Auto-generated method stub
                progressView2.setProgress(0);
            }
        });
        button1.setText(mWhichPeople1);
        button2.setText(mWhichPeople2);
        maxCount= TimeToSeconds.ToSeconds(mTime);
        progressView1.setmMaxProgress(TimeToSeconds.ToSeconds(mTime));
        progressView1.setMaxtime(TimeToSeconds.ToSeconds(mTime));
        progressView2.setmMaxProgress(TimeToSeconds.ToSeconds(mTime));
        progressView2.setMaxtime(TimeToSeconds.ToSeconds(mTime));
        count1=0;
        currentPro1=0;
        count2=0;
        currentPro2=0;
        TwoClockListener();
//        if(str.equals("对辩")){
//
//        }
//        else if(str.equals("自由辩")){
//            maxCount=240;
//            progressView1.setmMaxProgress(240);
//            progressView1.setMaxtime(240);
//            progressView2.setmMaxProgress(240);
//            progressView2.setMaxtime(240);
//            count1=0;
//            currentPro1=0;
//            count2=0;
//            currentPro2=0;
//            TwoClockListener();
//        }

        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(str.equals("对辩")){
                    Intent newintent =new Intent(Together.this,xinshengsai.class);
                    Section = Section+2;
                    newintent.putExtra("section",Section);
                    setResult(1,newintent);
                    finish();
                }
                else {
                    Intent newintent =new Intent(Together.this,xinshengsai.class);
                    Section = Section+2;
                    newintent.putExtra("section",Section);
                    setResult(2,newintent);
                    finish();
                }
            }
        });

    }
}

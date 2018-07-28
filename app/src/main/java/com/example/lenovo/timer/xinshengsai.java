package com.example.lenovo.timer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.timer.ViewModel.ItemRuleViewModel;
import com.example.lenovo.timer.databinding.XinshengsaiBinding;
import com.example.lenovo.timer.unils.TimeToSeconds;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/3/11.
 */

public class xinshengsai extends AppCompatActivity implements View.OnClickListener{
    private CircleProgressView progressView;
    private int currentPro = 0;
    private boolean ifrun=true;
    private TextView WhichPeople;
    private int count;
    private boolean Sectionend=false;//是否计时完毕
    private MediaPlayer voice1=new MediaPlayer();
    private MediaPlayer voice2=new MediaPlayer();
    private LinearLayout ClockBk;
    private int maxCount=210;
    private int TheSection=0;
    myThread myThread=new myThread();
    List<ItemRuleViewModel> mRuleList = new ArrayList<>();
    XinshengsaiBinding binding = null;
    String mType = "0";
    //用于新国辩赛制保存进度
    int mTempCount1 = 0;
    int mTempCurrentPro1 = 0;
    int mTempCount2 = 0;
    int mTempCurrentPro2 = 0;
    //用于标记正反方
    int mTag = 0;//0为正1为反

    //主线程里通过Handler处理，每收到一条message，进度+1并更新进度条
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    currentPro += 1;
                    progressView.setProgress(currentPro);
                    break;
            }
            return false;
        }
    });
    ////////////////////////
    private class myThread extends Thread{
        public boolean ifThreadRun=true;
        private final Object lock=new Object();
        private boolean ifpause = true;
        void pauseThread(){
            ifpause=true;

        }
        void resumeThread(){
            ifpause=false;
            synchronized (lock){
                lock.notifyAll();//线程获得对象锁，被唤醒
            }
        }
        void onPause(){
            synchronized (lock){
                try {
                    lock.wait();            //线程失去对象锁，挂起
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        void stops(){
            ifThreadRun=false;
            count=0;
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
                        while (count<maxCount){

                            while (ifpause) {
                                onPause();            //线程挂起
                            }
                            Message mm = new Message();
                            mm.what = 1;
                            mHandler.sendMessage(mm);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            count+=1;
                            if(count==maxCount-30){
                                voice1.start();
                            }
                        }
                        if(count>=maxCount){
                            voice2.start();
                            Sectionend=true;
                            ifrun=false;
                            stops();
                        }
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
    }
    /*************各个环节计时*******************/
    public void RuncLock(int section){
        if (mRuleList.size()>0&&section<mRuleList.size()){
            String mWho = mRuleList.get(section).getWhoAndAction().substring(0,1);
            String mAction = mRuleList.get(section).getWhoAndAction().substring(4);
            if (mAction.equals("对辩")){
                Intent intent = new Intent(xinshengsai.this,Together.class);
                intent.putExtra("style","对辩");
                String mWhichPeople1 = mRuleList.get(section).getWhoAndAction().substring(0,4);
                String mWhichPeople2 = mRuleList.get(section+1).getWhoAndAction().substring(0,4);
                intent.putExtra("Section",section);
                intent.putExtra("WhichPeople1",mWhichPeople1);
                intent.putExtra("WhichPeople2",mWhichPeople2);
                intent.putExtra("Time",mRuleList.get(section).getUseTime());
                startActivityForResult(intent,1);
            }
            else if (mAction.equals("辩论")){
                Intent intent = new Intent(xinshengsai.this,Together.class);
                intent.putExtra("style","自由辩");
                intent.putExtra("Section",section);
                intent.putExtra("WhichPeople1","正方");
                intent.putExtra("WhichPeople2","反方");
                intent.putExtra("Time",mRuleList.get(section).getUseTime());
                startActivityForResult(intent,1);
            }
            else {
                if (mWho.equals("正")){

                    //新国辩赛制时存储反方进度
                    if (mType.equals("2") && mTag == 1){
                        mTempCount2 = count;
                        mTempCurrentPro2 = currentPro;
                        count=mTempCount1;
                        currentPro=mTempCurrentPro1;
                        progressView.setProgress(currentPro);
                    }
                    else if (mType.equals("2") && mTag == 0){
                        mTempCount1 = count;
                        mTempCurrentPro1 = currentPro;
                    }
                    ClockBk.setBackgroundResource(R.mipmap.sun);
                    mTag = 0;
                }
                else {

                    //新国辩赛制时存储正方进度
                    if (mType.equals("2")&& mTag == 0){
                        mTempCount1 = count;
                        mTempCurrentPro1 = currentPro;
                        count=mTempCount2;
                        currentPro=mTempCurrentPro2;
                        progressView.setProgress(currentPro);
                    }
                    else if (mType.equals("2") && mTag == 1){
                        mTempCount2 = count;
                        mTempCurrentPro2 = currentPro;
                    }
                    ClockBk.setBackgroundResource(R.mipmap.moon);
                    mTag = 1;
                }
                WhichPeople.setText(mRuleList.get(section).getWhoAndAction());

                if (mType.equals("2")){

                }else {
                    maxCount = TimeToSeconds.ToSeconds(mRuleList.get(section).getUseTime());
                    progressView.setmMaxProgress(TimeToSeconds.ToSeconds(mRuleList.get(section).getUseTime()));
                    progressView.setMaxtime(TimeToSeconds.ToSeconds(mRuleList.get(section).getUseTime()));
                    count=0;
                    currentPro=0;
                    progressView.setProgress(currentPro);
                }

            }
        }
        else if (section>=mRuleList.size()){
            Intent intent = new Intent(xinshengsai.this,Theend.class);
            startActivity(intent);
            finish();
        }


//        if(section==1){
//            WhichPeople.setText("反二质询两分钟");
//            maxCount=120;
//            progressView.setmMaxProgress(120);
//            progressView.setMaxtime(120);
//            count=0;
//            currentPro=0;
//            ClockBk.setBackgroundResource(R.mipmap.moon);
//        }
//        else if (section==2){
//            WhichPeople.setText("反一立论三分半");
//            maxCount=210;
//            progressView.setmMaxProgress(210);
//            progressView.setMaxtime(210);
//            count=0;
//            currentPro=0;
//            ClockBk.setBackgroundResource(R.mipmap.moon);
//        }
//        else if(section==3){
//            WhichPeople.setText("正二质询两分钟");
//            maxCount=120;
//            progressView.setmMaxProgress(120);
//            progressView.setMaxtime(120);
//            count=0;
//            currentPro=0;
//            ClockBk.setBackgroundResource(R.mipmap.sun);
//        }
//        else if(section==4){
//            WhichPeople.setText("反二驳论一分半");
//            maxCount=90;
//            progressView.setmMaxProgress(90);
//            progressView.setMaxtime(90);
//            count=0;
//            currentPro=0;
//            ClockBk.setBackgroundResource(R.mipmap.moon);
//        }
//        else if(section==5){
//            WhichPeople.setText("正二驳论一分半");
//            maxCount=90;
//            progressView.setmMaxProgress(90);
//            progressView.setMaxtime(90);
//            count=0;
//            currentPro=0;
//            ClockBk.setBackgroundResource(R.mipmap.sun);
//        }
//        else if (section==6){
//            Intent intent = new Intent(xinshengsai.this,Together.class);
//            intent.putExtra("style","对辩");
//            startActivity(intent);
//        }
//        else if(section==7){
//            WhichPeople.setText("反三盘问一分半");
//            maxCount=90;
//            progressView.setmMaxProgress(90);
//            progressView.setMaxtime(90);
//            count=0;
//            currentPro=0;
//            ClockBk.setBackgroundResource(R.mipmap.moon);
//        }
//        else if (section==8){
//            WhichPeople.setText("正三小结一分半");
//            maxCount=90;
//            progressView.setmMaxProgress(90);
//            progressView.setMaxtime(90);
//            count=0;
//            currentPro=0;
//            ClockBk.setBackgroundResource(R.mipmap.sun);
//        }
//        else if (section==9){
//            WhichPeople.setText("反三小结一分半");
//            maxCount=90;
//            progressView.setmMaxProgress(90);
//            progressView.setMaxtime(90);
//            count=0;
//            currentPro=0;
//            ClockBk.setBackgroundResource(R.mipmap.moon);
//        }
//        else if (section==10){
//            Intent intent = new Intent(xinshengsai.this,Together.class);
//            intent.putExtra("style","自由辩");
//            startActivity(intent);
//        }
//        else if (section==11){
//            WhichPeople.setText("正四结辩三分半");
//            maxCount=210;
//            progressView.setmMaxProgress(210);
//            progressView.setMaxtime(210);
//            count=0;
//            currentPro=0;
//            ClockBk.setBackgroundResource(R.mipmap.sun);
//        }
//        else if(section==12){
//            Intent intent = new Intent(xinshengsai.this,Theend.class);
//            startActivity(intent);
//        }
    }
    ///////////////////////////

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
        voice1=MediaPlayer.create(this,R.raw.thirty);
        voice2=MediaPlayer.create(this,R.raw.timeover);
        binding = DataBindingUtil.setContentView(this,R.layout.xinshengsai);
        binding.setOnClicklistener(this);
        mRuleList = (List<ItemRuleViewModel>)getIntent().getSerializableExtra("RuleList");


//        final String tag=getIntent().getStringExtra("tag").toString();
        mType =getIntent().getStringExtra("type").toString();

        progressView = binding.pro;
        ClockBk=binding.ClockBk;
        WhichPeople=binding.WhichPeople;

        progressView.setmOnProgressListener(new CircleProgressView.onProgressListener() {
            @Override
            public void onEnd() {
                // TODO Auto-generated method stub
                progressView.setProgress(0);
            }
        });
        myThread.start();
        ifrun=false;
        ClockBk.setBackgroundResource(R.mipmap.sun);
        mTag = 0;
        WhichPeople.setText(mRuleList.get(TheSection).getWhoAndAction());

        if (mType.equals("2")){//新国辩赛制
            maxCount=1140;
            progressView.setmMaxProgress(1140);
            progressView.setMaxtime(1140);
        }
        else {
            maxCount=TimeToSeconds.ToSeconds(mRuleList.get(TheSection).getUseTime());
            progressView.setmMaxProgress(TimeToSeconds.ToSeconds(mRuleList.get(TheSection).getUseTime()));
            progressView.setMaxtime(TimeToSeconds.ToSeconds(mRuleList.get(TheSection).getUseTime()));
        }
        count=0;
        currentPro=0;



//        if (tag.equals("0")){
//            ClockBk.setBackgroundResource(R.mipmap.sun);
//            WhichPeople.setText("正一立论三分半");
//            maxCount=210;
//            progressView.setmMaxProgress(210);
//            progressView.setMaxtime(210);
//            count=0;
//            currentPro=0;
//            TheSection=Integer.parseInt(sections);
//        }
//        else if(tag.equals("1")){
//            WhichPeople.setText("正三盘问一分半");
//            maxCount=90;
//            progressView.setmMaxProgress(90);
//            progressView.setMaxtime(90);
//            count=0;
//            currentPro=0;
//            ClockBk.setBackgroundResource(R.mipmap.sun);
//            TheSection=Integer.parseInt(sections);
//        }
//        else if (tag.equals("2")){
//            WhichPeople.setText("反四结辩三分半");
//            maxCount=210;
//            progressView.setmMaxProgress(210);
//            progressView.setMaxtime(210);
//            count=0;
//            currentPro=0;
//            ClockBk.setBackgroundResource(R.mipmap.moon);
//            TheSection=Integer.parseInt(sections);
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == 1){//对辩的下一个环节
                Bundle bundle = data.getExtras();
                int tempSection = bundle.getInt("section");
                WhichPeople.setText(mRuleList.get(tempSection).getWhoAndAction());
                maxCount=TimeToSeconds.ToSeconds(mRuleList.get(tempSection).getUseTime());
                progressView.setmMaxProgress(TimeToSeconds.ToSeconds(mRuleList.get(tempSection).getUseTime()));
                progressView.setMaxtime(TimeToSeconds.ToSeconds(mRuleList.get(tempSection).getUseTime()));
                count=0;
                currentPro=0;
                progressView.setProgress(currentPro);
                String tempWho = mRuleList.get(tempSection).getWhoAndAction().substring(0,1);
                if (tempWho.equals("正")){
                    ClockBk.setBackgroundResource(R.mipmap.sun);
                    mTag = 0;
                }
                else {
                    ClockBk.setBackgroundResource(R.mipmap.moon);
                    mTag = 1;
                }
                TheSection=tempSection;
            }
            else if (resultCode == 2){//自由辩的下一个环节
                Bundle bundle = data.getExtras();
                int tempSection = bundle.getInt("section");
                WhichPeople.setText(mRuleList.get(tempSection).getWhoAndAction());
                if (mType.equals("2")){


                }else {
                    maxCount=TimeToSeconds.ToSeconds(mRuleList.get(tempSection).getUseTime());
                    progressView.setmMaxProgress(TimeToSeconds.ToSeconds(mRuleList.get(tempSection).getUseTime()));
                    progressView.setMaxtime(TimeToSeconds.ToSeconds(mRuleList.get(tempSection).getUseTime()));
                    count=0;
                    currentPro=0;
                    progressView.setProgress(currentPro);
                }
                String tempWho = mRuleList.get(tempSection).getWhoAndAction().substring(0,1);
                if (tempWho.equals("正")){
                    if (mType.equals("2") && mTag == 1){
                        mTempCount2 = count;
                        mTempCurrentPro2 = currentPro;
                        count=mTempCount1;
                        currentPro=mTempCurrentPro1;
                        progressView.setProgress(currentPro);
                    }
                    else if (mType.equals("2") && mTag == 0){
                        mTempCount1 = count;
                        mTempCurrentPro1 = currentPro;
                    }
                    ClockBk.setBackgroundResource(R.mipmap.sun);
                    mTag = 0;
                }
                else {
                    if (mType.equals("2")&& mTag == 0){
                        mTempCount1 = count;
                        mTempCurrentPro1 = currentPro;
                        count=mTempCount2;
                        currentPro=mTempCurrentPro2;
                        progressView.setProgress(currentPro);
                    }
                    else if (mType.equals("2") && mTag == 1){
                        mTempCount2 = count;
                        mTempCurrentPro2 = currentPro;
                    }
                    ClockBk.setBackgroundResource(R.mipmap.moon);
                    mTag = 1;
                }
                TheSection=tempSection;
            }
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.pause:
                if(ifrun==true){
                    myThread.pauseThread();
                    ifrun=false;
                }
                else {
                    myThread.resumeThread();
                    ifrun=true;
                }
                if (Sectionend==false){
                    myThread.restart();
                }
                else {
                    count=0;
                    currentPro=0;
                    myThread.restart();
                }
                break;
            case R.id.reback:
                if (mType.equals("2")){
                    if (mTag == 0){
                        count=mTempCount1;
                        currentPro=mTempCurrentPro1;
                    }else {
                        count=mTempCount2;
                        currentPro=mTempCurrentPro2;
                    }

                    progressView.setProgress(currentPro);
                }else {
                    count=0;
                    currentPro=0;
                    progressView.setProgress(currentPro);
                }
                break;
            case R.id.next:
                if(ifrun==true){
                    myThread.pauseThread();
                    ifrun=false;
                }
                TheSection+=1;
                Sectionend=false;
                RuncLock(TheSection);
                break;
        }
    }
}

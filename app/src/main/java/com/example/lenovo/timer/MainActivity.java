package com.example.lenovo.timer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.lenovo.timer.ViewModel.ItemRuleViewModel;
import com.example.lenovo.timer.databinding.ActivityMainBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityMainBinding binding = null;
    private MediaPlayer voice1=new MediaPlayer();
    private MediaPlayer voice2=new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //去除顶部标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setOnClicklistener(this);
        //提示音
        voice1 = MediaPlayer.create(this,R.raw.thirty);
        voice2 = MediaPlayer.create(this,R.raw.timeover);
    }
    @Override
    protected void onDestroy() {
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
    public void onClick(View view){
        switch (view.getId()){
            case R.id.thirty:
                voice1.start();
                break;
            case R.id.endtime:
                voice2.start();
                break;
            case R.id.academic_style:
                LayoutInflater inflater = getLayoutInflater();
                View rule=inflater.inflate(R.layout.rules,(ViewGroup)findViewById(R.id.rule));
                myrule(rule,0);
                break;
            case R.id.newtudent_style:
                LayoutInflater inflater2 = getLayoutInflater();
                View rule2=inflater2.inflate(R.layout.rules,(ViewGroup)findViewById(R.id.rule));
                myrule(rule2,1);
                break;
            case R.id.international_style:
                LayoutInflater inflater3 = getLayoutInflater();
                View rule3=inflater3.inflate(R.layout.rules,(ViewGroup)findViewById(R.id.rule));
                myrule(rule3,2);
                break;
            case R.id.my_style:
                Intent intent = new Intent(MainActivity.this,MyStyleActivity.class);
                startActivity(intent);
        }
    }
    //弹框介绍赛制详情
    private void myrule(View rule, int position){
        final PopupWindow popupWindow = new PopupWindow(rule, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);                                                                   //弹窗满layout显示
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));		//弹窗背景透明
        popupWindow.setOutsideTouchable(true);											//响应外部点击事件
        popupWindow.setTouchable(true);												//响应点击事件
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);						//设置弹窗动画
        Button mOK = rule.findViewById(R.id.ok);
        Button mCancel = rule.findViewById(R.id.cancel);
        RecyclerView mRuleStep = rule.findViewById(R.id.allrule);
        final List<ItemRuleViewModel> mRuleList = GetRuleList(position);
        RuleStepAdapter adapter = new RuleStepAdapter(mRuleList, BR.ItemRule);
        mRuleStep.setLayoutManager(new GridLayoutManager(rule.getContext(), 2));
        mRuleStep.setAdapter(adapter);
        if (position == 0||position == 1){
            mOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myintent =new Intent(MainActivity.this,xinshengsai.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("RuleList",(Serializable) mRuleList);
                    myintent.putExtra("type","0");//赛制类型
                    myintent.putExtras(bundle);
                    startActivity(myintent);
                    finish();
                }
            });
        }
        else if (position == 2){
            //新国辩
            mOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myintent =new Intent(MainActivity.this,xinshengsai.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("RuleList",(Serializable) mRuleList);
                    myintent.putExtra("type","2");//赛制类型
                    myintent.putExtras(bundle);
                    startActivity(myintent);
                    finish();
                }
            });
        }
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(rule, Gravity.BOTTOM, 0, 0);
    }
    //环节列表
    public List<ItemRuleViewModel> GetRuleList(int type){
        List<ItemRuleViewModel> tempRuleList = new ArrayList<>();
        if (type == 0){//院系际赛制
            ItemRuleViewModel mItemRuleViewModel = new ItemRuleViewModel();
            mItemRuleViewModel.setWhoAndAction("正方一辩立论");
            mItemRuleViewModel.setUseTime("三分钟");
            tempRuleList.add(mItemRuleViewModel);
            ItemRuleViewModel mItemRuleViewModel2 = new ItemRuleViewModel();
            mItemRuleViewModel2.setWhoAndAction("反方四辩质询");
            mItemRuleViewModel2.setUseTime("一分半");
            tempRuleList.add(mItemRuleViewModel2);
            ItemRuleViewModel mItemRuleViewModel3 = new ItemRuleViewModel();
            mItemRuleViewModel3.setWhoAndAction("反方一辩立论");
            mItemRuleViewModel3.setUseTime("三分钟");
            tempRuleList.add(mItemRuleViewModel3);
            ItemRuleViewModel mItemRuleViewModel4 = new ItemRuleViewModel();
            mItemRuleViewModel4.setWhoAndAction("正方四辩质询");
            mItemRuleViewModel4.setUseTime("一分半");
            tempRuleList.add(mItemRuleViewModel4);
            ItemRuleViewModel mItemRuleViewModel5 = new ItemRuleViewModel();
            mItemRuleViewModel5.setWhoAndAction("反方二辩驳论");
            mItemRuleViewModel5.setUseTime("两分钟");
            tempRuleList.add(mItemRuleViewModel5);
            ItemRuleViewModel mItemRuleViewModel6 = new ItemRuleViewModel();
            mItemRuleViewModel6.setWhoAndAction("正方二辩驳论");
            mItemRuleViewModel6.setUseTime("两分钟");
            tempRuleList.add(mItemRuleViewModel6);
            ItemRuleViewModel mItemRuleViewModel15 = new ItemRuleViewModel();
            mItemRuleViewModel15.setWhoAndAction("正方二辩对辩");
            mItemRuleViewModel15.setUseTime("两分钟");
            tempRuleList.add(mItemRuleViewModel15);
            ItemRuleViewModel mItemRuleViewModel16 = new ItemRuleViewModel();
            mItemRuleViewModel16.setWhoAndAction("反方二辩对辩");
            mItemRuleViewModel16.setUseTime("两分钟");
            tempRuleList.add(mItemRuleViewModel16);
            ItemRuleViewModel mItemRuleViewModel7 = new ItemRuleViewModel();
            mItemRuleViewModel7.setWhoAndAction("正方三辩盘问");
            mItemRuleViewModel7.setUseTime("三分钟");
            tempRuleList.add(mItemRuleViewModel7);
            ItemRuleViewModel mItemRuleViewModel8 = new ItemRuleViewModel();
            mItemRuleViewModel8.setWhoAndAction("反方三辩盘问");
            mItemRuleViewModel8.setUseTime("三分钟");
            tempRuleList.add(mItemRuleViewModel8);
            ItemRuleViewModel mItemRuleViewModel9 = new ItemRuleViewModel();
            mItemRuleViewModel9.setWhoAndAction("正方三辩小结");
            mItemRuleViewModel9.setUseTime("一分半");
            tempRuleList.add(mItemRuleViewModel9);
            ItemRuleViewModel mItemRuleViewModel10 = new ItemRuleViewModel();
            mItemRuleViewModel10.setWhoAndAction("反方三辩小结");
            mItemRuleViewModel10.setUseTime("一分半");
            tempRuleList.add(mItemRuleViewModel10);
            ItemRuleViewModel mItemRuleViewModel11 = new ItemRuleViewModel();
            mItemRuleViewModel11.setWhoAndAction("正方自由辩论");
            mItemRuleViewModel11.setUseTime("四分钟");
            tempRuleList.add(mItemRuleViewModel11);
            ItemRuleViewModel mItemRuleViewModel12 = new ItemRuleViewModel();
            mItemRuleViewModel12.setWhoAndAction("反方自由辩论");
            mItemRuleViewModel12.setUseTime("四分钟");
            tempRuleList.add(mItemRuleViewModel12);
            ItemRuleViewModel mItemRuleViewModel13 = new ItemRuleViewModel();
            mItemRuleViewModel13.setWhoAndAction("反方四辩结辩");
            mItemRuleViewModel13.setUseTime("三分钟");
            tempRuleList.add(mItemRuleViewModel13);
            ItemRuleViewModel mItemRuleViewModel14 = new ItemRuleViewModel();
            mItemRuleViewModel14.setWhoAndAction("正方四辩结辩");
            mItemRuleViewModel14.setUseTime("三分钟");
            tempRuleList.add(mItemRuleViewModel14);
        }
        else if(type == 1){//新生赛赛制
            ItemRuleViewModel mItemRuleViewModel = new ItemRuleViewModel();
            mItemRuleViewModel.setWhoAndAction("正方一辩立论");
            mItemRuleViewModel.setUseTime("三分钟");
            tempRuleList.add(mItemRuleViewModel);
            ItemRuleViewModel mItemRuleViewModel2 = new ItemRuleViewModel();
            mItemRuleViewModel2.setWhoAndAction("反方四辩质询");
            mItemRuleViewModel2.setUseTime("两分钟");
            tempRuleList.add(mItemRuleViewModel2);
            ItemRuleViewModel mItemRuleViewModel3 = new ItemRuleViewModel();
            mItemRuleViewModel3.setWhoAndAction("反方一辩立论");
            mItemRuleViewModel3.setUseTime("三分钟");
            tempRuleList.add(mItemRuleViewModel3);
            ItemRuleViewModel mItemRuleViewModel4 = new ItemRuleViewModel();
            mItemRuleViewModel4.setWhoAndAction("正方四辩质询");
            mItemRuleViewModel4.setUseTime("两分钟");
            tempRuleList.add(mItemRuleViewModel4);
            ItemRuleViewModel mItemRuleViewModel5 = new ItemRuleViewModel();
            mItemRuleViewModel5.setWhoAndAction("正方二辩驳论");
            mItemRuleViewModel5.setUseTime("两分半");
            tempRuleList.add(mItemRuleViewModel5);
            ItemRuleViewModel mItemRuleViewModel6 = new ItemRuleViewModel();
            mItemRuleViewModel6.setWhoAndAction("反方二辩驳论");
            mItemRuleViewModel6.setUseTime("两分半");
            tempRuleList.add(mItemRuleViewModel6);
            ItemRuleViewModel mItemRuleViewModel7 = new ItemRuleViewModel();
            mItemRuleViewModel7.setWhoAndAction("反方三辩盘问");
            mItemRuleViewModel7.setUseTime("两分半");
            tempRuleList.add(mItemRuleViewModel7);
            ItemRuleViewModel mItemRuleViewModel8 = new ItemRuleViewModel();
            mItemRuleViewModel8.setWhoAndAction("正方三辩盘问");
            mItemRuleViewModel8.setUseTime("两分半");
            tempRuleList.add(mItemRuleViewModel8);
            ItemRuleViewModel mItemRuleViewModel9 = new ItemRuleViewModel();
            mItemRuleViewModel9.setWhoAndAction("正方三辩小结");
            mItemRuleViewModel9.setUseTime("两分半");
            tempRuleList.add(mItemRuleViewModel9);
            ItemRuleViewModel mItemRuleViewModel10 = new ItemRuleViewModel();
            mItemRuleViewModel10.setWhoAndAction("反方三辩小结");
            mItemRuleViewModel10.setUseTime("两分半");
            tempRuleList.add(mItemRuleViewModel10);
            ItemRuleViewModel mItemRuleViewModel11 = new ItemRuleViewModel();
            mItemRuleViewModel11.setWhoAndAction("正方自由辩论");
            mItemRuleViewModel11.setUseTime("四分钟");
            tempRuleList.add(mItemRuleViewModel11);
            ItemRuleViewModel mItemRuleViewModel12 = new ItemRuleViewModel();
            mItemRuleViewModel12.setWhoAndAction("反方自由辩论");
            mItemRuleViewModel12.setUseTime("四分钟");
            tempRuleList.add(mItemRuleViewModel12);
            ItemRuleViewModel mItemRuleViewModel13 = new ItemRuleViewModel();
            mItemRuleViewModel13.setWhoAndAction("反方四辩结辩");
            mItemRuleViewModel13.setUseTime("三分钟");
            tempRuleList.add(mItemRuleViewModel13);
            ItemRuleViewModel mItemRuleViewModel14 = new ItemRuleViewModel();
            mItemRuleViewModel14.setWhoAndAction("正方四辩结辩");
            mItemRuleViewModel14.setUseTime("三分钟");
            tempRuleList.add(mItemRuleViewModel14);
        }
        else if (type == 2){//新国辩赛制
            ItemRuleViewModel mItemRuleViewModel1 = new ItemRuleViewModel();
            mItemRuleViewModel1.setWhoAndAction("正方辩手立论");
            mItemRuleViewModel1.setUseTime("不定时");
            tempRuleList.add(mItemRuleViewModel1);
            ItemRuleViewModel mItemRuleViewModel2 = new ItemRuleViewModel();
            mItemRuleViewModel2.setWhoAndAction("反方辩手质询");
            mItemRuleViewModel2.setUseTime("不定时");
            tempRuleList.add(mItemRuleViewModel2);
            ItemRuleViewModel mItemRuleViewModel3 = new ItemRuleViewModel();
            mItemRuleViewModel3.setWhoAndAction("反方辩手立论");
            mItemRuleViewModel3.setUseTime("不定时");
            tempRuleList.add(mItemRuleViewModel3);
            ItemRuleViewModel mItemRuleViewModel4 = new ItemRuleViewModel();
            mItemRuleViewModel4.setWhoAndAction("正方辩手质询");
            mItemRuleViewModel4.setUseTime("不定时");
            tempRuleList.add(mItemRuleViewModel4);
            ItemRuleViewModel mItemRuleViewModel5 = new ItemRuleViewModel();
            mItemRuleViewModel5.setWhoAndAction("反方辩手驳论");
            mItemRuleViewModel5.setUseTime("不定时");
            tempRuleList.add(mItemRuleViewModel5);
            ItemRuleViewModel mItemRuleViewModel6 = new ItemRuleViewModel();
            mItemRuleViewModel6.setWhoAndAction("正方辩手盘问");
            mItemRuleViewModel6.setUseTime("不定时");
            tempRuleList.add(mItemRuleViewModel6);
            ItemRuleViewModel mItemRuleViewModel7 = new ItemRuleViewModel();
            mItemRuleViewModel7.setWhoAndAction("正方辩手驳论");
            mItemRuleViewModel7.setUseTime("不定时");
            tempRuleList.add(mItemRuleViewModel7);
            ItemRuleViewModel mItemRuleViewModel8 = new ItemRuleViewModel();
            mItemRuleViewModel8.setWhoAndAction("反方辩手盘问");
            mItemRuleViewModel8.setUseTime("不定时");
            tempRuleList.add(mItemRuleViewModel8);
            ItemRuleViewModel mItemRuleViewModel9 = new ItemRuleViewModel();
            mItemRuleViewModel9.setWhoAndAction("正方辩手小结");
            mItemRuleViewModel9.setUseTime("不定时");
            tempRuleList.add(mItemRuleViewModel9);
            ItemRuleViewModel mItemRuleViewModel10 = new ItemRuleViewModel();
            mItemRuleViewModel10.setWhoAndAction("反方辩手小结");
            mItemRuleViewModel10.setUseTime("不定时");
            tempRuleList.add(mItemRuleViewModel10);
            ItemRuleViewModel mItemRuleViewModel11 = new ItemRuleViewModel();
            mItemRuleViewModel11.setWhoAndAction("正方自由辩论");
            mItemRuleViewModel11.setUseTime("四分钟");
            tempRuleList.add(mItemRuleViewModel11);
            ItemRuleViewModel mItemRuleViewModel12 = new ItemRuleViewModel();
            mItemRuleViewModel12.setWhoAndAction("反方自由辩论");
            mItemRuleViewModel12.setUseTime("四分钟");
            tempRuleList.add(mItemRuleViewModel12);
            ItemRuleViewModel mItemRuleViewModel13 = new ItemRuleViewModel();
            mItemRuleViewModel13.setWhoAndAction("反方辩手结辩");
            mItemRuleViewModel13.setUseTime("剩余时");
            tempRuleList.add(mItemRuleViewModel13);
            ItemRuleViewModel mItemRuleViewModel14 = new ItemRuleViewModel();
            mItemRuleViewModel14.setWhoAndAction("正方辩手结辩");
            mItemRuleViewModel14.setUseTime("剩余时");
            tempRuleList.add(mItemRuleViewModel14);
        }
        return tempRuleList;
    }

}

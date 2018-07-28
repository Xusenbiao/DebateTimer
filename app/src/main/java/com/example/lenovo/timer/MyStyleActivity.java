package com.example.lenovo.timer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.lenovo.timer.ViewModel.ItemRuleViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2018/6/20.
 */

public class MyStyleActivity extends AppCompatActivity{
    private OptionsPickerView optionsPickerView;
    private ArrayList<String> mPickerViewItem1 = new ArrayList<>();//数据源1
    private ArrayList<ArrayList<String>> mPickerViewItem2 = new ArrayList<>();//数据源2
    private ArrayList<ArrayList<ArrayList<String>>> mPickerViewItem3 = new ArrayList<>();//数据源3
    protected void onCreate(Bundle savedInstanceState) {
        //去除顶部标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules);
        Button mOK = findViewById(R.id.ok);
        Button mCancel = findViewById(R.id.cancel);
        RecyclerView mRuleStep = findViewById(R.id.allrule);
        final List<ItemRuleViewModel> mRuleList = new ArrayList<>();
        ItemRuleViewModel mItemRuleViewModel1 = new ItemRuleViewModel();
        mItemRuleViewModel1.setWhoAndAction("选择环节行为");
        mItemRuleViewModel1.setUseTime("环节时");
        mRuleList.add(mItemRuleViewModel1);
        final RuleStepAdapter adapter = new RuleStepAdapter(mRuleList, BR.ItemRule);
        //选项
        mPickerViewItem1.add("正方一辩");
        mPickerViewItem1.add("正方二辩");
        mPickerViewItem1.add("正方三辩");
        mPickerViewItem1.add("正方四辩");
        mPickerViewItem1.add("正方");
        mPickerViewItem1.add("反方一辩");
        mPickerViewItem1.add("反方二辩");
        mPickerViewItem1.add("反方三辩");
        mPickerViewItem1.add("反方四辩");
        mPickerViewItem1.add("反方");
        ArrayList<String> mAction = new ArrayList<>();
        mAction.add("立论");
        mAction.add("质询");
        mAction.add("驳论");
        mAction.add("对辩");
        mAction.add("盘问");
        mAction.add("小结");
        mAction.add("结辩");
        for (int i = 0; i<4; i++){
            mPickerViewItem2.add(mAction);
        }
        ArrayList<String> mAction2 = new ArrayList<>();
        mAction2.add("自由辩论");
        mPickerViewItem2.add(mAction2);
        for (int i = 0; i<4; i++){
            mPickerViewItem2.add(mAction);
        }
        mPickerViewItem2.add(mAction2);
        ArrayList<String> mTime = new ArrayList<>();
        mTime.add("三十秒");
        mTime.add("一分钟");
        mTime.add("一分半");
        mTime.add("两分钟");
        mTime.add("两分半");
        mTime.add("三分钟");
        mTime.add("三分半");
        mTime.add("四分钟");
        mTime.add("四分半");
        mTime.add("五分钟");
        ArrayList<ArrayList<String>> mTimeList = new ArrayList<>();
        for (int i = 0; i<7; i++){
            mTimeList.add(mTime);
        }
        for (int i = 0; i<10; i++){
            mPickerViewItem3.add(mTimeList);
        }

        adapter.setOnItemClickListener(new RuleStepAdapter.OnItemClickListener() {
            @Override
            public void onClick(final int position) {
                optionsPickerView = new  OptionsPickerView.Builder(MyStyleActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                        //返回的分别是三个级别的选中位置
                        mRuleList.get(position).setWhoAndAction(mPickerViewItem1.get(options1) + mPickerViewItem2.get(options1).get(option2));
                        mRuleList.get(position).setUseTime(mPickerViewItem3.get(options1).get(option2).get(options3));
                        if (position == mRuleList.size()-1){
                            ItemRuleViewModel mItemRuleViewModel1 = new ItemRuleViewModel();
                            mItemRuleViewModel1.setWhoAndAction("选择环节行为");
                            mItemRuleViewModel1.setUseTime("环节时");
                            mRuleList.add(mItemRuleViewModel1);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                        .setSubmitText("确定")//确定按钮文字
                        .setCancelText("取消")//取消按钮文字
                        .setSubCalSize(18)//确定和取消文字大小
                        .setTitleSize(20)//标题文字大小
                        .setTitleColor(Color.BLACK)//标题文字颜色
                        .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                        .setCancelColor(Color.BLACK)//取消按钮文字颜色
                        .setContentTextSize(18)//滚轮文字大小
                        .setCyclic(true, true, true)//循环与否
                        .setSelectOptions(1, 1, 1)  //设置默认选中项
                        .setOutSideCancelable(false)//点击外部dismiss default true
                        .build();
                optionsPickerView.setPicker(mPickerViewItem1, mPickerViewItem2, mPickerViewItem3);//添加数据源
                optionsPickerView.show();


            }
        });
        mRuleStep.setLayoutManager(new GridLayoutManager(this, 2));
        mRuleStep.setAdapter(adapter);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRuleList.remove(mRuleList.size()-1);
                int mIfRight = 0;
                int m1V1 = 0;
                int m4V4 = 0;
                for (int i = 0;i<mRuleList.size()-1;i++){
                    String mAction1 = mRuleList.get(i).getWhoAndAction().substring(4);
                    String mAction2 = mRuleList.get(i+1).getWhoAndAction().substring(4);
                    if ((mAction1.equals("对辩")&&mAction2.equals("对辩"))||(mAction1.equals("辩论")&&mAction2.equals("辩论"))){
                        if (mRuleList.get(i).getUseTime().equals(mRuleList.get(i+1).getUseTime())){

                        }else {
                            Toast.makeText(MyStyleActivity.this,"对辩或自由辩双方的时间必须相同才公平！",Toast.LENGTH_SHORT).show();
                            mIfRight = 1;
                            break;
                        }
                    }
                    if ((mAction1.equals("对辩")&&mAction2.equals("辩论"))||(mAction1.equals("辩论")&&mAction2.equals("对辩"))){
                        Toast.makeText(MyStyleActivity.this,"对辩和自由辩相连不合理",Toast.LENGTH_SHORT).show();
                        mIfRight = 1;
                        break;
                    }
                    if ((mAction1.equals("对辩")&&mAction2.equals("对辩"))&&i==mRuleList.size()-2){
                        Toast.makeText(MyStyleActivity.this,"对辩结束不合理",Toast.LENGTH_SHORT).show();
                        mIfRight = 1;
                        break;
                    }
                    if ((mAction1.equals("辩论")&&mAction2.equals("辩论"))&&i==mRuleList.size()-2){
                        Toast.makeText(MyStyleActivity.this,"自由辩结束不合理",Toast.LENGTH_SHORT).show();
                        mIfRight = 1;
                        break;
                    }
                    if (mAction1.equals("对辩")){
                        m1V1 += 1;
                    }
                    if (mAction1.equals("辩论")){
                        m4V4 += 1;
                    }
                }
                String mAction3 = mRuleList.get(mRuleList.size()-1).getWhoAndAction().substring(4);
                if (mAction3.equals("对辩")){
                    m1V1 += 1;
                }
                if (mAction3.equals("辩论")){
                    m4V4 += 1;
                }
                if (m1V1 % 2 == 0 && m4V4 % 2 == 0){

                }else {
                    Toast.makeText(MyStyleActivity.this,"单独一方的对辩或自由辩不存在！",Toast.LENGTH_SHORT).show();
                    mIfRight = 1;
                }
                if (mIfRight == 0){
                    Intent myintent =new Intent(MyStyleActivity.this,xinshengsai.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("RuleList",(Serializable) mRuleList);
                    myintent.putExtra("type","0");//赛制类型
                    myintent.putExtras(bundle);
                    startActivity(myintent);
                    finish();
                }

            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

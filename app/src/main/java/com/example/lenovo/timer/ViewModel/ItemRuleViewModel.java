package com.example.lenovo.timer.ViewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.lenovo.timer.BR;

import java.io.Serializable;

/**
 * Created by Lenovo on 2018/6/6.
 */

public class ItemRuleViewModel extends BaseObservable implements Serializable{
    @Bindable
    public String getWhoAndAction() {
        return whoAndAction;
    }

    public void setWhoAndAction(String whoAndAction) {
        this.whoAndAction = whoAndAction;
        notifyPropertyChanged(BR.whoAndAction);
    }
    @Bindable
    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
        notifyPropertyChanged(BR.useTime);
    }

    private String whoAndAction;
    private String useTime;

}

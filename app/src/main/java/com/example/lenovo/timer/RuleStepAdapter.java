package com.example.lenovo.timer;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.timer.ViewModel.ItemRuleViewModel;
import com.example.lenovo.timer.databinding.ItemRuleBinding;

import java.util.List;

/**
 * Created by Lenovo on 2018/6/6.
 */

public class RuleStepAdapter extends RecyclerView.Adapter<RuleStepAdapter.RuleStepViewHolder>{
    private List<ItemRuleViewModel> mDatas;
    private int brId;
    private RuleStepAdapter.OnItemClickListener myOnItemClickListener=null;
    public RuleStepAdapter(List<ItemRuleViewModel> mDatas, int brId) {
        this.mDatas = mDatas;
        this.brId = brId;
    }
    @Override
    public RuleStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRuleBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_rule, parent, false);
        RuleStepViewHolder viewHolder = new RuleStepViewHolder(binding);
        return  viewHolder;
    }
    @Override
    public void onBindViewHolder(final RuleStepViewHolder holder, int position) {
        holder.binding.setVariable(brId,mDatas.get(position));
        holder.binding.executePendingBindings();
        if(myOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
        }
        if (position%2 == 0){
            holder.binding.leftline.setVisibility(View.INVISIBLE);
            holder.binding.rightline.setVisibility(View.VISIBLE);
        }
        else {
            holder.binding.leftline.setVisibility(View.VISIBLE);
            holder.binding.rightline.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }
    public interface OnItemClickListener
    {
        void onClick(int position);
    }
    public void setOnItemClickListener(RuleStepAdapter.OnItemClickListener onItemClickListener)
    {
        this.myOnItemClickListener=onItemClickListener;
    }
    public class RuleStepViewHolder extends RecyclerView.ViewHolder {
        ItemRuleBinding binding;
        public RuleStepViewHolder(ItemRuleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

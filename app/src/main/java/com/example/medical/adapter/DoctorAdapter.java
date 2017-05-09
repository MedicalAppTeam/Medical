package com.example.medical.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.medical.R;
import com.example.medical.bean.HelperBean;
import com.example.medical.tool.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${liyan} on 2017/5/9.
 */

public class DoctorAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private Context context;
    private List<HelperBean> list;

    public void setList(List<HelperBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public DoctorAdapter(Context context) {
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseViewHolder.onCreatMyViewHolder(context,parent, R.layout.fragment_doctor_recycleview_item);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
            holder.setText(R.id.fragment_doctor_recycleView_item_name,list.get(position).getName());
            holder.setText(R.id.fragment_doctor_recycleView_item_sex,list.get(position).getSex());
            holder.setText(R.id.fragment_doctor_recycleView_item_content,list.get(position).getContent());
            holder.setText(R.id.fragment_doctor_recycleView_item_age, String.valueOf(list.get(position).getAge()));
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

package com.hemaapp.thp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.thp.R;
import com.hemaapp.thp.activity.InformationActivity;
import com.hemaapp.thp.activity.LoginActivity;
import com.hemaapp.thp.base.JhctmApplication;
import com.hemaapp.thp.model.Tender;

import java.util.ArrayList;

import xtom.frame.util.XtomTimeUtil;

/**
 * Created by lenovo on 2017/7/3.
 * 信息的adapter
 */
public class InforAdapter extends HemaAdapter {
    private ArrayList<Tender> threads;

    public InforAdapter(Context mContext, ArrayList<Tender> threads) {
        super(mContext);
        this.threads = threads;
    }

    @Override
    public boolean isEmpty() {
        return threads == null || threads.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : threads.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isEmpty())
            return getEmptyView(parent);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_infor, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setView(holder, position);
        return convertView;
    }

    private class ViewHolder {
        LinearLayout item_layout;
        TextView infor_name;
        TextView infor_loaction;
        TextView infor_type;
        TextView infor_time;
    }

    private void findView(ViewHolder holder, View view) {
        holder.item_layout = (LinearLayout) view.findViewById(R.id.item_layout);
        holder.infor_name = (TextView) view.findViewById(R.id.infor_name);
        holder.infor_loaction = (TextView) view.findViewById(R.id.infor_loaction);
        holder.infor_type = (TextView) view.findViewById(R.id.infor_type);
        holder.infor_time = (TextView) view.findViewById(R.id.infor_time);
    }

    private void setView(ViewHolder holder, int position) {
        Tender thread = threads.get(position);
        holder.infor_name.setText(thread.getName());
        holder.infor_loaction.setText(thread.getProvince() + thread.getCity() + thread.getArea());
        holder.infor_type.setText(thread.getType());
        String regdate = XtomTimeUtil.TransTime(thread.getCreatetime(),
                "yyyy-MM-dd");
        holder.infor_time.setText(regdate);
        //详情
        holder.item_layout.setTag(R.id.TAG, thread);
        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JhctmApplication.getInstance().getUser() == null) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.putExtra("keytype", "1");
                    mContext.startActivity(intent);
                } else {
                    Tender tender = (Tender) v.getTag(R.id.TAG);
                    Intent intent = new Intent(mContext, InformationActivity.class);
                    intent.putExtra("id", tender.getId());
                    mContext.startActivity(intent);
                }

            }
        });
    }
}

package com.hemaapp.thp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.thp.R;

/**
 * Created by lenovo on 2017/6/29.
 * 选择城市三部曲
 * keytype 1 全国 2 全省 3 全市
 */
public class SelectCityAdapter extends HemaAdapter {
    private String keytype;
    public SelectCityAdapter(Context mContext,String keytype) {
        super(mContext);
        this.keytype = keytype;
    }

    @Override
    public int getCount() {
        return 0;
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
        return null;
    }
    private class ViewHolder{
        TextView item_name;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.item_name = (TextView) view.findViewById(R.id.item_name);
    }
    private void setView(ViewHolder holder,int position)
    {

    }
}

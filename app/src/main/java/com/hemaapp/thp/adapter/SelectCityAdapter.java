package com.hemaapp.thp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.thp.R;
import com.hemaapp.thp.model.CityChildren;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/6/29.
 * 选择城市三部曲
 * keytype 1 全国 2 全省 3 全市
 */
public class SelectCityAdapter extends HemaAdapter {
    private ArrayList<CityChildren> cityChildrens;
    public SelectCityAdapter(Context mContext,ArrayList<CityChildren> cityChildrens) {
        super(mContext);
        this.cityChildrens = cityChildrens;
    }

    @Override
    public boolean isEmpty() {
        return cityChildrens==null || cityChildrens.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?0:cityChildrens.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_city_select, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setView(holder, position);
        return convertView;
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
        CityChildren cityChildren = cityChildrens.get(position);
        holder.item_name.setText(cityChildren.getName());
        //选择省份
        holder.item_name.setTag(R.id.TAG,cityChildren);

    }
}

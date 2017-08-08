package com.hemaapp.thp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.thp.R;
import com.hemaapp.thp.activity.VipOperationActivity;
import com.hemaapp.thp.model.Notice;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/7/19.
 * 会员时间
 */
public class VipTimeAdapter extends HemaAdapter {
    private ArrayList<Notice> notices;

    public VipTimeAdapter(Context mContext,ArrayList<Notice> notices) {
        super(mContext);
        this.notices = notices;
    }

    @Override
    public int getCount() {
        return notices.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_vip_time, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);
        return convertView;
    }
    private class ViewHolder{
        LinearLayout check_layout;
        TextView vip_time;
        ImageView check_img;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.check_img = (ImageView) view.findViewById(R.id.check_img);
        holder.vip_time = (TextView) view.findViewById(R.id.vip_time);
        holder.check_layout = (LinearLayout) view.findViewById(R.id.check_layout);
    }
    private void setData(ViewHolder holder,int position)
    {
        final Notice notice = notices.get(position);
        if (notice.isCheck())
            holder.check_img.setImageResource(R.mipmap.check_on_vip_img);
        else
            holder.check_img.setImageResource(R.mipmap.check_off_vip_img);
        holder.vip_time.setText(notice.getDuration());
        holder.check_layout.setTag(R.id.TAG_VIEWHOLDER,notice);
        holder.check_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notice notice1 = (Notice) v.getTag(R.id.TAG_VIEWHOLDER);
//                if (notice1.isCheck())
//                    notice1.setCheck(false);
//                else
//                    notice1.setCheck(true);
                for (Notice notice2:notices)
                {
                    if (notice2.getDuration().equals(notice1.getDuration()))
                        notice2.setCheck(true);
                    else
                        notice2.setCheck(false);
                }
                notifyDataSetChanged();
                VipOperationActivity activity = (VipOperationActivity) mContext;
                activity.setMoney();
            }
        });
    }
}

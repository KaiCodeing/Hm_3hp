package com.hemaapp.thp.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.thp.R;
import com.hemaapp.thp.model.Notice;

import java.util.ArrayList;

import xtom.frame.util.XtomTimeUtil;

/**
 * Created by lenovo on 2017/7/5.
 * 消息列表
 */
public class MessageAdapter extends HemaAdapter {
    private ArrayList<Notice> notices;

    public MessageAdapter(Context mContext, ArrayList<Notice> notices) {
        super(mContext);
        this.notices = notices;
    }

    @Override
    public boolean isEmpty() {
        return notices == null || notices.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : notices.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_message_system, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);
        return convertView;
    }

    private class ViewHolder {
        //adapter_message_system
        ImageView user_img;
        TextView user_name;
        TextView message_content;
        View view_show;
        TextView time_text;
    }

    private void findView(ViewHolder holder, View view) {
        holder.user_img = (ImageView) view.findViewById(R.id.user_img);
        holder.user_name = (TextView) view.findViewById(R.id.user_name);
        holder.message_content = (TextView) view.findViewById(R.id.message_content);
        holder.view_show = view.findViewById(R.id.view_show);
        holder.time_text = (TextView) view.findViewById(R.id.time_text);
    }

    private void setData(ViewHolder holder, int position) {
        Notice notice = notices.get(position);
        //时间
        holder.time_text.setText(XtomTimeUtil.TransTime(notice.getRegdate(),
                "yyyy-MM-dd"));
        //详情
        if ("1".equals(notice.getKeytype()))
            holder.message_content.setText(notice.getContent());
        else
        {
            SpannableStringBuilder style = new SpannableStringBuilder(notice.getContent());
            //  style.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.backgroud_title)), notice.getContent().length()-4,notice.getContent().length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.message_content.setText(style);
        }//    holder.message_content.setText(notice.getContent());

        //是否已读
        if ("1".equals(notice.getLooktype())) {
            holder.view_show.setVisibility(View.VISIBLE);
        } else
            holder.view_show.setVisibility(View.INVISIBLE);
    }
//    //其他的消息
//    private class ViewHolder2{
//        LinearLayout item_layout;
//        TextView infor_name;
//        TextView infor_loaction;
//        TextView infor_type;
//        TextView infor_time;
//    }
//    private void findView(ViewHolder2 holder,View view)
//    {
//        holder.item_layout = (LinearLayout) view.findViewById(R.id.item_layout);
//        holder.infor_name = (TextView) view.findViewById(R.id.infor_name);
//        holder.infor_loaction = (TextView) view.findViewById(R.id.infor_loaction);
//        holder.infor_type = (TextView) view.findViewById(R.id.infor_type);
//        holder.infor_time = (TextView) view.findViewById(R.id.infor_time);
//    }
//    private void setView(ViewHolder2 holder,int position)
//    {
//
//    }
}

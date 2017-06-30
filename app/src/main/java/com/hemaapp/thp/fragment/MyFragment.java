package com.hemaapp.thp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.thp.R;
import com.hemaapp.thp.activity.SettingActivity;
import com.hemaapp.thp.activity.SuggestionActivity;
import com.hemaapp.thp.activity.WebViewActivity;
import com.hemaapp.thp.base.JhFragment;

/**
 * Created by lenovo on 2017/6/29.
 * 我的
 */
public class MyFragment extends JhFragment implements View.OnClickListener {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private RoundedImageView user_img;
    private TextView user_name;
    private ImageView vip_type;
    private TextView vip_name;
    private TextView vip_name_text;
    private TextView input_year;
    private LinearLayout layout_tel;
    private LinearLayout layout_about;
    private LinearLayout layout_yj;
    private LinearLayout layout_xy;
    private LinearLayout layout_ys;
    private LinearLayout layout_set;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_my);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        user_img = (RoundedImageView) findViewById(R.id.user_img);
        user_name = (TextView) findViewById(R.id.user_name);
        vip_type = (ImageView) findViewById(R.id.vip_type);
        vip_name = (TextView) findViewById(R.id.vip_name);
        vip_name_text = (TextView) findViewById(R.id.vip_name_text);
        input_year = (TextView) findViewById(R.id.input_year);
    }

    @Override
    protected void setListener() {
        back_button.setVisibility(View.INVISIBLE);
        title_text.setText("个人中心");
        next_button.setVisibility(View.INVISIBLE);
        layout_tel.setOnClickListener(this);
        layout_about.setOnClickListener(this);
        layout_yj.setOnClickListener(this);
        layout_xy.setOnClickListener(this);
        layout_ys.setOnClickListener(this);
        layout_set.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.layout_tel://客服电话
                break;
            case R.id.layout_about://关于我们
                Intent intent = new Intent(getActivity(), WebViewActivity.class) ;
                intent.putExtra("keytpe","3");
                startActivity(intent);
                break;
            case R.id.layout_yj://意见反馈
                Intent intent1 = new Intent(getActivity(), SuggestionActivity.class) ;
                startActivity(intent1);
                break;
            case R.id.layout_xy://用户协议
                break;
            case R.id.layout_ys://隐私政策
                break;
            case R.id.layout_set://设置
                Intent intent2 = new Intent(getActivity(), SettingActivity.class) ;
                startActivity(intent2);
                break;
            case R.id.user_img://个人资料
                break;
            case R.id.change_user://修改个人资料
                break;
            case R.id.layout_year://会员续费或购买会员
                break;
        }
    }
}

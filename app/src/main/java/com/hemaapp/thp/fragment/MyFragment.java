package com.hemaapp.thp.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.thp.R;
import com.hemaapp.thp.activity.CommonVipActivity;
import com.hemaapp.thp.activity.LoginActivity;
import com.hemaapp.thp.activity.Register2Activity;
import com.hemaapp.thp.activity.SettingActivity;
import com.hemaapp.thp.activity.SuggestionActivity;
import com.hemaapp.thp.activity.VipOperationActivity;
import com.hemaapp.thp.activity.WebViewActivity;
import com.hemaapp.thp.base.JhFragment;
import com.hemaapp.thp.base.JhctmApplication;
import com.hemaapp.thp.model.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import xtom.frame.util.XtomTimeUtil;

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
    private LinearLayout layout_vip;
    private ImageView change_user;
    private LinearLayout layout_year;
    private ScrollView login_layout;
    private TextView goto_login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_my);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        inIt();
    }

    /**
     * 初始化
     */
    private void inIt() {
        if (JhctmApplication.getInstance().getUser() == null) {
            goto_login.setVisibility(View.VISIBLE);
            login_layout.setVisibility(View.GONE);
        } else {
            goto_login.setVisibility(View.GONE);
            login_layout.setVisibility(View.VISIBLE);
            String token = JhctmApplication.getInstance().getUser().getToken();
            String id = JhctmApplication.getInstance().getUser().getId();
            getNetWorker().clientGet(token, id);
        }

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        HemaArrayResult<User> result = (HemaArrayResult<User>) hemaBaseResult;
        User user = result.getObjects().get(0);
        setData(user);
    }

    /**
     * 填充数据
     *
     * @param user
     */
    private void setData(User user) {
        String path = user.getAvatar();
        user_img.setCornerRadius(100);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.my_fr_user_img)
                .showImageForEmptyUri(R.mipmap.my_fr_user_img)
                .showImageOnFail(R.mipmap.my_fr_user_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, user_img, options);
        user_name.setText(user.getNickname());
        //判断会员角色
        //非会员
        if ("1".equals(user.getFeeaccount())) {
            layout_vip.setVisibility(View.GONE);
            vip_name_text.setText("成为会员");
            input_year.setText("申请");
            input_year.setTextColor(getResources().getColor(R.color.backgroud_title));
        }
        //普通会员
        else if ("2".equals(user.getFeeaccount())) {
            vip_type.setImageResource(R.mipmap.vip_img);
            vip_name.setText("普通会员");
            vip_name_text.setText("普通会员");
            input_year.setText(XtomTimeUtil.TransTime(user.getSigninflag(),
                    "yyyy-MM-dd") + "到期");
        }
        //超级会员
        else {
            vip_type.setImageResource(R.mipmap.sup_vip_img);
            vip_name.setText("高级会员");
            vip_name_text.setText("高级会员");
            input_year.setText(user.getLevel_name()
                     + "到期");
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        showTextDialog("获取用户信息失败，请稍后重试");
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
        layout_tel = (LinearLayout) findViewById(R.id.layout_tel);
        layout_about = (LinearLayout) findViewById(R.id.layout_about);
        layout_yj = (LinearLayout) findViewById(R.id.layout_yj);
        layout_xy = (LinearLayout) findViewById(R.id.layout_xy);
        layout_ys = (LinearLayout) findViewById(R.id.layout_ys);
        layout_set = (LinearLayout) findViewById(R.id.layout_set);
        layout_vip = (LinearLayout) findViewById(R.id.layout_vip);
        change_user = (ImageView) findViewById(R.id.change_user);
        layout_year = (LinearLayout) findViewById(R.id.layout_year);
        login_layout = (ScrollView) findViewById(R.id.login_layout);
        goto_login = (TextView) findViewById(R.id.goto_login);
    }

    @Override
    protected void setListener() {
        user_img.setCornerRadius(100);
        back_button.setVisibility(View.INVISIBLE);
        title_text.setText("个人中心");
        next_button.setVisibility(View.INVISIBLE);
        layout_tel.setOnClickListener(this);
        layout_about.setOnClickListener(this);
        layout_yj.setOnClickListener(this);
        layout_xy.setOnClickListener(this);
        layout_ys.setOnClickListener(this);
        layout_set.setOnClickListener(this);
        change_user.setOnClickListener(this);
        layout_year.setOnClickListener(this);
        goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("keytype","1");
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_tel://客服电话
                String tel = JhctmApplication.getInstance().getSysInitInfo().getSys_service_phone();
                Intent intent3 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent3);
                break;
            case R.id.layout_about://关于我们
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("keytype", "3");
                startActivity(intent);
                break;
            case R.id.layout_yj://意见反馈
                Intent intent1 = new Intent(getActivity(), SuggestionActivity.class);
                startActivity(intent1);
                break;
            case R.id.layout_xy://用户协议
                Intent intent4 = new Intent(getActivity(), WebViewActivity.class);
                intent4.putExtra("keytype", "5");
                startActivity(intent4);
                break;
            case R.id.layout_ys://隐私政策
                Intent intent5 = new Intent(getActivity(), WebViewActivity.class);
                intent5.putExtra("keytype", "6");
                startActivity(intent5);
                break;
            case R.id.layout_set://设置
                Intent intent2 = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent2);
                break;
            case R.id.user_img://个人资料
                break;
            case R.id.change_user://修改个人资料
                Intent intent6 = new Intent(getActivity(), Register2Activity.class);
                intent6.putExtra("keytype", "1");
                getActivity().startActivity(intent6);
                break;
            case R.id.layout_year://会员续费或购买会员
                String vip = JhctmApplication.getInstance().getUser().getFeeaccount();
                if ("1".equals(vip)) {
                    Intent intent7 = new Intent(getActivity(), VipOperationActivity.class);
                    intent7.putExtra("keytype", "1");
                    startActivity(intent7);
                } else {
                    Intent intent7 = new Intent(getActivity(), CommonVipActivity.class);
                    startActivity(intent7);
                }

                break;
        }
    }
}

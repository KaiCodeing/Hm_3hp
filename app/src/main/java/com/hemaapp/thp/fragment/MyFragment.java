package com.hemaapp.thp.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
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
import com.hemaapp.thp.base.JhHttpInformation;
import com.hemaapp.thp.base.JhctmApplication;
import com.hemaapp.thp.model.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import xtom.frame.util.XtomSharedPreferencesUtil;
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
    private DeleteView deleteView;//清空
    private TextView login_text;//退出
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
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGINOUT:
                showProgressDialog("退出登录");
                break;
            case CLIENT_GET:

                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGINOUT:
                cancelProgressDialog();
                break;
            case CLIENT_GET:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGINOUT:
                JhctmApplication.getInstance().setUser(null);
                XtomSharedPreferencesUtil.save(getActivity(), "username", "");// 清空用户名
                XtomSharedPreferencesUtil.save(getActivity(), "password", "");// 青空密码
                XtomSharedPreferencesUtil.save(getActivity(), "autoLogin", "");
                //XtomSharedPreferencesUtil.save(getActivity(), "city_name", "");
                // XtomActivityManager.finishAll();
                getActivity().finish();
                Intent it = new Intent(getActivity(), LoginActivity.class);
                it.putExtra("keytype", "1");
                startActivity(it);
                break;
            case CLIENT_GET:
                HemaArrayResult<User> result = (HemaArrayResult<User>) hemaBaseResult;
                User user = result.getObjects().get(0);
                setData(user);
                break;
        }

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
        //判断VIP角色
        //非VIP
        if ("1".equals(user.getFeeaccount())) {
            layout_vip.setVisibility(View.GONE);
            vip_name_text.setText("成为VIP");
            input_year.setText("申请");
            input_year.setTextColor(getResources().getColor(R.color.backgroud_title));
        }
        //普通VIP
        else if ("2".equals(user.getFeeaccount())) {
            vip_type.setImageResource(R.mipmap.vip_img);
            vip_name.setText("普通VIP");
            vip_name_text.setText("普通VIP");
            input_year.setText(XtomTimeUtil.TransTime(user.getSigninflag(),
                    "yyyy-MM-dd") + "到期");
        }
        //超级VIP
        else {
            vip_type.setImageResource(R.mipmap.sup_vip_img);
            vip_name.setText("高级VIP");
            vip_name_text.setText("高级VIP");
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

        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGINOUT:
                showTextDialog("退出登录失败，请稍后重试");
                break;
            case CLIENT_GET:
                showTextDialog("获取用户信息失败，请稍后重试");
                break;
        }
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
        login_text = (TextView) findViewById(R.id.login_text);
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
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelete();
            }
        });
        goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("keytype", "1");
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
            case R.id.layout_year://VIP续费或购买VIP
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

    private class DeleteView {
        TextView close_pop;
        TextView yas_pop;
        TextView text;
        TextView iphone_number;
    }

    private void showDelete() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_show_hint, null);
        deleteView = new DeleteView();
        deleteView.close_pop = (TextView) view.findViewById(R.id.close_pop);
        deleteView.yas_pop = (TextView) view.findViewById(R.id.yas_pop);
        deleteView.iphone_number = (TextView) view.findViewById(R.id.iphone_number);
        deleteView.text = (TextView) view.findViewById(R.id.text);
        deleteView.iphone_number.setVisibility(View.GONE);
        deleteView.text.setText("确定要退出软件吗");
        deleteView.text.setTextColor(getResources().getColor(R.color.black));
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        deleteView.close_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        deleteView.yas_pop.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      popupWindow.dismiss();
                                                      String token = JhctmApplication.getInstance().getUser().getToken();
                                                      getNetWorker().clientLoginout(token);
                                                  }


                                              }
        );
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new
                BitmapDrawable()
        );
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // popupWindow.showAsDropDown(findViewById(R.id.ll_item));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

}

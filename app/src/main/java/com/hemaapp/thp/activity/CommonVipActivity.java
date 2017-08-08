package com.hemaapp.thp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.thp.R;
import com.hemaapp.thp.base.JhActivity;
import com.hemaapp.thp.base.JhctmApplication;
import com.hemaapp.thp.model.User;

import xtom.frame.util.XtomTimeUtil;

/**
 * Created by lenovo on 2017/6/30.
 * 普通会员续费
 */
public class CommonVipActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private TextView vip_type;
    private TextView vip_time;
    private TextView xf_text;
    private TextView buy_angin;
    private TextView login_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_common_vip);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        inIt();
    }

    //初始化
    private void inIt()
    {
        String token = JhctmApplication.getInstance().getUser().getToken();
        String id = JhctmApplication.getInstance().getUser().getId();
        getNetWorker().clientGet(token,id);
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog("获取个人信息");
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        HemaArrayResult<User> result = (HemaArrayResult<User>) hemaBaseResult;
        User user = result.getObjects().get(0);
        setData(user);
    }
    //填充数据
    private void setData(User user)
    {
        //判断类型
        if ("3".equals(user.getLevel_imgurl()))
            vip_type.setText("全部");
        else if("1".equals(user.getLevel_imgurl()))
            vip_type.setText("工程信息");
        else
            vip_type.setText("采购信息");
        //普通会员
        if ("2".equals(user.getFeeaccount()))
        {
            title_text.setText("普通会员");
            vip_time.setText(XtomTimeUtil.TransTime(user.getSigninflag(),"yyyy.MM.dd"));
        }
        else
        {
            login_text.setVisibility(View.INVISIBLE);
            title_text.setText("高级会员");
            vip_time.setText(XtomTimeUtil.TransTime(user.getLevel_name(),"yyyy.MM.dd"));
        }
        //续费
        xf_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,VipOperationActivity.class);
                intent.putExtra("keytype","2");
                startActivity(intent);
            }
        });
        //升级高级会员
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,VipOperationActivity.class);
                intent.putExtra("keytype","3");
                startActivity(intent);
            }
        });
        //重新购买
        buy_angin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,VipOperationActivity.class);
                intent.putExtra("keytype","1");
                startActivity(intent);
            }
        });
    }
    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        showTextDialog("获取个人信息失败，请稍后重试");
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        vip_type = (TextView) findViewById(R.id.vip_type);
        vip_time = (TextView) findViewById(R.id.vip_time);
        xf_text = (TextView) findViewById(R.id.xf_text);
        buy_angin = (TextView) findViewById(R.id.buy_angin);
        login_text = (TextView) findViewById(R.id.login_text);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("普通会员");
        next_button.setText("会员说明");
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,WebViewActivity.class);
                intent.putExtra("keytype","10");
                startActivity(intent);
            }
        });
    }
}

package com.hemaapp.thp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.thp.R;
import com.hemaapp.thp.base.JhActivity;

/**
 * Created by lenovo on 2017/6/30.
 * 会员，keytype 1购买会员，2续费，3会员升级
 */
public class VipOperationActivity extends JhActivity implements RadioGroup.OnCheckedChangeListener{
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private LinearLayout vip_type_layout;
    private RadioGroup vip_group;
    private RadioButton vip_sup;
    private RadioButton vip_op;
    private RadioGroup infor_group;
    private RadioButton infor1;
    private RadioButton infor2;
    private RadioButton infor3;
    private RadioGroup service_group;
    private RadioGroup year_group;
    private ImageView wx_img;
    private ImageView zfb_img;
    private TextView login_text;
    private int  apliyType = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vip_operation);
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
        vip_type_layout = (LinearLayout) findViewById(R.id.vip_type_layout);
        vip_group = (RadioGroup) findViewById(R.id.vip_group);
        vip_sup = (RadioButton) findViewById(R.id.vip_sup);
        vip_op = (RadioButton) findViewById(R.id.vip_op);
        infor_group = (RadioGroup) findViewById(R.id.infor_group);
        infor1 = (RadioButton) findViewById(R.id.infor1);
        infor2 = (RadioButton) findViewById(R.id.infor2);
        infor3 = (RadioButton) findViewById(R.id.infor3);
        service_group = (RadioGroup) findViewById(R.id.service_group);
        year_group = (RadioGroup) findViewById(R.id.year_group);
        wx_img = (ImageView) findViewById(R.id.wx_img);
        zfb_img = (ImageView) findViewById(R.id.zfb_img);
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
        next_button.setText("会员说明");
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        wx_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (apliyType==1)
                {
                    apliyType=0;
                    wx_img.setImageResource(R.mipmap.check_on_vip_img);
                    zfb_img.setImageResource(R.mipmap.check_off_vip_img);
                }
            }
        });
        zfb_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (apliyType==0)
                {
                    apliyType=1;
                    wx_img.setImageResource(R.mipmap.check_off_vip_img);
                    zfb_img.setImageResource(R.mipmap.check_on_vip_img);
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
}

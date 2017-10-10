package com.hemaapp.thp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.thp.R;
import com.hemaapp.thp.adapter.VipTimeAdapter;
import com.hemaapp.thp.alipay.PayResult;
import com.hemaapp.thp.base.JhActivity;
import com.hemaapp.thp.base.JhHttpInformation;
import com.hemaapp.thp.base.JhctmApplication;
import com.hemaapp.thp.config.XsmConfig;
import com.hemaapp.thp.model.AlipayTrade;
import com.hemaapp.thp.model.Notice;
import com.hemaapp.thp.model.User;
import com.hemaapp.thp.model.WeixinTrade;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;

import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2017/6/30.
 * VIP，keytype 1购买VIP，2续费，3VIP升级
 */
public class VipOperationActivity extends JhActivity implements RadioGroup.OnCheckedChangeListener {
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
    private int apliyType = 0;
    private String keytype;
    private ArrayList<Notice> notices = new ArrayList<>();
    private VipTimeAdapter adapter;
    private GridView gridview;
    //VIP类型
    private String vipType = "1";
    //信息类型
    private String type = "3";
    //vip时间
    private String vipTime = "";
    private TextView money_text;
    private String money;
    IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private ReceiveBroadCast receiveBroadCast; // 广播实例
    private String aplyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vip_operation);
        super.onCreate(savedInstanceState);
        inIt();
        msgApi.registerApp(XsmConfig.APPID_WEIXIN);
        registerMyBroadcast();
    }

    @Override
    protected void onDestroy() {
        unregisterMyBroadcast();
        super.onDestroy();
    }

    private void unregisterMyBroadcast() {
        unregisterReceiver(receiveBroadCast);
    }

    private void registerMyBroadcast() {
        // 注册广播接收
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("hemaapp.dtyw.buy.congzhi.infor"); // 只有持有相同的action的接受者才能接收此广播

        registerReceiver(receiveBroadCast, filter);
    }

    public class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("hemaapp.dtyw.buy.congzhi.infor".equals(intent.getAction())) {
                int err = intent.getIntExtra("res", -1);
                //成功
                if (0 == err) {
                    showTextDialog("支付成功");
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                } else if (-1 == err) {
                    showTextDialog("支付错误");

                } else {
                    showTextDialog("取消支付");

                }

            }
        }
    }

    //获取时间
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().durationGet(token);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DURATION_GET:
                showProgressDialog("获取VIP时间...");
                break;
            case MEMBERFEE_GET:
                showProgressDialog("获取VIP费用...");
                break;
            case MEMBER_BUY:
                showProgressDialog("提交VIP信息...");
                break;
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
                showProgressDialog("正在支付...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DURATION_GET:
            case MEMBER_BUY:
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
            case MEMBERFEE_GET:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DURATION_GET:
                HemaArrayResult<Notice> result = (HemaArrayResult<Notice>) hemaBaseResult;
                notices = result.getObjects();
                notices.get(0).setCheck(true);
                if (!"1".equals(keytype)) {
                    String token = JhctmApplication.getInstance().getUser().getToken();
                    String id = JhctmApplication.getInstance().getUser().getId();
                    getNetWorker().clientGet(token, id);
                } else {
                    setMoney();
                    freshData();
                }

                break;
            case MEMBERFEE_GET:
                HemaArrayResult<String> result2 = (HemaArrayResult<String>) hemaBaseResult;
                money = result2.getObjects().get(0);
                money_text.setText("¥" + money);
                break;
            case CLIENT_GET:
                HemaArrayResult<User> result1 = (HemaArrayResult<User>) hemaBaseResult;
                User user = result1.getObjects().get(0);
                setVipView(user);
                setMoney();
                freshData();
                break;
            case MEMBER_BUY:
                HemaArrayResult<String> result3 = (HemaArrayResult<String>) hemaBaseResult;
                aplyId = result3.getObjects().get(0);
                String token = JhctmApplication.getInstance().getUser().getToken();
                //支付宝
                if (apliyType == 1) {
                    getNetWorker().alipay(token, aplyId, money);
                }
                //微信
                else {
                    getNetWorker().weixinpay(token, aplyId, money);
                }
                break;
            case ALIPAY:
                HemaArrayResult<AlipayTrade> aResult = (HemaArrayResult<AlipayTrade>) hemaBaseResult;
                AlipayTrade trade = aResult.getObjects().get(0);
                String orderInfo = trade.getAlipaysign();
                new AlipayThread(orderInfo).start();
                break;

            case WXPAYMENT:
                HemaArrayResult<WeixinTrade> wResult = (HemaArrayResult<WeixinTrade>) hemaBaseResult;
                WeixinTrade wTrade = wResult.getObjects().get(0);
                goWeixin(wTrade);
                break;
        }
    }

    private void goWeixin(WeixinTrade trade) {
        XtomSharedPreferencesUtil.save(mContext, "order_id", aplyId);
        msgApi.registerApp(XsmConfig.APPID_WEIXIN);
        PayReq request = new PayReq();
        request.appId = trade.getAppid();
        request.partnerId = trade.getPartnerid();
        request.prepayId = trade.getPrepayid();
        request.packageValue = trade.getPackageValue();
        request.nonceStr = trade.getNoncestr();
        request.timeStamp = trade.getTimestamp();
        request.sign = trade.getSign();
        msgApi.sendReq(request);
    }

    private class AlipayThread extends Thread {
        String orderInfo;
        AlipayHandler alipayHandler;

        public AlipayThread(String orderInfo) {
            this.orderInfo = orderInfo;
            alipayHandler = new AlipayHandler(VipOperationActivity.this, aplyId);
        }

        @Override
        public void run() {
            PayTask alipay = new PayTask(VipOperationActivity.this);
            // 调用支付接口，获取支付结果
            String result = alipay.pay(orderInfo);

            Message msg = new Message();
            msg.obj = result;
            alipayHandler.sendMessage(msg);
        }
    }

    private static class AlipayHandler extends Handler {
        VipOperationActivity activity;
        String order_id;

        public AlipayHandler(VipOperationActivity activity,
                             String order_id) {
            this.activity = activity;
            this.order_id = order_id;
        }

        public void handleMessage(Message msg) {
            if (msg == null) {
                activity.showTextDialog("支付失败");
                return;
            }
            PayResult result = new PayResult((String) msg.obj);
            String staus = result.getResultStatus();
            switch (staus) {
                case "9000":
                    activity.showTextDialog("恭喜\n支付成功");

                    postAtTime(new Runnable() {

                        @Override
                        public void run() {
                            activity.finish();
                        }
                    }, 1500);
                    break;
                case "8000":
                    activity.showTextDialog("支付结果确认中");
                    break;
                default:
                    activity.showTextDialog("您取消了支付");
                    break;
            }
        }
    }

    //填充数据
    private void setVipView(User user) {
        //判断续费
        if ("2".equals(keytype)) {
            //判断VIP
            //普通VIP
            if ("2".equals(user.getFeeaccount())) {
                //隐藏VIP选择
                vipType = "2";
            } else {
                vipType = "1";
            }
            vip_type_layout.setVisibility(View.GONE);
            //信息类型
            if ("1".equals(user.getLevel_imgurl())) {
                type = "3";
                infor1.setChecked(true);
                infor2.setVisibility(View.GONE);
                infor3.setVisibility(View.GONE);
            } else if ("2".equals(user.getLevel_imgurl())) {
                type = "1";
                infor1.setVisibility(View.GONE);
                infor2.setChecked(true);
                infor3.setVisibility(View.GONE);
            } else {
                type = "2";
                infor1.setVisibility(View.GONE);
                infor2.setVisibility(View.GONE);
                infor3.setChecked(true);
            }
        }
        //升级
        else {
            vipType = "1";
            vip_op.setVisibility(View.GONE);
        }

    }

    private void freshData() {
        if (adapter == null) {
            adapter = new VipTimeAdapter(mContext, notices);
            adapter.setEmptyString("暂无信息");
            gridview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无信息");
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DURATION_GET:
            case MEMBERFEE_GET:
            case CLIENT_GET:
            case MEMBER_BUY:
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DURATION_GET:
                showTextDialog("获取VIP时间失败，请稍后重试");
                break;
            case MEMBERFEE_GET:
                showTextDialog("获取费用失败，请稍后重试");
                break;
            case CLIENT_GET:
                showTextDialog("获取个人信息失败，请稍后重试");
                break;
            case MEMBER_BUY:
                showTextDialog("提交VIP申请失败，请稍后重试");
                break;
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
                showTextDialog("抱歉，支付失败");
                break;
        }
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
        gridview = (GridView) findViewById(R.id.gridview);
        money_text = (TextView) findViewById(R.id.money_text);
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next_button.setText("VIP说明");
        if ("1".equals(keytype))
            title_text.setText("购买VIP");
        else if ("2".equals(keytype))
            title_text.setText("续费");
        else
            title_text.setText("VIP升级");
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("keytype", "10");
                startActivity(intent);
            }
        });
        wx_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (apliyType == 1) {
                    apliyType = 0;
                    wx_img.setImageResource(R.mipmap.check_on_vip_img);
                    zfb_img.setImageResource(R.mipmap.check_off_vip_img);
                }
            }
        });
        zfb_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (apliyType == 0) {
                    apliyType = 1;
                    wx_img.setImageResource(R.mipmap.check_off_vip_img);
                    zfb_img.setImageResource(R.mipmap.check_on_vip_img);
                }
            }
        });
        vip_group.setOnCheckedChangeListener(this);
        infor_group.setOnCheckedChangeListener(this);
        //购买
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().memberBuy(token, vipType, type, vipTime);
            }
        });

    }

    public void setMoney() {
        //得到时间
        for (Notice notice : notices) {
            if (notice.isCheck())
                vipTime = notice.getDuration();
        }
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().memberfeeGet(token, vipType, type, vipTime);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.vip_sup://高级VIP
                vipType = "1";
                setMoney();
                break;
            case R.id.vip_op://普通VIP
                vipType = "2";
                setMoney();
                break;
            case R.id.infor1://全部
                type = "3";
                setMoney();
                break;
            case R.id.infor2://工程
                type = "1";
                setMoney();
                break;
            case R.id.infor3://采购
                type = "2";
                setMoney();
                break;
        }
    }
}

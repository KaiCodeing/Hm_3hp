package com.hemaapp.thp.activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.thp.R;
import com.hemaapp.thp.base.JhFragmentActivity;
import com.hemaapp.thp.base.JhUpGrade;
import com.hemaapp.thp.fragment.BidFragment;
import com.hemaapp.thp.fragment.CustomizedFragment;
import com.hemaapp.thp.fragment.FollowFragment;
import com.hemaapp.thp.fragment.MyFragment;
import com.hemaapp.thp.fragment.TenderFragment;
import com.hemaapp.thp.model.User;
import com.hemaapp.thp.push.PushUtils;

import java.util.List;

import xtom.frame.util.XtomToastUtil;

//主页
public class MainActivity extends JhFragmentActivity {
    private FrameLayout content_frame;
    private RadioGroup radiogroup;
    private JhUpGrade upGrade;
    private RadioButton radiobutton0;
    private RadioButton radiobutton2;
    private long exitTime = 0;
    private RadioButton radiobutton1;
    private TextView main_point;
    private long checkTime = 0;
    private String keytype;
    private ReceiveBroadCast receiveBroadCast; // 广播实例
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        if (isNull(keytype)) {
            toogleFragment(TenderFragment.class);
            if (TenderFragment.getInstance() != null) {
                TenderFragment.getInstance().inIt();
            }
        } else {
            radiobutton1.setChecked(true);
            // toogleFragment(BidFragment.class);
        }
        // 个推相关 start
        startGeTuiPush();
        // 个推相关 end
        upGrade = new JhUpGrade(this);
        registerMyBroadcast();
    }

    @Override
    protected void findView() {
        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        radiobutton1 = (RadioButton) findViewById(R.id.radiobutton1);
        main_point = (TextView) findViewById(R.id.main_point);
        radiobutton0 = (RadioButton) findViewById(R.id.radiobutton0);
        radiobutton1 = (RadioButton) findViewById(R.id.radiobutton1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (upGrade != null) {
            upGrade.check();
        }
    }
    private void registerMyBroadcast() {
        // 注册广播接收
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("hemaapp.3hp.user.infor"); // 只有持有相同的action的接受者才能接收此广播

        registerReceiver(receiveBroadCast, filter);
    }

    public class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("hemaapp.3hp.user.infor".equals(intent.getAction())) {
//                toogleFragment(TenderFragment.class);
//                if (TenderFragment.getInstance() != null) {
//                    TenderFragment.getInstance().inIt();
//                }
                if (radiobutton0!=null)
                radiobutton0.setChecked(true);
            }
        }
    }
    private void unregisterMyBroadcast() {
        unregisterReceiver(receiveBroadCast);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPush();
        unregisterMyBroadcast();
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        radiogroup.setOnCheckedChangeListener(new OnTabListener());
    }

    private class OnTabListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radiobutton0:// 招标
                    toogleFragment(TenderFragment.class);
                    if (TenderFragment.getInstance() != null) {
                        TenderFragment.getInstance().inIt();
                    }
                    break;
                case R.id.radiobutton1:// 中标
                    toogleFragment(BidFragment.class);
                    if (BidFragment.getInstance() != null) {
                        BidFragment.getInstance().inIt();
                    }
                    break;
                case R.id.radiobutton2:// 定制

                    toogleFragment(CustomizedFragment.class);
                    if (CustomizedFragment.getInstance() != null) {
                        CustomizedFragment.getInstance().showVip();
                    }
                    break;
                case R.id.radiobutton3://关注
                    toogleFragment(FollowFragment.class);
                    if (FollowFragment.getInstance() != null) {
                        FollowFragment.getInstance().showVip();
                    }
                    break;
                case R.id.radiobutton4:
                    toogleFragment(MyFragment.class);
                    break;
            }
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

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    /**
     * 显示或更换Fragment
     *
     * @param c
     */
    public void toogleFragment(Class<? extends Fragment> c) {
        FragmentManager manager = getSupportFragmentManager();
        String tag = c.getName();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(tag);

        if (fragment == null) {
            try {
                fragment = c.newInstance();
                log_i("第一次+++++++++++++++++++++");
                // 替换时保留Fragment,以便复用
                transaction.add(R.id.content_frame, fragment, tag);
            } catch (Exception e) {
                // ignore
            }
        } else {
            // nothing
        }
        // 遍历存在的Fragment,隐藏其他Fragment
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null)
            for (Fragment fm : fragments)
                if (!fm.equals(fragment))
                    transaction.hide(fm);

        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                XtomToastUtil.showShortToast(mContext, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                // moveTaskToBack(false);
                finish();
//                XtomActivityManager.finishAll();
                NotificationManager nm = (NotificationManager) mContext
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancelAll();

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /* 推送相关 */
    private PushReceiver pushReceiver;

    private void startPush() {
        User user = getApplicationContext().getUser();
        if (user == null) {
            log_i("未登录，无需启动推送服务");
            return;
        }
        if (!PushUtils.hasBind(getApplicationContext())) {
            PushManager.startWork(getApplicationContext(),
                    PushConstants.LOGIN_TYPE_API_KEY,
                    PushUtils.getMetaValue(this, "api_key"));
            // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
            // PushManager.enableLbs(getApplicationContext());
        } else {
            saveDevice();
        }
        registerPushReceiver();
    }

    private void stopPush() {
        PushManager.stopWork(getApplicationContext());
        PushUtils.setBind(mContext, false);
        unregisterPushReceiver();
    }

    private void startGeTuiPush() {
        com.igexin.sdk.PushManager.getInstance().initialize(mContext);
        registerPushReceiver();
    }

    private void stopGeTuiPush() {
        unregisterPushReceiver();
    }

    private void registerPushReceiver() {
        if (pushReceiver == null) {
            pushReceiver = new PushReceiver();
            IntentFilter mFilter = new IntentFilter("com.hemaapp.push.connect");
            mFilter.addAction("com.hemaapp.push.msg");
            mFilter.addAction("com.hemaapp.car.number");
            registerReceiver(pushReceiver, mFilter);
        }
    }

    private void unregisterPushReceiver() {
        if (pushReceiver != null)
            unregisterReceiver(pushReceiver);
    }

    private class PushReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            handleEvent(intent);
        }

        private void handleEvent(Intent intent) {
            String action = intent.getAction();

            if ("com.hemaapp.push.connect".equals(action)) {
                saveDevice();

            } else if ("com.hemaapp.push.msg".equals(action)) {
                boolean unread = PushUtils.getmsgreadflag(
                        getApplicationContext(), "2");
                if (unread) {
                    // showNoticePoint();
                    log_i("有未读推送");
                } else {
                    log_i("无未读推送");
                    // hideNoticePoint();
                }
            } else if ("com.hemaapp.car.number".equals(action)) {
//                //获取购物车商品数量
//                if (!isNull(XtomSharedPreferencesUtil.get(MainActivity.this,"username")))
//                {  String token = DtywApplication.getInstance().getUser().getToken();
//                    getNetWorker().getCartgoodsnum(token);
//                }
//                else
//                {
//                    main_point.setVisibility(View.INVISIBLE);
//                }
            }
        }
    }

    public void saveDevice() {
        User user = getApplicationContext().getUser();
        if (user == null) {
            return;
        }
        getNetWorker().deviceSave(user.getToken(),
                PushUtils.getUserId(mContext), "2",
                PushUtils.getChannelId(mContext));
    }

	/* 推送相关end */
}

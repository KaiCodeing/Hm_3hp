package com.hemaapp.thp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.HemaWebView;
import com.hemaapp.thp.R;
import com.hemaapp.thp.base.JhActivity;

/**
 * Created by lenovo on 2017/6/28.
 * 网页
 */
public class WebViewActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private HemaWebView webview_aboutwe;
    private String keytype;
    private String url;
    private FrameLayout vip_layout;
    private TextView project;
    private TextView purchase;
    private View view1;
    private View view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_webveiw);
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
        webview_aboutwe = (HemaWebView) findViewById(R.id.webview_aboutwe);
        vip_layout = (FrameLayout) findViewById(R.id.vip_layout);
        project = (TextView) findViewById(R.id.project);
        purchase = (TextView) findViewById(R.id.purchase);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
        url = mIntent.getStringExtra("url");
        log_i("+++++" + url);
    }

    @Override
    protected void setListener() {
        next_button.setVisibility(View.INVISIBLE);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String sys_web_service = getApplicationContext().getSysInitInfo()
                .getSys_web_service();
        if ("1".equals(keytype)) {
            String path = sys_web_service + "webview/parm/protocal";
            webview_aboutwe.loadUrl(path);
            title_text.setText("注册协议");
            return;
        } else if ("2".equals(keytype)) {
//            webview_aboutwe.loadUrl(url);
            title_text.setText("预览");
            return;
        } else if ("3".equals(keytype)) {
            String path = sys_web_service + "webview/parm/instructions";
            webview_aboutwe.loadUrl(path);
            title_text.setText("关于我们");
        } else if ("4".equals(keytype)) {
            String path = sys_web_service + "webview/parm/news_" + url;
            webview_aboutwe.loadUrl(path);
            title_text.setText("要闻详情");
        } else if ("5".equals(keytype)) {
            String path = sys_web_service + "webview/parm/service";
            webview_aboutwe.loadUrl(path);
            title_text.setText("服务协议");
        } else if ("6".equals(keytype)) {
            String path = sys_web_service + "webview/parm/privacy";
            webview_aboutwe.loadUrl(path);
            title_text.setText("隐私政策");
        } else if ("10".equals(keytype)) {
            vip_layout.setVisibility(View.VISIBLE);
            String path = sys_web_service + "webview/parm/advanced";
            webview_aboutwe.loadUrl(path);
            title_text.setText("VIP说明");
        }
        //选择高级VIP
        project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sys_web_service = getApplicationContext().getSysInitInfo()
                        .getSys_web_service();
                String path = sys_web_service + "webview/parm/advanced";
                webview_aboutwe.loadUrl(path);
                //  title_text.setText("VIP说明");
                project.setTextColor(getResources().getColor(R.color.backgroud_title));
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.INVISIBLE);
                purchase.setTextColor(getResources().getColor(R.color.web));
            }
        });
        //选择普通VIP
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sys_web_service = getApplicationContext().getSysInitInfo()
                        .getSys_web_service();
                String path = sys_web_service + "webview/parm/description";
                webview_aboutwe.loadUrl(path);
                //    title_text.setText("VIP说明");
                purchase.setTextColor(getResources().getColor(R.color.backgroud_title));
                view2.setVisibility(View.VISIBLE);
                view1.setVisibility(View.INVISIBLE);
                project.setTextColor(getResources().getColor(R.color.web));
            }
        });

    }
}

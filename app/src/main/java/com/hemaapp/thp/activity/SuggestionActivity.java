package com.hemaapp.thp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.thp.R;
import com.hemaapp.thp.base.JhActivity;
import com.hemaapp.thp.base.JhctmApplication;

/**
 * Created by lenovo on 2017/6/30.
 * 一间反馈
 */
public class SuggestionActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private EditText input_content;
    private EditText input_tel;
    private TextView login_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_suggestion);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog("提交意见...");
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog("提交成功!");
        next_button.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        showTextDialog("提交失败，请稍后重试！");
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        input_content = (EditText) findViewById(R.id.input_content);
        input_tel = (EditText) findViewById(R.id.input_tel);
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
        title_text.setText("意见反馈");
        next_button.setVisibility(View.INVISIBLE);
        //提交
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = input_content.getText().toString();
                String tel = input_tel.getText().toString();
                if (isNull(content)) {
                    showTextDialog("请输入意见");
                    return;
                }
                if (isNull(tel)) {
                    showTextDialog("请输入您的电话号码");
                    return;
                }
                String mobile = "\\d{11}";// 只判断11位
                if (!tel.matches(mobile)) {
                    showTextDialog("您输入的手机号不正确");
                    return;
                }
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().adviceAdd(token, content, tel);
            }
        });
    }
}

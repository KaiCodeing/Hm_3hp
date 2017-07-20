package com.hemaapp.thp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.thp.R;
import com.hemaapp.thp.base.JhActivity;
import com.hemaapp.thp.base.JhHttpInformation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xtom.frame.XtomActivityManager;

/**
 * Created by lenovo on 2017/6/27.
 * 注册第一步
 */
public class Register1Activity extends JhActivity {
    private ImageButton bank_img;
    private EditText username_text_add;
    private EditText yanzheng_text;
    private Button send_button;
    private EditText input_psw;
    private CheckBox check_pwd;
    private TextView forget_password;//注册协议
    private TextView login_text;//下一步
    private TextView goto_login;//去登陆
    private LinearLayout agin_layout;
    private TextView second;
    private TimeThread timeThread;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.register_1_activity);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                showProgressDialog("正在验证手机号");
                break;
            case CODE_GET:
                showProgressDialog("正在发送验证码");
                break;
            case CODE_VERIFY:
                showProgressDialog("正在验证验证码");
                break;
            case PASSWORD_RESET:
                showProgressDialog("正在修改密码");
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
            case CODE_GET:
            case CODE_VERIFY:
            case PASSWORD_RESET:
                cancelProgressDialog();
                break;

            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                showTextDialog("手机号已被注册！");
                break;
            case CODE_GET:
                this.username = hemaNetTask.getParams().get("username");
//                phone_show.setText("验证码已发送到 "
//                        + HemaUtil.hide(hemaNetTask.getParams().get("username"), "1"));
//                phone_show.setVisibility(View.VISIBLE);

                timeThread = new TimeThread(new TimeHandler(this));
                timeThread.start();
                break;
            case CODE_VERIFY:
                HemaArrayResult<String> result = (HemaArrayResult<String>) hemaBaseResult;
                String temp_token = result.getObjects().get(0);
                log_i("接口返回的token++" + temp_token);
                String userName = username_text_add.getText().toString();
                if (isNull(this.username)) {
                    showTextDialog("请填写手机号");
                    return;
                }
                if (this.username.equals(userName)) {
                } else {
                    showTextDialog("两次输入手机号码不一致，\n请确认");
                    return;
                }
                Intent intent = new Intent(Register1Activity.this,Register2Activity.class);
                intent.putExtra("temp_token",temp_token);
                intent.putExtra("code",yanzheng_text.getText().toString());
                intent.putExtra("username",userName);
                intent.putExtra("password",input_psw.getText().toString());
                startActivity(intent);
                break;

            default:

                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                String username = hemaNetTask.getParams().get("username");
                getNetWorker().codeGet(username, "1");
                break;
            case CODE_GET:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case CODE_VERIFY:
                if (hemaBaseResult.getError_code() == 103) {
                    showTextDialog("输入的验证码不正确！");
                } else {
                    showTextDialog(hemaBaseResult.getMsg());
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                showTextDialog("验证手机号失败");
                break;
            case CODE_GET:
                showTextDialog("发送验证码失败");
                break;
            case CODE_VERIFY:
                showTextDialog("校验验证码失败");
                break;
            case PASSWORD_RESET:
                showTextDialog("修改密码失败，请稍后重试");
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
        bank_img = (ImageButton) findViewById(R.id.bank_img);
        username_text_add = (EditText) findViewById(R.id.username_text_add);
        yanzheng_text = (EditText) findViewById(R.id.yanzheng_text);
        send_button = (Button) findViewById(R.id.send_button);
        input_psw = (EditText) findViewById(R.id.input_psw);
        check_pwd = (CheckBox) findViewById(R.id.check_pwd);
        forget_password = (TextView) findViewById(R.id.forget_password);
        login_text = (TextView) findViewById(R.id.login_text);
        goto_login = (TextView) findViewById(R.id.goto_login);
        agin_layout = (LinearLayout) findViewById(R.id.agin_layout);
        second = (TextView) findViewById(R.id.second);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        bank_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        /**
         * 发送验证码
         */
        send_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = username_text_add.getText().toString();
                if (isNull(username)) {
                    showTextDialog("请输入手机号");
                    return;
                }
                String mobile = "\\d{11}";// 只判断11位
                if (!username.matches(mobile)) {
                    showTextDialog("您输入的手机号不正确");
                    return;
                }
                getNetWorker().clientVerify(username);
            }
        });
        //y验证验证码
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNull(username_text_add.getText().toString())) {
                    showTextDialog("请先验证手机号");
                    return;
                }
                if (isNull(yanzheng_text.getText().toString())) {
                    showTextDialog("输入的验证码不能为空!");
                    return;
                }
                if (!check_pwd.isChecked()) {
                    showTextDialog("请同意注册说明");
                    return;
                }

                if (isNull(input_psw.getText().toString())) {
                    showTextDialog("输入的密码不能为空!");
                    return;
                }
                if (input_psw.getText().toString().trim().length() >= 8 && input_psw.getText().toString().trim().length() <= 16) {

                } else {
                    showTextDialog("密码输入不正确\n请输入8-16位密码");
                    return;
                }
                if (judgeContainsStr(input_psw.getText().toString())) {
                } else {
                    showTextDialog("密码请输入数字和字母的组合");
                    return;
                }
                String codeString = yanzheng_text.getText().toString();
                getNetWorker().codeVerify(username_text_add.getText().toString(),
                        codeString);
            }
        });
        //去登陆
        goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XtomActivityManager.finishAll();
                Intent intent = new Intent(Register1Activity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        //注册协议
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("keytype", "1");
                startActivity(intent);
            }
        });
    }

    private class TimeThread extends Thread {
        private int curr;

        private TimeHandler timeHandler;

        public TimeThread(TimeHandler timeHandler) {
            this.timeHandler = timeHandler;
        }

        void cancel() {
            curr = 0;
        }

        @Override
        public void run() {
            curr = 60;
            while (curr > 0) {
                timeHandler.sendEmptyMessage(curr);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // ignore
                }
                curr--;
            }
            timeHandler.sendEmptyMessage(-1);
        }
    }

    private static class TimeHandler extends Handler {
        Register1Activity activity;

        public TimeHandler(Register1Activity activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:

                    activity.send_button.setText("重新发送");
                    activity.send_button.setVisibility(View.VISIBLE);
                    activity.agin_layout.setVisibility(View.INVISIBLE);
                    break;
                default:
                    activity.send_button.setVisibility(View.GONE);
                    activity.agin_layout.setVisibility(View.VISIBLE);
                    activity.second.setText("" + msg.what);
                    break;
            }
        }
    }
    /**
     * 该方法主要使用正则表达式来判断字符串中是否包含字母
     *
     * @param cardNum 待检验的原始卡号
     * @return 返回是否包含
     * @author fenggaopan 2015年7月21日 上午9:49:40
     */
    public boolean judgeContainsStr(String cardNum) {
        String regex = ".*[a-zA-Z]+.*";
        String num =  ".*[0-9]+.*";
        Matcher m = Pattern.compile(regex).matcher(cardNum);
        Matcher n = Pattern.compile(num).matcher(cardNum);
        return m.matches() && n.matches();
    }
}

package com.hemaapp.thp.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
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
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.thp.R;
import com.hemaapp.thp.base.JhActivity;
import com.hemaapp.thp.base.JhctmApplication;

import java.math.BigDecimal;

import xtom.frame.XtomActivityManager;
import xtom.frame.image.cache.XtomImageCache;
import xtom.frame.media.XtomVoicePlayer;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2017/6/30.
 * 设置
 */
public class SettingActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private ImageView push_img;
    private LinearLayout layout_safely;
    private LinearLayout layout_cache;
    private TextView login_text;//退出
    private TextView cache_text;
    private DeleteView deleteView;//清空
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
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
        JhctmApplication.getInstance().setUser(null);
        XtomSharedPreferencesUtil.save(mContext, "username", "");// 清空用户名
        XtomSharedPreferencesUtil.save(mContext, "password", "");// 青空密码
        XtomSharedPreferencesUtil.save(mContext,"autoLogin","");
        //XtomSharedPreferencesUtil.save(getActivity(), "city_name", "");
        XtomActivityManager.finishAll();
        Intent it = new Intent(mContext, LoginActivity.class);
        startActivity(it);
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        showTextDialog("退出登录失败，请稍后重试");
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        push_img = (ImageView) findViewById(R.id.push_img);
        layout_safely = (LinearLayout) findViewById(R.id.layout_safely);
        layout_cache = (LinearLayout) findViewById(R.id.layout_cache);
        login_text = (TextView) findViewById(R.id.login_text);
        cache_text = (TextView) findViewById(R.id.cache_text);
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
        title_text.setText("设置");
        next_button.setVisibility(View.INVISIBLE);
        String notice = XtomSharedPreferencesUtil.get(mContext, "notice");
        if (isNull(notice))
            push_img.setImageResource(R.mipmap.set_push_off);
        else
            push_img.setImageResource(R.mipmap.set_push_on);
        //接受推送
        push_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notice = XtomSharedPreferencesUtil.get(mContext, "notice");
                if (isNull(notice)) {
                    push_img.setImageResource(R.mipmap.set_push_on);
                    XtomSharedPreferencesUtil.save(mContext, "notice", "1");
                } else {
                    push_img.setImageResource(R.mipmap.set_push_off);
                    XtomSharedPreferencesUtil.save(mContext, "notice", "");
                }
            }
        });
        //缓存
        cache_text.setText(bytes2kb(XtomImageCache.getInstance(mContext).getCacheSize()));
        layout_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClearTask().execute();
            }
        });
        //账号安全
        layout_safely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ChangePWDActivity.class);
                startActivity(intent);
            }
        });
        //退出登录
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelete();
            }
        });
    }
    private class DeleteView {
        TextView close_pop;
        TextView yas_pop;
        TextView text;
        TextView iphone_number;
    }

    private void showDelete() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_show_hint, null);
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

    private class ClearTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // 删除图片缓存
            XtomImageCache.getInstance(mContext).deleteCache();
            // 删除语音缓存
            XtomVoicePlayer player = XtomVoicePlayer.getInstance(mContext);
            player.deleteCache();
            player.release();
            return null;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog("正在清除缓存");
        }

        @Override
        protected void onPostExecute(Void result) {
            cancelProgressDialog();
            showTextDialog("缓存清理完毕！");
            cache_text.setText(bytes2kb(XtomImageCache.getInstance(mContext).getCacheSize()));
        }
    }

    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "M");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "K");
    }

}

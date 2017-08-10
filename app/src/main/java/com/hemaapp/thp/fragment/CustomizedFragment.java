package com.hemaapp.thp.fragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.thp.R;
import com.hemaapp.thp.activity.LoginActivity;
import com.hemaapp.thp.activity.VipOperationActivity;
import com.hemaapp.thp.activity.WebViewActivity;
import com.hemaapp.thp.base.JhFragment;
import com.hemaapp.thp.base.JhHttpInformation;
import com.hemaapp.thp.base.JhNetWorker;
import com.hemaapp.thp.base.JhctmApplication;
import com.hemaapp.thp.model.CityChildren;
import com.hemaapp.thp.model.CitySan;
import com.hemaapp.thp.model.User;
import com.hemaapp.thp.view.AreaDialog;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by lenovo on 2017/6/29.
 * 定制
 */
public class CustomizedFragment extends JhFragment {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private TextView input_city;
    private TextView input_sex;
    private TextView data_time;
    private TextView input_type;
    private EditText input_word;
    private EditText input_email;
    private TextView over_time;
    private TextView agin_vip;
    private TextView login_text;
    private AreaDialog areaDialog;
    private DeleteView deleteView;//清空
    private String province;//省
    private String city;//市
    private String area;//区
    private String district1 = "";//省id
    private String district2 = "";//市id
    private String district3 = "";//区id
    private String Loaction;
    private ViewSex viewSex;
    private TimePickerDialog timePickerDialog;
    private ViewType viewType;
    private String typeDJ = "";
    private static CustomizedFragment fragment;

    public static CustomizedFragment getInstance() {
        return fragment;
    }

    private LinearLayout login_layout;
    private TextView goto_login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        fragment = this;
        setContentView(R.layout.fragment_customized);
        super.onCreate(savedInstanceState);
        if (JhctmApplication.getInstance().getUser() == null) {
            login_layout.setVisibility(View.GONE);
            goto_login.setVisibility(View.VISIBLE);
        } else {
            inIt();
        }

    }

    //展示是否是会员
    public void showVip() {
        if (JhctmApplication.getInstance().getUser() == null) {
            login_layout.setVisibility(View.GONE);
            goto_login.setVisibility(View.VISIBLE);
        } else {
            inIt();
        }
    }

    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        String id = JhctmApplication.getInstance().getUser().getId();
        JhNetWorker netWorker = getNetWorker();
        if (netWorker == null)
            netWorker = new JhNetWorker(getActivity());
        netWorker.clientGet(token, id);

        //  CustomizedFragment.this.getNetWorker().clientGet(token, id);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                break;
            case MADEINFO:
                showProgressDialog("提交定制信息...");
                break;
            case ADDRESS_LIST:
                showProgressDialog("获取地区信息");
                break;

        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                break;
            case MADEINFO:

            case ADDRESS_LIST:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        final JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                HemaArrayResult<User> result = (HemaArrayResult<User>) hemaBaseResult;
                User user = result.getObjects().get(0);
                String vip = user.getFeeaccount();
                login_layout.setVisibility(View.VISIBLE);
                goto_login.setVisibility(View.GONE);
                if ("1".equals(vip) || "2".equals(vip)) {
                    showDelete();
                    input_word.setEnabled(false);
                    input_email.setEnabled(false);
                } else {
                    input_word.setEnabled(true);
                    input_email.setEnabled(true);
                    setData(user);
                    if (JhctmApplication.getInstance().getCityInfo() == null)
                        getNetWorker().districtALLList();
                }
                break;
            case MADEINFO:
                showTextDialog("提交定制信息成功！");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        inIt();
                    }
                }, 1000);
                break;
            case ADDRESS_LIST:
                HemaArrayResult<CityChildren> result1 = (HemaArrayResult<CityChildren>) hemaBaseResult;
//                CitySan citySan = result1.getObjects().get(0);
//                ArrayList<CitySan> citySanArrayList = ;
//                CitySan citySan = new CitySan();
                //     ArrayList<CityChildren> cityChildrens= result1.getObjects();
                ArrayList<CityChildren> cityChildrens = result1.getObjects();
                CitySan citySan = new CitySan();
                citySan.setChildren(cityChildrens);
                JhctmApplication.getInstance().setCityInfo(citySan);
                break;
        }
    }

    /**
     * 填充数据
     *
     * @param user
     */
    private void setData(User user) {
        input_city.setText(user.getTop_week() + user.getTop_year() + user.getBackimg());
        Loaction = user.getTop_week() + "," + user.getTop_year() + "," + user.getBackimg();
        //推送时间
        if (isNull(user.getSelfsign())) {
            input_sex.setText("及时");
            data_time.setVisibility(View.GONE);
        } else {
            data_time.setVisibility(View.VISIBLE);
            input_sex.setText("定时");
            data_time.setText(user.getSelfsign());
        }

        if ("1".equals(user.getContisignintimes())) {
            typeDJ = "1";
            input_type.setText("全部");
        } else if ("2".equals(user.getContisignintimes())) {
            typeDJ = "2";
            input_type.setText("工程信息");
        } else if ("3".equals(user.getContisignintimes())) {
            typeDJ = "3";
            input_type.setText("采购信息信息");
        }
        input_word.setText(user.getScore());
        input_email.setText(user.getExpiredflag());
        //判断普通会员，高级会员
        String vipt = user.getFeeaccount();
        if ("2".equals(vipt))
            over_time.setText(user.getSigninflag());
        else
            over_time.setText(user.getLevel_name());
        agin_vip.setVisibility(View.VISIBLE);
        agin_vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VipOperationActivity.class);
                intent.putExtra("keytype", "2");
                startActivity(intent);
            }
        });
        //推送消息地区
        input_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCity();
            }
        });
        //选择推送时间
        input_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSex();
            }
        });
        login_text.setVisibility(View.VISIBLE);
        //选择类型
        input_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showType();
            }
        });
        //提交
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = input_city.getText().toString();
                String time = input_sex.getText().toString();
                String time_data = data_time.getText().toString();
                String inforType = input_type.getText().toString();
                String inforword = input_word.getText().toString();
                String inforemail = input_email.getText().toString();
                if (isNull(address)) {
                    showTextDialog("请选择推送地区");
                    return;
                }
                if (isNull(time)) {
                    showTextDialog("请选择推送时间");
                    return;
                }
                if (isNull(inforType)) {
                    showTextDialog("请选择信息类型");
                    return;
                }
                if (isNull(inforword)) {
                    showTextDialog("请填写推送关键词");
                    return;
                }
                String token = JhctmApplication.getInstance().getUser().getToken();
                if ("及时".equals(time)) {
                    getNetWorker().madeInfo(token, Loaction, "", typeDJ, inforword, inforemail);
                } else {
                    getNetWorker().madeInfo(token, Loaction, time_data, typeDJ, inforword, inforemail);
                }
            }
        });
    }

    private void showCity() {
        if (areaDialog == null) {
            areaDialog = new AreaDialog(getActivity(), "1");
            areaDialog.setType();
//            areaDialog.closeSan();
            areaDialog.setButtonListener(new onbutton());
            return;
        }
        areaDialog.show();
    }

    private class onbutton implements com.hemaapp.thp.view.AreaDialog.OnButtonListener {

        @Override
        public void onLeftButtonClick(AreaDialog dialog) {
            // TODO Auto-generated method stub

            areaDialog.cancel();
        }


        @Override
        public void onRightButtonClick(AreaDialog dialog) {
            // TODO Auto-generated method stub
            input_city.setText(areaDialog.getCityName());
//            homecity = home_text.getText().toString();
//            home_text.setTag(areaDialog.getId());
            String[] cityid = areaDialog.getId().split(",");
            String[] cityName = areaDialog.getCityJGName().split(",");
            district1 = cityid[0];
            district2 = cityid[1];
            district3 = cityid[2];
            province = cityName[0];
            city = cityName[1];
            area = cityName[2];
            if (area.equals("全部"))
                Loaction = province + "," + city;
            else
                Loaction = province + "," + city + "," + area;
            log_i("++" + province + " ---" + city + "==" + area);
            areaDialog.cancel();
        }
    }

    //时间选择
    //选择性别
    public class ViewSex {
        TextView camera_text;
        TextView album_text;
        TextView textView1_camera;
    }

    private void showSex() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popwindo_camera, null);
        viewSex = new ViewSex();
        viewSex.camera_text = (TextView) view.findViewById(R.id.camera_text);
        viewSex.album_text = (TextView) view.findViewById(R.id.album_text);
        viewSex.textView1_camera = (TextView) view.findViewById(R.id.textView1_camera);
        viewSex.camera_text.setText("及时");
        viewSex.album_text.setText("定时");
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        viewSex.camera_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_sex.setText("及时");
                data_time.setVisibility(View.GONE);
                popupWindow.dismiss();
            }
        });
        viewSex.album_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                input_sex.setText("女");
                final Calendar c = Calendar.getInstance();
                popupWindow.dismiss();
                timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        c.set(0, 0, 0, hourOfDay, minute);
                        input_sex.setText("定时");
                        // data_time.setText(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
                        data_time.setText(DateFormat.format("HH:mm", c));
                        data_time.setVisibility(View.VISIBLE);
                    }
                }, 0, 0, true);
                timePickerDialog.setTitle("选择时间");
                timePickerDialog.show();
            }
        });
        viewSex.textView1_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
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
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
            case MADEINFO:
            case ADDRESS_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showTextDialog("获取个人信息失败，请稍后重试");
                break;
            case MADEINFO:
                showTextDialog("上传定制信息失败，请稍后重试");
                break;
            case ADDRESS_LIST:
                showTextDialog("获取地区失败，请稍后重试");
                break;

        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        input_city = (TextView) findViewById(R.id.input_city);
        input_sex = (TextView) findViewById(R.id.input_sex);
        data_time = (TextView) findViewById(R.id.data_time);
        input_type = (TextView) findViewById(R.id.input_type);
        input_word = (EditText) findViewById(R.id.input_word);
        input_email = (EditText) findViewById(R.id.input_email);
        over_time = (TextView) findViewById(R.id.over_time);
        agin_vip = (TextView) findViewById(R.id.agin_vip);
        login_text = (TextView) findViewById(R.id.login_text);
        goto_login = (TextView) findViewById(R.id.goto_login);
        login_layout = (LinearLayout) findViewById(R.id.login_layout);
    }

    @Override
    protected void setListener() {
        back_button.setVisibility(View.INVISIBLE);
        title_text.setText("定制");
        next_button.setText("会员说明");
        data_time.setVisibility(View.GONE);
        agin_vip.setVisibility(View.GONE);
        login_text.setVisibility(View.GONE);
        //会员说明
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("keytype", "10");
                getActivity().startActivity(intent);
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

    private class DeleteView {
        TextView close_pop;
        TextView yas_pop;
        TextView text;
        TextView iphone_number;
    }

    //提示
    private void showDelete() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.pop_show_hint, null);
        deleteView = new DeleteView();
        deleteView.close_pop = (TextView) view.findViewById(R.id.close_pop);
        deleteView.yas_pop = (TextView) view.findViewById(R.id.yas_pop);
        deleteView.text = (TextView) view.findViewById(R.id.text);
        deleteView.iphone_number = (TextView) view.findViewById(R.id.iphone_number);
        deleteView.text.setText("温馨提示");
        deleteView.iphone_number.setText("您现在还不是高级会员，不具备定制服务\n的权限，是否申请成为高级会员？");
        deleteView.close_pop.setText("我知道了");
        deleteView.yas_pop.setText("去购买");
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
                                                      Intent intent = new Intent(getActivity(), VipOperationActivity.class);
                                                      intent.putExtra("keytype", "1");
                                                      getActivity().startActivity(intent);
                                                      popupWindow.dismiss();
                                                  }
                                              }
        );
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
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

    //清空
    //选择性别
    public class ViewType {
        TextView camera_text;
        TextView album_text;
        TextView textView1_camera;
        TextView all_type;
    }

    private void showType() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_select_type, null);
        viewType = new ViewType();
        viewType.camera_text = (TextView) view.findViewById(R.id.camera_text);
        viewType.album_text = (TextView) view.findViewById(R.id.album_text);
        viewType.textView1_camera = (TextView) view.findViewById(R.id.textView1_camera);
        viewType.all_type = (TextView) view.findViewById(R.id.all_type);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });
        //工程
        viewType.camera_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeDJ = "2";
                input_type.setText("工程信息");
                popupWindow.dismiss();
            }
        });
        //采购
        viewType.album_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeDJ = "3";
                input_type.setText("采购信息");
                popupWindow.dismiss();
            }
        });
        //全部
        viewType.all_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeDJ = "1";
                input_type.setText("全部");
                popupWindow.dismiss();
            }
        });
        viewType.textView1_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
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
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
}

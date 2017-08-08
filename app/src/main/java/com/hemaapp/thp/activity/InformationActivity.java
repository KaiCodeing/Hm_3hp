package com.hemaapp.thp.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.HemaWebView;
import com.hemaapp.thp.R;
import com.hemaapp.thp.base.JhActivity;
import com.hemaapp.thp.base.JhHttpInformation;
import com.hemaapp.thp.base.JhctmApplication;
import com.hemaapp.thp.model.Tender;
import com.hemaapp.thp.model.User;

import xtom.frame.util.XtomTimeUtil;

/**
 * Created by lenovo on 2017/7/3.
 * 详细信息
 */
public class InformationActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private TextView word_title;
    private TextView city_text;//标题1
    private TextView city;
    private TextView time_text;//标题1
    private TextView time;
    private TextView over_time_text;//标题1
    private TextView over_time;
    private TextView tj_text;//标题1
    private TextView tj;
    private TextView text_text;//标题1
    private TextView text;
    private TextView fj;//标题1
    private TextView add_attention;
    private TextView type_name;//标题1
    private HemaWebView webview;
    private LinearLayout no_vip_layout;
    private TextView login_text;
    private String id;
    private Tender tender;
    private User user;
    private LinearLayout email_layout;
    private LinearLayout web_layout;
    private LinearLayout jion_layout;
    private LinearLayout dl_layout;
    private TextView content_text;
    private TextView fj_text;
    private DeleteView deleteView;//清空

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_information);
        super.onCreate(savedInstanceState);
        inIt();
    }

    /**
     * 初始化
     */
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        String userid = JhctmApplication.getInstance().getUser().getId();
        getNetWorker().clientGet(token, userid);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TENDER_GET:
                showProgressDialog("获取信息详情");
                break;
            case CLIENT_GET:
                break;
            case SENDEMAIL:
                showProgressDialog("发送邮件中...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TENDER_GET:
            case SENDEMAIL:
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
            case TENDER_GET:
                HemaArrayResult<Tender> result = (HemaArrayResult<Tender>) hemaBaseResult;
                tender = result.getObjects().get(0);
                setData();
                break;
            case CLIENT_GET:
                HemaArrayResult<User> result1 = (HemaArrayResult<User>) hemaBaseResult;
                user = result1.getObjects().get(0);
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().tenderGet(token, id);
                break;
            case TENDER_OPE:
                String keytype = hemaNetTask.getParams().get("keytype");

                if ("1".equals(keytype)) {
                    tender.setIs_like("1");
                    showTextDialog("关注成功");
                    add_attention.setText("已关注");
                } else {
                    tender.setIs_like("0");
                    showTextDialog("取消关注成功");
                    add_attention.setText("关注");
                }
                break;
            case SENDEMAIL:
               showDelete(4);
                break;
        }
    }

    //填充数据
    private void setData() {
        //标题
        word_title.setText(tender.getName());
        //地区
        city.setText(tender.getProvince() + tender.getCity() + tender.getArea());
        //判断不是会员
        if (user.getFeeaccount().equals("1")) {
            //判断招标
            if ("1".equals(tender.getStatus()) || "2".equals(tender.getStatus())) {
                //报名时间
                time.setText((XtomTimeUtil.TransTime(tender.getSignuptime(),
                        "yyyy-MM-dd")));
                //禁止时间
                over_time.setText("******");
                //b报名条件
                tj.setText("*****");
                //右键
                email_layout.setVisibility(View.GONE);
                text_text.setText("招标公告:");
                text.setText("******");
                web_layout.setVisibility(View.GONE);
                content_text.setVisibility(View.VISIBLE);
                content_text.setText("当前您为免费注册用户，无法查看该招标具体信息，成为会员，海量招标信息随意看！！");
            }
            //中标
            else if ("3".equals(tender.getStatus()) || "4".equals(tender.getStatus())) {
                time_text.setText("加入时间:");
                time.setText(XtomTimeUtil.TransTime(tender.getJointime(),
                        "yyyy-MM-dd"));
                over_time_text.setText("中标单位:");
                over_time.setText("******");
                jion_layout.setVisibility(View.GONE);
                dl_layout.setVisibility(View.GONE);
                web_layout.setVisibility(View.GONE);
                email_layout.setVisibility(View.GONE);
                content_text.setVisibility(View.VISIBLE);
                content_text.setText("当前您为免费注册用户，无法查看该中标具体信息，成为会员，海量中标信息随意看！！");
            }
        } else {
            //判断中标，招标
            String sys_web_service = getApplicationContext().getSysInitInfo()
                    .getSys_web_service();
            //判断招标
            if ("1".equals(tender.getStatus()) || "2".equals(tender.getStatus())) {
                dl_layout.setVisibility(View.GONE);
                time.setText((XtomTimeUtil.TransTime(tender.getSignuptime(),
                        "yyyy-MM-dd")));
                over_time.setText((XtomTimeUtil.TransTime(tender.getEndtime(),
                        "yyyy-MM-dd")));
                tj.setText(tender.getConditions());
                fj_text.setText("招标公告:");
                fj.setText(tender.getTenderdemoname());
                String path = sys_web_service + "webview/parm/tendercontent/id/"+id;
                webview.loadUrl(path);
            }
            //中标
            else if ("3".equals(tender.getStatus()) || "4".equals(tender.getStatus())) {
                time_text.setText("加入时间:");
                time.setText((XtomTimeUtil.TransTime(tender.getJointime(),
                        "yyyy-MM-dd")));
                over_time_text.setText("中标单位:");
                over_time.setText(tender.getWincompany());
                tj_text.setText("中标金额:");
                tj.setText(tender.getWinmoney());
                dl_layout.setVisibility(View.GONE);
                fj_text.setText("中标公告:");
                fj.setText(tender.getWindemoname());
                String path = sys_web_service + "webview/parm/wincontent/id/"+id;
                webview.loadUrl(path);
            }
            no_vip_layout.setVisibility(View.GONE);
        }
        if ("1".equals(tender.getKeytype()))
            type_name.setText("工程信息-" + tender.getType());
        else
            type_name.setText("采购信息-" + tender.getType());

        //会员
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VipOperationActivity.class);
                intent.putExtra("keytype", "1");
                startActivity(intent);
            }
        });

        //关注
        if ("1".equals(tender.getIs_like()))
            add_attention.setText("已关注");
        else
            add_attention.setText("关注");
        add_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();

                if ("1".equals(tender.getIs_like())) {
                    getNetWorker().tenderOpe(token, "2", tender.getId());
                } else {
                    //判断会员
                    if (user.getFeeaccount().equals("1")) {
                        showDelete(3);
                    }
                    else if(user.getFeeaccount().equals("2"))
                    {
                        showDelete(2);
                    }
                    else
                    {
                        showDelete(1);
                    }
                }
            }
        });
        //附件
        fj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().sendemail(token,tender.getId());
            }
        });
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TENDER_GET:
            case CLIENT_GET:
            case SENDEMAIL:
            case TENDER_OPE:
                showTextDialog(hemaBaseResult.getMsg());
                break;

        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TENDER_GET:
                showTextDialog("获取信息详情失败，请稍后重试");
                break;
            case CLIENT_GET:
                showTextDialog("获取个人信息失败，请稍后重试");
                break;
            case SENDEMAIL:
                showTextDialog("发送邮件失败，请稍后重试");
                break;
            case TENDER_OPE:
                showTextDialog("关注操作失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        word_title = (TextView) findViewById(R.id.word_title);
        city_text = (TextView) findViewById(R.id.city_text);
        city = (TextView) findViewById(R.id.city);
        time_text = (TextView) findViewById(R.id.time_text);
        time = (TextView) findViewById(R.id.time);
        over_time_text = (TextView) findViewById(R.id.over_time_text);
        over_time = (TextView) findViewById(R.id.over_time);
        tj_text = (TextView) findViewById(R.id.tj_text);
        tj = (TextView) findViewById(R.id.tj);
        text_text = (TextView) findViewById(R.id.text_text);
        text = (TextView) findViewById(R.id.text);
        fj = (TextView) findViewById(R.id.fj);
        add_attention = (TextView) findViewById(R.id.add_attention);
        type_name = (TextView) findViewById(R.id.type_name);
        webview = (HemaWebView) findViewById(R.id.webview);
        no_vip_layout = (LinearLayout) findViewById(R.id.no_vip_layout);
        login_text = (TextView) findViewById(R.id.login_text);
        email_layout = (LinearLayout) findViewById(R.id.email_layout);
        web_layout = (LinearLayout) findViewById(R.id.web_layout);
        jion_layout = (LinearLayout) findViewById(R.id.jion_layout);
        dl_layout = (LinearLayout) findViewById(R.id.dl_layout);
        content_text = (TextView) findViewById(R.id.content_text);
        fj_text = (TextView) findViewById(R.id.fj_text);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("公告详情");
        next_button.setVisibility(View.INVISIBLE);
    }

    private class DeleteView {
        TextView close_pop;
        TextView yas_pop;
        TextView text;
        TextView iphone_number;
    }

    /**
     * @param keytype 1：高级会员
     *                2：普通会员
     *                3：非会员
     *                4：发送邮箱
     */
    private void showDelete(final int keytype) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_show_hint, null);
        deleteView = new DeleteView();
        deleteView.close_pop = (TextView) view.findViewById(R.id.close_pop);
        deleteView.yas_pop = (TextView) view.findViewById(R.id.yas_pop);
        deleteView.text = (TextView) view.findViewById(R.id.text);
        deleteView.iphone_number = (TextView) view.findViewById(R.id.iphone_number);
        deleteView.text.setText("温馨提示");
        if (keytype == 1) {

            deleteView.iphone_number.setText("您已关注本中标公告，我们将实时为您推送该公告的阶段信息，方便您参与竞标或了解该公告的实时动态。（再次点击可取消关注）");
            deleteView.yas_pop.setText("关注");
            deleteView.close_pop.setText("我知道了");
        } else if (keytype == 2) {
            deleteView.iphone_number.setText("成为高级会员后才能进行关注\n关注本条中标信息后，本条后续变更信息会及时推送");
            deleteView.yas_pop.setText("关注");
            deleteView.close_pop.setText("我知道了");

        } else if (keytype == 3) {
            deleteView.iphone_number.setText("成为高级会员才能进行关注");
            deleteView.yas_pop.setText("去购买");
            deleteView.close_pop.setText("我知道了");
        } else {
            deleteView.iphone_number.setText("中标公告已发送至您的邮箱，请注意查收。");
            deleteView.yas_pop.setText("去购买");
            deleteView.yas_pop.setVisibility(View.GONE);
            deleteView.close_pop.setText("我知道了");
        }
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
                                                      String token = JhctmApplication.getInstance().getUser().getToken();
                                                      if (keytype == 1) {
                                                          getNetWorker().tenderOpe(token, "1", tender.getId());
                                                      } else if (keytype == 2) {
                                                          Intent intent = new Intent(mContext, CommonVipActivity.class);
                                                          startActivity(intent);
                                                      } else if (keytype == 3) {
                                                          Intent intent = new Intent(mContext, VipOperationActivity.class);
                                                          intent.putExtra("keytype", "1");
                                                          startActivity(intent);
                                                      } else {

                                                      }
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

}

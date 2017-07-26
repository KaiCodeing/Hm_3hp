package com.hemaapp.thp.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.thp.R;
import com.hemaapp.thp.activity.MessageActivity;
import com.hemaapp.thp.activity.SearchActivity;
import com.hemaapp.thp.activity.SelectProvinceActivity;
import com.hemaapp.thp.adapter.InforAdapter;
import com.hemaapp.thp.base.JhFragment;
import com.hemaapp.thp.base.JhHttpInformation;
import com.hemaapp.thp.base.JhctmApplication;
import com.hemaapp.thp.model.Tender;
import com.hemaapp.thp.model.TypeGet;
import com.hemaapp.thp.view.FlowLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/6/29.
 * 中标的fragemnt
 */
public class BidFragment extends JhFragment {
    private TextView loaction_text;
    private TextView search_input;//搜索
    private ImageView message_to;
    private TextView project;//工程细腻
    private TextView purchase;//采购信息
    private View view1;
    private View view2;
    private ImageView time;
    private ImageView type;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private InforAdapter adapter;
    private TimeView timeView;
    private TypeView typeView;
    private String keytype = "1";
    private String period = "";
    private String begintime = "";
    private String endtime = "";
    private String jktype = "";
    private Integer page = 0;
    private ArrayList<Tender> tenders = new ArrayList<>();
    private int timeType = 0;
    private int typeType = 0;
    PopupWindow popupWindow;
    PopupWindow typepopupWindow;

    private ArrayList<TypeGet> list1 = new ArrayList<>();
    private ArrayList<TypeGet> list2 = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_tender);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        inIt();
    }

    //初始化
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().noticeUnread(token);

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TENDER_LIST:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TENDER_LIST:
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TENDER_LIST:
                HemaPageArrayResult<Tender> result = (HemaPageArrayResult<Tender>) hemaBaseResult;
                ArrayList<Tender> tenders = result.getObjects();
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.tenders.clear();
                    this.tenders.addAll(tenders);
                    JhctmApplication application = JhctmApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (tenders.size() < sysPagesize) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (tenders.size() > 0)
                        this.tenders.addAll(tenders);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                        XtomToastUtil.showShortToast(getActivity(), "已经到最后啦");
                    }
                }
                freshData();
                loaction_text.setText(XtomSharedPreferencesUtil.get(getActivity(), "cityselect_dan"));
                break;
            case TYPE_GET:
                String keytype = hemaNetTask.getParams().get("keytype");
                HemaArrayResult<TypeGet> result1 = (HemaArrayResult<TypeGet>) hemaBaseResult;
                ArrayList<TypeGet> typeGets = result1.getObjects();
                if ("1".equals(keytype)) {
                    list1 = typeGets;
                } else {
                    list2 = typeGets;
                }
                showType(typeGets);
                break;
            case UNREAD_GET:
                HemaArrayResult<String> result2 = (HemaArrayResult<String>) hemaBaseResult;
                String num = result2.getObjects().get(0);
                if (isNull(num) || "0".equals(num))
                    message_to.setImageResource(R.mipmap.message_img);
                else
                    message_to.setImageResource(R.mipmap.message_view_img);
                String address = XtomSharedPreferencesUtil.get(getActivity(), "cityselect");
                getNetWorker().tenderList("2", this.keytype, address, period, begintime, endtime, jktype, String.valueOf(page));
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new InforAdapter(getContext(), tenders);
            adapter.setEmptyString("暂无信息");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无信息");
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TENDER_LIST:
            case UNREAD_GET:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TENDER_LIST:
                showTextDialog("获取信息失败，请稍后重试");
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                break;
            case UNREAD_GET:
                showTextDialog("获取未读消息失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        loaction_text = (TextView) findViewById(R.id.loaction_text);
        search_input = (TextView) findViewById(R.id.search_input);
        message_to = (ImageView) findViewById(R.id.message_to);
        project = (TextView) findViewById(R.id.project);
        purchase = (TextView) findViewById(R.id.purchase);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        time = (ImageView) findViewById(R.id.time);
        type = (ImageView) findViewById(R.id.type);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    protected void setListener() {

        //选择地区
        loaction_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(getActivity(), SelectProvinceActivity.class);
                it1.putExtra("keytype","1");
                startActivity(it1);
            }
        });
        //搜索
        search_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
            }
        });
        //消息
        message_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
            }
        });
        //选择工程信息
        project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keytype = "1";
                project.setTextColor(getResources().getColor(R.color.white));
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.INVISIBLE);
                purchase.setTextColor(getResources().getColor(R.color.select_text_off));
                page = 0;
                inIt();
            }
        });
        //选择采购信息
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keytype = "2";
                project.setTextColor(getResources().getColor(R.color.select_text_off));
                view1.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.VISIBLE);
                purchase.setTextColor(getResources().getColor(R.color.white));
                page = 0;
                inIt();
            }
        });
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                inIt();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                inIt();
            }
        });
        //时间
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeType == 0) {
                    time.setImageResource(R.mipmap.select_shang_img);
                    timeType = 1;
                    showTime();
                } else {
                    timeType = 0;
                    time.setImageResource(R.mipmap.select_main_img);
                    popupWindow.dismiss();
                }
            }
        });
        //类型
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeType == 0) {
                    type.setImageResource(R.mipmap.select_shang_img);
                    typeType = 1;
                    //获取类型
                    if ("1".equals(keytype)) {
                        if (list1 == null || list1.size() == 0) {
                            getNetWorker().typeGet("1");
                        } else
                            showType(list1);
                    } else {
                        if (list2 == null || list2.size() == 0) {
                            getNetWorker().typeGet("2");
                        } else
                            showType(list2);
                    }

                } else {
                    typeType = 0;
                    type.setImageResource(R.mipmap.select_main_img);
                    typepopupWindow.dismiss();
                }
            }
        });
    }

    //一周时间
    public String getdate(int i) // //获取前后日期 i为正数 向后推迟i天，负数时向前提前i天
    {
        Date dat = null;
        Calendar cd = Calendar.getInstance();
        cd.add(Calendar.DATE, i);
        dat = cd.getTime();
        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");

        return dformat.format(dat);
    }
    //

    /**
     * 获取当前年月日
     *
     * @return
     */
    public static String StringData() {

        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        return mYear + "-" + mMonth + "-" + mDay;
    }

    //获取n个月后的日期
    private String getM(int mon) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + mon);
        Date day = c.getTime();
        String str = new SimpleDateFormat("yyyy-MM-dd").format(day);
        return str;
    }

    //选择时间的pop    pop_time_select
    private class TimeView {
        CheckBox week;
        CheckBox one_mon;
        CheckBox three_mon;
        TextView star_time;
        TextView over_time;
        TextView login_text;
        TextView add_user;

    }

    //时间选择
    private void showTime() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_tiem_select, null);
        timeView = new TimeView();
        timeView.week = (CheckBox) view.findViewById(R.id.week);
        timeView.one_mon = (CheckBox) view.findViewById(R.id.one_mon);
        timeView.three_mon = (CheckBox) view.findViewById(R.id.three_mon);
        timeView.star_time = (TextView) view.findViewById(R.id.star_time);
        timeView.over_time = (TextView) view.findViewById(R.id.over_time);
        timeView.login_text = (TextView) view.findViewById(R.id.login_text);
        timeView.add_user = (TextView) view.findViewById(R.id.add_user);
        popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT) {
            @Override
            public void dismiss() {
                super.dismiss();
                timeType = 0;
                time.setImageResource(R.mipmap.select_main_img);
            }
        };

        //一周
        timeView.week.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //获取系统时间
//                Calendar a = Calendar.getInstance();
//                String starTime = String.valueOf(a.get(Calendar.YEAR)+"-"+a.get(c))

                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateNowStr = sdf.format(d);
                if (isChecked) {
                    timeView.one_mon.setChecked(false);
                    timeView.three_mon.setChecked(false);
                    String endtime = getdate(7);
                    timeView.star_time.setText(dateNowStr);
                    timeView.over_time.setText(endtime);
                } else {
                    timeView.star_time.setText("");
                    timeView.over_time.setText("");
                }
            }
        });
        //一个月
        timeView.one_mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateNowStr = sdf.format(d);
                if (isChecked) {
                    timeView.week.setChecked(false);
                    timeView.three_mon.setChecked(false);
                    String endtime = getM(1);
                    timeView.star_time.setText(dateNowStr);
                    timeView.over_time.setText(endtime);
                } else {
                    timeView.star_time.setText("");
                    timeView.over_time.setText("");
                }
            }
        });
        //三个月
        timeView.three_mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateNowStr = sdf.format(d);
                if (isChecked) {
                    timeView.week.setChecked(false);
                    timeView.one_mon.setChecked(false);
                    String endtime = getM(3);
                    timeView.star_time.setText(dateNowStr);
                    timeView.over_time.setText(endtime);
                } else {
                    timeView.star_time.setText("");
                    timeView.over_time.setText("");
                }
            }
        });
        //选择开始时间
        timeView.star_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeView.week.setChecked(false);
                timeView.one_mon.setChecked(false);
                timeView.three_mon.setChecked(false);
                final Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        timeView.star_time.setText(DateFormat.format("yyy-MM-dd", c));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        //书写各种状态
        if (period.equals("1"))
            timeView.week.setChecked(true);
        else if (period.equals("2"))
            timeView.one_mon.setChecked(true);
        else if (period.equals("3"))
            timeView.three_mon.setChecked(true);
            //日期
        else {
            timeView.star_time.setText(begintime);
            timeView.over_time.setText(endtime);
        }

        //选择结束时间
        timeView.over_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeView.week.setChecked(false);
                timeView.one_mon.setChecked(false);
                timeView.three_mon.setChecked(false);
                final Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        timeView.over_time.setText(DateFormat.format("yyy-MM-dd", c));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        //重置
        timeView.add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeView.week.setChecked(false);
                timeView.one_mon.setChecked(false);
                timeView.three_mon.setChecked(false);
                timeView.over_time.setText("");
                timeView.star_time.setText("");
                period="";
                begintime="";
                endtime="";
                inIt();
                //    popupWindow.dismiss();
            }
        });
        //确定
        timeView.login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                begintime = timeView.star_time.getText().toString();
                endtime = timeView.over_time.getText().toString();
                if (timeView.week.isChecked()) {
                    period = "1";
                    begintime = "";
                    endtime = "";
                }
                if (timeView.one_mon.isChecked()) {
                    period = "2";
                    begintime = "";
                    endtime = "";
                }
                if (timeView.three_mon.isChecked()) {
                    period = "3";
                    begintime = "";
                    endtime = "";
                }
                if (isNull(timeView.star_time.getText().toString()) || isNull(timeView.over_time.getText().toString()))
                {
                    showTextDialog("请选择日期");
                    return;
                }

                page = 0;
                inIt();
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
//        popupWindow.setBackgroundDrawable(new
//                BitmapDrawable()
//        );
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE && !popupWindow.isFocusable()) {
                    //如果焦点不在popupWindow上，且点击了外面，不再往下dispatch事件：
                    //不做任何响应,不 dismiss popupWindow
                    return true;
                }
                //否则default，往下dispatch事件:关掉popupWindow，
                return false;
            }
        });

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAsDropDown(findViewById(R.id.time));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    //选择类型
    private class TypeView {
        FlowLayout tag_fly;
        TextView login_text;
        TextView add_user;
    }

    private void showType(final ArrayList<TypeGet> typeGets) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_type_select, null);
        typeView = new TypeView();
        typeView.tag_fly = (FlowLayout) view.findViewById(R.id.tag_fly);
        typeView.login_text = (TextView) view.findViewById(R.id.login_text);
        typeView.add_user = (TextView) view.findViewById(R.id.add_user);
        typepopupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT) {
            @Override
            public void dismiss() {
                super.dismiss();
                typeType = 0;
                type.setImageResource(R.mipmap.select_main_img);
            }
        };
        typeView.tag_fly.removeAllViews();
        final ArrayList<AddViewHolder> viewHolders = new ArrayList<>();
        viewHolders.clear();
        if (typeGets == null || typeGets.size() == 0) {
        } else {
            for (int i = 0; i < typeGets.size(); i++) {
                View viewT = getActivity().getLayoutInflater().inflate(
                        R.layout.item_gridview_industry, null);
                final AddViewHolder addViewHolder = new AddViewHolder();
                addViewHolder.industry_text = (TextView) viewT.findViewById(R.id.industry_text);
                addViewHolder.position = i;
                addViewHolder.industry_text.setText(typeGets.get(i).getName());
                addViewHolder.industry_text.setTag(R.id.TAG, i);
                addViewHolder.industry_text.setTag(R.id.TAG_VIEWHOLDER, typeGets.get(i));
                addViewHolder.get = typeGets.get(i);
                if (typeGets.get(i).isCheck()) {
                    addViewHolder.industry_text.setTextColor(getResources().getColor(R.color.white));
                    addViewHolder.industry_text.setBackgroundResource(R.drawable.button_title_bg);
                } else {
                    addViewHolder.industry_text.setTextColor(getResources().getColor(R.color.indust));
                    addViewHolder.industry_text.setBackgroundResource(R.drawable.indust_select_bg);
                }
//                addViewHolder.industry_text.setTag(R.id.TAG_VIEWHOLDER,industry_text);
                addViewHolder.industry_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TypeGet get = (TypeGet) v.getTag(R.id.TAG_VIEWHOLDER);
                        int p = (int) v.getTag(R.id.TAG);

                        //多选操作
//                        if (get.isCheck()) {
//                            get.setCheck(false);
//                            industry_text.setTextColor(getResources().getColor(R.color.indust));
//                            industry_text.setBackgroundResource(R.drawable.indust_select_bg);
//                        } else {
//                            get.setCheck(true);
//                            industry_text.setTextColor(getResources().getColor(R.color.white));
//                            industry_text.setBackgroundResource(R.drawable.button_title_bg);
//                        }
                        for (AddViewHolder holder : viewHolders) {
                            if (holder.position == p) {
                                holder.get.setCheck(true);
                                holder.industry_text.setTextColor(getResources().getColor(R.color.white));
                                holder.industry_text.setBackgroundResource(R.drawable.button_title_bg);
                            } else {
                                holder.get.setCheck(false);
                                holder.industry_text.setTextColor(getResources().getColor(R.color.indust));
                                holder.industry_text.setBackgroundResource(R.drawable.indust_select_bg);
                            }
                        }
                    }
                });
                viewHolders.add(addViewHolder);
                typeView.tag_fly.addView(viewT);
            }
        }
        //完成
        typeView.login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeString = "";
                for (TypeGet get : typeGets) {
                    if (get.isCheck())
                        typeString = get.getName();
                }
                if (isNull(typeString)) {
                    showTextDialog("请选择类型");
                    jktype = "";
                    return;
                } else
                    jktype = typeString;
                page = 0;
                inIt();
                typepopupWindow.dismiss();
            }
        });
        //重置
        typeView.add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolders != null || viewHolders.size() != 0) {
                    for (AddViewHolder holder : viewHolders) {
                        holder.get.setCheck(false);
                        holder.industry_text.setTextColor(getResources().getColor(R.color.indust));
                        holder.industry_text.setBackgroundResource(R.drawable.indust_select_bg);
                    }
                    inIt();
                    //   typepopupWindow.dismiss();
                }
            }
        });
        typepopupWindow.setOutsideTouchable(true);
        typepopupWindow.setFocusable(true);
        typepopupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        typepopupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        typepopupWindow.setBackgroundDrawable(new
                BitmapDrawable()
        );
        typepopupWindow.setTouchable(true);
        typepopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        typepopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        typepopupWindow.showAsDropDown(findViewById(R.id.time));
        typepopupWindow.setAnimationStyle(R.style.PopupAnimation);
        typepopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private class AddViewHolder {
        TextView industry_text;
        int position;
        TypeGet get;
    }
}

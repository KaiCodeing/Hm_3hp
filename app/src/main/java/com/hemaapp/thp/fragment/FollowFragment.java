package com.hemaapp.thp.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.thp.R;
import com.hemaapp.thp.activity.VipOperationActivity;
import com.hemaapp.thp.adapter.InforAdapter;
import com.hemaapp.thp.base.JhFragment;
import com.hemaapp.thp.base.JhctmApplication;
import com.hemaapp.thp.model.Tender;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/6/29.
 * 关注
 */
public class FollowFragment extends JhFragment {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private TextView project;
    private TextView purchase;
    private View view1;
    private View view2;
    private XtomListView listview;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private ProgressBar progressbar;
    private DeleteView deleteView;//清空
    private FrameLayout vip_layout;
    private String keytype = "1";
    private Integer page = 0;
    private ArrayList<Tender> tenders = new ArrayList<>();
    private InforAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_follow);
        super.onCreate(savedInstanceState);
        String vip = JhctmApplication.getInstance().getUser().getFeeaccount();
        if ("1".equals(vip)  || "2".equals(vip)) {
            showDelete();
            refreshLoadmoreLayout.setVisibility(View.VISIBLE);
            progressbar.setVisibility(View.GONE);
            refreshLoadmoreLayout.setLoadmoreable(false);
            refreshLoadmoreLayout.setRefreshable(false);
            refreshLoadmoreLayout.setEnabled(false);
            vip_layout.setEnabled(false);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        String vip = JhctmApplication.getInstance().getUser().getFeeaccount();
        if ("1".equals(vip) || "2".equals(vip)) {
            refreshLoadmoreLayout.setVisibility(View.VISIBLE);
            progressbar.setVisibility(View.GONE);
        } else {
            page = 0;
            inIt();
        }
    }

    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().liketenderList(token, keytype, String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        refreshLoadmoreLayout.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
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
            showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        showTextDialog("获取信息失败，请稍后重试");
        String page = hemaNetTask.getParams().get("page");
        if ("0".equals(page)) {
            refreshLoadmoreLayout.refreshFailed();
        } else {
            refreshLoadmoreLayout.loadmoreFailed();
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        project = (TextView) findViewById(R.id.project);
        purchase = (TextView) findViewById(R.id.purchase);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        listview = (XtomListView) findViewById(R.id.listview);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        vip_layout = (FrameLayout) findViewById(R.id.vip_layout);
    }

    @Override
    protected void setListener() {
        back_button.setVisibility(View.INVISIBLE);
        title_text.setText("我的关注");
        next_button.setVisibility(View.INVISIBLE);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page=0;
                inIt();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
            page++;
                inIt();
            }
        });
        //招标公告
        project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vip = JhctmApplication.getInstance().getUser().getFeeaccount();
                if ("1".equals(vip) || "2".equals(vip)) {
                    return;
                }
                project.setTextColor(getResources().getColor(R.color.backgroud_title));
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.INVISIBLE);
                purchase.setTextColor(getResources().getColor(R.color.web));
                keytype="1";
                page=0;
                inIt();

            }
        });
        //中标
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vip = JhctmApplication.getInstance().getUser().getFeeaccount();
                if ("1".equals(vip) || "2".equals(vip)) {
                    return;
                }
                purchase.setTextColor(getResources().getColor(R.color.backgroud_title));
                view2.setVisibility(View.VISIBLE);
                view1.setVisibility(View.INVISIBLE);
                project.setTextColor(getResources().getColor(R.color.web));
                keytype="2";
                page=0;
                inIt();
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
        deleteView.text.setText("成为高级会员才能进行关注");
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

}

package com.hemaapp.thp.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.thp.R;
import com.hemaapp.thp.adapter.InforAdapter;
import com.hemaapp.thp.base.JhActivity;
import com.hemaapp.thp.base.JhctmApplication;
import com.hemaapp.thp.model.Tender;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/7/4.
 * 搜索结果
 */
public class SearchResultActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private XtomListView listview;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private ProgressBar progressbar;
    private TextView search_word;
    private String type;
    private String address;
    private String keywords;
    private String typeid;
    private ArrayList<Tender> tenders = new ArrayList<>();
    private InforAdapter adapter;
    private Integer page =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_result_search);
        super.onCreate(savedInstanceState);
        inIt();
    }

    private void inIt() {
        getNetWorker().tenderSelect(type, address, keywords, typeid,String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
     //   showProgressDialog("获取搜索信息");
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
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
                XtomToastUtil.showShortToast(mContext, "已经到最后啦");
            }
        }
        freshData();
    }
    private void freshData() {
        if (adapter == null) {
            adapter = new InforAdapter(mContext, tenders);
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
        showTextDialog("获取搜索信息失败，请稍后重试");
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
        listview = (XtomListView) findViewById(R.id.listview);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        search_word = (TextView) findViewById(R.id.search_word);
    }

    @Override
    protected void getExras() {
        type = mIntent.getStringExtra("type");
        address = mIntent.getStringExtra("address");
        keywords = mIntent.getStringExtra("keywords");
        typeid = mIntent.getStringExtra("typeid");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("搜素结果");
        next_button.setVisibility(View.INVISIBLE);
        if (isNull(keywords))
            search_word.setText("关键词:无");
        else
            search_word.setText("关键词:" + Html.fromHtml("<u>" + keywords + "</u>"));
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
    }
}

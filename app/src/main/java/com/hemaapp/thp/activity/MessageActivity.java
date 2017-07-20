package com.hemaapp.thp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.hemaapp.thp.adapter.MessageAdapter;
import com.hemaapp.thp.base.JhActivity;
import com.hemaapp.thp.base.JhHttpInformation;
import com.hemaapp.thp.base.JhctmApplication;
import com.hemaapp.thp.model.Notice;

import java.util.ArrayList;

import swipemenulistview.SwipeMenu;
import swipemenulistview.SwipeMenuCreator;
import swipemenulistview.SwipeMenuItem;
import swipemenulistview.SwipeMenuListView;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/7/4.
 * 消息列表
 */
public class MessageActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private RadioGroup message_group;
    private View view1;
    private View view2;
    private View view3;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private SwipeMenuListView listview;
    private ProgressBar progressbar;
    private ViewSex viewSex;
    private Integer page = 0;
    private ArrayList<Notice> notices = new ArrayList<Notice>();
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_message);
        super.onCreate(savedInstanceState);
        inIt();
    }

    /**
     * 初始化
     */
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().noticeList(token, String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
                // showProgressDialog("获取消息列表...");
                break;
            case NOTICE_SAVEOPERATE:
                showProgressDialog("保存消息操作");
                break;

        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
                //  cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                break;
            case NOTICE_SAVEOPERATE:
                cancelProgressDialog();
                break;

        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
                HemaPageArrayResult<Notice> iResult = (HemaPageArrayResult<Notice>) hemaBaseResult;
                ArrayList<Notice> notices1 = iResult.getObjects();
                notices.addAll(iResult.getObjects());
                if ("0".equals(page.toString())) {
                    refreshLoadmoreLayout.refreshSuccess();
                    notices.clear();
                    notices.addAll(iResult.getObjects());
                    int sysPagesize = getApplicationContext().getSysInitInfo()
                            .getSys_pagesize();
                    if (notices1.size() < sysPagesize)
                        refreshLoadmoreLayout.setLoadmoreable(false);
                    else
                        refreshLoadmoreLayout.setLoadmoreable(true);
                    //}
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (notices1.size() > 0)
                        this.notices.addAll(iResult.getObjects());
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                    }
                }
                freshData();
                break;
            case NOTICE_SAVEOPERATE:
                page = 0;
                inIt();
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new MessageAdapter(mContext, notices);
            adapter.setEmptyString("暂无消息");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无消息");
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
            case NOTICE_SAVEOPERATE:
                showTextDialog(hemaBaseResult.getMsg());
                break;

        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
                showTextDialog("获取消息失败，请稍后重试");
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                break;
            case NOTICE_SAVEOPERATE:
                showTextDialog("操作失败，请稍后重试");
                break;

        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        message_group = (RadioGroup) findViewById(R.id.message_group);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (SwipeMenuListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
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
        next_button.setImageResource(R.mipmap.message_more_img);
        //全部
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSex();
            }
        });
        title_text.setText("消息中心");
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
        next_button.setImageResource(R.mipmap.message_more_img);
        final SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(241,
                        2, 21)));
                // set item width
                deleteItem.setWidth(dp2px(70));
                //  deleteItem.setIcon(null);

                // set a icon
                // deleteItem.setIcon(R.mipmap.delete);
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(16);
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listview.setMenuCreator(creator);
        listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                switch (index) {
                    case 0:
                        getNetWorker().noticeOperate(token, notices.get(position).getId(), "2");
                        break;
                }
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                //未读
                if ("1".equals(notices.get(position).getLooktype())) {
                    getNetWorker().noticeOperate(token, notices.get(position).getId(), "1");
                } else {
//                    Intent intent = new Intent(MessageActivity.this, MessageInforActivity.class);
//                    intent.putExtra("notice", notices.get(position));
//                    startActivity(intent);
                    //判断类型
                    if ("1".equals(notices.get(position).getKeytype())) {
                    } else if ("2".equals(notices.get(position).getKeytype())) {
                        Intent intent = new Intent(mContext, InformationActivity.class);
                        intent.putExtra("id", notices.get(position).getKeyid());
                        startActivity(intent);
                    } else {
                        Intent intent2 = new Intent(mContext, MessageListActivity.class);
                        intent2.putExtra("id", notices.get(position).getId());
                        startActivity(intent2);
                    }
                }
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    //清空
    //选择性别
    public class ViewSex {
        TextView camera_text;
        TextView album_text;
        TextView textView1_camera;
    }

    private void showSex() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwindo_camera, null);
        viewSex = new ViewSex();
        viewSex.camera_text = (TextView) view.findViewById(R.id.camera_text);
        viewSex.album_text = (TextView) view.findViewById(R.id.album_text);
        viewSex.textView1_camera = (TextView) view.findViewById(R.id.textView1_camera);
        viewSex.camera_text.setText("全部清空消息");
        viewSex.album_text.setText("全部标为已读");
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });
        //删除
        viewSex.camera_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().noticeOperate(token, "", "3");
                popupWindow.dismiss();
            }
        });
        //置为已读
        viewSex.album_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().noticeOperate(token, "", "4");
                popupWindow.dismiss();
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
}

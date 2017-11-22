package com.hemaapp.thp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.thp.R;
import com.hemaapp.thp.base.JhActivity;
import com.hemaapp.thp.base.JhHttpInformation;
import com.hemaapp.thp.base.JhctmApplication;
import com.hemaapp.thp.db.SearchDBClient;
import com.hemaapp.thp.model.CityChildren;
import com.hemaapp.thp.model.CitySan;
import com.hemaapp.thp.model.TypeGet;
import com.hemaapp.thp.view.AreaDialog;
import com.hemaapp.thp.view.FlowLayout;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/7/4.
 * 搜索
 */
public class SearchActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private RadioGroup vip_group;
    private TextView input_city;
    private LinearLayout layout_tel;
    private EditText search_input;
    private FlowLayout tag_fly;
    private FlowLayout cg_fly;
    private FlowLayout history_fly;
    private TextView add_user;
    private ArrayList<TypeGet> list1 = new ArrayList<>();
    private ArrayList<TypeGet> list2 = new ArrayList<>();
    private SearchDBClient client;
    private ArrayList<String> search_strs;
    private AreaDialog areaDialog;
    private String province;//省
    private String city;//市
    private String area;//区
    private String district1 = "";//省id
    private String district2 = "";//市id
    private String district3 = "";//区id
    private String Loaction;
    private TextView search_goto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
        client = SearchDBClient.get(mContext);
//        if (JhctmApplication.getInstance().getCityInfo() == null)
            getNetWorker().districtALLList();
//        else {
//            getNetWorker().typeGet("1");
//        }
    }

    @Override
    protected void onResume() {
        getHistoryList();
        super.onResume();
    }

    private void getHistoryList() {
        new LoadDBTask().execute();
    }

    private class LoadDBTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // XtomProcessDialog.show(mContext, R.string.loading);
        }

        @Override
        protected Void doInBackground(Void... params) {
            search_strs = client.select();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            //   getHistoryList_done();
//            refreshLoadmoreLayout.setVisibility(View.VISIBLE);
//            progressbar.setVisibility(View.GONE);
//            adapter = new SearchAdapter(mContext, search_strs, SearchActivity.this, keytype);
//            listview.setAdapter(adapter);
            setHistory();
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TYPE_GET:
                showProgressDialog("获取信息..");
                break;
            case ADDRESS_LIST:
                showProgressDialog("获取地区列表");
                break;
            case CLIENT_ADD:
                showProgressDialog("保存注册信息");
                break;
            case FILE_UPLOAD:
                showProgressDialog("保存图片");
                break;

        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TYPE_GET:
            case ADDRESS_LIST:
            case FILE_UPLOAD:
            case CLIENT_ADD:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TYPE_GET:
                String keytype = hemaNetTask.getParams().get("keytype");
                HemaArrayResult<TypeGet> result = (HemaArrayResult<TypeGet>) hemaBaseResult;
                ArrayList<TypeGet> typeGets = result.getObjects();
                if ("1".equals(keytype)) {
                    list1 = typeGets;
                    getNetWorker().typeGet("2");
                } else {
                    list2 = typeGets;
                    setInfor();
                }
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
                getNetWorker().typeGet("1");
                break;
        }
    }

    //填写信息
    private void setInfor() {
        tag_fly.removeAllViews();
        cg_fly.removeAllViews();
        //工程信息
        if (list1 == null || list1.size() == 0) {
        } else {
            for (int i = 0; i < list1.size(); i++) {
                View view = getLayoutInflater().inflate(
                        R.layout.item_gridview_industry, null);
                final TextView industry_text = (TextView) view.findViewById(R.id.industry_text);
                industry_text.setText(list1.get(i).getName());
                industry_text.setTag(R.id.TAG, list1.get(i));
                industry_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TypeGet get = (TypeGet) v.getTag(R.id.TAG);
                        if (get.isCheck()) {
                            get.setCheck(false);
                            industry_text.setTextColor(getResources().getColor(R.color.indust));
                            industry_text.setBackgroundResource(R.drawable.indust_select_bg);
                        } else {
                            get.setCheck(true);
                            industry_text.setTextColor(getResources().getColor(R.color.white));
                            industry_text.setBackgroundResource(R.drawable.button_title_bg);
                        }
                    }
                });
                tag_fly.addView(view);
            }
        }
        if (list2 == null || list2.size() == 0) {
        } else {
            for (int i = 0; i < list2.size(); i++) {
                View view = getLayoutInflater().inflate(
                        R.layout.item_gridview_industry, null);
                final TextView industry_text = (TextView) view.findViewById(R.id.industry_text);
                industry_text.setText(list2.get(i).getName());
                industry_text.setTag(R.id.TAG, list2.get(i));
                industry_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TypeGet get = (TypeGet) v.getTag(R.id.TAG);
                        if (get.isCheck()) {
                            get.setCheck(false);
                            industry_text.setTextColor(getResources().getColor(R.color.indust));
                            industry_text.setBackgroundResource(R.drawable.indust_select_bg);
                        } else {
                            get.setCheck(true);
                            industry_text.setTextColor(getResources().getColor(R.color.white));
                            industry_text.setBackgroundResource(R.drawable.button_title_bg);
                        }
                    }
                });
                cg_fly.addView(view);
            }
        }
    }

    //历史记录
    private void setHistory() {
        history_fly.removeAllViews();
        final ArrayList<AddViewHolder> viewHolders = new ArrayList<>();
        viewHolders.clear();
        if (search_strs == null || search_strs.size() == 0) {
            search_input.setText("");
        } else {
            for (int i = 0; i < search_strs.size(); i++) {
                View view = getLayoutInflater().inflate(
                        R.layout.item_gridview_industry, null);
                AddViewHolder addViewHolder = new AddViewHolder();
                addViewHolder.industry_text = (TextView) view.findViewById(R.id.industry_text);
                addViewHolder.position = i;
                addViewHolder.industry_text.setText(search_strs.get(i).toString());
                addViewHolder.industry_text.setTag(R.id.TAG, search_strs.get(i));
                addViewHolder.industry_text.setTag(R.id.TAG, i);
                addViewHolder.industry_text.setTag(R.id.TAG_VIEWHOLDER, search_strs.get(i));
                addViewHolder.get = search_strs.get(i);
                addViewHolder.check = false;
                if (addViewHolder.check) {
                    addViewHolder.industry_text.setTextColor(getResources().getColor(R.color.white));
                    addViewHolder.industry_text.setBackgroundResource(R.drawable.button_title_bg);
                } else {
                    addViewHolder.industry_text.setTextColor(getResources().getColor(R.color.indust));
                    addViewHolder.industry_text.setBackgroundResource(R.drawable.indust_select_bg);
                }
                addViewHolder.industry_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String word = (String) v.getTag(R.id.TAG_VIEWHOLDER);
                        int p = (int) v.getTag(R.id.TAG);
                        for (AddViewHolder holder : viewHolders) {
                            if (holder.position == p) {
                                if (holder.check) {
                                    holder.check = false;
                                    holder.industry_text.setTextColor(getResources().getColor(R.color.indust));
                                    holder.industry_text.setBackgroundResource(R.drawable.indust_select_bg);
                                    search_input.setText("");
                                } else {
                                    holder.check = true;
                                    holder.industry_text.setTextColor(getResources().getColor(R.color.white));
                                    holder.industry_text.setBackgroundResource(R.drawable.button_title_bg);
                                    search_input.setText(word);
                                }

                            } else {
                                holder.check = false;
                                holder.industry_text.setTextColor(getResources().getColor(R.color.indust));
                                holder.industry_text.setBackgroundResource(R.drawable.indust_select_bg);
//                                search_input.setText("");
                            }
                        }
                    }
                });
                viewHolders.add(addViewHolder);
                history_fly.addView(view);
            }
        }
    }

    private class AddViewHolder {
        TextView industry_text;
        int position;
        String get;
        boolean check;
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TYPE_GET:
            case ADDRESS_LIST:
            case CLIENT_ADD:
            case FILE_UPLOAD:
            case CLIENT_LOGIN:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TYPE_GET:
                showTextDialog("获取信息失败，请稍后重试");
                break;
            case ADDRESS_LIST:
                showTextDialog("获取地区列表失败，请稍后重试");
                break;
            case CLIENT_ADD:
                showTextDialog("注册失败，请稍后重试");
                break;
            case FILE_UPLOAD:
                showTextDialog("上传文件失败，请稍后重试");
                break;
            case CLIENT_LOGIN:
                showTextDialog("登录失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        vip_group = (RadioGroup) findViewById(R.id.vip_group);
        input_city = (TextView) findViewById(R.id.input_city);
        layout_tel = (LinearLayout) findViewById(R.id.layout_tel);
        search_input = (EditText) findViewById(R.id.search_input);
        tag_fly = (FlowLayout) findViewById(R.id.tag_fly);
        cg_fly = (FlowLayout) findViewById(R.id.cg_fly);
        history_fly = (FlowLayout) findViewById(R.id.history_fly);
        add_user = (TextView) findViewById(R.id.add_user);
        search_goto = (TextView) findViewById(R.id.search_goto);
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
        title_text.setText("搜索");
        next_button.setVisibility(View.INVISIBLE);
        //搜索
        search_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "1";
                if (vip_group.getCheckedRadioButtonId() == R.id.vip_sup)
                    type = "1";
                else
                    type = "2";
                String search = search_input.getText().toString().trim();
                if (!isNull(search))
                    select_search_str(search);
                //获取购买数量
                StringBuffer gctypeid = new StringBuffer();
                StringBuffer cgtypeid = new StringBuffer();
                for (TypeGet get : list1
                        ) {
                    if (get.isCheck())
                        gctypeid.append(get.getId() + ",");
                }
                for (TypeGet get : list2
                        ) {
                    if (get.isCheck())
                        cgtypeid.append(get.getId() + ",");
                }
                String gctype = null;
                if (!gctypeid.toString().equals("") || gctypeid.toString().length() != 0)
                    gctype = gctypeid.substring(0, gctypeid.length() - 1);
                String cgtype = null;
                int m = cgtypeid.length();
                if (!cgtypeid.toString().equals("") || cgtypeid.toString().length() != 0)
                    cgtype = cgtypeid.substring(0, cgtypeid.length() - 1);
                String typeid = "";
                if (gctype == null && cgtype == null)
                    typeid = "";
                else if (gctype == null && cgtype != null)
                    typeid = cgtype;
                else if (gctype != null && cgtype == null)
                    typeid = gctype;
                else
                    typeid = gctype + "," + cgtype;
                log_i("+++++++++++++++++++++++++++++++" + typeid);
                if (isNull(Loaction)) {
                    showTextDialog("请选择地区");
                    return;
                }
                Intent intent = new Intent(mContext, SearchResultActivity.class);
                intent.putExtra("type", type);

                intent.putExtra("address", Loaction);
                intent.putExtra("keywords", search);
                intent.putExtra("typeid", typeid);
                startActivity(intent);

            }
        });
        //选择地区
        layout_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCity();
            }
        });
        //清空
        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear_HistoryList();
            }
        });
    }

    /**
     * 删除
     */
    public void Clear_HistoryList() {
        client.clear();
        if (search_strs == null) {
        } else {
            search_strs.clear();
        }
        // adapter.notifyDataSetChanged();
        setHistory();
    }

    /**
     * 添加
     */

    public void select_search_str(String str) {

        boolean found = false;
        if (search_strs != null && search_strs.size() > 0) {
            for (String hstr : search_strs) {
                if (hstr.equals(str)) {
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            if (search_strs == null)
                search_strs = new ArrayList<String>();
            search_strs.add(0, str);
            client.insert(str);
        }
    }

    private void showCity() {
        if (areaDialog == null) {
            areaDialog = new AreaDialog(mContext, "1");
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
            Loaction = province + "," + city + "," + area;
            log_i("++" + province + " ---" + city + "==" + area);
            areaDialog.cancel();
        }
    }

}

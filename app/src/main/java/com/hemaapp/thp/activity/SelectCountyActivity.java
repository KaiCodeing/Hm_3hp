package com.hemaapp.thp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.thp.R;
import com.hemaapp.thp.adapter.SelectCityAdapter;
import com.hemaapp.thp.base.JhActivity;
import com.hemaapp.thp.model.CityChildren;

import java.util.ArrayList;

import xtom.frame.XtomActivityManager;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2017/6/29.
 * 选择县 区
 */
public class SelectCountyActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private TextView all_name;
    private ListView listview;
    private SelectCityAdapter adapter;
    private ArrayList<CityChildren> cityChildrens = new ArrayList<>();
    private String id;
    private String provinceName;
    private String cityName;
    private String keytype;
    private String main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_province);
        super.onCreate(savedInstanceState);
        inIt();
    }

    /**
     * 初始化
     */
    private void inIt() {
        getNetWorker().addressGet("3", id);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog("获取县区信息");
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        HemaArrayResult<CityChildren> result = (HemaArrayResult<CityChildren>) hemaBaseResult;
        cityChildrens = result.getObjects();
        freshData();
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new SelectCityAdapter(mContext, cityChildrens);
            adapter.setEmptyString("暂无城市信息");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无城市信息");
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        showTextDialog("获取失败，请稍后重试");
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        all_name = (TextView) findViewById(R.id.all_name);
        listview = (ListView) findViewById(R.id.listview);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
        provinceName = mIntent.getStringExtra("provinceName");
        cityName = mIntent.getStringExtra("cityName");
        keytype = mIntent.getStringExtra("keytype");
        main = mIntent.getStringExtra("main");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("选择县区");
        all_name.setText("全市");
        next_button.setVisibility(View.INVISIBLE);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //   XtomActivityManager.finishAll();
//                finish();
//                Intent intent = new Intent(mContext, MainActivity.class);
//                intent.putExtra("id", cityChildrens.get(position).getId());
//                intent.putExtra("keytype",keytype);
                XtomSharedPreferencesUtil.save(mContext, "cityselect_dan", cityChildrens.get(position).getName());
                XtomSharedPreferencesUtil.save(mContext, "cityselect", provinceName + "," + cityName + "," + cityChildrens.get(position).getName());
//                startActivity(intent);
                if (isNull(main)) {
                    setResult(RESULT_OK, mIntent);
                    finish();
                } else {
                    XtomActivityManager.finishAll();
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        all_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  XtomActivityManager.finishAll();
//                finish();
//                Intent intent = new Intent(mContext, MainActivity.class);
//                intent.putExtra("keytype",keytype);
                XtomSharedPreferencesUtil.save(mContext, "cityselect", cityName);
                XtomSharedPreferencesUtil.save(mContext, "cityselect_dan", cityName);
//                startActivity(intent);
                if (isNull(main)) {
                    setResult(RESULT_OK, mIntent);
                    finish();
                } else {
                    XtomActivityManager.finishAll();
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}

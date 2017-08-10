package com.hemaapp.thp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.thp.R;
import com.hemaapp.thp.base.JhActivity;
import com.hemaapp.thp.base.JhHttpInformation;
import com.hemaapp.thp.base.JhctmApplication;
import com.hemaapp.thp.config.XsmConfig;
import com.hemaapp.thp.model.CityChildren;
import com.hemaapp.thp.model.CitySan;
import com.hemaapp.thp.model.TypeGet;
import com.hemaapp.thp.model.User;
import com.hemaapp.thp.view.AreaDialog;
import com.hemaapp.thp.view.BottomPopuWindowTimePicker;
import com.hemaapp.thp.view.FlowLayout;
import com.hemaapp.thp.view.JhctmImageWay;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import xtom.frame.XtomActivityManager;
import xtom.frame.XtomConfig;
import xtom.frame.image.load.XtomImageTask;
import xtom.frame.util.Md5Util;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2017/6/28.
 * 注册第二步
 * item_gridview_industry
 */
public class Register2Activity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private RoundedImageView user_img;
    private EditText input_name;//名字
    private TextView input_sex;//性别
    private LinearLayout select_sex;
    private TextView input_year;
    private LinearLayout layout_year;
    private TextView input_city;//地区
    private LinearLayout layout_city;
    private EditText input_company;//公司
    private EditText input_duty;//职务
    private EditText input_email;//邮箱
    private FlowLayout tag_fly;//工程信息
    private FlowLayout cg_fly;//采购信息
    private EditText input_type_text;//输入类型关键字
    private TextView login_text;//登陆
    private JhctmImageWay imageWay;
    private String imagePathCamera;
    private String tempPath = "";
    BottomPopuWindowTimePicker selectTime;
    private ViewSex viewSex;
    private ArrayList<TypeGet> list1 = new ArrayList<>();
    private ArrayList<TypeGet> list2 = new ArrayList<>();
    private AreaDialog areaDialog;
    private String province;//省
    private String city;//市
    private String area;//区
    private String district1 = "";//省id
    private String district2 = "";//市id
    private String district3 = "";//区id
    private String temp_token;
    private String username;
    private String password;
    private String Loaction;
    private String keytype;
    private User user;
    private String bz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_2_register);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            imageWay = new JhctmImageWay(mContext, 1, 2);
        } else {
            imagePathCamera = savedInstanceState.getString("imagePathCamera");
            imageWay = new JhctmImageWay(mContext, 1, 2);
        }
        if (JhctmApplication.getInstance().getCityInfo() == null)
            getNetWorker().districtALLList();
        else {
            getNetWorker().typeGet("1");
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
            case CLIENT_GET:
                showProgressDialog("获取个人信息");
                break;
            case CLIENT_SAVE:
                showProgressDialog("保存个人信息...");
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
            case CLIENT_GET:
            case CLIENT_SAVE:
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
                    if (!isNull(this.keytype)) {
                        String token = JhctmApplication.getInstance().getUser().getToken();
                        String id = JhctmApplication.getInstance().getUser().getId();
                        getNetWorker().clientGet(token, id);
                    } else
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
            case FILE_UPLOAD:
                if (isNull(this.keytype))
                    getNetWorker().clientLogin(username, Md5Util.getMd5(XtomConfig.DATAKEY
                            + Md5Util.getMd5(password)));
                else {
                    showTextDialog("修改个人信息成功！");
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                }
                break;
            case CLIENT_ADD:
                HemaArrayResult<String> result3 = (HemaArrayResult<String>) hemaBaseResult;
                String token = result3.getObjects().get(0);
                if (!isNull(tempPath))
                    getNetWorker().fileUpload(token, "1", "0", "0", "0", "无", tempPath);
                else
                    getNetWorker().clientLogin(username, Md5Util.getMd5(XtomConfig.DATAKEY
                            + Md5Util.getMd5(password)));
                break;
            case CLIENT_LOGIN:
                HemaArrayResult<User> result2 = (HemaArrayResult<User>) hemaBaseResult;
                User user = result2.getObjects().get(0);
                JhctmApplication.getInstance().setUser(user);
                //自动登录
                XtomSharedPreferencesUtil.save(mContext, "autoLogin", "yes");
                XtomSharedPreferencesUtil.save(mContext, "username", username);
                XtomSharedPreferencesUtil.save(mContext, "password", Md5Util.getMd5(XtomConfig.DATAKEY
                        + Md5Util.getMd5(password)));
                if (isNull(XtomSharedPreferencesUtil.get(mContext, "cityselect"))) {
                    XtomActivityManager.finishAll();
                    Intent it1 = new Intent(mContext, SelectProvinceActivity.class);
                    it1.putExtra("main", "1");
                    startActivity(it1);
                } else {
                    if (isNull(bz)) {
                        XtomActivityManager.finishAll();
                        Intent it1 = new Intent(mContext, MainActivity.class);
                        startActivity(it1);
                    } else {
                        finish();
                        Intent intent1 = new Intent();
                        intent1.setAction("hemaapp.3hp.user.infor");
                        sendBroadcast(intent1);
                    }

                }
                break;
            case CLIENT_GET:
                HemaArrayResult<User> result4 = (HemaArrayResult<User>) hemaBaseResult;
                user = result4.getObjects().get(0);
                setData(user);
                break;
            case CLIENT_SAVE:
                String token2 = JhctmApplication.getInstance().getUser().getToken();
                if (!isNull(tempPath))
                    getNetWorker().fileUpload(token2, "1", "0", "0", "0", "无", tempPath);
                else {
                    showTextDialog("修改个人信息成功！");
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                }
                break;
        }
    }

    //书写个人信息
    private void setData(User user) {
        String path = user.getAvatar();
        user_img.setCornerRadius(100);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.my_fr_user_img)
                .showImageForEmptyUri(R.mipmap.my_fr_user_img)
                .showImageOnFail(R.mipmap.my_fr_user_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, user_img, options);
        input_name.setEnabled(false);
        select_sex.setEnabled(false);
        input_name.setText(user.getNickname());
        input_sex.setText(user.getSex());
        input_year.setText(user.getMobile());
        //地区
        input_city.setText(user.getRole());
        //公司名称
        input_company.setText(user.getCompany());
        input_duty.setText(user.getNoticecount());
        input_email.setText(user.getEmail());
        input_type_text.setText(user.getBlogcount());
        //填充类型
        //工程信息
        if (!isNull(user.getUsername())) {
            String gcinfor[] = user.getUsername().split(",");
            for (int i = 0; i < gcinfor.length; i++) {
                for (int j = 0; j < list1.size(); j++) {
                    if (gcinfor[i].equals(list1.get(j).getId()))
                        list1.get(j).setCheck(true);
                }
            }
        }
        //采购信息
        if (!isNull(user.getFollowcount())) {
            String cginfor[] = user.getFollowcount().split(",");
            for (int i = 0; i < cginfor.length; i++) {
                for (int j = 0; j < list2.size(); j++) {
                    if (cginfor[i].equals(list2.get(j).getId()))
                        list2.get(j).setCheck(true);
                }
            }
        }
        setInfor();
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
                if (list1.get(i).isCheck()) {
                    industry_text.setTextColor(getResources().getColor(R.color.white));
                    industry_text.setBackgroundResource(R.drawable.button_title_bg);
                } else {
                    industry_text.setTextColor(getResources().getColor(R.color.indust));
                    industry_text.setBackgroundResource(R.drawable.indust_select_bg);
                }
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
                if (list2.get(i).isCheck()) {
                    industry_text.setTextColor(getResources().getColor(R.color.white));
                    industry_text.setBackgroundResource(R.drawable.button_title_bg);
                } else {
                    industry_text.setTextColor(getResources().getColor(R.color.indust));
                    industry_text.setBackgroundResource(R.drawable.indust_select_bg);
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case 1:
                album(data);
                break;
            case 2:
                camera();
                break;
            case 3:
                user_img.setCornerRadius(100);
                imageWorker.loadImage(new XtomImageTask(user_img, tempPath,
                        mContext, new XtomImageTask.Size(180, 180)));
                break;
            default:
                break;
        }
    }

    private void album(Intent data) {
        if (data == null)
            return;
        Uri selectedImageUri = data.getData();
        startPhotoZoom(selectedImageUri, 3);
    }

    private void editImage(String path, int requestCode) {
        File file = new File(path);
        startPhotoZoom(Uri.fromFile(file), requestCode);
    }

    private void startPhotoZoom(Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", XsmConfig.IMAGE_WIDTH);
        intent.putExtra("aspectY", XsmConfig.IMAGE_WIDTH);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", XsmConfig.IMAGE_WIDTH);
        intent.putExtra("outputY", XsmConfig.IMAGE_WIDTH);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, requestCode);
    }

    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    private File getTempFile() {
        String savedir = XtomFileUtil.getTempFileDir(mContext);
        File dir = new File(savedir);
        if (!dir.exists())
            dir.mkdirs();
        // 保存入sdCard
        tempPath = savedir + XtomBaseUtil.getFileName() + ".jpg";// 保存路径
        File file = new File(tempPath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return file;
    }

    private void camera() {
//		imagePathCamera = null;
//		if (imagePathCamera == null) {
//			imagePathCamera = imageWay.getCameraImage();
//		}
        String path = imageWay.getCameraImage();
        if (!isNull(path))
            imagePathCamera = path;
        log_i("imagePathCamera=" + imagePathCamera);
        editImage(imagePathCamera, 3);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageWay != null)
            outState.putString("imagePathCamera", imageWay.getCameraImage());
        super.onSaveInstanceState(outState);
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
            case CLIENT_GET:
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
            case CLIENT_GET:
                showTextDialog("获取个人信息失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        user_img = (RoundedImageView) findViewById(R.id.user_img);
        input_name = (EditText) findViewById(R.id.input_name);
        input_sex = (TextView) findViewById(R.id.input_sex);
        select_sex = (LinearLayout) findViewById(R.id.select_sex);
        input_year = (TextView) findViewById(R.id.input_year);
        layout_year = (LinearLayout) findViewById(R.id.layout_year);
        input_city = (TextView) findViewById(R.id.input_city);
        layout_city = (LinearLayout) findViewById(R.id.layout_city);
        input_company = (EditText) findViewById(R.id.input_company);
        input_duty = (EditText) findViewById(R.id.input_duty);
        input_email = (EditText) findViewById(R.id.input_email);
        tag_fly = (FlowLayout) findViewById(R.id.tag_fly);
        cg_fly = (FlowLayout) findViewById(R.id.cg_fly);
        input_type_text = (EditText) findViewById(R.id.input_type_text);
        login_text = (TextView) findViewById(R.id.login_text);
    }

    @Override
    protected void getExras() {
        temp_token = mIntent.getStringExtra("temp_token");
        username = mIntent.getStringExtra("username");
        password = mIntent.getStringExtra("password");
        keytype = mIntent.getStringExtra("keytype");
        bz = mIntent.getStringExtra("bz");
    }

    @Override
    protected void setListener() {
        if (!isNull(keytype)) {
            login_text.setVisibility(View.GONE);
            next_button.setVisibility(View.VISIBLE);
            next_button.setText("保存");
            next_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = input_name.getText().toString();
                    String sex = input_sex.getText().toString();
                    String age = input_year.getText().toString();
                    String email = input_email.getText().toString();
                    String address = input_city.getText().toString();
                    String company = input_company.getText().toString();
                    String position = input_duty.getText().toString();
                    String typekeyword = input_type_text.getText().toString();
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
                    //判断各种是否必填
                    if (isNull(name)) {
                        showTextDialog("请填写姓名");
                        return;
                    }
                    if (isNull(sex)) {
                        showTextDialog("请选择性别");
                        return;
                    }
                    if (isNull(address)) {
                        showTextDialog("请选择所在地区");
                        return;
                    }
                    if (isNull(company)) {
                        showTextDialog("请填写公司名称");
                        return;
                    }
                    if (isNull(email)) {
                        showTextDialog("请填写邮箱");
                        return;
                    }
                    String gctype = null;
                    if (!gctypeid.toString().equals("") || gctypeid.toString().length() != 0)
                        gctype = gctypeid.substring(0, gctypeid.length() - 1);
                    String cgtype = null;
                    int m = cgtypeid.length();
                    if (!cgtypeid.toString().equals("") || cgtypeid.toString().length() != 0)
                        cgtype = cgtypeid.substring(0, cgtypeid.length() - 1);
                    String token = JhctmApplication.getInstance().getUser().getToken();
                    getNetWorker().clientSave(token, name, sex, age, email, address, company, position, gctype, cgtype, typekeyword);
                }
            });
        } else
            next_button.setVisibility(View.INVISIBLE);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("个人资料");


        //选择照片
        //照片
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                        0);
                imageWay.show();
            }
        });
        //选择年龄
        layout_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectTimeBottomPopu();
            }
        });
        //选择性别
        select_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSex();
            }
        });
        //选择地区
        input_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCity();
            }
        });
        //注册登录
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = input_name.getText().toString();
                String sex = input_sex.getText().toString();
                String age = input_year.getText().toString();
                String email = input_email.getText().toString();
                String address = input_city.getText().toString();
                String company = input_company.getText().toString();
                String position = input_duty.getText().toString();
                String typekeyword = input_type_text.getText().toString();
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
                //判断各种是否必填
                if (isNull(name)) {
                    showTextDialog("请填写姓名");
                    return;
                }
                if (isNull(sex)) {
                    showTextDialog("请选择性别");
                    return;
                }
                if (isNull(address)) {
                    showTextDialog("请选择所在地区");
                    return;
                }
                if (isNull(company)) {
                    showTextDialog("请填写公司名称");
                    return;
                }
                if (isNull(email)) {
                    showTextDialog("请填写邮箱");
                    return;
                }
                String gctype = null;
                if (!gctypeid.toString().equals("") || gctypeid.toString().length() != 0)
                    gctype = gctypeid.substring(0, gctypeid.length() - 1);
                String cgtype = null;
                int m = cgtypeid.length();
                if (!cgtypeid.toString().equals("") || cgtypeid.toString().length() != 0)
                    cgtype = cgtypeid.substring(0, cgtypeid.length() - 1);
                getNetWorker().clientAdd(temp_token, username, Md5Util.getMd5(XtomConfig.DATAKEY
                                + Md5Util.getMd5(password)), name, sex, age, email,
                        Loaction, company, position, gctype, cgtype, typekeyword);
            }
        });
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

    //选择时间
    public void showSelectTimeBottomPopu() {
        selectTime = new BottomPopuWindowTimePicker(mContext, popuWidnwTimePickerOnClick, 2017, 60);
        //显示窗口
        selectTime.showAtLocation(layout_year, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    private BottomPopuWindowTimePicker.OnBackClickForString popuWidnwTimePickerOnClick = new BottomPopuWindowTimePicker.OnBackClickForString() {
        @Override
        public void onBackResult(String string, int year) {
            Calendar a = Calendar.getInstance();
            input_year.setText(String.valueOf(a.get(Calendar.YEAR) - year));
        }
    };

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
        viewSex.camera_text.setText("男");
        viewSex.album_text.setText("女");
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
                input_sex.setText("男");
                popupWindow.dismiss();
            }
        });
        viewSex.album_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_sex.setText("女");
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

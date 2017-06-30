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
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.thp.R;
import com.hemaapp.thp.base.JhActivity;
import com.hemaapp.thp.config.XsmConfig;
import com.hemaapp.thp.view.BottomPopuWindowTimePicker;
import com.hemaapp.thp.view.FlowLayout;
import com.hemaapp.thp.view.JhctmImageWay;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;

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
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

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

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

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

    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("个人资料");
        next_button.setText("跳过");

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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

    }
    //选择时间
    public void showSelectTimeBottomPopu() {
        selectTime = new BottomPopuWindowTimePicker(mContext,popuWidnwTimePickerOnClick, 2017, 40);
        //显示窗口
        selectTime.showAtLocation(layout_year, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }
    private BottomPopuWindowTimePicker.OnBackClickForString popuWidnwTimePickerOnClick = new BottomPopuWindowTimePicker.OnBackClickForString() {
        @Override
        public void onBackResult(String string,int year) {
            Calendar a = Calendar.getInstance();
            input_year.setText(String.valueOf(a.get(Calendar.YEAR)-year));
        }
    };
    //选择性别
    public class ViewSex{
        TextView camera_text;
        TextView album_text;
        TextView textView1_camera;
    }
    private void showSex()
    {
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

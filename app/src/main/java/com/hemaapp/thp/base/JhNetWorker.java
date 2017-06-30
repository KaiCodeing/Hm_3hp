package com.hemaapp.thp.base;

import android.content.Context;

import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.thp.nettask.ClientLoginTask;
import com.hemaapp.thp.nettask.InitTask;

import java.util.HashMap;

import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/9/6.
 */
public class JhNetWorker extends HemaNetWorker {
    /**
     * 实例化网络请求工具类
     *
     * @param mContext
     */
    private Context mContext;

    public JhNetWorker(Context mContext) {

        super(mContext);
        this.mContext = mContext;


    }

    @Override
    public void clientLogin() {

        JhHttpInformation information = JhHttpInformation.CLIENT_LOGIN;
        HashMap<String, String> params = new HashMap<String, String>();
        String username = XtomSharedPreferencesUtil.get(mContext, "username");
        params.put("username", username);// 用户登录名 手机号或邮箱
        String password = XtomSharedPreferencesUtil.get(mContext, "password");
        params.put("password", password); // 登陆密码 服务器端存储的是32位的MD5加密串
        params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
        String version = HemaUtil.getAppVersionForSever(mContext);
        params.put("lastloginversion", version);// 登陆所用的系统版本号,记录用户的登录版本，方便服务器运维统计

  //      JhNetTask task = new ClientLoginTask(information, params);
   //     executeTask(task);

    }

    @Override
    public boolean thirdSave() {
        return false;
    }

    /**
     * @param
     * @param
     * @方法名称: inIt
     * @功能描述: TODO初始化
     * @返回值: void
     */
    public void inIt() {
        JhHttpInformation information = JhHttpInformation.INIT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("lastloginversion", HemaUtil.getAppVersionForSever(mContext));// 版本号
        params.put("devicetype", "2");
        JhNetTask netTask = new InitTask(information, params);
        executeTask(netTask);
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     */
    public void clientLogin(String username, String password) {
        JhHttpInformation information = JhHttpInformation.CLIENT_LOGIN;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);// 版本号
        params.put("password", password);
        params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
        String version = HemaUtil.getAppVersionForSever(mContext);
        params.put("lastloginversion", version);// 登陆所用的系统版本号,记录用户的登录版本，方便服务器运维统计
        JhNetTask netTask = new ClientLoginTask(information, params);
        executeTask(netTask);
    }

    /**
     * 用户详情
     *
     * @param //username
     * @param //password
     */
    public void clientGet(String token, String id) {
//        JhHttpInformation information = JhHttpInformation.CLIENT_GET;
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("token", token);// 版本号
//        params.put("id", id);
//
//        String version = HemaUtil.getAppVersionForSever(mContext);
//        JhNetTask netTask = new ClientLoginTask(information, params);
//        executeTask(netTask);
    }

    /**
     * @param username
     * @param code
     * @方法名称: codeVerify
     * @功能描述: TODO验证验证码
     * @返回值: void
     */
    public void codeVerify(String username, String code) {
        JhHttpInformation information = JhHttpInformation.CODE_VERIFY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("code", code);
//        JhNetTask task = new CodeVerifyTask(information, params);
//        executeTask(task);
    }

    /**
     * @param username
     * @方法名称: clientVerify
     * @功能描述: TODO验证用户是否注册过
     * @返回值: void
     */
    public void clientVerify(String username) {
        JhHttpInformation information = JhHttpInformation.CLIENT_VERIFY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);

//        JhNetTask task = new ClientVerifyTask(information, params);
//        executeTask(task);
    }

    /**
     * @param username
     * @方法名称: codeGet
     * @功能描述: TODO发送验证码
     * @返回值: void
     */
    public void codeGet(String username, String keytype) {
        JhHttpInformation information = JhHttpInformation.CODE_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("keytype", keytype);
//        JhNetTask task = new CodeGetTask(information, params);
//        executeTask(task);
    }

    /**
     * @param temp_token
     * @param keytype
     * @param new_password
     * @方法名称: passwordReset
     * @功能描述: TODO修改密码
     * @返回值: void
     */
    public void passwordReset(String temp_token, String username, String keytype,
                              String new_password) {
        JhHttpInformation information = JhHttpInformation.PASSWORD_RESET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("temp_token", temp_token);
        params.put("keytype", keytype);
        params.put("username", username);
        params.put("new_password", new_password);
//        JhNetTask task = new PasswordResetTask(information, params);
//        executeTask(task);
    }

    /**
     * @param token
     * @param deviceid
     * @param devicetype
     * @param channelid
     * @方法名称: deviceSave
     * @功能描述: TODO硬件保存
     * @返回值: void
     */
    public void deviceSave(String token, String deviceid, String devicetype,
                           String channelid) {
//        JhHttpInformation information = JhHttpInformation.DEVICE_SAVE;
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("token", token);
//        params.put("deviceid", deviceid);
//        params.put("devicetype", devicetype);
//        params.put("channelid", channelid);

//        JhNetTask task = new CodeGetTask(information, params);
//        executeTask(task);
    }


}

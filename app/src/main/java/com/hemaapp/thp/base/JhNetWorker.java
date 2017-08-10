package com.hemaapp.thp.base;

import android.content.Context;

import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.thp.nettask.AlipayTradeTask;
import com.hemaapp.thp.nettask.ClientAddTask;
import com.hemaapp.thp.nettask.ClientLoginTask;
import com.hemaapp.thp.nettask.ClientVerifyTask;
import com.hemaapp.thp.nettask.CodeGetTask;
import com.hemaapp.thp.nettask.CodeVerifyTask;
import com.hemaapp.thp.nettask.DistrictAllListTask;
import com.hemaapp.thp.nettask.DurationGetTask;
import com.hemaapp.thp.nettask.FileUploadTask;
import com.hemaapp.thp.nettask.InitTask;
import com.hemaapp.thp.nettask.IsDisplayTask;
import com.hemaapp.thp.nettask.MemberBuyTask;
import com.hemaapp.thp.nettask.MemberfeeGetTask;
import com.hemaapp.thp.nettask.NoticeListTask;
import com.hemaapp.thp.nettask.NoticeUnreadTask;
import com.hemaapp.thp.nettask.PasswordResetTask;
import com.hemaapp.thp.nettask.TenderGetTask;
import com.hemaapp.thp.nettask.TenderListTask;
import com.hemaapp.thp.nettask.TypeGetTask;
import com.hemaapp.thp.nettask.WeixinTradeTask;

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
        JhNetTask task = new ClientLoginTask(information, params);
        executeTask(task);
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
        JhHttpInformation information = JhHttpInformation.CLIENT_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);// 版本号
        params.put("id", id);
        JhNetTask netTask = new ClientLoginTask(information, params);
        executeTask(netTask);
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
        JhNetTask task = new CodeVerifyTask(information, params);
        executeTask(task);
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

        JhNetTask task = new ClientVerifyTask(information, params);
        executeTask(task);
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
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * @param temp_token
     * @param keytype
     * @param new_password
     * @方法名称: passwordReset
     * @功能描述: TODO修改密码
     * @返回值: void
     */
    public void passwordReset(String temp_token, String keytype,
                              String new_password, String name) {
        JhHttpInformation information = JhHttpInformation.PASSWORD_RESET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("temp_token", temp_token);
        params.put("keytype", keytype);
        params.put("name", name);
        params.put("new_password", new_password);
        JhNetTask task = new PasswordResetTask(information, params);
        executeTask(task);
    }

    /**
     * @param temp_token
     * @param keytype
     * @param new_password
     * @方法名称: passwordReset
     * @功能描述: TODO修改密码
     * @返回值: void
     */
    public void password2Reset(String temp_token, String keytype,
                               String new_password, String oldpassword) {
        JhHttpInformation information = JhHttpInformation.PASSWORD_RESET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("temp_token", temp_token);
        params.put("keytype", keytype);
        params.put("old_password", oldpassword);
        params.put("new_password", new_password);
        JhNetTask task = new PasswordResetTask(information, params);
        executeTask(task);
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
        JhHttpInformation information = JhHttpInformation.DEVICE_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("deviceid", deviceid);
        params.put("devicetype", devicetype);
        params.put("channelid", channelid);

        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * @param keytype
     * @方法名称: passwordReset
     * @功能描述: TODO修改密码
     * @返回值: void
     */
    public void typeGet(String keytype) {
        JhHttpInformation information = JhHttpInformation.TYPE_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("keytype", keytype);
        JhNetTask task = new TypeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 地区列表district_list
     */
    public void districtALLList() {
        JhHttpInformation information = JhHttpInformation.ADDRESS_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        JhNetTask netTask = new DistrictAllListTask(information, params);
        executeTask(netTask);
    }

    /**
     * @param temp_token
     * @param username
     * @param password
     * @方法名称: clientAdd
     * @功能描述: TODO 用户注册
     * @返回值: void
     */
    public void clientAdd(String temp_token, String username, String password,
                          String name, String sex, String age, String email, String address, String company,
                          String position,
                          String gctypeid,
                          String cgtypeid,
                          String typekeyword) {
        JhHttpInformation information = JhHttpInformation.CLIENT_ADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("temp_token", temp_token);
        params.put("username", username);
        params.put("password", password);
        params.put("name", name);
        params.put("sex", sex);
        params.put("age", age);
        params.put("email", email);
        params.put("address", address);
        params.put("company", company);
        params.put("position", position);
        params.put("gctypeid", gctypeid);
        params.put("cgtypeid", cgtypeid);
        params.put("typekeyword", typekeyword);
        JhNetTask task = new ClientAddTask(information, params);
        executeTask(task);
    }

    /**
     * @param token
     * @param keytype
     * @param keyid
     * @param orderby
     * @param temp_file
     * @方法名称: fileUpload
     * @功能描述: TODO上传文件
     * @返回值: void
     */
    public void fileUpload(String token, String keytype, String keyid, String orderby,
                           String duration, String content, String temp_file) {
        JhHttpInformation information = JhHttpInformation.FILE_UPLOAD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
//        params.put("orderby", orderby);
//        params.put("duration", duration);
//        params.put("content", content);
        HashMap<String, String> files = new HashMap<String, String>();
        files.put("temp_file", temp_file);
        JhNetTask task = new FileUploadTask(information, params, files);
        executeTask(task);
    }

    /**
     * 单独获取地区列表
     *
     * @param keytype
     * @param id
     */
    public void addressGet(String keytype, String id) {
        JhHttpInformation information = JhHttpInformation.ADDRESS_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);
        params.put("id", id);
        JhNetTask task = new DistrictAllListTask(information, params);
        executeTask(task);
    }

    /**
     * 信息列表
     *
     * @param keytype
     * @param
     */
    public void tenderList(String key, String keytype, String address, String period, String begintime, String endtime, String type, String page) {
        JhHttpInformation information = JhHttpInformation.TENDER_LIST;
        HashMap<String, String> params = new HashMap<>();
        String token;
        if (JhctmApplication.getInstance().getUser() == null) {
            token = "";
        } else {
            token = JhctmApplication.getInstance().getUser().getToken();
        }
        params.put("token", token);
        params.put("key", key);
        params.put("keytype", keytype);
        params.put("address", address);
        params.put("period", period);
        params.put("begintime", begintime);
        params.put("endtime", endtime);
        params.put("type", type);
        params.put("page", page);
        JhNetTask task = new TenderListTask(information, params);
        executeTask(task);
    }

    /**
     * 消息详情
     *
     * @param token
     * @param id
     */
    public void tenderGet(String token, String id) {
        JhHttpInformation information = JhHttpInformation.TENDER_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        JhNetTask task = new TenderGetTask(information, params);
        executeTask(task);
    }

    /**
     * 关注
     *
     * @param token
     * @param keytype
     * @param id
     */
    public void tenderOpe(String token, String keytype, String id) {
        JhHttpInformation information = JhHttpInformation.TENDER_OPE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("id", id);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 发送公告
     *
     * @param token
     * @param id
     */
    public void sendemail(String token, String id) {
        JhHttpInformation information = JhHttpInformation.SENDEMAIL;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 搜索
     *
     * @param type
     * @param address
     * @param keywords
     * @param typeid
     */
    public void tenderSelect(String type, String address, String keywords, String typeid, String page) {
        JhHttpInformation information = JhHttpInformation.TENDER_SELECT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        params.put("address", address);
        params.put("keywords", keywords);
        params.put("typeid", typeid);
        params.put("page", page);
        JhNetTask task = new TenderListTask(information, params);
        executeTask(task);
    }

    /**
     * 未读通知数
     *
     * @param token
     * @param
     */
    public void noticeUnread(String token) {
        JhHttpInformation information = JhHttpInformation.UNREAD_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        JhNetTask task = new NoticeUnreadTask(information, params);
        executeTask(task);
    }

    /**
     * 获取用户通知列表
     */
    public void noticeList(String token, String page) {
        JhHttpInformation information = JhHttpInformation.NOTICE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);// 登陆令牌
        params.put("page", page);// 当前列表翻页索引 第一页时请传递page=0，翻页时依次递增。
        JhNetTask task = new NoticeListTask(information, params);
        executeTask(task);
    }

    /**
     * 通知操作
     *
     * @param token
     * @param id
     */
    public void noticeOperate(String token, String id, String operatetype) {
        JhHttpInformation information = JhHttpInformation.NOTICE_SAVEOPERATE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("operatetype", operatetype);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 消息列表子列表
     *
     * @param token
     * @param id
     * @param page
     */
    public void noticeTender(String token, String id, String page) {
        JhHttpInformation information = JhHttpInformation.NOTICE_TENDER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("page", page);
        JhNetTask task = new TenderListTask(information, params);
        executeTask(task);
    }

    /**
     * 意见反馈
     *
     * @param token
     * @param content
     * @param username
     */
    public void adviceAdd(String token, String content, String username) {
        JhHttpInformation information = JhHttpInformation.ADVICE_ADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("content", content);
        params.put("username", username);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 退出登录
     *
     * @param
     * @param
     */
    public void clientLoginout(String token) {
        JhHttpInformation information = JhHttpInformation.CLIENT_LOGINOUT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 用户保存
     *
     * @param token
     * @param name
     * @param sex
     * @param age
     * @param email
     * @param address
     * @param company
     * @param position
     * @param gctypeid
     * @param cgtypeid
     * @param typekeyword
     */
    public void clientSave(String token,
                           String name, String sex, String age, String email, String address, String company,
                           String position,
                           String gctypeid,
                           String cgtypeid,
                           String typekeyword) {
        JhHttpInformation information = JhHttpInformation.CLIENT_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("name", name);
        params.put("sex", sex);
        params.put("age", age);
        params.put("email", email);
        params.put("address", address);
        params.put("company", company);
        params.put("position", position);
        params.put("gctypeid", gctypeid);
        params.put("cgtypeid", cgtypeid);
        params.put("typekeyword", typekeyword);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 获取时长
     *
     * @param token
     */
    public void durationGet(String token) {
        JhHttpInformation information = JhHttpInformation.DURATION_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        JhNetTask task = new DurationGetTask(information, params);
        executeTask(task);
    }

    /**
     * 获取会员费用
     *
     * @param token
     * @param keytype
     * @param type
     * @param duration
     */
    public void memberfeeGet(String token, String keytype, String type, String duration) {
        JhHttpInformation information = JhHttpInformation.MEMBERFEE_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("type", type);
        params.put("duration", duration);
        JhNetTask task = new MemberfeeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 购买会员
     *
     * @param token
     * @param keytype
     * @param type
     * @param duration
     */
    public void memberBuy(String token, String keytype, String type, String duration) {
        JhHttpInformation information = JhHttpInformation.MEMBER_BUY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("member", keytype);
        params.put("type", type);
        params.put("duration", duration);
        JhNetTask task = new MemberBuyTask(information, params);
        executeTask(task);
    }

    /**
     * 获取支付宝交易签名串
     */
    public void alipay(String token,
                       String keyid, String total_fee) {
        JhHttpInformation information = JhHttpInformation.ALIPAY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);// 登陆令牌
        params.put("paytype", "1");// 支付类型 固定传2
        params.put("keytype", "2");// 业务类型,1：账户余额充值2：商品立即购买
        params.put("keyid", keyid);// 业务相关,id当keytype=1时,keyid=0当keytype=2时,keyid=blog_id
        params.put("total_fee", total_fee);// 支付交易金额,单位：元(测试时统一传递0.01元)
//        params.put("total_score", total_score);// 支付交易金额,单位：元(测试时统一传递0.01元)
//        params.put("total_share_score", total_share_score);// 支付交易金额,单位：元(测试时统一传递0.01元)
        JhNetTask task = new AlipayTradeTask(information, params);
        executeTask(task);
    }

    /**
     * 获取微信预支付交易会话标识
     */
    public void weixinpay(String token,
                          String keyid, String total_fee) {
        JhHttpInformation information = JhHttpInformation.WXPAYMENT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);// 登陆令牌
        params.put("keytype", "2");// 业务类型,1：账户余额充值2：商品立即购买
        params.put("keyid", keyid);// 业务相关,id当keytype=1时,keyid=0当keytype=2时,keyid=blog_id
        params.put("paytype", "3");
        params.put("total_fee", total_fee);
//        params.put("total_score", total_score);
//        params.put("total_share_score", total_share_score);
        JhNetTask task = new WeixinTradeTask(information, params);
        executeTask(task);
    }

    /**
     * 关注列表
     *
     * @param token
     * @param keytype
     * @param page
     */
    public void liketenderList(String token, String keytype, String page) {
        JhHttpInformation information = JhHttpInformation.LIKETENDER_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("page", page);
        JhNetTask task = new TenderListTask(information, params);
        executeTask(task);
    }

    /**
     * 反馈城市
     *
     * @param token
     * @param address
     */
    public void feedbackAdd(String token, String address) {
        JhHttpInformation information = JhHttpInformation.FEEDBACK_ADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("address", address);
        params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
        String version = HemaUtil.getAppVersionForSever(mContext);
        params.put("lastloginversion", version);// 登陆所用的系统版本号,记录用户的登录版本，方便服务器运维统计
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 上定制信息
     * @param token
     * @param address
     * @param time
     * @param keytype
     * @param keywords
     * @param pushemail
     */
    public void madeInfo(String token, String address,String time,String keytype,String keywords,String pushemail) {
        JhHttpInformation information = JhHttpInformation.MADEINFO;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("address", address);
        params.put("time", time);
        params.put("keytype", keytype);
        params.put("keywords", keywords);
        params.put("pushemail", pushemail);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 是否弹窗
     * @param address
     */
    public void isDisplay(String address) {
        JhHttpInformation information = JhHttpInformation.IS_DISPLAY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("address", address);
        JhNetTask task = new IsDisplayTask(information, params);
        executeTask(task);
    }
}

package com.hemaapp.thp.base;

import com.hemaapp.HemaConfig;
import com.hemaapp.hm_FrameWork.HemaHttpInfomation;
import com.hemaapp.thp.config.XsmConfig;
import com.hemaapp.thp.model.SysInitInfo;

/**
 * Created by lenovo on 2016/9/6.
 */
public  enum JhHttpInformation implements HemaHttpInfomation {
    /**
     * 登录
     */
    CLIENT_LOGIN(HemaConfig.ID_LOGIN, "client_login", "登录", false),
    // 注意登录接口id必须为HemaConfig.ID_LOGIN

    /**
     * 后台服务接口根路径
     */
    SYS_ROOT(0, XsmConfig.SYS_ROOT, "后台服务接口根路径", true),
    /**
     * 初始化
     */
    INIT(1, "index.php/webservice/index/init", "初始化", false),
    /**
     * code_get发送验证码
     */
    CODE_GET(2, "code_get", "发送验证码", false),
    /**
     * code_verify验证验证码
     */
    CODE_VERIFY(3, "code_verify", "验证验证码", false),
    /**
     * client_verify验证用户是否注册
     */
    CLIENT_VERIFY(4, "client_verify", "验证用户是否注册", false),
    /**
     * 密码重设password_reset
     */
    PASSWORD_RESET(5, "password_reset", "密码重设", false),
    /**
     * 支付宝alipay
     */
    ALIPAY(7,"OnlinePay/Alipay/alipaysign_get.php","支付宝获取串口号",false),
    /**
     * 银联支付unionpay
     */
    UNIONPAY(8,"OnlinePay/Unionpay/unionpay_get.php","银联获取串口号",false),
    /**
     * 微信支付wxpayment
     */
    WXPAYMENT(9,"OnlinePay/Weixinpay/weixinpay_get.php","微信获取串口号",false),
    /**
     * type_get 类型获取
     */
    TYPE_GET(10,"type_get","类型获取",false),
    /**
     * address_list 地区列表
     */
    ADDRESS_LIST(11,"address_list","地区列表",false),
    /**
     * 注册client_add
     */
    CLIENT_ADD(12,"client_add","注册",false),
    /**
     * 上传文件 FILE_UPLOAD
     */
    FILE_UPLOAD(13,"FILE_UPLOAD","上传文件",false),
    /**
     * address_get 地区列表
     */
    ADDRESS_GET(14,"address_get","地区列表",false),
    /**
     * tender_list 信息列表
     */
    TENDER_LIST(15,"tender_list","信息列表",false),
    /**
     * tender_get 信息详情
     */
    TENDER_GET(16,"tender_get","信息详情",false),
    /**
     * client_get 获取用户详情
     */
    CLIENT_GET(17,"client_get","获取用户详情",false),
    /**
     * device_save硬件保存
     */
    DEVICE_SAVE(18,"device_save","硬件保存",false),
    /**
     * tender_ope 关注操作
     */
    TENDER_OPE(19,"tender_ope","关注操作",false),
    /**
     * sendemail 发送公告
     */
    SENDEMAIL(20,"sendemail","发送公告",false),
    /**
     * tender_select 搜索
     */
    TENDER_SELECT(21,"tender_select","搜索",false),
    /**
     * unread_get未读通知数
     */
    UNREAD_GET(22,"unread_get","未读通知数",false),
    /**
     * notice_list 消息列表
     */
    NOTICE_LIST(23,"notice_list","消息列表",false),
    /**
     * 消息操作
     * notice_saveoperate
     */
    NOTICE_SAVEOPERATE(24,"notice_saveoperate","消息操作",false),
    /**
     * notice_tender消息列表中的子列表
     */
    NOTICE_TENDER(25,"notice_tender","消息列表中的子列表",false),
    /**
     * advice_add 意见反馈
     */
    ADVICE_ADD(26,"advice_add","意见反馈",false),
    /**
     * 退出登录client_loginout
     */
    CLIENT_LOGINOUT(27,"client_loginout","退出登录",false),
    /**
     * client_save  个人信息修改保存
     */
    CLIENT_SAVE(28,"client_save","个人信息修改",false),
    /**
     * duration_get 获取时长
     */
    DURATION_GET(29,"duration_get","获取时长",false),
    /**
     * memberfee_get 获取费用
     */
    MEMBERFEE_GET(30,"memberfee_get","获取费用",false),
    /**
     * member_buy 购买会员
     */
    MEMBER_BUY(31,"member_buy","购买会员",false),
    /**
     * liketender_list 关注列表
     */
    LIKETENDER_LIST(32,"liketender_list","关注列表",false),
    /**
     * feedback_add 反馈城市
     */
    FEEDBACK_ADD(33,"feedback_add","反馈城市",false),
    /**
     * madeinfo 定制消息
     */
    MADEINFO(34,"madeinfo","定制消息",false);
    private int id;// 对应NetTask的id
    private String urlPath;// 请求地址
    private String description;// 请求描述
    private boolean isRootPath;// 是否是根路径

    private JhHttpInformation(int id, String urlPath, String description,
                              boolean isRootPath) {
        this.id = id;
        this.urlPath = urlPath;
        this.description = description;
        this.isRootPath = isRootPath;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getUrlPath() {
        if (isRootPath)
            return urlPath;

        String path = SYS_ROOT.urlPath + urlPath;

        if (this.equals(INIT))
            return path;

        JhctmApplication application = JhctmApplication.getInstance();
        SysInitInfo info = application.getSysInitInfo();
        path = info.getSys_web_service() + urlPath;
         if (this.equals(ALIPAY))
         path = info.getSys_plugins() + urlPath;
         if (this.equals(UNIONPAY))
         path = info.getSys_plugins() + urlPath;
        if (this.equals(WXPAYMENT))
            path = info.getSys_plugins() + urlPath;
        return path;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isRootPath() {
        return isRootPath;
    }

}

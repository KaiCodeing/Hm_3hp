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
    WXPAYMENT(9,"OnlinePay/Weixinpay/weixinpay_get.php","微信获取串口号",false);


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

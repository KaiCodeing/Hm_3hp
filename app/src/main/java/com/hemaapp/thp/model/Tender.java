package com.hemaapp.thp.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/7/13.
 */
public class Tender extends XtomObject implements Serializable {
    private String id;
    private String province;
    private String city;
    private String area;
    private String name;
    private String signuptime;
    private String endtime;
    private String conditions;
    private String keytype;
    private String type;
    private String tendercontent;
    private String jointime;
    private String wincompany;
    private String winmoney;
    private String wincontent;
    private String is_like;
    private String createtime;
    private String status;
    private String tenderdemoname;
    private String windemoname;
    private String agency;
    private String tender;
    private String gctype;
    private String cgtype;
    public Tender(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                agency = get(jsonObject, "agency");
                tender = get(jsonObject, "tender");
                gctype = get(jsonObject, "gctype");
                cgtype = get(jsonObject, "cgtype");
                province = get(jsonObject, "province");
                keytype = get(jsonObject, "keytype");
                city = get(jsonObject, "city");
                area = get(jsonObject, "area");
                name = get(jsonObject, "name");
                signuptime = get(jsonObject, "signuptime");
                endtime = get(jsonObject, "endtime");
                conditions = get(jsonObject, "conditions");
                type = get(jsonObject, "type");
                tendercontent = get(jsonObject, "tendercontent");
                jointime = get(jsonObject, "jointime");
                wincompany = get(jsonObject, "wincompany");
                winmoney = get(jsonObject, "winmoney");
                wincontent = get(jsonObject, "wincontent");
                is_like = get(jsonObject, "is_like");
                createtime = get(jsonObject, "createtime");
                status = get(jsonObject, "status");
                tenderdemoname = get(jsonObject, "tenderdemoname");
                windemoname = get(jsonObject, "windemoname");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public String getAgency() {
        return agency;
    }

    public String getCgtype() {
        return cgtype;
    }

    public String getGctype() {
        return gctype;
    }

    public String getTender() {
        return tender;
    }

    @Override
    public String toString() {
        return "Tender{" +
                "agency='" + agency + '\'' +
                ", id='" + id + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", name='" + name + '\'' +
                ", signuptime='" + signuptime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", conditions='" + conditions + '\'' +
                ", keytype='" + keytype + '\'' +
                ", type='" + type + '\'' +
                ", tendercontent='" + tendercontent + '\'' +
                ", jointime='" + jointime + '\'' +
                ", wincompany='" + wincompany + '\'' +
                ", winmoney='" + winmoney + '\'' +
                ", wincontent='" + wincontent + '\'' +
                ", is_like='" + is_like + '\'' +
                ", createtime='" + createtime + '\'' +
                ", status='" + status + '\'' +
                ", tenderdemoname='" + tenderdemoname + '\'' +
                ", windemoname='" + windemoname + '\'' +
                ", tender='" + tender + '\'' +
                ", gctype='" + gctype + '\'' +
                ", cgtype='" + cgtype + '\'' +
                '}';
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public String getTenderdemoname() {
        return tenderdemoname;
    }

    public String getWindemoname() {
        return windemoname;
    }

    public String getStatus() {
        return status;
    }

    public String getArea() {
        return area;
    }

    public String getCity() {
        return city;
    }

    public String getConditions() {
        return conditions;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getId() {
        return id;
    }

    public String getIs_like() {
        return is_like;
    }

    public String getJointime() {
        return jointime;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }

    public String getSignuptime() {
        return signuptime;
    }

    public String getTendercontent() {
        return tendercontent;
    }

    public String getType() {
        return type;
    }

    public String getWincompany() {
        return wincompany;
    }

    public String getWincontent() {
        return wincontent;
    }

    public String getWinmoney() {
        return winmoney;
    }
}

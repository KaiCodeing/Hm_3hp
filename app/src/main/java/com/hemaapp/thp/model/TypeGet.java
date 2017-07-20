package com.hemaapp.thp.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/7/7.
 * 获取类型
 */
public class TypeGet extends XtomObject implements Serializable {
    private String id;
    private String keytype;
    private String name;
    private boolean check;
    public TypeGet(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                keytype = get(jsonObject, "keytype");
                check = false;
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "TypeGet{" +
                "check=" + check +
                ", id='" + id + '\'' +
                ", keytype='" + keytype + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }

    public String getId() {
        return id;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getName() {
        return name;
    }
}

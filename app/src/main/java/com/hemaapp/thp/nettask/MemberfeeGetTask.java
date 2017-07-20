package com.hemaapp.thp.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.thp.base.JhHttpInformation;
import com.hemaapp.thp.base.JhNetTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/7/19.
 * 获取会员费用
 */
public class MemberfeeGetTask extends JhNetTask {
    public MemberfeeGetTask(JhHttpInformation information,
                          HashMap<String, String> params) {
        super(information, params);
    }

    public MemberfeeGetTask(JhHttpInformation information,
                          HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }
    private class Result extends HemaArrayResult<String> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public String parse(JSONObject jsonObject) throws DataParseException {
            try {
                return get(jsonObject, "money");
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }

    }
}

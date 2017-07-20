package com.hemaapp.thp.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.thp.base.JhHttpInformation;
import com.hemaapp.thp.base.JhNetTask;
import com.hemaapp.thp.model.Notice;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/7/19.
 * 获取时长
 */
public class DurationGetTask extends JhNetTask{

    public DurationGetTask(JhHttpInformation information,
                               HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<Notice> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public Notice parse(JSONObject jsonObject) throws DataParseException {
            return new Notice(jsonObject);
        }

    }
}

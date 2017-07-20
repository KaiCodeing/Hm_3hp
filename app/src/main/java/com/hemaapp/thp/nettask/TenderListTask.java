package com.hemaapp.thp.nettask;

import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.thp.base.JhHttpInformation;
import com.hemaapp.thp.base.JhNetTask;
import com.hemaapp.thp.model.Tender;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/7/13.
 * 信息列表
 */
public class TenderListTask extends JhNetTask {

    public TenderListTask(JhHttpInformation information,
                               HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaPageArrayResult<Tender> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public Tender parse(JSONObject jsonObject) throws DataParseException {
            return new Tender(jsonObject);
        }

    }
}

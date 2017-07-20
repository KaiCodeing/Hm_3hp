package com.hemaapp.thp.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.thp.base.JhHttpInformation;
import com.hemaapp.thp.base.JhNetTask;
import com.hemaapp.thp.model.CityChildren;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * ���ȫ������
 * @author lenovo
 *
 */
public class DistrictAllListTask extends JhNetTask {

	public DistrictAllListTask(JhHttpInformation information,
							   HashMap<String, String> params) {
		super(information, params);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new Result(jsonObject);
	}

	private class Result extends HemaArrayResult<CityChildren> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
		}

		@Override
		public CityChildren parse(JSONObject jsonObject) throws DataParseException {
			return new CityChildren(jsonObject);
		}

	}
}

package com.pw.schoolknow.utils;

import org.json.JSONArray;
import org.json.JSONObject;


public class GoogleApi {
	private static String QUERYADDRESS = "http://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&sensor=true&language=zh_cn";
    public static String queryLocation(String lat, String lng) {
            String url = String.format(QUERYADDRESS, lat, lng);
            String result = GetUtil.getRes(url);
            String address = "Î´ÄÜ²éÑ¯µ½";
            try {
                    JSONArray jsonArray = new JSONObject(result)
                                    .getJSONArray("results");
                    if (jsonArray.length() > 1) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            address = jsonObject.optString("formatted_address");
                    }

            } catch (Exception e) {
                    // TODO: handle exception
            }
            return address;

    }
}

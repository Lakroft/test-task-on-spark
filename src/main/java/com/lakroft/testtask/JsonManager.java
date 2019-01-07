package com.lakroft.testtask;

import net.minidev.json.JSONObject;

public class JsonManager {
    public static final String RESULT = "result";
    public static final String ERROR = "error";
    public static final String SUCCESS = "success";
    public static final String MESSAGE = "message";

    public static JSONObject getSuccess() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(RESULT, SUCCESS);
        return jsonObject;
    }

    public static JSONObject getSuccess(String key, Object object) {
        JSONObject jsonObject = getSuccess();
        jsonObject.put(key, object);
        return jsonObject;
    }

    public static JSONObject getError() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(RESULT, ERROR);
        return jsonObject;
    }

    public static JSONObject getError(String message) {
        JSONObject jsonObject = getError();
        jsonObject.put(MESSAGE, message);
        return jsonObject;
    }
}

package com.example.cy.cody_;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonRequest extends StringRequest {
    private Map<String, String> parameters;

    public JsonRequest(JSONObject json, String URL, Response.Listener<String> listener){
        super(com.android.volley.Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();   // Map 형식으로 만들어서 반환
        parameters.put("user", json.toString());

    }

    public Map<String, String> getParams(){
        return parameters;
    }
}

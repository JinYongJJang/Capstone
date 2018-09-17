package com.example.cy.cody_;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest{
    final static private  String URL = "http://hama0130.cafe24.com/Login.php";
    private Map<String, String> parameters;

    public LoginRequest(String UserEmail, String UserPassword, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("UserEmail", UserEmail);
        parameters.put("UserPassword", UserPassword);
    }

    public Map<String, String> getParams(){
        return parameters;
    }
}

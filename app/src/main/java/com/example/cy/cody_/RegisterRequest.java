package com.example.cy.cody_;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    final static private  String URL = "http://hama0130.cafe24.com/Register.php";
    private Map<String, String> parameters;

    public RegisterRequest(String UserEmail, String UserPassword, String UserName, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("UserEmail", UserEmail);
        parameters.put("UserPassword", UserPassword);
        parameters.put("UserName", UserName);
    }

    public Map<String, String> getParams(){
        return parameters;
    }
}

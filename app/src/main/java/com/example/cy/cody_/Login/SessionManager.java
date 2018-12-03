package com.example.cy.cody_.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.cy.cody_.Login.UserinfoActivity;
import com.example.cy.cody_.MainActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String REG ="REGISTER";
    private static final String LOGIN ="IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String EMAIL = "EMAIL";
    //public static final String ID="ID";

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("PREF_NAME",PRIVATE_MODE);
        editor = sharedPreferences.edit();

    }
    public void createSession(String Name, String Email, String Password){
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, Name);
        editor.putString(EMAIL, Email);
        editor.putString(PASSWORD, Password);
        editor.apply();
    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if(!this.isLoggin()){
            Intent i = new Intent(context,LoginActivity.class);
            context.startActivity(i);
            ((UserinfoActivity)context).finish();
        }
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context,LoginActivity.class);
        context.startActivity(i);
        ((UserinfoActivity)context).finish();
    }

    public HashMap<String, String> SessiongetUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME,sharedPreferences.getString(NAME,null));
        user.put(EMAIL,sharedPreferences.getString(EMAIL,null));
        user.put(PASSWORD, sharedPreferences.getString(PASSWORD, null));
        //user.put(ID, sharedPreferences.getString(ID,null));

        return user;
    }

}

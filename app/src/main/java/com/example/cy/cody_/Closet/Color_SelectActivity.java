package com.example.cy.cody_.Closet;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.cy.cody_.Expert.ManagerSubActivity;
import com.example.cy.cody_.JsonRequest;
import com.example.cy.cody_.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.Math.abs;

public class Color_SelectActivity extends AppCompatActivity {
    static final int REQUEST_POPUPS = 9090;

    ImageView views = null;
    Bitmap bitmap = null;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button research;

    int [] btn1_rgb = new int [3];
    int [] btn2_rgb = new int [3];
    int [] btn3_rgb = new int [3];
    int [] btn4_rgb = new int [3];
    int [] btn5_rgb = new int [3];
    int [] btn6_rgb = new int [3];


    String check_man = "yes";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color__select);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비
        int height = dm.heightPixels; //디바이스 화면 높이

        Popup_Activity cd = new Popup_Activity(this);
        WindowManager.LayoutParams wm = cd.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
        wm.copyFrom(cd.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = width ;  //화면 너비의 절반
        wm.height = height ;  //화면 높이의 절반
        cd.show();




        views = (ImageView) findViewById(R.id.test) ;
        button1 = (Button) findViewById(R.id.color_button1);
        button2 = (Button) findViewById(R.id.color_button2);
        button3 = (Button) findViewById(R.id.color_button3);
        button4 = (Button) findViewById(R.id.color_button4);
        button5 = (Button) findViewById(R.id.color_button5);
        button6 = (Button) findViewById(R.id.color_button6);
        research = (Button)findViewById(R.id.color_research);
        Log.e ("CY_","나 들어왔음");
        Intent intent = getIntent();
        if(intent.hasExtra("colorimage")){
            bitmap = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("colorimage"),0,
                    getIntent().getByteArrayExtra("colorimage").length);
        }

        views.setImageBitmap(bitmap);
        Palette.from(bitmap).maximumColorCount(24).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                setPalette(palette);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Intent intent = new Intent();
                intent.putExtra("yes", check_man);
                intent.putExtra("red", btn1_rgb[0]);
                intent.putExtra("green", btn1_rgb[1]);
                intent.putExtra("blue", btn1_rgb[2]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("yes", check_man);
                intent.putExtra("red", btn2_rgb[0]);
                intent.putExtra("green", btn2_rgb[1]);
                intent.putExtra("blue", btn2_rgb[2]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("yes", check_man);
                intent.putExtra("red", btn3_rgb[0]);
                intent.putExtra("green", btn3_rgb[1]);
                intent.putExtra("blue", btn3_rgb[2]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("yes", check_man);
                intent.putExtra("red", btn4_rgb[0]);
                intent.putExtra("green", btn4_rgb[1]);
                intent.putExtra("blue", btn4_rgb[2]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("yes", check_man);
                intent.putExtra("red", btn5_rgb[0]);
                intent.putExtra("green", btn5_rgb[1]);
                intent.putExtra("blue", btn5_rgb[2]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("yes", check_man);
                intent.putExtra("red", btn6_rgb[0]);
                intent.putExtra("green", btn6_rgb[1]);
                intent.putExtra("blue", btn6_rgb[2]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        research.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("yes", "no");
                //intent.putExtra("Password", Password);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }


    private void setPalette(Palette palette) {
        if(palette==null) return;
        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
        if(vibrantSwatch!=null){
            GradientDrawable bg = (GradientDrawable)button1.getBackground().getCurrent();
            bg.setColor(vibrantSwatch.getRgb());
            button1.setEnabled(true);

            btn1_rgb[0] = Color.red(vibrantSwatch.getRgb());
            btn1_rgb[1] =Color.green(vibrantSwatch.getRgb());
            btn1_rgb[2] =Color.blue(vibrantSwatch.getRgb());
        }else {
            button1.setEnabled(false);
            button1.setTextColor(Color.parseColor("#FFD1B2FF"));
        }

        Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
        if(darkVibrantSwatch!=null){
            GradientDrawable bg = (GradientDrawable)button2.getBackground().getCurrent();
            bg.setColor(darkVibrantSwatch.getRgb());

            btn2_rgb[0] = Color.red(darkVibrantSwatch.getRgb());
            btn2_rgb[1] =Color.green(darkVibrantSwatch.getRgb());
            btn2_rgb[2] =Color.blue(darkVibrantSwatch.getRgb());

            button2.setEnabled(true);
        }else {
            button2.setEnabled(false);
            button2.setTextColor(Color.parseColor("#FFD1B2FF"));
        }

        Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();
        if(lightVibrantSwatch!=null){
            GradientDrawable bg = (GradientDrawable)button3.getBackground().getCurrent();
            bg.setColor(lightVibrantSwatch.getRgb());

            btn3_rgb[0] = Color.red(lightVibrantSwatch.getRgb());
            btn3_rgb[1] =Color.green(lightVibrantSwatch.getRgb());
            btn3_rgb[2] =Color.blue(lightVibrantSwatch.getRgb());

            button3.setEnabled(true);
        }else {
            button3.setEnabled(false);
            button3.setTextColor(Color.parseColor("#FFD1B2FF"));
        }

        Palette.Swatch mutedSwatch = palette.getMutedSwatch();
        if(mutedSwatch!=null){
            GradientDrawable bg = (GradientDrawable)button4.getBackground().getCurrent();
            bg.setColor(mutedSwatch.getRgb());

            btn4_rgb[0] = Color.red(mutedSwatch.getRgb());
            btn4_rgb[1] =Color.green(mutedSwatch.getRgb());
            btn4_rgb[2] =Color.blue(mutedSwatch.getRgb());

            button4.setEnabled(true);
        }else {
            button4.setEnabled(false);
            button4.setTextColor(Color.parseColor("#FFD1B2FF"));
        }

        Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
        if(darkMutedSwatch!=null){
            GradientDrawable bg = (GradientDrawable)button5.getBackground().getCurrent();
            bg.setColor(darkMutedSwatch.getRgb());

            btn5_rgb[0] = Color.red(darkMutedSwatch.getRgb());
            btn5_rgb[1] =Color.green(darkMutedSwatch.getRgb());
            btn5_rgb[2] =Color.blue(darkMutedSwatch.getRgb());

            button5.setEnabled(true);
        }else {
            button5.setEnabled(false);
            button5.setTextColor(Color.parseColor("#FFD1B2FF"));
        }

        Palette.Swatch lightMutedSwatch = palette.getLightMutedSwatch();
        if(lightMutedSwatch!=null){
            GradientDrawable bg = (GradientDrawable)button6.getBackground().getCurrent();
            bg.setColor(lightMutedSwatch.getRgb());

            btn6_rgb[0] = Color.red(lightMutedSwatch.getRgb());
            btn6_rgb[1] =Color.green(lightMutedSwatch.getRgb());
            btn6_rgb[2] =Color.blue(lightMutedSwatch.getRgb());

            button6.setEnabled(true);
        }else {
            button6.setEnabled(false);
            button6.setTextColor(Color.parseColor("#FFD1B2FF"));
        }
    }

    public void toRGB(int color) {
        float red = color >>  16 & 0xff;
        float green = color >> 8 & 0xff;
        float blue = color  & 0xff;
        Log.d("DEBUG1",red+" / "+green+ " / " +blue);
    }




}

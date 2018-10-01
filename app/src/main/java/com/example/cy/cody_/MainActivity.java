package com.example.cy.cody_;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Intent ClosetIntent;
    TextView Main_Login_Button;
    ImageButton btnShowLocation;
    ImageView weather_Icons;
    TextView temp;
    TextView city;
    TextView weather;
    TextView country;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    // GPSTracker class
    private GpsInfo gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowLocation = (ImageButton) findViewById(R.id.btn_start); // 날씨 부분
        temp = (TextView) findViewById(R.id.temp); // 온도
        city = (TextView) findViewById(R.id.city); // 도시 이름
        weather =(TextView) findViewById(R.id.weather); // 날씨
        country = (TextView) findViewById(R.id.country); // 나라 이니셜
        weather_Icons = (ImageView)findViewById(R.id.weather_icon); // 아이콘
        temp.setText("0℃");
        weather.setText("Weather Info");
        country.setText("Country");
        city.setText("Name");
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // 권한 요청을 해야 함
                if (!isPermission) {
                    callPermission();
                    return;
                }

                gps = new GpsInfo(MainActivity.this);
                // GPS 사용유무 가져오기
                if (gps.isGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    Log.v("GPS Check ", String.valueOf(latitude)+" "+String.valueOf(longitude));
                    JsonLoadingTask Thread_Json = new JsonLoadingTask();
                    Thread_Json.execute();//Async스레드를 시작
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }
            }
        });
        callPermission();  // 권한 요청을 해야 함

        View view = getWindow().getDecorView();
        Intent intent = new Intent(this, loadingActivity.class);
        startActivity(intent);



        ImageButton buttonCloset = (ImageButton) findViewById(R.id.Closet);
        buttonCloset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent closetintent = new Intent(MainActivity.this,ClosetActivity.class);
                startActivity(closetintent);
            }
        });
        ImageButton buttonCody = (ImageButton) findViewById(R.id.Fast_Cody);
        buttonCody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent codyintent = new Intent(MainActivity.this,Fast_codyActivity.class);
                startActivity(codyintent);
            }
        });
        ImageButton buttonpro = (ImageButton) findViewById(R.id.How_Cloth);
        buttonpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prointent = new Intent(MainActivity.this,How_clothActivity.class);
                startActivity(prointent);
            }
        });
        ImageButton buttonuser = (ImageButton) findViewById(R.id.Expert);
        buttonuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userintent = new Intent(MainActivity.this,ExpertActivity.class);
                startActivity(userintent);
            }
        });
        Main_Login_Button = (TextView) findViewById(R.id.Main_Login_button);
        Main_Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(LoginIntent);
            }
        });
    }
/** -----------------------------------GPS 권한 주고 하는 부분  -------------------------------**/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessFineLocation = true;

        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }

    // GPS 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }

 /**------------------------------- 날씨 정보 파싱 함수 ---------------------------------------**/
    private class JsonLoadingTask extends AsyncTask<String, Void, weather_check> {
        @Override
        protected weather_check doInBackground(String... strs) {
            Log.v("User's Log Thread","start");
            return getJsonText();
        } // doInBackground : 백그라운드 작업을 진행한다.
        @Override
        protected void onPostExecute(weather_check result) {
            Double T = Double.parseDouble(result.getTemp()) - 273;
            weather_Icons.setImageDrawable(SetingIcon(result.getIcon_name()));
            weather.setText(result.getWeather())  ;
            temp.setText(String.format("%.1f",T)+"℃");
            city.setText(result.getCity_name());
            country.setText(result.getCountry());
        } // onPostExecute : 백그라운드 작업이 끝난 후 UI 작업을 진행한다.
    } // JsonLoadingTask


    public Drawable SetingIcon(String icon_name){
        Drawable image_src = null;
        switch (icon_name) {
            case "01n":
                image_src = getResources().getDrawable(R.drawable.n01);
                break;
            case "01d":
                image_src = getResources().getDrawable(R.drawable.d01);
                break;
            case "02n":
                image_src = getResources().getDrawable(R.drawable.n02);
                break;
            case "02d":
                image_src = getResources().getDrawable(R.drawable.d02);
                break;
            case "03n":
                image_src = getResources().getDrawable(R.drawable.d03);
                break;
            case "03d":
                image_src = getResources().getDrawable(R.drawable.d03);
                break;
            case "04n":
                image_src = getResources().getDrawable(R.drawable.d04);
                break;
            case "04d":
                image_src = getResources().getDrawable(R.drawable.d04);
                break;
            case "09n":
                image_src = getResources().getDrawable(R.drawable.d09);
                break;
            case "09d":
                image_src = getResources().getDrawable(R.drawable.d09);
                break;
            case "10n":
                image_src = getResources().getDrawable(R.drawable.d09);
                break;
            case "10d":
                image_src = getResources().getDrawable(R.drawable.d09);
                break;
            case "11n":
                image_src = getResources().getDrawable(R.drawable.d11);
                break;
            case "11d":
                image_src = getResources().getDrawable(R.drawable.d11);
                break;
            case "13n":
                image_src = getResources().getDrawable(R.drawable.d13);
                break;
            case "13d":
                image_src = getResources().getDrawable(R.drawable.d13);
                break;
            case "50n":
                image_src = getResources().getDrawable(R.drawable.d50);
                break;
            case "50d":
                image_src = getResources().getDrawable(R.drawable.d50);
            break;
            default:
                break;
        }
        return image_src;
    }


    /** json 출력 **/
    public weather_check getJsonText() {

        String Lati = String.valueOf(gps.getLatitude());
        String Long= String.valueOf( gps.getLongitude());

        String key = "0d7fd67de81f34df173f3b9569020e45";
        String Weather = "",Icon = "",Temp= "",Country = "",Name= "";
        try {
            //주어진 URL 문서의 내용을 문자열로 얻는다.
            String jsonPage = getStringFromUrl("https://api.openweathermap.org/data/2.5/weather?"+
                    "appid="+key+"&lat="+Lati+"&lon="+Long);

            Log.v("User's Log json",jsonPage);
            //읽어들인 JSON포맷의 데이터를 JSON객체로 변환
            JSONObject json = new JSONObject(jsonPage); //TODO
            //ksk_list의 값은 배열로 구성 되어있으므로 JSON 배열생성
            JSONArray jArr = json.getJSONArray("weather");

            //배열의 크기만큼 반복하면서, ksNo과 korName의 값을 추출함
            for (int i=0; i<jArr.length(); i++){

                //i번째 배열 할당
                json = jArr.getJSONObject(i);

                //날씨 , 아이콘 데이터
                Weather = json.getString("main");
                Icon= json.getString("icon");
            }
            json = new JSONObject(jsonPage);
            JSONObject test = json.getJSONObject("main");
            Temp = test.getString("temp");


            JSONObject test2 = json.getJSONObject("sys");
            Country = test2.getString("country");
            Name = json.getString("name");

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("User's Log Thread err",e.toString());
        }
        return new weather_check(Country,Name,Temp,Weather,Icon);
    }//getJsonText()-----------


    // getStringFromUrl : 주어진 URL의 문서의 내용을 문자열로 반환
    public String getStringFromUrl(String pUrl){

        BufferedReader bufreader=null;
        HttpURLConnection urlConnection = null;

        StringBuffer page= new StringBuffer(); //읽어온 데이터를 저장할 StringBuffer객체 생성
        Log.v("User URL ",pUrl);

        try {
            URL url= new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream contentStream = urlConnection.getInputStream();

            bufreader = new BufferedReader(new InputStreamReader(contentStream,"UTF-8"));
            String line = null;

            //버퍼의 웹문서 소스를 줄단위로 읽어(line), Page에 저장함
            while((line = bufreader.readLine())!=null){
                Log.d("line:",line);
                page.append(line);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //자원해제
            try {
                if (bufreader != null) bufreader.close();
                if (urlConnection != null)urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return page.toString();
    }// getStringFromUrl()-------------------------

    /** ------------------------------- 받아온 날씨 정보 저장 클래스 ---------------------------**/
    public class weather_check {
        private String icon_name;
        private String country;
        private String city_name;
        private String temp;
        private String weather;
        weather_check(String country,String city_name,String temp,String weather,String icon_name) {
            this.country = country;
            this.city_name = city_name;
            this.temp = temp;
            this.weather = weather;
            this.icon_name = icon_name;
        }

        public String getCountry() {
            return country;
        }
        public String getCity_name() {
            return city_name;
        }
        public String getTemp() {
            return temp;
        }
        public String getWeather() {
            return weather;
        }
        public String getIcon_name() {
            return icon_name;
        }
    }

}

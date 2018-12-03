package com.example.cy.cody_;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.cy.cody_.Calendar.CalendarActivity;
import com.example.cy.cody_.Closet.ClosetActivity;
import com.example.cy.cody_.Expert.ExpertActivity;
import com.example.cy.cody_.How_Cloth.How_clothActivity;
import com.example.cy.cody_.How_Cloth.SubActivity;
import com.example.cy.cody_.Login.LoginActivity;
import com.example.cy.cody_.Login.SessionManager;
import com.example.cy.cody_.Login.UserinfoActivity;
import com.example.cy.cody_.Weather.GpsInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    private com.google.api.services.calendar.Calendar mService = null;


    TextView Main_Login_Button;
    GoogleAccountCredential mCredential;
    TextView Calendar_Result;


    static final int REQUEST_ACCOUNT_PICKER = 1100;
    static final int REQUEST_AUTHORIZATION = 1101;
    static final int REQUEST_LOGIN = 1104;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1102;
    static final int PERMISSIONS_ACCOUNT = 1103;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};
    private Intent ClosetIntent;
    TextView Main_Login_button;

    ImageButton btnShowLocation;
    ImageView weather_Icons;
    TextView temp;
    TextView city;
    TextView weather;
    TextView country;
    String User_Email = null;
    String User_Name = null;
    SessionManager sessionManager;

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
        ImageButton buttonCloset = (ImageButton) findViewById(R.id.Closet);
        Main_Login_Button = (TextView) findViewById(R.id.Main_Login_button);
        ImageButton buttonFastCody = (ImageButton) findViewById(R.id.Fast_Cody);
        ImageButton buttonHowCloth = (ImageButton) findViewById(R.id.How_Cloth);
        ImageButton buttonExpert = (ImageButton) findViewById(R.id.Expert);
        Calendar_Result = (TextView) findViewById(R.id.Calendar_Result);
        temp = (TextView) findViewById(R.id.temp); // 온도
        city = (TextView) findViewById(R.id.city); // 도시 이름
        weather =(TextView) findViewById(R.id.weather); // 날씨
        country = (TextView) findViewById(R.id.country); // 나라 이니셜
        weather_Icons = (ImageView)findViewById(R.id.weather_icon); // 아이콘



        sessionManager = new SessionManager(this);   /** 세션 시작  **/
        HashMap<String, String> user = sessionManager.SessiongetUserDetail();
        User_Email = user.get(sessionManager.EMAIL);
        if(sessionManager.isLoggin() == true){  //로그인이 되어있을떄
            Main_Login_Button.setText(User_Email);
        }
        else{
            Main_Login_Button.setText("로그인");
        }


        // Google Calendar API 사용하기 위해 필요한 인증 초기화( 자격 증명 credentials, 서비스 객체 )
        // OAuth 2.0를 사용하여 구글 계정 선택 및 인증하기 위한 준비
        // /** (나도 몰랑) */
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(),
                Arrays.asList(SCOPES)
        ).setBackOff(new ExponentialBackOff()); // I/O 예외 상황을 대비해서 백오프 정책 사용


        getResultsFromApi();



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
                    Thread_Json.execute(); // Async스레드를 시작
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



        buttonCloset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLoggin() == false) {   // 로그인이 안되어 있을 때

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(" 오늘 뭐 입지?");
                    builder.setMessage("로그인이 필요합니다");
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);  // 로그인페이지로 이동시킨후
                            startActivityForResult(LoginIntent, REQUEST_LOGIN);  // onActivityResult 로 이동
                        }
                    });
                    builder.setNegativeButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(),"로그인 후 이용가능합니다",Toast.LENGTH_LONG).show();
                                }
                            });
                    builder.show();
                }
                else { // 로그인이 되어 있을 경우
                    Intent closetintent = new Intent(MainActivity.this,ClosetActivity.class);
                    startActivity(closetintent);
                }
            }
        });

        buttonFastCody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent codyintent = new Intent(MainActivity.this,Fast_codyActivity.class);
                startActivity(codyintent);
            }
        });

        buttonHowCloth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent HowClothintent = new Intent(MainActivity.this,How_clothActivity.class);
                startActivity(HowClothintent);
            }
        });

        buttonExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sessionManager.isLoggin() == true) {
                    Intent Expertintent = new Intent(MainActivity.this, ExpertActivity.class);
                    startActivity(Expertintent);
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(" 오늘 뭐 입지?");
                    builder.setMessage("로그인이 필요합니다, 로그인 하시겠습니까?");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);  // 로그인페이지로 이동시킨후
                                    startActivityForResult(LoginIntent, REQUEST_LOGIN);  // onActivityResult 로 이동
                                }
                            });
                    builder.setNegativeButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "아니오를 선택했습니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                    builder.show();
                }
            }
        });

        Main_Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sessionManager.isLoggin() == false){       /**  로그인 안되어 있을 경우  **/
                    Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(LoginIntent,REQUEST_LOGIN);
                }
                else{
                    /**  수정 화면으로  **/
                    Intent Modify_User = new Intent(MainActivity.this, UserinfoActivity.class);
                    startActivity(Modify_User);
                }

            }
        });


        Calendar_Result.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent Calendar = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(Calendar);
            }
        });

    }

    private String getResultsFromApi() {

//        if (!isGooglePlayServicesAvailable()) { // Google Play Services를 사용할 수 없는 경우
//
//            acquireGooglePlayServices();
//        }
        if (mCredential.getSelectedAccountName() == null) { // 유효한 Google 계정이 선택되어 있지 않은 경우
            chooseAccount();
        }
//        else if (!isDeviceOnline()) {    // 인터넷을 사용할 수 없는 경우
//
//            Calendar_Result.setText("No network connection available.");
//        }
        else {
            // Google Calendar API 호출
            new MakeRequestTask(this, mCredential).execute();
        }
        return null;
    }


    /** 안드로이드 디바이스에 최신 버전의 Google Play Services가 설치되어 있는지 확인 */
    private boolean isGooglePlayServicesAvailable() {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /** Google Play Services 업데이트로 해결가능하다면 사용자가 최신 버전으로 업데이트하도록 유도하기위해 대화상자를 보여줌. */
    private void acquireGooglePlayServices() {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    /** 안드로이드 디바이스가 인터넷 연결되어 있는지 확인한다. 연결되어 있다면 True 리턴, 아니면 False 리턴 */
    private boolean isDeviceOnline() {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }


    private String chooseAccount() {

        // GET_ACCOUNTS 권한을 가지고 있다면
        if (EasyPermissions.hasPermissions(this, Manifest.permission.GET_ACCOUNTS)) {
            // SharedPreferences에서 저장된 Google 계정 이름을 가져온다.
            String accountName = getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                // 선택된 구글 계정 이름으로 설정한다.
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            }
            else {
                // 사용자가 구글 계정을 선택할 수 있는 다이얼로그를 보여준다.
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
            // GET_ACCOUNTS 권한을 가지고 있지 않다면
        }
        else {
            // 사용자에게 GET_ACCOUNTS 권한을 요구하는 다이얼로그를 보여준다.(주소록 권한 요청함)
            /** 주소록 퍼미션 실행 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int ACCOUNT_Permission = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);
                if (ACCOUNT_Permission == PackageManager.PERMISSION_GRANTED) {

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, 100);
                    getResultsFromApi();
                }
            }
        }
        return null;
    }


    @Override
    protected void onActivityResult(
            int requestCode,  // onActivityResult가 호출되었을 때 요청 코드로 요청을 구분
            int resultCode,   // 요청에 대한 결과 코드
            Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_LOGIN:   // 로그인 버튼을 눌렀을 시
                    User_Email = data.getStringExtra("Email");
                    User_Name = data.getStringExtra("Name");
                    if(sessionManager.isLoggin() == true){  //로그인이 되어있을떄
                        Main_Login_Button.setText(User_Email);
                    }
                    else{
                        Main_Login_Button.setText("로그인");
                    }

                    break;
            }

        }
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();

                        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
                        SharedPreferences.Editor shared_editor = sharedPreferences.edit();
                        shared_editor.putString(PREF_ACCOUNT_NAME,accountName);
                        shared_editor.apply();

                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();

//
//                        SharedPreferences settings = getSharedPreferences("asd", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = settings.edit();
//                        editor.putString(PREF_ACCOUNT_NAME,accountName);
//                        editor.commit();
//                        mCredential.setSelectedAccountName(accountName);
//                        getResultsFromApi();
                    }
                }
                break;
        }
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, String> {

        private Exception mLastError = null;
        List<String> eventStrings = new ArrayList<String>();


        private MakeRequestTask(MainActivity activity, GoogleAccountCredential credential) {

            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            mService = new com.google.api.services.calendar.Calendar
                    .Builder(transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }


        @Override
        protected void onPreExecute() {   // 스레드 돌리기 전에 준비단계중 셋팅
        }


        /* 백그라운드에서 Google Calendar API 호출 처리 */
        @Override
        protected String doInBackground(Void... params) {
            try {
                getEvent();
            } catch (Exception e) {
                mLastError = e;    // 쓰레드 돌리는 중에 에러나면 반환해서 @override 된 onCancelled()를 호출
                cancel(true);
                return null;
            }
            return null;
        }


        private void getEvent() throws IOException {
            DateTime now = new DateTime(System.currentTimeMillis());

            Events events = mService.events().list("primary")
                    .setMaxResults(10)  // 10개만
                    .setTimeMin(now)  // 최소 now 시간부터
                    .setOrderBy("startTime")  // 오름차순 정렬
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();

            eventStrings.add(String.format("%s \n (%s)",
                    items.get(0).getSummary(),  // summary 일정 이름
                    items.get(0).getStart().getDateTime())  // start 일정 시작 시간
            );
        }


        @Override
        protected void onPostExecute(String output) {
            Calendar_Result.setText(TextUtils.join("\n\n", eventStrings));
        }


        @Override
        protected void onCancelled() {

            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                }
                else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            MainActivity.REQUEST_AUTHORIZATION);
                }
                else {
                    // 에러가 날 경우 1) 인터넷 연결 안되어 있을 경우,  2) 구글플레이스토어 없거나  3) 옛날 버전일때
                    Calendar_Result.setText("MakeRequestTask The following error occurred:\n" + mLastError.getMessage());
                }
            }
            else {
                Calendar_Result.setText("요청 취소됨.");
            }
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode
    ) {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        Dialog dialog = apiAvailability.getErrorDialog(
                MainActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES
        );
        dialog.show();
    }
/** -----------------------------------GPS 권한 주고 하는 부분  -------------------------------**/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case PERMISSIONS_ACCESS_FINE_LOCATION :
                isAccessFineLocation = true;
                break;
            case PERMISSIONS_ACCESS_COARSE_LOCATION :
                isAccessCoarseLocation = true;
                break;
            case PERMISSIONS_ACCOUNT : break;
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

    @Override
    public void onResume() {
        super.onResume();
        HashMap<String, String> user = sessionManager.SessiongetUserDetail();
        String User_Email = user.get(sessionManager.EMAIL);
        if(sessionManager.isLoggin() == true){  //로그인이 되어있을떄
            Main_Login_Button.setText(User_Email);
        }
        else{
            Main_Login_Button.setText("로그인");
        }
    }

}

package com.example.cy.cody_.Calendar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.cy.cody_.R;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


public class CalendarActivity extends AppCompatActivity {

    private com.google.api.services.calendar.Calendar mService = null;

    GoogleAccountCredential mCredential;
    MaterialCalendarView materialCalendarView;
    int Year_INT;
    List<String> Date_String;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);


        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(),
                Arrays.asList(SCOPES)
        ).setBackOff(new ExponentialBackOff()); // I/O 예외 상황을 대비해서 백오프 정책 사용


        SharedPreferences sharedPreferences = getSharedPreferences("save",MODE_PRIVATE);
        String accountName = sharedPreferences.getString(PREF_ACCOUNT_NAME, null);

        mCredential.setSelectedAccountName(accountName);  // api에 gmail 연결

        Year_INT = Calendar.getInstance().get(Calendar.YEAR);  // 당해 년도를 얻는다.

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(Year_INT,0,1))
                .setMaximumDate(CalendarDay.from(Year_INT,11,31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        new CalendarRequestTask(this,mCredential).execute();

        final TextView Result_Data = (TextView) findViewById(R.id.Result_Date);
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Current_Month = date.getMonth() + 1;  // 현재 달
                int Current_Date = date.getDay();   // 현재 월
                List<String> Send_Result_Data = new ArrayList<String>();  // 혹시 이러면 메모리 낭비가 생기니?    // 누를때마다 생성인데
                widget.setSelectionColor(Color.RED); // 컬러 설정


                for(String event : Date_String){
                    int event_Month = Integer.parseInt(event.substring(5,7));
                    int event_Date = Integer.parseInt(event.substring(8,10));

                    if(Current_Month == event_Month){
                        if(Current_Date == event_Date){  // 월과 일이 같을 경우
                            Send_Result_Data.add(event.substring(29,event.length()));
                        }
                    }

                }
                Result_Data.setText(Send_Result_Data.toString());




            }
        });


        // 1) asynctask 를 많이 써도 되냐?
        // 2) 종료되는 시점은 어딘가  .cancle(true)가 종료던데 어디에 둬야해
    }


    private class CalendarRequestTask extends AsyncTask<Void, Void, List<CalendarDay>> {

        Exception mLastError = null;

        private CalendarRequestTask(CalendarActivity activity, GoogleAccountCredential credential) {

            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            mService = new com.google.api.services.calendar.Calendar
                    .Builder(transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }


        @Override
        protected void onPreExecute() {

        }


        /** 백그라운드에서 Google Calendar API 호출 처리 */
        @Override
        protected List<CalendarDay> doInBackground(Void... params) {
            try {
                return getEvent();
            } catch (Exception e) {
                Log.v("JIN_err",e.toString());
                mLastError = e;    // 쓰레드 돌리다가 에러나면 반환해서 @override 된 onCancelled()를 호출
                cancel(true);
                return null;
            }
        }

        private List<CalendarDay> getEvent() throws IOException {

            Events events = mService.events().list("primary")
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();

            Calendar calendar = Calendar.getInstance();
            List<CalendarDay> dates = new ArrayList<>();
            Date_String = new ArrayList<String>();

            for (Event event : items) {

                DateTime start = event.getStart().getDateTime();

                int start_Year = Integer.parseInt(start.toString().substring(0,4));
                int start_month = Integer.parseInt(start.toString().substring(5,7));
                int start_date = Integer.parseInt(start.toString().substring(8,10));

                if(start_Year == Year_INT){
                    calendar.set(start_Year,start_month - 1,start_date);  //  0월부터 시작임 그래서 -1하였음
                    CalendarDay day = CalendarDay.from(calendar);
                    dates.add(day);

                    Date_String.add(String.format("%s%s",event.getStart().getDateTime(), event.getSummary()));
                }
            }
            return dates;
        }


        @Override
        protected void onPostExecute(List<CalendarDay> output) {
            materialCalendarView.addDecorator(new EventDecorator(Color.RED, output));
        }
    }

    public class EventDecorator implements DayViewDecorator {

        // private final Drawable drawable;
        private int color;
        private HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            //drawable = context.getResources().getDrawable(R.drawable.more);
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            //view.setSelectionDrawable(drawable);
            view.addSpan(new DotSpan(10, color)); // 날짜밑에 점
        }
    }


}

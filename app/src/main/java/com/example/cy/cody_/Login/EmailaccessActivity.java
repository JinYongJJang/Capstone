package com.example.cy.cody_.Login;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.cy.cody_.R;

public class EmailaccessActivity extends AppCompatActivity {

    TextView emailAuth_time_counter;
    Button emailAuth_btn;

    CountDownTimer countDownTimer;

    final int MILLISINFUTURE = 180 * 1000;//180 * 1000; //총 시간 (180초 = 3분)
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailaccess);

        countDownTimer();
    }

    public void countDownTimer() { //카운트 다운 메소드

        emailAuth_time_counter = (TextView) findViewById(R.id.emailAuth_time_counter);
        emailAuth_btn = (Button) findViewById(R.id.emailAuth_btn);

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) { //(180초에서 1초 마다 계속 줄어듬)

                long emailAuthCount = millisUntilFinished / 1000;
                Log.d("Alex", emailAuthCount + "");

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    emailAuth_time_counter.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    emailAuth_time_counter.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }
                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.
            }


            @Override
            public void onFinish() { // 시간 다 지나면 액티비티 종료
                finish();
            }

            // 시간이 다 지나갔을 때 재전송 버튼을 활성화 시켜 다시 보내게 한다
            //
        }.start();
    }
}

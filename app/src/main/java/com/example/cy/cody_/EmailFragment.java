package com.example.cy.cody_;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class EmailFragment extends Fragment {

    Button Send_btn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.emailfind, container, false);


        Send_btn = (Button) view.findViewById(R.id.Send_btn);
        Send_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent accesscode = new Intent(getActivity(), EmailaccessActivity.class);
                startActivity(accesscode);
            }
        });

        // 이메일로 찾기에서 전송 버튼을 누를시 전송코드를 적을수 있는 곳으로 이동
        // 이메일을 인텐트 인자로 넘겨서   "~~~~~~~   로 승인코드를 보냈다"
        // 확인 버튼

        return view;
    }
}

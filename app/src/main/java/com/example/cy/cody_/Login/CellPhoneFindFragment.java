package com.example.cy.cody_.Login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cy.cody_.R;

public class CellPhoneFindFragment extends Fragment {

    private Button btnTest;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cellphonefind, container, false);
//        btnTest = (Button) view.findViewById(R.id.btnTest2);
//
//        btnTest.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(),"Button2",Toast.LENGTH_SHORT).show();
//            }
//        });


        return view;
    }
}

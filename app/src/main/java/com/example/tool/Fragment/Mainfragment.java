package com.example.tool.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tool.Activity.Diet;
import com.example.tool.Activity.Exercise;
import com.example.tool.Activity.LoginActivity;
import com.example.tool.Activity.MainActivity;
import com.example.tool.Bean.Food;
import com.example.tool.R;
import com.google.gson.Gson;

public class Mainfragment extends Fragment {
    Button btn_yinshi,btn_yundong,btn_tizhong,btn_yaowei,btn_daka,btn_xiguan;
    TextView txt_yinshi,txt_yundong,txt_tizhong,txt_yaowei;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tool, container, false);

        btn_yinshi = view.findViewById(R.id.frag_tool_btn_yinshi);
        btn_yundong = view.findViewById(R.id.frag_tool_btn_yundong);
        btn_tizhong = view.findViewById(R.id.frag_tool_btn_weight);
        btn_yaowei = view.findViewById(R.id.frag_tool_btn_yaowei);
        btn_daka = view.findViewById(R.id.frag_tool_btn_meiridaka);
        btn_xiguan = view.findViewById(R.id.frag_tool_btn_xiguan);

        setView();
        return view;
    }

    private void setView(){
        btn_yinshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Diet.class);
                startActivity(intent);
            }
        });

        btn_yundong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Exercise.class);
                startActivity(intent);
            }
        });
    }

}

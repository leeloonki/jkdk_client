package com.example.tool.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tool.Activity.LoginActivity;
import com.example.tool.Activity.MainActivity;
import com.example.tool.Activity.ModifyInfo;
import com.example.tool.R;
import com.example.tool.Utils.Utils;

public class SelfFragment extends Fragment {


    Button btn_modify,btn_exit;
    TextView txt_uid,txt_gender,txt_date,txt_height,txt_weight;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        btn_modify = view.findViewById(R.id.self_btn_modifyinfo);
        btn_exit = view.findViewById(R.id.self_btn_exit);
        txt_uid = view.findViewById(R.id.frag_self_uid);
        txt_gender = view.findViewById(R.id.frag_self_gender);
        txt_date = view.findViewById(R.id.frag_self_date);
        txt_height = view.findViewById(R.id.frag_self_height);
        txt_weight = view.findViewById(R.id.frag_self_weight);

        txt_uid.setText(Utils.user.uname);
        if(Utils.user.gender==1){
            txt_gender.setText("男");
        }else{
            txt_gender.setText("女");
        }
        txt_date.setText(String.valueOf(Utils.user.birthday));
        txt_height.setText(String.valueOf(Utils.user.height));
        txt_weight.setText(String.valueOf(Utils.user.weight));
        setView();
        return view;
    }

    private void setView(){
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ModifyInfo.class);
                startActivity(intent);
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }


}

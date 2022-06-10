package com.example.tool.Activity;

import static com.example.tool.Utils.Utils.remain;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tool.Bean.Food;
import com.example.tool.Bean.Result;
import com.example.tool.Bean.User;
import com.example.tool.R;
import com.example.tool.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;

public class Diet extends AppCompatActivity {

    private Button btn_zaocan,btn_wucan,btn_wancan;
    public TextView textView,txt_shengyu;
    static String ret_data ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        initView();
        setView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 &&resultCode ==2) {
            String food_ret = data.getStringExtra("food_ret");
//            String kilo_ret = data.getStringExtra("kilo_ret");

            HashMap map = new Gson().fromJson(food_ret,HashMap.class);

            ret_data+=map.get("foodname_ret") + " "+((double)map.get("kilo_ret")/100.0) *(double)map.get("heat_ret") +"千焦 \n";
            textView.setText(ret_data);
        }
    }

    private void initView() {
        btn_wucan = findViewById(R.id.diet_btn_wucan);
        btn_zaocan = findViewById(R.id.diet_btn_zaocan);
        btn_wancan = findViewById(R.id.diet_btn_wancan);
        textView = findViewById(R.id.diet_txt_zaocan);
        txt_shengyu = findViewById(R.id.diet_shengyu);
    }


    @SuppressLint("SetTextI18n")
    private void setView() {
        MyClickListener onclick = new MyClickListener();
        btn_zaocan.setOnClickListener(onclick);
        btn_wucan.setOnClickListener(onclick);
        btn_wancan.setOnClickListener(onclick);
        sendRequestWithOkHttp();
    }
    public class MyClickListener implements View.OnClickListener {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.diet_btn_zaocan:
                    startActivityForResult(new Intent(Diet.this,ListviewActivity.class),1);
                    break;
                case R.id.diet_btn_wucan:
                case R.id.diet_btn_wancan:
                    ChooseFood();
                    break;
                default:
                    break;
            }
//            ChooseFood();
        }
    }
    public void ChooseFood(){

    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject obj = new JSONObject();
                MediaType type = MediaType.parse("application/json;charset=utf-8");
                RequestBody Request = RequestBody.create(type, "" + obj.toString());
                try {
                    OkHttpClient client = new OkHttpClient();
                    okhttp3.Request request = new Request.Builder()
                            // 指定访问的服务器地址
                            .url(Utils.URL + "/diet/query").post(Request)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void parseJSONWithJSONObject(String jsonData) {

        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<Result<User>>() {}.getType();
        final Result<User> loginResp = gson.fromJson(jsonData, type);
        String jsonInString = gson.toJson(loginResp);
        System.out.println(jsonInString);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(loginResp.code==200){
                }else{
                }
            }
        });
    }
}
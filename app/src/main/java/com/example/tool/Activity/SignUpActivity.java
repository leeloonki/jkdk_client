package com.example.tool.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.tool.Bean.Result;
import com.example.tool.Bean.User;
import com.example.tool.R;
import com.example.tool.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SignUpActivity extends AppCompatActivity {

    private Button btn_signup,btn_ret;
    private EditText edt_username,edt_password,edt_date,edt_height,edt_weight;
    private RadioButton rd_female;

    private String username,password,birthday;
    private int gender,height,weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        setView();
    }
    private void initView() {
        edt_username = findViewById(R.id.signup_edt_username);
        edt_password = findViewById(R.id.signup_edt_password);
        rd_female = findViewById(R.id.signup_rd_female);
        edt_date = findViewById(R.id.signup_edt_birthday);
        edt_height = findViewById(R.id.signup_edt_height);
        edt_weight = findViewById(R.id.signup_edt_weight);

        btn_signup = findViewById(R.id.signup_btn_signup);
        btn_ret = findViewById(R.id.signup_btn_ret);
    }

    private void setView() {
        MyClickListener onclick = new MyClickListener();
        btn_ret.setOnClickListener(onclick);
        btn_signup.setOnClickListener(onclick);
    }
    public class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.signup_btn_signup:
                    Signup();
                    break;
                case R.id.signup_btn_ret:
                    Ret();
                    break;
                default:
                    break;
            }
        }

    }

    private void Signup(){
        username = edt_username.getText().toString().trim();
        password = edt_password.getText().toString().trim();
        birthday = edt_date.getText().toString().trim();
        height = Integer.parseInt(edt_height.getText().toString().trim());
        weight = Integer.parseInt(edt_weight.getText().toString().trim());
        if(rd_female.isChecked()){
            gender =0;
        }else{
            gender =1;
        }
        sendRequestWithOkHttp();
    }
    private void Ret(){
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("uname", username);
                    obj.put("passwd", password);
                    obj.put("gender",gender);
                    obj.put("birthday",birthday);
                    obj.put("height",height);
                    obj.put("weight",weight);
                    System.out.println(obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MediaType type = MediaType.parse("application/json;charset=utf-8");
                RequestBody Request = RequestBody.create(type, "" + obj.toString());
                try {
                    OkHttpClient client = new OkHttpClient();
                    okhttp3.Request request = new Request.Builder()
                            // 指定访问的服务器地址
                            .url(Utils.URL + "/user/signup").post(Request)
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
                    Toast.makeText(SignUpActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(SignUpActivity.this,"用户名已存在",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

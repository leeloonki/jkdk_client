package com.example.tool.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tool.Bean.Result;
import com.example.tool.Bean.User;
import com.example.tool.Fragment.Mainfragment;
import com.example.tool.Fragment.SelfFragment;
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

public class ModifyInfo extends AppCompatActivity {

    Button btn_modify,btn_ret;
    TextView txt_uname;
    EditText edt_passwd,edt_date,edt_height,edt_weight;
    String passwd,date;
    Integer height,weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        initView();
        setView();
    }

    private void initView() {
        txt_uname = findViewById(R.id.modify_txt_uname);
        btn_modify = findViewById(R.id.modify_btn_modify);
        btn_ret = findViewById(R.id.modify_btn_ret);
        edt_passwd = findViewById(R.id.modify_edt_passwd);
        edt_date = findViewById(R.id.modify_edt_date);
        edt_height = findViewById(R.id.modify_edt_height);
        edt_weight = findViewById(R.id.modify_edt_weight);
    }

    private void setView() {
        txt_uname.setText(Utils.user.uname);
        btn_ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModifyInfo.this,MainActivity.class);
                intent.putExtra("index",1); //设置标志
                startActivity(intent);
            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyInfo();
            }
        });
    }
    private  void modifyInfo(){
        passwd = edt_passwd.getText().toString().trim();
        date = edt_date.getText().toString().trim();
        height = Integer.parseInt(edt_height.getText().toString());
        weight = Integer.parseInt(edt_weight.getText().toString());
        sendRequestWithOkHttp();
    }
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("uname", Utils.user.uname);
                    obj.put("passwd", passwd);
                    obj.put("gender",Utils.user.gender);
                    obj.put("birthday",date);
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
                            .url(Utils.URL + "/user/modify").post(Request)
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
        final Result<User> userResult = gson.fromJson(jsonData, type);
        Utils.user=userResult.data;
//        System.out.println(Utils.user.toString());
        String jsonInString = gson.toJson(userResult);
        System.out.println(jsonInString);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(userResult.code==200) {
                    Utils.user = userResult.data;
                    Toast.makeText(ModifyInfo.this, "修改成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ModifyInfo.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ModifyInfo.this,"修改失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
package com.example.tool.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.util.LocaleData;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tool.Bean.Food;
import com.example.tool.Bean.Result;
import com.example.tool.Fragment.Mainfragment;
import com.example.tool.R;
import com.example.tool.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ListviewActivity extends AppCompatActivity {

    Button btn_queding;
    EditText edt_zhongliang;
    TextView txt_selected;
    ListView listView;
    Result<Diet> dietResult;
    Result<List<Food>> RestfoodList;
    //    下拉单 适配器ArrayList
    List<String> ListStr = new ArrayList<>();
    int POS;
    //    数据
    List<Food> AllFood;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        btn_queding = findViewById(R.id.listview_btn_queding);
        edt_zhongliang = findViewById(R.id.listview_edt_zhongliang);
        txt_selected = findViewById(R.id.listview_selected);
        listView = findViewById(R.id.view_list);
        sendRequestWithOkHttp();
        setView();
    }

    private void setView() {

        //        为item点击事件绑定
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                for(int i=0;i<adapterView.getCount();i++){
                    View v=adapterView.getChildAt(i);
                    if (pos== i) {
                        POS =pos;
                        Food food = AllFood.get(POS);
                        System.out.println("已选item序号为："+POS);
                        Utils.food = food;
//                        Intent intent = new Intent(ListviewActivity.this, Diet.class);
//                        startActivity(intent);
                    }
                }
                txt_selected.setText(Utils.food.getFoodName());
            }
        });

        btn_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestWithOkHttp1();
                System.out.println("网络请求/diet/insert");
                JSONObject obj = new JSONObject();
                int kilo_ret = Integer.parseInt(edt_zhongliang.getText().toString().trim());
                int heat_ret = Utils.food.getFoodHeat();
                String foodname_ret = Utils.food.getFoodName();
                try {
                    obj.put("kilo_ret",kilo_ret);
                    obj.put("heat_ret",heat_ret);
                    obj.put("foodname_ret",foodname_ret);
                    System.out.println(obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                setResult(2,new Intent().putExtra("kilo_ret",Utils.food.getFoodName() + " "+ edt_zhongliang.getText().toString().trim()+"克"));
                setResult(2,new Intent().putExtra("food_ret", obj.toString()));
                finish();
            }
        });
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
                            .url(Utils.URL + "/food/query").post(Request)
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
        java.lang.reflect.Type type = new TypeToken<Result<List<Food>>>() {}.getType();
        RestfoodList = gson.fromJson(jsonData, type);
        String jsonInString = gson.toJson(RestfoodList);
        System.out.println(jsonInString);
        runOnUiThread(() -> {
            AllFood = RestfoodList.data;
            //第一步：定义下拉列表内容
            int i =0;
            for(Food food:AllFood){
                i++;
                ListStr.add(food.getFoodName()+"  "+ food.getFoodHeat()+"千焦/100g");
                adapter = new ArrayAdapter<String>(ListviewActivity.this, android.R.layout. simple_list_item_1, ListStr);
                //6将适配器和布局管理器加载到控件当中
                listView.setAdapter(adapter);
            }
        });
    }

    private void sendRequestWithOkHttp1() {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                JSONObject obj = new JSONObject();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
                String date = LocalDate.now().format(dtf);// 2021-01-29 十位转八位 ==> 20210129
                try {
                    obj.put("uid", Utils.user.uid);
                    obj.put("bld",1);
                    obj.put("fid", Utils.food.fid);
                    obj.put("ttime",date);
                    obj.put("heatConsume", Utils.food.getFoodHeat()*Integer.parseInt(edt_zhongliang.getText().toString().trim()));
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
                            .url(Utils.URL + "/diet/insert").post(Request)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject1(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithJSONObject1(String jsonData) {

        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<Result<Diet>>() {}.getType();
        dietResult = gson.fromJson(jsonData, type);
        String jsonInString = gson.toJson(dietResult);
        System.out.println(jsonInString);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(dietResult.code==200){
                    Toast.makeText(ListviewActivity.this,dietResult.msg,Toast.LENGTH_SHORT);
                }else{
                    Toast.makeText(ListviewActivity.this,dietResult.msg,Toast.LENGTH_SHORT);
                }
            }
        });
    }

}
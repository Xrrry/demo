package com.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;

public class SecondController {

    String theQuery(String out_id){
        Gson gson = new Gson();
        String result = transformJson(getJsonData("http://112.126.96.134:8888/test/queryOut",out_id));
        String[] strings = gson.fromJson(transformList(result), String[].class);
        return gson.toJson(strings,String[].class);
    }

    public String transformJson(String s){
        String string = s.substring(6);
        string = string.replace("{","{\"").replace("}","\"}");
        string = string.replaceAll("=","\":\"").replaceAll(", ","\",\"");
        return string;
    }
    public String transformList(String s){
        String a;
        a = "\":\"";
        return s.replace("{","[").replace("}","]").replaceAll(a,":");
    }

    public static String getJsonData(String urlStr,String out_id) {
        String data = "";
        RequestBody formBody = new FormBody.Builder()
                .add("out_id",out_id)
                .build();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlStr)
                .post(formBody)
                .build();
        Call call = client.newCall(request);

        try {
            Response response = call.execute();
            data = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}

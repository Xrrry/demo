package com.controller;

import com.bean.*;
import com.google.common.reflect.TypeToken;
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
import java.util.List;

public class SecondController {

    String theQuery(String out_id){
        Gson gson = new Gson();
        RequestBody outFormBody = new FormBody.Builder()
                .add("out_id",out_id)
                .build();
        String outResult = transformJson(getJsonData("http://112.126.96.134:8888/test/queryOut",outFormBody));
        Out out = gson.fromJson(outResult, Out.class);
        RequestBody commodityFormBody = new FormBody.Builder()
                .add("pro_acc",out.getPro_acc())
                .add("com_id",out.getCom_id())
                .build();
        RequestBody producerFormBody = new FormBody.Builder()
                .add("pro_acc",out.getPro_acc())
                .build();
        RequestBody sellFormBody = new FormBody.Builder()
                .add("sell_id",out.getOut_id())
                .add("sell_time","1590291662")
                .build();
        String commodityResult = transformJson(getJsonData("http://112.126.96.134:8888/test/queryCommodity",commodityFormBody));
        Commodity commodity = gson.fromJson(commodityResult, Commodity.class);
        String producerResult = transformJson(getJsonData("http://112.126.96.134:8888/test/queryProducer",producerFormBody));
        Producer producer = gson.fromJson(producerResult, Producer.class);
        String sellResult = transformJson(getJsonData("http://112.126.96.134:8888/test/querySell",sellFormBody));
        Sell sell = gson.fromJson(sellResult, Sell.class);
        SearchDetail searchDetail = new SearchDetail(out.getOut_id(), out.getCom_id(), out.getPro_acc(),
                out.getOut_birthday(), producer.getNickname(), commodity.getCom_name(), commodity.getCom_cate(),
                commodity.getCom_price(),sell.getSell_id(),sell.getSell_sal_acc(),sell.getSell_cus_acc(),sell.getSell_track_num());
        System.out.println(searchDetail);
        return searchDetail.toString();
    }

    public String transformJson(String s){
        String string = s.substring(6);
        string = string.replace("{","{\"").replace("}","\"}");
        string = string.replaceAll("=","\":\"").replaceAll(", ","\",\"");
        return string;
    }

    public static String getJsonData(String urlStr,RequestBody formBody) {
        String data = "";

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

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
import java.util.ArrayList;
import java.util.List;

public class SecondController {

    public String query(String out_id){
        Gson gson = new Gson();
        RequestBody outFormBody = new FormBody.Builder()
                .add("out_id",out_id)
                .build();
        String outResult = transformJson(getJsonData("http://112.126.96.134:8888/test/queryOut",outFormBody));
        Out out = gson.fromJson(outResult, Out.class);
        System.out.println(out.toString());
        RequestBody commodityFormBody = new FormBody.Builder()
                .add("pro_acc",out.getPro_acc())
                .add("com_id",out.getCom_id())
                .build();
        RequestBody producerFormBody = new FormBody.Builder()
                .add("pro_acc",out.getPro_acc())
                .build();
        RequestBody sellAllFormBody = new FormBody.Builder()
                .add("sell_id",out_id)
                .build();
        String commodityResult = transformJson(getJsonData("http://112.126.96.134:8888/test/queryCommodity",commodityFormBody));
        Commodity commodity = gson.fromJson(commodityResult, Commodity.class);
        System.out.println(commodity.toString());
        String producerResult = transformJson(getJsonData("http://112.126.96.134:8888/test/queryProducer",producerFormBody));
        Producer producer = gson.fromJson(producerResult, Producer.class);
        System.out.println(producer.toString());
        String sellAllResult = getJsonData("http://112.126.96.134:8888/test/queryAllSell",sellAllFormBody);
        System.out.println(sellAllResult);
        List<String> ss = sellAlltransform(sellAllResult);
        System.out.println(ss);
        RequestBody sellFormBody = new FormBody.Builder()
                .add("sell_id",out.getOut_id())
                .add("sell_time",ss.get(0)) //应该为size-1
                .build();
        String sellResult = transformJson(getJsonData("http://112.126.96.134:8888/test/querySell",sellFormBody));
        Sell sell = gson.fromJson(sellResult, Sell.class);
        System.out.println(sell.toString());
        RequestBody salerFormBody = new FormBody.Builder()
                .add("sal_acc",sell.getSell_sal_acc())
                .build();
        RequestBody customerFormBody = new FormBody.Builder()
                .add("cus_acc",sell.getSell_cus_acc())
                .build();
        String salerResult = transformJson(getJsonData("http://112.126.96.134:8888/test/querySaler",salerFormBody));
        Saler saler = gson.fromJson(salerResult,Saler.class);
        System.out.println(saler.toString());
        String customerResult = transformJson(getJsonData("http://112.126.96.134:8888/test/queryCustomer",customerFormBody));
        Customer customer = gson.fromJson(customerResult,Customer.class);
        System.out.println(customer.toString());
        SearchDetail searchDetail = new SearchDetail(out.getOut_id(), out.getCom_id(), out.getPro_acc(),
                out.getOut_birthday(), producer.getNickname(), commodity.getCom_name(), commodity.getCom_cate(),
                commodity.getCom_price(),commodity.getCom_place(),ss.size(),ss,sell.getSell_id(),sell.getSell_sal_acc(),saler.getSal_nickname(),
                saler.getSal_cred(),saler.getSal_cnt(),sell.getSell_time(),
                sell.getSell_cus_acc(),customer.getCus_nickname(),sell.getSell_track_num());
        System.out.println(searchDetail);
        return gson.toJson(searchDetail);
    }

    String perAllHistory(String cus_acc) {
        RequestBody hisFormBody = new FormBody.Builder()
                .add("cus_acc",cus_acc)
                .build();
        return transformJson(getJsonData("http://112.126.96.134:8888/test/queryPerAllHistory",hisFormBody));
    }
    String proAllCom(String pro_acc) {
        RequestBody comFormBody = new FormBody.Builder()
                .add("pro_acc",pro_acc)
                .build();
        return transformJson(getJsonData("http://112.126.96.134:8888/test/queryAllCommodity",comFormBody));
    }
    String upOut(String out_id,String pro_acc, String com_id, String out_time) {
        RequestBody outFormBody = new FormBody.Builder()
                .add("out_id",out_id)
                .add("pro_acc",pro_acc)
                .add("com_id",com_id)
                .add("out_birthday",out_time)
                .build();
        return getJsonData("http://112.126.96.134:8888/test/upOut",outFormBody);
    }

    String loginSearch(String phone) {
        RequestBody loginFormBody = new FormBody.Builder()
                .add("user_id",phone)
                .build();
        return transformJson(getJsonData("http://112.126.96.134:8888/test/queryUser",loginFormBody));
    }
    String auth(String user_id,String type,String content) {
        RequestBody authFormBody = new FormBody.Builder()
                .add("user_id",user_id)
                .add("user_type",type)
                .build();
        return getJsonData("http://112.126.96.134:8888/test/upTempUser",authFormBody);
    }

    String upCom(String pro_acc, String name, String type, String price, String locate) {
        Gson gson = new Gson();
        RequestBody countFormBody = new FormBody.Builder()
                .add("typename","count")
                .build();
        String countResult = transformJson(getJsonData("http://112.126.96.134:8888/test/queryCount",countFormBody));
        Count count = gson.fromJson(countResult,Count.class);
        RequestBody upFormBody = new FormBody.Builder()
                .add("pro_acc",pro_acc)
                .add("com_id",count.getCount())
                .add("com_name",name)
                .add("com_cate",type)
                .add("com_price",price)
                .add("com_place",locate)
                .build();
        RequestBody updateCountFormBody = new FormBody.Builder()
                .add("typename","count")
                .add("value",String.valueOf(Integer.parseInt(count.getCount())+1))
                .build();
        getJsonData("http://112.126.96.134:8888/test/updateCount",updateCountFormBody);
        return getJsonData("http://112.126.96.134:8888/test/upCommodity",upFormBody);
    }
    public List<String> sellAlltransform(String s){
        int count = Integer.parseInt(s.substring(6,7));
        String[] sList = s.split("value3=");
        List<String> sResult = new ArrayList<String>();
        for (int i=1;i<sList.length;i++) {
            sResult.add(sList[i].substring(0,10));
        }
        System.out.println(sResult);
        return sResult;
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

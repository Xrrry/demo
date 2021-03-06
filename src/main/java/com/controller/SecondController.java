package com.controller;

import com.bean.*;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.swagger.models.auth.In;
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
import java.sql.SQLOutput;
import java.util.*;
import java.util.Base64;
import java.io.UnsupportedEncodingException;

public class SecondController {

    public String test(String str) {
        try {
            return Base64.getEncoder().encodeToString(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String query(String out_id, String user_id, String type){
        Gson gson = new Gson();
        RequestBody outFormBody = new FormBody.Builder()
                .add("out_id",out_id)
                .build();
        String outTResult = getJsonData("http://112.126.96.134:8888/test/queryOut",outFormBody);
        if (outTResult.equals("不存在")) {
            return "不存在";
        }
        String outResult = transformJson(outTResult);
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
        String hisSell = hisSellList(ss, out_id);
//        RequestBody sellFormBody = new FormBody.Builder()
//                .add("sell_id",out.getOut_id())
//                .add("sell_time",ss.get(0)) //应该为size-1
//                .build();
//        String sellResult = transformJson(getJsonData("http://112.126.96.134:8888/test/querySell",sellFormBody));
//        Sell sell = gson.fromJson(sellResult, Sell.class);
//        RequestBody salerFormBody = new FormBody.Builder()
//                .add("sal_acc",sell.getSell_sal_acc())
//                .build();
//        RequestBody customerFormBody = new FormBody.Builder()
//                .add("cus_acc",sell.getSell_cus_acc())
//                .build();
        RequestBody hisQueryFormBody = new FormBody.Builder()
                .add("his_out_id",out_id)
                .build();
        String hisQueryResult = getJsonData("http://112.126.96.134:8888/test/queryAllHistory",hisQueryFormBody);
//        String salerResult = transformJson(getJsonData("http://112.126.96.134:8888/test/querySaler",salerFormBody));
//        Saler saler = gson.fromJson(salerResult,Saler.class);
//        System.out.println(saler.toString());
//        String customerResult = transformJson(getJsonData("http://112.126.96.134:8888/test/queryCustomer",customerFormBody));
//        Customer customer = gson.fromJson(customerResult,Customer.class);
//        System.out.println(customer.toString());
        String hisQuery = "";
        if (hisQueryResult.equals("count=0")) {
            hisQuery = "count=0";
        }
        else{
            hisQuery = hisQueryList(hisQueryResult);
        }
        SearchDetail searchDetail = new SearchDetail(out.getOut_id(), out.getCom_id(), out.getPro_acc(),
                out.getOut_birthday(), producer.getPro_nickname(), commodity.getCom_name(), commodity.getCom_cate(),
                commodity.getCom_price(),commodity.getCom_place(),hisSell,hisQuery);
        System.out.println(searchDetail);
        if(type.equals("1")){
            System.out.println("uphistory");
            queryElse(searchDetail, user_id);
        }
        else {
            System.out.println("nouphistory");
        }
        return gson.toJson(searchDetail);
    }
    void queryElse(SearchDetail searchDetail, String user_id) {
        Long timeStamp = System.currentTimeMillis();  //获取当前时间戳
        String time = String.valueOf(timeStamp);
        time = time.substring(0,10);
        RequestBody upHistoryFormBody = new FormBody.Builder()
                .add("his_out_id",searchDetail.getOut_id())
                .add("his_time",time)
                .add("his_cus_acc",user_id)
                .build();
        String upHistoryResult = getJsonData("http://112.126.96.134:8888/test/upHistory",upHistoryFormBody);
        System.out.println(upHistoryResult);
        RequestBody upPerHistoryFormBody = new FormBody.Builder()
                .add("cus_acc",user_id)
                .add("his_time",time)
                .add("out_id",searchDetail.getOut_id())
                .add("com_name",searchDetail.getCom_name())
                .build();
        String upPerHistoryResult = getJsonData("http://112.126.96.134:8888/test/upPerHistory",upPerHistoryFormBody);
        System.out.println(upPerHistoryResult);
    }

    String perAllHistory(String cus_acc) {
        RequestBody hisFormBody = new FormBody.Builder()
                .add("cus_acc",cus_acc)
                .build();
        return transformJson(getJsonData("http://112.126.96.134:8888/test/queryPerAllHistory",hisFormBody));
    }
    String sellAllHistory(String sal_acc) {
        RequestBody hisFormBody = new FormBody.Builder()
                .add("sal_acc",sal_acc)
                .build();
        return transformJson(getJsonData("http://112.126.96.134:8888/test/queryAllSellHistory",hisFormBody));
    }
    String outAllHistory(String pro_acc) {
        RequestBody hisFormBody = new FormBody.Builder()
                .add("pro_acc",pro_acc)
                .build();
        return transformJson(getJsonData("http://112.126.96.134:8888/test/queryAllOutHistory",hisFormBody));
    }

    String proAllCom(String pro_acc) {
        RequestBody comFormBody = new FormBody.Builder()
                .add("pro_acc",pro_acc)
                .build();
        return transformJson(getJsonData("http://112.126.96.134:8888/test/queryAllCommodity",comFormBody));
    }
    String upOut(String out_id,String pro_acc, String com_id, String out_time, String com_name) {
        RequestBody outFormBody = new FormBody.Builder()
                .add("out_id",out_id)
                .add("pro_acc",pro_acc)
                .add("com_id",com_id)
                .add("out_birthday",out_time)
                .build();
        RequestBody perFormBody = new FormBody.Builder()
                .add("pro_acc",pro_acc)
                .add("his_time",out_time)
                .add("out_id",out_id)
                .add("com_name",com_name)
                .build();
        String result1 = getJsonData("http://112.126.96.134:8888/test/upOut",outFormBody);
        System.out.println(result1);
        String result2 = getJsonData("http://112.126.96.134:8888/test/upOutHistory",perFormBody);
        System.out.println(result2);
        return result2;
    }
    String upSell(String sell_id, String sell_time, String sell_sal_acc, String sell_cus_acc,
                  String sell_track_num) {
        Gson gson = new Gson();
        RequestBody sellFormBody = new FormBody.Builder()
                .add("sell_id",sell_id)
                .add("sell_time",sell_time)
                .add("sell_sal_acc",sell_sal_acc)
                .add("sell_cus_acc",sell_cus_acc)
                .add("sell_track_num",sell_track_num)
                .build();
        String result = getJsonData("http://112.126.96.134:8888/test/upSell",sellFormBody);
        System.out.println(result);
        RequestBody queryOutFormBody = new FormBody.Builder()
                .add("out_id",sell_id)
                .build();
        String outResult = transformJson(getJsonData("http://112.126.96.134:8888/test/queryOut",queryOutFormBody));
        Out out = gson.fromJson(outResult,Out.class);
        RequestBody querycommFormBody = new FormBody.Builder()
                .add("pro_acc",out.getPro_acc())
                .add("com_id",out.getCom_id())
                .build();
        String commodityResult = transformJson(getJsonData("http://112.126.96.134:8888/test/queryCommodity",querycommFormBody));
        Commodity commodity = gson.fromJson(commodityResult,Commodity.class);
        RequestBody perFormBody = new FormBody.Builder()
                .add("sal_acc",sell_sal_acc)
                .add("his_time",sell_time)
                .add("out_id",sell_id)
                .add("com_name",commodity.getCom_name())
                .build();
        String result2 = getJsonData("http://112.126.96.134:8888/test/upSellHistory",perFormBody);
        System.out.println(result2);
        return result2;
    }
    String rank(){
        RequestBody rankFormBody = new FormBody.Builder().build();
        Gson gson = new Gson();
        List<String> sList = listTransform(getJsonData("http://112.126.96.134:8888/test/queryAllSaler",rankFormBody));
        List<Saler> salerList = new ArrayList<Saler>();
        for (String item : sList) {
            salerList.add(gson.fromJson(item,Saler.class));
        }
        salerList.sort(Comparator.comparingInt(x -> Integer.parseInt(x.getSal_cred())));
        Collections.reverse(salerList);
        System.out.println(salerList.toString());
        List<String> finalList = new ArrayList<String>();
        for(Saler item:salerList) {
            finalList.add(gson.toJson(item));
        }
        System.out.println(finalList);
        return gson.toJson(finalList);
    }
    String hisSellList(List<String> tList, String sell_id) {
        Gson gson = new Gson();
        HisSell hisSell = new HisSell();
        List<HisSellitem> hisList = new ArrayList<>();
        for(String a : tList) {
            RequestBody sellFormBody = new FormBody.Builder()
                    .add("sell_id",sell_id)
                    .add("sell_time",a)
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
            String customerResult = transformJson(getJsonData("http://112.126.96.134:8888/test/queryCustomer",customerFormBody));
            Customer customer = gson.fromJson(customerResult,Customer.class);
            HisSellitem hisSellitem = new HisSellitem(sell.getId(),sell.getSell_id(),sell.getSell_time(),sell.getSell_sal_acc(),
                    saler.getSal_nickname(),saler.getSal_cred(),sell.getSell_cus_acc(),customer.getCus_nickname(),sell.getSell_track_num(),
                    saler.getSal_cnt(),saler.getSal_total());
            hisList.add(hisSellitem);
        }
        hisSell.setHisSellList(hisList);
        System.out.println(hisSell.toString());
        String trans = gson.toJson(hisSell);
        System.out.println(trans);
//        HisSell hissell1 = gson.fromJson(trans, HisSell.class);
        return trans;
    }
    String hisQueryList(String s) {
        Gson gson = new Gson();
        HisQuery hisQuery = new HisQuery();
        List<HisQueryitem> hisList = new ArrayList<>();
        List<String> ll = listTransform(s);
        for (String item : ll) {
            hisList.add(gson.fromJson(item, HisQueryitem.class));
        }
        hisQuery.setHisQueryList(hisList);
        System.out.println(hisQuery.toString());
        String trans = gson.toJson(hisQuery);
        return trans;
    }

    String loginSearch(String phone) {
        RequestBody loginFormBody = new FormBody.Builder()
                .add("user_id",phone)
                .build();
        return getJsonData("http://112.126.96.134:8888/test/queryUser",loginFormBody);
    }
    String auth(String user_id,String type,String content) {
        RequestBody authFormBody = new FormBody.Builder()
                .add("user_id",user_id)
                .add("user_type",type)
                .add("certify",content)
                .build();
        return getJsonData("http://112.126.96.134:8888/test/upTempUser",authFormBody);
    }
    String feedback(String user_id, String content) {
        RequestBody feedbackFormBody = new FormBody.Builder()
                .add("user_id",user_id)
                .add("content",content)
                .build();
        return getJsonData("http://112.126.96.134:8888/test/upContent",feedbackFormBody);
    }
    String comment(String sal_acc, String sal_nickname, String sal_cred, String sal_cnt, String sal_total) {
        RequestBody upFormBody = new FormBody.Builder()
                .add("sal_acc",sal_acc)
                .add("sal_nickname",sal_nickname)
                .add("sal_cred", sal_cred)
                .add("sal_cnt", sal_cnt)
                .add("sal_total",sal_total)
                .build();
        return getJsonData("http://112.126.96.134:8888/test/updateSaler",upFormBody);
    }
    public String newcus(String acc, String name) {
        RequestBody upCusFormBody = new FormBody.Builder()
                .add("cus_acc",acc)
                .add("cus_nickname",name)
                .build();
        RequestBody upUserFormBody = new FormBody.Builder()
                .add("user_id",acc)
                .add("user_type","1")
                .build();
        System.out.println(getJsonData("http://112.126.96.134:8888/test/upUser",upUserFormBody));
        return getJsonData("http://112.126.96.134:8888/test/upCustomer",upCusFormBody);
    }
    public String searchname(String phone, String type){
        Gson gson = new Gson();
        if(type.equals("1")) {
            RequestBody searchFormBody = new FormBody.Builder()
                    .add("cus_acc",phone)
                    .build();
            String r = transformJson(getJsonData("http://112.126.96.134:8888/test/queryCustomer",searchFormBody));
            Customer customer = gson.fromJson(r, Customer.class);
            return customer.getCus_nickname();
        }
        else if (type.equals("2")) {
            RequestBody searchFormBody = new FormBody.Builder()
                    .add("sal_acc",phone)
                    .build();
            String r = transformJson(getJsonData("http://112.126.96.134:8888/test/querySaler",searchFormBody));
            Saler saler = gson.fromJson(r, Saler.class);
            return saler.getSal_nickname();
        }
        else {
            RequestBody searchFormBody = new FormBody.Builder()
                    .add("pro_acc",phone)
                    .build();
            String r = transformJson(getJsonData("http://112.126.96.134:8888/test/queryProducer",searchFormBody));
            Producer producer = gson.fromJson(r, Producer.class);
            return producer.getPro_nickname();
        }
    }
    String modi(String type, String user_id, String nickname) {
        if(type.equals("1")) {
            RequestBody modiFormBody = new FormBody.Builder()
                    .add("cus_acc",user_id)
                    .add("cus_nickname",nickname)
                    .build();
            return getJsonData("http://112.126.96.134:8888/test/updateCustomer",modiFormBody);
        }
        else if(type.equals("2")) {
            RequestBody modiFormBody = new FormBody.Builder()
                    .add("pro_acc",user_id)
                    .add("pro_nickname",nickname)
                    .build();
            return getJsonData("http://112.126.96.134:8888/test/updateProducer",modiFormBody);
        }
        else {
            RequestBody salFormBody = new FormBody.Builder()
                    .add("sal_acc",user_id)
                    .build();
            Gson gson = new Gson();
            String s = transformJson(getJsonData("http://112.126.96.134:8888/test/querySaler",salFormBody));
            Saler saler = gson.fromJson(s, Saler.class);
            RequestBody modiFormBody = new FormBody.Builder()
                    .add("sal_acc",user_id)
                    .add("sal_nickname",nickname)
                    .add("sal_cred",saler.getSal_cred())
                    .add("sal_cnt",saler.getSal_cnt())
                    .build();
            return getJsonData("http://112.126.96.134:8888/test/updateSaler",modiFormBody);
        }
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
        String[] sList = s.split("value3=");
        List<String> sResult = new ArrayList<String>();
        for (int i=1;i<sList.length;i++) {
            sResult.add(sList[i].substring(0,10));
        }
        System.out.println(sResult);
        return sResult;
    }
    public List<String> listTransform(String s) {
        String ss = s.substring(8);
        String[] sList = ss.split("}");
        List<String> sResult = new ArrayList<String>();
        for (String item : sList) {
            sResult.add(transformJson(item + "}"));
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

package com.bean;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Commodity {
    @SerializedName("value1")
    private String id;
    @SerializedName("value2")
    private String pro_acc;
    @SerializedName("value3")
    private String com_id;
    @SerializedName("value4")
    private String com_name;
    @SerializedName("value5")
    private String com_cate;
    @SerializedName("value6")
    private String com_price;
    @SerializedName("value7")
    private String com_place;
}

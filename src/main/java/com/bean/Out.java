package com.bean;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Out {
    @SerializedName("value1")
    private String id;
    @SerializedName("value2")
    private String out_id;
    @SerializedName("value3")
    private String pro_acc;
    @SerializedName("value4")
    private String com_id;
    @SerializedName("value5")
    private String out_birthday;
}

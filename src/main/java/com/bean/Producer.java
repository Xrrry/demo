package com.bean;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Producer {
    @SerializedName("value1")
    private String id;
    @SerializedName("value2")
    private String pro_acc;
    @SerializedName("value3")
    private String pro_nickname;
}

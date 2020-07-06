package com.bean;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Customer {
    @SerializedName("value1")
    private String id;
    @SerializedName("value2")
    private String cus_acc;
    @SerializedName("value3")
    private String cus_nickname;
}

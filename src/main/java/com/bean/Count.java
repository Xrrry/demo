package com.bean;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Count {
    @SerializedName("value1")
    private String id;
    @SerializedName("value2")
    private String value;
    @SerializedName("value3")
    private String count;
}

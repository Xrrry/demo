package com.bean;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class HisQuery {
    private List<HisQueryitem> hisQueryList;
}

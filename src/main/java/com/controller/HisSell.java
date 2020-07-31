package com.controller;

import com.bean.Sell;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class HisSell {
    private List<Sell> hisSellList;
}

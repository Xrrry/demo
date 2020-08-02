package com.controller;

import com.bean.HisSellitem;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class HisSell {
    private List<HisSellitem> hisSellList;
}

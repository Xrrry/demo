package com.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class SearchDetail {
    private String out_id; //唯一标识
    private String com_id; //模板标识
    private String pro_acc; //厂商id
    private String out_birthday; //出厂时间
    private String pro_nickname; //厂商昵称
    private String com_name; //商品名
    private String com_cate; //商品类型
    private String com_price; //商品价格
    private String sell_id; //卖出信息id
    private String sell_sal_acc; //卖家id
    private String sell_cus_acc; //买家id
    private String sell_track_num; //快递号
}

package com.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {
    private Integer id;
    private String userName;
    private String password;
    private String name;
    private String age;
    private String sex;
}

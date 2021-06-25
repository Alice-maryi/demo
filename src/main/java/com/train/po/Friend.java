package com.train.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: Y.C
 * @Date: 2021/6/7 1:28 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friend<T> {
    @JsonIgnore
    private String name;

    @JsonProperty("Age")
    private int age;

    @JsonProperty("oneMate")
    private T classMate;

    @JsonFormat(pattern = "yyyy年MM月dd日")
    private Date date;
}

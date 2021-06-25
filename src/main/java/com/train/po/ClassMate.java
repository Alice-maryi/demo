package com.train.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Y.C
 * @Date: 2021/6/7 2:01 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonIgnoreProperties({"name","age"})
public class ClassMate {
    private String name;
    private int age;
}

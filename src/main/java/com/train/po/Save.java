package com.train.po;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Y.C
 * @Date: 2021/6/8 2:23 下午
 */

@Data
public class Save<T> {

    private T data;

    private HashMap mapData;

    private Date createTime;

    private int id;

    private int size;
}

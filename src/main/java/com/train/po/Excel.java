package com.train.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Y.C
 * @Date: 2021/6/22 5:41 下午
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Excel {

    private String name;
    private Integer gender;
    private Integer age;
    private Date date;
    private BigDecimal height;

}

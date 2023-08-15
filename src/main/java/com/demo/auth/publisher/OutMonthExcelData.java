package com.demo.auth.publisher;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 这个是每个商户每月的统计数据
 */
@Data
public class OutMonthExcelData {

    // 每月的交易数
    private Integer count;
    // 每月的交易额
    private BigDecimal quality;

}
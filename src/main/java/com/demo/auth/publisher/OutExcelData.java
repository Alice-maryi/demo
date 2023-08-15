package com.demo.auth.publisher;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 这个类是每个商户一年统计的数据
 */
@Data
public class OutExcelData {

    // 分支机构
    private String department;
    // 商户名称
    private String storeName;
    // 商户号码
    private String storeNumber;

    // 一年的统计数据
    // 一年的交易数
    private Integer allYearChange;
    // 一年的交易额
    private BigDecimal allYearQuality;

    private Map<Integer, OutMonthExcelData> monthDataMap;

}

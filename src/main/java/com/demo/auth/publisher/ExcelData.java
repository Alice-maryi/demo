package com.demo.auth.publisher;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 这个对应原始Excel的每一条记录
 */
@Data
public class ExcelData {


    // 分支机构
    private String department;
    // 商户名称
    private String storeName;
    // 商户号码
    private String storeNumber;

    // 支付方式
    private String payType;
    // 交易金额
    private BigDecimal changeQuality;
    // 交易次数
    private Integer changeNumber;
    // 退款金额
    private BigDecimal outQuality;
    // 退款次数
    private Integer outNumber;
    // 实际交易额
    private BigDecimal currentChangeQuality;


}

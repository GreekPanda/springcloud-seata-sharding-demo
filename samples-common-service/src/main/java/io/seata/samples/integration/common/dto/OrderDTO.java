package io.seata.samples.integration.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @Author: heshouyou
 * @Description 订单信息
 * @Date Created in 2019/1/13 16:33
 */
@Data
public class OrderDTO implements Serializable {

    private String orderNo;

    private Integer userId;

    private String commodityCode;

    private Integer orderCount;

    private BigDecimal orderAmount;

    private String createMonth;
}

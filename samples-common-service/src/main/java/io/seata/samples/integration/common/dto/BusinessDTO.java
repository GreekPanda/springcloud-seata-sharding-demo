package io.seata.samples.integration.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @Author: heshouyou
 * @Description
 * @Date Created in 2019/1/14 17:26
 */
@Data
public class BusinessDTO implements Serializable {

    private Integer userId;

    private String commodityCode;

    private String name;

    private Integer count;

    private BigDecimal amount;

    private String createMonth;
}

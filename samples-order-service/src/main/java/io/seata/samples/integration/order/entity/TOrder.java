package io.seata.samples.integration.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;


/**
 * <p>
 *
 * </p>
 *
 * * @author lidong
 * @since 2019-09-04
 */
@Data
public class TOrder extends Model<TOrder> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String orderNo;
    private Integer userId;
    private String commodityCode;
    private Integer count;
    private Double amount;
    private Integer createMonth;



}

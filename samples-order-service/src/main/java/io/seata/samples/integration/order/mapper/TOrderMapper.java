package io.seata.samples.integration.order.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.seata.samples.integration.common.dto.OrderDTO;
import io.seata.samples.integration.common.response.ObjectResponse;
import io.seata.samples.integration.order.entity.TOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * * @author lidong
 * @since 2019-09-04
 */
@Mapper
public interface TOrderMapper extends BaseMapper<TOrder> {

    /**
     * 创建订单
     * @Param:  order 订单信息
     * @Return:
     */
    void createOrder(@Param("order") TOrder order);

    /**
     * 按月份查找订单
     * @return
     */
    List<OrderDTO> listOrderByCreateMonth(@Param("startMonth") Integer startMonth, @Param("endMonth") Integer endMonth);
}
